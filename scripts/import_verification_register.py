#!/usr/bin/env python3
"""Importa un export CSV (Airtable) nel registro pubblico delle verifiche AMEV.

Uso tipico (dalla root del repository):

    python scripts/import_verification_register.py export.csv --check   # sola validazione
    python scripts/import_verification_register.py export.csv           # validazione + scrittura
    python scripts/import_verification_register.py --check             # registro e HTML coerenti?
    python scripts/import_verification_register.py --render            # rigenera solo l'HTML statico
    python scripts/import_verification_register.py --due 12            # Authority da controllare

Nessun file viene scritto se anche una sola riga non supera la validazione.
Solo libreria standard: nessuna rete, nessuna credenziale, nessuna API Airtable.
"""
import argparse
import csv
import html
import json
import os
import re
import sys
import tempfile
from datetime import date, datetime, timedelta, timezone
from pathlib import Path
from urllib.parse import urlsplit
from zoneinfo import ZoneInfo

ROOT = Path(__file__).resolve().parents[1]
GUIDE_DIR = ROOT / "verifica-prima di pagare"
DEFAULT_REGISTER = GUIDE_DIR / "registro-verifiche.json"
DEFAULT_HTML = GUIDE_DIR / "index.html"
DEFAULT_DATASET = ROOT / "financial_authorities_database.json"

SCHEMA_VERSION = 1
TITLE = "Registro verifiche di coerenza home page Authorithy"
TIMEZONE_NAME = "Europe/Rome"
TZ = ZoneInfo(TIMEZONE_NAME)
CYCLE_LENGTH = 20
DAILY_TARGET = 12
VALIDITY_DAYS = 20
FIELDS = (
    "authorityId", "cycleId", "cycleStartDate", "cycleDay", "reviewedAt", "status",
    "checkedUrl", "finalUrl", "reviewedBy", "notes", "evidenceRef",
)
STATUSES = ("verified", "needs_review", "unreachable", "blocked")
STATUS_LABELS = {
    "verified": "Coerente (verifica positiva)",
    "needs_review": "Da approfondire",
    "unreachable": "Non raggiungibile",
    "blocked": "Accesso bloccato",
}
SLOT_LABELS = {
    "unreviewed": "Nessuna verifica documentata",
    "partial": "Parziale",
    "completed": "Completato",
    "problematic": "Con criticità",
}
MONTHS = ("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic")
BEGIN_MARKER = "<!-- REGISTRO-VERIFICHE:STATIC:BEGIN (generato da scripts/import_verification_register.py) -->"
END_MARKER = "<!-- REGISTRO-VERIFICHE:STATIC:END -->"

CYCLE_ID_RE = re.compile(r"[A-Za-z0-9][A-Za-z0-9_.-]{0,39}")
AUTHORITY_ID_RE = re.compile(r"[a-z0-9_]{1,80}")
DATE_RE = re.compile(r"\d{4}-\d{2}-\d{2}")
DATETIME_RE = re.compile(r"\d{4}-\d{2}-\d{2}T\d{2}:\d{2}(:\d{2})?(Z|[+-]\d{2}:\d{2})")
DAY_RE = re.compile(r"\d{1,2}")
SCHEME_RE = re.compile(r"[A-Za-z][A-Za-z0-9+.-]*:")
CONTROL_RE = re.compile(r"[\x00-\x09\x0b-\x1f\x7f\u202a-\u202e\u2066-\u2069]")
EMAIL_RE = re.compile(r"[^\s@]+@[^\s@]+\.[^\s@]+")
LIMITS = {"reviewedBy": 80, "notes": 1000, "evidenceRef": 300, "url": 2048}


class ValidationError(Exception):
    def __init__(self, errors):
        super().__init__("\n".join(errors))
        self.errors = errors


# ---------------------------------------------------------------- helpers

def normalize_url(value):
    """Restituisce l'URL http(s) normalizzato per il confronto, o None se non sicuro."""
    if not isinstance(value, str) or not value or len(value) > LIMITS["url"]:
        return None
    if value != value.strip() or any(ch.isspace() for ch in value) or CONTROL_RE.search(value):
        return None
    try:
        parts = urlsplit(value)
        port = parts.port
    except ValueError:
        return None
    scheme = parts.scheme.lower()
    if scheme not in ("http", "https") or not parts.hostname or "@" in parts.netloc:
        return None
    host = parts.hostname.lower()
    if port is not None and not ((scheme == "http" and port == 80) or (scheme == "https" and port == 443)):
        host = f"{host}:{port}"
    path = parts.path or "/"
    query = f"?{parts.query}" if parts.query else ""
    return f"{scheme}://{host}{path}{query}"


def parse_date(value):
    if not isinstance(value, str) or not DATE_RE.fullmatch(value):
        raise ValueError("data non in formato AAAA-MM-GG")
    return date.fromisoformat(value)


def parse_datetime(value):
    if not isinstance(value, str) or not DATETIME_RE.fullmatch(value):
        raise ValueError("data/ora non in formato ISO 8601 con fuso esplicito (es. 2026-10-06T10:15:00+02:00)")
    parsed = datetime.fromisoformat(value.replace("Z", "+00:00"))
    return parsed


def rome_date(instant):
    return instant.astimezone(TZ).date()


def format_date_it(day):
    return f"{day.day} {MONTHS[day.month - 1]} {day.year}"


def format_datetime_it(instant):
    local = instant.astimezone(TZ)
    return f"{format_date_it(local.date())}, {local:%H:%M} (ora di Roma)"


def load_authorities(dataset_path):
    data = json.loads(Path(dataset_path).read_text(encoding="utf-8"))
    authorities = {}
    for country, record in data.items():
        fa = record.get("financial_authority") or {}
        authority_id = fa.get("authorityId")
        if not authority_id or authority_id in authorities:
            raise ValueError(f"authorityId mancante o duplicato nel dataset: {country}")
        authorities[authority_id] = {
            "name": fa.get("name") or authority_id,
            "country": record.get("country_name") or country,
            "homepage": fa.get("homepage") or "",
        }
    return authorities


def empty_register():
    return {
        "schemaVersion": SCHEMA_VERSION,
        "title": TITLE,
        "authoritySource": "financial_authorities_database.json",
        "timezone": TIMEZONE_NAME,
        "cycleLengthDays": CYCLE_LENGTH,
        "dailyTarget": DAILY_TARGET,
        "validityDays": VALIDITY_DAYS,
        "statuses": list(STATUSES),
        "lastRecordedReviewAt": None,
        "cycles": [],
        "reviews": [],
    }


def clean_text(value, field, errors, where, *, required=False, multiline=False):
    value = (value or "").replace("\r\n", "\n").replace("\r", "\n").strip()
    if not value:
        if required:
            errors.append(f"{where}: {field} obbligatorio")
        return ""
    if "\n" in value and not multiline:
        errors.append(f"{where}: {field} deve stare su una sola riga")
    if CONTROL_RE.search(value):
        errors.append(f"{where}: {field} contiene caratteri di controllo non ammessi")
    if value[0] in "=+@" or (value[0] == "-" and len(value) > 1 and value[1] != " "):
        errors.append(f"{where}: {field} inizia con un carattere da formula di foglio di calcolo (= + - @)")
    limit = LIMITS.get(field)
    if limit and len(value) > limit:
        errors.append(f"{where}: {field} supera {limit} caratteri")
    return value


def cycle_day_for(start, instant):
    return (rome_date(instant) - start).days + 1


# ---------------------------------------------------------------- validation

def validate_review(raw, authorities, errors, where, *, now=None, check_source=True):
    """Valida un record (riga CSV o record già pubblicato). Restituisce (record, startDate)."""
    before = len(errors)
    get = lambda key: raw.get(key) if isinstance(raw.get(key), str) else ("" if raw.get(key) is None else str(raw.get(key)))
    authority_id = get("authorityId").strip()
    if not AUTHORITY_ID_RE.fullmatch(authority_id):
        errors.append(f"{where}: authorityId non valido: {authority_id!r}")
    elif authority_id not in authorities and check_source:
        errors.append(f"{where}: authorityId inesistente nel dataset: {authority_id}")
    cycle_id = get("cycleId").strip()
    if not CYCLE_ID_RE.fullmatch(cycle_id):
        errors.append(f"{where}: cycleId non valido (lettere, cifre, . _ -; max 40): {cycle_id!r}")
    start = None
    try:
        start = parse_date(get("cycleStartDate").strip())
    except ValueError as error:
        errors.append(f"{where}: cycleStartDate {error}")
    reviewed_at = None
    try:
        reviewed_at = parse_datetime(get("reviewedAt").strip())
    except ValueError as error:
        errors.append(f"{where}: reviewedAt {error}")
    day_text = get("cycleDay").strip()
    day = int(day_text) if DAY_RE.fullmatch(day_text) else None
    if day is None or not 1 <= day <= CYCLE_LENGTH:
        errors.append(f"{where}: cycleDay deve essere un intero da 1 a {CYCLE_LENGTH}")
    elif start and reviewed_at:
        expected = cycle_day_for(start, reviewed_at)
        if expected != day:
            errors.append(
                f"{where}: cycleDay {day} incoerente con reviewedAt (giorno {expected} del ciclo "
                f"iniziato il {start.isoformat()}, fuso {TIMEZONE_NAME})")
    if reviewed_at and now and reviewed_at > now:
        errors.append(f"{where}: reviewedAt nel futuro ({reviewed_at.isoformat()})")
    status = get("status").strip()
    if status not in STATUSES:
        errors.append(f"{where}: status non ammesso {status!r} (ammessi: {', '.join(STATUSES)})")
    checked_url = get("checkedUrl").strip()
    normalized = normalize_url(checked_url)
    if not normalized:
        errors.append(f"{where}: checkedUrl assente o non sicuro (solo http/https, senza credenziali)")
    elif check_source and authority_id in authorities:
        source = normalize_url(authorities[authority_id]["homepage"])
        if not source:
            errors.append(f"{where}: {authority_id} non ha una homepage valida nel dataset")
        elif source != normalized:
            errors.append(
                f"{where}: checkedUrl {checked_url} non coincide con la homepage del dataset "
                f"{authorities[authority_id]['homepage']}")
    final_url = get("finalUrl").strip()
    if final_url and not normalize_url(final_url):
        errors.append(f"{where}: finalUrl non sicuro (solo http/https, senza credenziali)")
    if status == "verified" and not final_url:
        errors.append(f"{where}: finalUrl obbligatorio per una verifica positiva")
    reviewed_by = clean_text(get("reviewedBy"), "reviewedBy", errors, where, required=True)
    if reviewed_by and EMAIL_RE.search(reviewed_by):
        errors.append(f"{where}: reviewedBy deve essere un nome pubblico, non un indirizzo email")
    notes = clean_text(get("notes"), "notes", errors, where, required=status in STATUSES[1:], multiline=True)
    evidence = clean_text(get("evidenceRef"), "evidenceRef", errors, where)
    if evidence and SCHEME_RE.match(evidence) and not normalize_url(evidence):
        errors.append(f"{where}: evidenceRef con schema URL non ammesso (solo http/https)")
    if len(errors) != before:
        return None, None
    return {
        "authorityId": authority_id,
        "cycleId": cycle_id,
        "cycleDay": day,
        "reviewedAt": reviewed_at.isoformat(timespec="seconds"),
        "status": status,
        "checkedUrl": checked_url,
        "finalUrl": final_url,
        "reviewedBy": reviewed_by,
        "notes": notes,
        "evidenceRef": evidence,
    }, start


def read_csv(path):
    errors = []
    try:
        with open(path, encoding="utf-8-sig", newline="") as handle:
            reader = csv.reader(handle, strict=True)
            rows = list(reader)
    except UnicodeDecodeError:
        raise ValidationError([f"{path}: il file deve essere codificato in UTF-8"])
    except csv.Error as error:
        raise ValidationError([f"{path}: CSV non valido: {error}"])
    if not rows:
        raise ValidationError([f"{path}: file vuoto, manca l'intestazione"])
    header = [name.strip() for name in rows[0]]
    if len(set(header)) != len(header) or set(header) != set(FIELDS):
        missing = sorted(set(FIELDS) - set(header))
        extra = sorted(set(header) - set(FIELDS))
        raise ValidationError([
            f"{path}: intestazione non conforme. Mancanti: {missing or '-'}; non ammesse: {extra or '-'}; "
            f"duplicate: {sorted({h for h in header if header.count(h) > 1}) or '-'}"])
    records = []
    for index, row in enumerate(rows[1:], start=2):
        if not any(cell.strip() for cell in row):
            continue
        if len(row) != len(header):
            errors.append(f"riga CSV {index}: {len(row)} colonne invece di {len(header)}")
            continue
        records.append((index, dict(zip(header, row))))
    if errors:
        raise ValidationError(errors)
    return records


def review_key(record):
    return record["authorityId"], parse_datetime(record["reviewedAt"])


def sort_key(record, cycles):
    instant = parse_datetime(record["reviewedAt"]).astimezone(timezone.utc)
    return cycles[record["cycleId"]], instant, record["authorityId"]


def merge(register, csv_rows, authorities, now):
    """Unisce le righe CSV al registro esistente. Solleva ValidationError senza effetti collaterali."""
    errors = []
    cycles = {}
    for index, cycle in enumerate(register.get("cycles", [])):
        try:
            start = parse_date(cycle.get("startDate"))
        except ValueError as error:
            errors.append(f"registro, ciclo {index}: startDate {error}")
            continue
        cycle_id = cycle.get("cycleId")
        if not isinstance(cycle_id, str) or not CYCLE_ID_RE.fullmatch(cycle_id) or cycle_id in cycles:
            errors.append(f"registro, ciclo {index}: cycleId non valido o duplicato")
            continue
        cycles[cycle_id] = start
    existing = {}
    reviews = []
    for index, raw in enumerate(register.get("reviews", [])):
        where = f"registro, record {index}"
        if not isinstance(raw, dict) or raw.get("cycleId") not in cycles:
            errors.append(f"{where}: ciclo non dichiarato")
            continue
        raw = dict(raw, cycleStartDate=cycles[raw["cycleId"]].isoformat())
        record, _ = validate_review(raw, authorities, errors, where, check_source=False)
        if record:
            key = review_key(record)
            if key in existing:
                errors.append(f"{where}: record duplicato nel registro")
                continue
            existing[key] = record
            reviews.append(record)
    if errors:
        raise ValidationError(errors)
    added = skipped = 0
    for line, raw in csv_rows:
        where = f"riga CSV {line}"
        record, start = validate_review(raw, authorities, errors, where, now=now)
        if not record:
            continue
        known = cycles.get(record["cycleId"])
        if known and known != start:
            errors.append(f"{where}: il ciclo {record['cycleId']} ha già data di inizio {known.isoformat()}")
            continue
        if not known:
            for other_id, other_start in cycles.items():
                if abs((other_start - start).days) < CYCLE_LENGTH:
                    errors.append(
                        f"{where}: il nuovo ciclo {record['cycleId']} ({start.isoformat()}) si sovrappone "
                        f"al ciclo {other_id} ({other_start.isoformat()}); i cicli devono restare separati")
                    break
            else:
                cycles[record["cycleId"]] = start
        key = review_key(record)
        if key in existing:
            if existing[key] == record:
                skipped += 1
            else:
                errors.append(
                    f"{where}: conflitto con una verifica già registrata per {record['authorityId']} "
                    f"alle {record['reviewedAt']} (contenuto diverso: correggere a mano, non sovrascrivere)")
            continue
        existing[key] = record
        reviews.append(record)
        added += 1
    if errors:
        raise ValidationError(errors)
    reviews.sort(key=lambda record: sort_key(record, cycles))
    result = empty_register()
    result["cycles"] = [
        {"cycleId": cycle_id, "startDate": start.isoformat(),
         "endDate": (start + timedelta(days=CYCLE_LENGTH - 1)).isoformat()}
        for cycle_id, start in sorted(cycles.items(), key=lambda item: (item[1], item[0]))
        if any(record["cycleId"] == cycle_id for record in reviews)
    ]
    if reviews:
        latest = max(reviews, key=lambda record: parse_datetime(record["reviewedAt"]))
        result["lastRecordedReviewAt"] = latest["reviewedAt"]
    result["reviews"] = reviews
    return result, added, skipped


# ---------------------------------------------------------------- derived states

def authority_status(reviews, homepage, now):
    """Stato corrente di una Authority. reviews: record della sola Authority."""
    if not reviews:
        return "undocumented", None
    ordered = sorted(reviews, key=lambda record: parse_datetime(record["reviewedAt"]))
    latest = ordered[-1]
    instant = parse_datetime(latest["reviewedAt"])
    if latest["status"] != "verified" or instant > now:
        return "warning", latest
    if normalize_url(latest["checkedUrl"]) != normalize_url(homepage):
        return "warning", latest
    if now - instant >= timedelta(days=VALIDITY_DAYS):
        return "renewal", latest
    return "verified", latest


def cycle_slots(cycle, reviews):
    start = parse_date(cycle["startDate"]) if cycle else None
    slots = []
    for day in range(1, CYCLE_LENGTH + 1):
        latest_by_authority = {}
        for record in reviews:
            if cycle and record["cycleId"] == cycle["cycleId"] and record["cycleDay"] == day:
                current = latest_by_authority.get(record["authorityId"])
                if not current or parse_datetime(record["reviewedAt"]) > parse_datetime(current["reviewedAt"]):
                    latest_by_authority[record["authorityId"]] = record
        reviewed = len(latest_by_authority)
        positive = sum(1 for record in latest_by_authority.values() if record["status"] == "verified")
        problems = reviewed - positive
        if not reviewed:
            state = "unreviewed"
        elif problems:
            state = "problematic"
        elif reviewed >= DAILY_TARGET:
            state = "completed"
        else:
            state = "partial"
        slots.append({
            "day": day,
            "date": (start + timedelta(days=day - 1)).isoformat() if start else None,
            "reviewed": reviewed, "positive": positive, "problems": problems, "state": state,
        })
    return slots


# ---------------------------------------------------------------- static HTML

def esc(value):
    return html.escape(str(value), quote=True)


def link(url):
    if not url:
        return "—"
    if not normalize_url(url):
        return esc(url)
    return f'<a href="{esc(url)}" target="_blank" rel="noopener noreferrer nofollow">{esc(url)}</a>'


def render_static(register, authorities, indent="      "):
    reviews = register["reviews"]
    cycle = register["cycles"][-1] if register["cycles"] else None
    lines = ['<div id="registro-data" class="register-data">']
    distinct = len({record["authorityId"] for record in reviews})
    if reviews:
        lines.append(
            f'  <p class="register-summary">Verifiche pubblicate: {len(reviews)} · Authority distinte '
            f'controllate: {distinct} su {len(authorities)} nel dataset · Ultima verifica registrata: '
            f'{esc(format_datetime_it(parse_datetime(register["lastRecordedReviewAt"])))}.</p>')
    else:
        lines.append(
            f'  <p class="register-summary">Nessuna verifica documentata. Authority nel dataset: '
            f'{len(authorities)}; nessun ciclo avviato.</p>')
    if cycle:
        title = (f'Ciclo {esc(cycle["cycleId"])}: dal {esc(format_date_it(parse_date(cycle["startDate"])))} '
                 f'al {esc(format_date_it(parse_date(cycle["endDate"])))}')
    else:
        title = "Ciclo non ancora avviato: giorni numerati senza date di calendario"
    lines.append(f'  <h3 id="registro-cycle-title">{title}</h3>')
    lines.append('  <ol class="cycle-grid" aria-labelledby="registro-cycle-title">')
    for slot in cycle_slots(cycle, reviews):
        when = (f'<span class="slot-date">{esc(format_date_it(parse_date(slot["date"])))}</span>'
                if slot["date"] else '<span class="slot-date">Data non assegnata</span>')
        lines.append(
            f'    <li class="cycle-slot slot-{slot["state"]}"><span class="slot-label">'
            f'<span class="visually-hidden">Giorno </span>{slot["day"]}/{CYCLE_LENGTH}</span>{when}<span class="slot-state">'
            f'{esc(SLOT_LABELS[slot["state"]])}</span><span class="slot-count">{slot["reviewed"]}/{DAILY_TARGET} '
            f'controlli · {slot["positive"]} positivi · {slot["problems"]} criticità</span></li>')
    lines.append("  </ol>")
    lines.append('  <h3 id="registro-table-title">Verifiche pubblicate</h3>')
    lines.append('  <div class="table-scroll" role="region" aria-labelledby="registro-table-title" tabindex="0">')
    lines.append('    <table class="register-table">')
    lines.append(
        "      <thead><tr><th scope=\"col\">Authority</th><th scope=\"col\">Homepage nel dataset</th>"
        "<th scope=\"col\">URL controllato</th><th scope=\"col\">URL finale</th>"
        "<th scope=\"col\">Data e ora</th><th scope=\"col\">Ciclo / giorno</th><th scope=\"col\">Esito</th>"
        "<th scope=\"col\">Revisore</th><th scope=\"col\">Note</th><th scope=\"col\">Evidenza pubblica</th></tr></thead>")
    lines.append("      <tbody>")
    if not reviews:
        lines.append('        <tr><td colspan="10" class="empty-state">Nessuna verifica documentata</td></tr>')
    for record in sorted(reviews, key=lambda r: parse_datetime(r["reviewedAt"]), reverse=True):
        authority = authorities.get(record["authorityId"])
        name = (f'{esc(authority["name"])} ({esc(authority["country"])})' if authority
                else "ID non presente nel dataset attuale")
        notes = esc(record["notes"]).replace("\n", "<br>") or "—"
        lines.append(
            f'        <tr><th scope="row"><span class="authority-name">{name}</span>'
            f'<code>{esc(record["authorityId"])}</code></th>'
            f'<td>{link(authority["homepage"]) if authority else "—"}</td><td>{link(record["checkedUrl"])}</td>'
            f'<td>{link(record["finalUrl"])}</td>'
            f'<td><time datetime="{esc(record["reviewedAt"])}">'
            f'{esc(format_datetime_it(parse_datetime(record["reviewedAt"])))}</time></td>'
            f'<td>{esc(record["cycleId"])} · {record["cycleDay"]}/{CYCLE_LENGTH}</td>'
            f'<td class="outcome-{record["status"]}">{esc(STATUS_LABELS[record["status"]])}</td>'
            f'<td>{esc(record["reviewedBy"])}</td><td>{notes}</td><td>{link(record["evidenceRef"])}</td></tr>')
    lines.append("      </tbody>")
    lines.append("    </table>")
    lines.append("  </div>")
    lines.append("</div>")
    return "\n".join(indent + line if line else line for line in lines)


def inject_static(page, static_html):
    if page.count(BEGIN_MARKER) != 1 or page.count(END_MARKER) != 1:
        raise ValidationError(["wrapper HTML: marcatori del blocco statico mancanti o duplicati"])
    head, rest = page.split(BEGIN_MARKER)
    _, tail = rest.split(END_MARKER)
    line_start = head.rfind("\n") + 1
    indent = head[line_start:]
    return f"{head}{BEGIN_MARKER}\n{static_html}\n{indent}{END_MARKER}{tail}"


def serialize(register):
    return json.dumps(register, ensure_ascii=False, indent=2) + "\n"


def atomic_write(path, content):
    path = Path(path)
    descriptor, temporary = tempfile.mkstemp(dir=path.parent, prefix=f".{path.name}.", suffix=".tmp")
    try:
        with os.fdopen(descriptor, "w", encoding="utf-8", newline="\n") as handle:
            handle.write(content)
        os.replace(temporary, path)
    except BaseException:
        if os.path.exists(temporary):
            os.unlink(temporary)
        raise


def due_list(register, authorities, now, limit):
    by_authority = {}
    for record in register["reviews"]:
        by_authority.setdefault(record["authorityId"], []).append(record)
    priority = {"undocumented": 0, "warning": 1, "renewal": 2, "verified": 3}
    rows = []
    for authority_id, info in authorities.items():
        if not normalize_url(info["homepage"]):
            continue
        state, latest = authority_status(by_authority.get(authority_id, []), info["homepage"], now)
        last = parse_datetime(latest["reviewedAt"]).astimezone(timezone.utc) if latest else datetime.min.replace(tzinfo=timezone.utc)
        rows.append((priority[state], last, authority_id, state, info))
    rows.sort(key=lambda row: row[:3])
    return rows[:limit]


# ---------------------------------------------------------------- CLI

def parse_now(value):
    if value is None:
        return datetime.now(timezone.utc)
    return parse_datetime(value)


def run(argv=None, out=sys.stdout, err=sys.stderr):
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("csv", nargs="?", help="export CSV Airtable (UTF-8, separatore virgola)")
    parser.add_argument("--check", action="store_true", help="valida senza scrivere file")
    parser.add_argument("--render", action="store_true", help="rigenera l'HTML statico dal registro esistente")
    parser.add_argument("--due", type=int, metavar="N", help="elenca le N Authority da controllare per prime")
    parser.add_argument("--now", help="istante di riferimento ISO 8601 con fuso (test riproducibili)")
    parser.add_argument("--register", default=DEFAULT_REGISTER, type=Path)
    parser.add_argument("--html", default=DEFAULT_HTML, type=Path)
    parser.add_argument("--dataset", default=DEFAULT_DATASET, type=Path)
    args = parser.parse_args(argv)
    try:
        now = parse_now(args.now)
        authorities = load_authorities(args.dataset)
        register = (json.loads(args.register.read_text(encoding="utf-8"))
                    if args.register.exists() else empty_register())
        for key in ("schemaVersion", "timezone", "cycleLengthDays", "dailyTarget", "validityDays"):
            if register.get(key) != empty_register()[key]:
                raise ValidationError([f"registro: {key} inatteso ({register.get(key)!r})"])
        rows = read_csv(args.csv) if args.csv else []
        merged, added, skipped = merge(register, rows, authorities, now)
        if args.due is not None:
            for _, _, authority_id, state, info in due_list(merged, authorities, now, args.due):
                print(f"{authority_id}\t{state}\t{info['name']} ({info['country']})\t{info['homepage']}", file=out)
            return 0
        page = args.html.read_text(encoding="utf-8")
        new_page = inject_static(page, render_static(merged, authorities))
        new_json = serialize(merged)
        old_json = args.register.read_text(encoding="utf-8") if args.register.exists() else ""
        changed = [path for path, old, new in ((args.register, old_json, new_json), (args.html, page, new_page))
                   if old != new]
        print(f"Righe CSV valide: {len(rows)}; nuove verifiche: {added}; duplicati identici ignorati: {skipped}; "
              f"totale nel registro: {len(merged['reviews'])}.", file=out)
        if args.check:
            if changed:
                print("Da aggiornare: " + ", ".join(str(path) for path in changed), file=out)
                return 1 if not args.csv else 0
            print("Registro e HTML statico già aggiornati.", file=out)
            return 0
        if not args.csv and not args.render:
            parser.error("indicare un file CSV, --check, --render o --due")
        if old_json != new_json:
            atomic_write(args.register, new_json)
        if page != new_page:
            atomic_write(args.html, new_page)
        print("File aggiornati: " + (", ".join(str(path) for path in changed) or "nessuno"), file=out)
        return 0
    except ValidationError as error:
        print("Validazione non superata; nessun file modificato:", file=err)
        for message in error.errors:
            print(f"  - {message}", file=err)
        return 2
    except (OSError, ValueError) as error:
        print(f"Errore: {error}; nessun file modificato.", file=err)
        return 2


if __name__ == "__main__":
    sys.exit(run())
