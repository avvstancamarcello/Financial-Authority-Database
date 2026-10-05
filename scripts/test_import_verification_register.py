#!/usr/bin/env python3
"""Test dell'importer del registro verifiche (solo libreria standard).

    python -m unittest scripts/test_import_verification_register.py
"""
import csv
import io
import json
import shutil
import sys
import tempfile
import unittest
from datetime import timedelta
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import import_verification_register as reg  # noqa: E402

ROOT = Path(__file__).resolve().parents[1]
NOW = "2026-10-05T12:00:00+00:00"
FIELDS = list(reg.FIELDS)


def row(index, day=1, **overrides):
    values = {
        "authorityId": f"country_{index:02d}__auth",
        "cycleId": "2026-C01",
        "cycleStartDate": "2026-10-01",
        "cycleDay": str(day),
        "reviewedAt": f"2026-10-{day:02d}T09:{index:02d}:00+02:00",
        "status": "verified",
        "checkedUrl": f"https://a{index:02d}.example.org/",
        "finalUrl": f"https://www.a{index:02d}.example.org/en/",
        "reviewedBy": "Revisore AMEV",
        "notes": "",
        "evidenceRef": "",
    }
    values.update(overrides)
    return values


class RegisterTestCase(unittest.TestCase):
    def setUp(self):
        self.tmp = Path(tempfile.mkdtemp(prefix="amev-register-test-"))
        self.addCleanup(shutil.rmtree, self.tmp)
        dataset = {}
        for index in range(15):
            dataset[f"Country {index:02d}"] = {
                "country_name": f"Country {index:02d}",
                "financial_authority": {
                    "name": f"Authority {index:02d}",
                    "homepage": f"https://a{index:02d}.example.org" + ("/" if index % 2 else ""),
                    "authorityId": f"country_{index:02d}__auth",
                },
            }
        dataset["No Homepage"] = {"country_name": "No Homepage",
                                  "financial_authority": {"name": "NH", "homepage": "", "authorityId": "nohome__nh"}}
        self.dataset = self.tmp / "dataset.json"
        self.dataset.write_text(json.dumps(dataset), encoding="utf-8")
        self.register = self.tmp / "registro-verifiche.json"
        self.html = self.tmp / "index.html"
        self.html.write_text(
            "<main>\n      " + reg.BEGIN_MARKER + "\n      " + reg.END_MARKER + "\n</main>\n", encoding="utf-8")

    def write_csv(self, rows, name="export.csv", bom=False, header=None):
        buffer = io.StringIO()
        writer = csv.DictWriter(buffer, fieldnames=header or FIELDS, lineterminator="\r\n")
        writer.writeheader()
        for item in rows:
            writer.writerow(item)
        path = self.tmp / name
        path.write_text(("\ufeff" if bom else "") + buffer.getvalue(), encoding="utf-8", newline="")
        return path

    def run_cli(self, *args):
        out, err = io.StringIO(), io.StringIO()
        code = reg.run([*map(str, args), "--now", NOW, "--register", str(self.register),
                        "--html", str(self.html), "--dataset", str(self.dataset)], out=out, err=err)
        return code, out.getvalue(), err.getvalue()

    def snapshot(self):
        return (self.register.read_bytes() if self.register.exists() else None, self.html.read_bytes())

    def assert_rejected(self, rows, fragment, **kwargs):
        before = self.snapshot()
        code, _, err = self.run_cli(self.write_csv(rows, **kwargs))
        self.assertEqual(code, 2, err)
        self.assertIn(fragment, err)
        self.assertEqual(before, self.snapshot(), "no file may change after a failed validation")

    def load(self):
        return json.loads(self.register.read_text(encoding="utf-8"))


class EmptyAndGrid(RegisterTestCase):
    def test_empty_register_renders_twenty_cycle_days_without_dates(self):
        code, _, err = self.run_cli("--render")
        self.assertEqual(code, 0, err)
        page = self.html.read_text(encoding="utf-8")
        for day in range(1, 21):
            self.assertIn(f"</span>{day}/20</span>", page)
        self.assertEqual(page.count('class="cycle-slot slot-unreviewed"'), 20)
        self.assertIn("Nessuna verifica documentata", page)
        self.assertIn("Data non assegnata", page)
        self.assertNotIn("✓", page)
        self.assertEqual(self.load()["reviews"], [])
        self.assertIsNone(self.load()["lastRecordedReviewAt"])

    def test_day_counts_partial_completed_problematic_and_no_double_counting(self):
        rows = [row(i, day=1) for i in range(12)]
        rows += [row(i, day=2) for i in range(5)]
        rows.append(row(5, day=3))
        rows.append(row(5, day=3, reviewedAt="2026-10-03T11:00:00+02:00"))  # same authority twice
        rows.append(row(6, day=4, status="unreachable", finalUrl="", notes="Timeout"))
        code, out, err = self.run_cli(self.write_csv(rows))
        self.assertEqual(code, 0, err)
        register = self.load()
        slots = reg.cycle_slots(register["cycles"][0], register["reviews"])
        self.assertEqual(len(slots), 20)
        self.assertEqual((slots[0]["state"], slots[0]["reviewed"]), ("completed", 12))
        self.assertEqual((slots[1]["state"], slots[1]["reviewed"]), ("partial", 5))
        self.assertEqual((slots[2]["state"], slots[2]["reviewed"]), ("partial", 1))
        self.assertEqual((slots[3]["state"], slots[3]["problems"]), ("problematic", 1))
        self.assertEqual(slots[4]["state"], "unreviewed")
        self.assertEqual([slot["date"] for slot in slots][:2], ["2026-10-01", "2026-10-02"])
        self.assertEqual(slots[19]["date"], "2026-10-20")
        self.assertEqual(register["cycles"], [{"cycleId": "2026-C01", "startDate": "2026-10-01",
                                               "endDate": "2026-10-20"}])
        self.assertEqual(register["lastRecordedReviewAt"], "2026-10-04T09:06:00+02:00")
        page = self.html.read_text(encoding="utf-8")
        self.assertIn("12/12 controlli", page)
        self.assertIn("1 ott 2026", page)


class Validation(RegisterTestCase):
    def test_unknown_id_and_url_mismatch(self):
        self.assert_rejected([row(1, authorityId="country_99__auth")], "inesistente nel dataset")
        self.assert_rejected([row(1, checkedUrl="https://other.example.org/")], "non coincide")
        self.assert_rejected([row(1, authorityId="nohome__nh", checkedUrl="https://x.example.org/")],
                             "non ha una homepage valida")

    def test_url_normalisation_accepts_equivalent_forms(self):
        code, _, err = self.run_cli(self.write_csv([row(0, checkedUrl="HTTPS://A00.example.org:443")]))
        self.assertEqual(code, 0, err)

    def test_unsafe_urls_rejected(self):
        self.assert_rejected([row(1, checkedUrl="javascript:alert(1)")], "checkedUrl")
        self.assert_rejected([row(1, finalUrl="data:text/html,<script>alert(1)</script>")], "finalUrl")
        self.assert_rejected([row(1, finalUrl="https://" + "user:pw" + "@a01.example.org/")], "finalUrl")
        self.assert_rejected([row(1, evidenceRef="javascript:alert(1)")], "evidenceRef")
        self.assert_rejected([row(1, evidenceRef="vbscript:x")], "evidenceRef")

    def test_dates_days_and_status(self):
        self.assert_rejected([row(1, reviewedAt="2026-10-01 09:00")], "reviewedAt")
        self.assert_rejected([row(1, reviewedAt="2026-10-01T09:00:00")], "fuso esplicito")
        self.assert_rejected([row(1, cycleStartDate="2026-02-30")], "cycleStartDate")
        self.assert_rejected([row(1, cycleDay="21")], "cycleDay")
        self.assert_rejected([row(1, cycleDay="0")], "cycleDay")
        self.assert_rejected([row(1, cycleDay="2")], "incoerente")
        self.assert_rejected([row(1, day=6, reviewedAt="2026-10-06T09:00:00+02:00")], "nel futuro")
        self.assert_rejected([row(1, status="ok")], "status non ammesso")
        self.assert_rejected([row(1, status="needs_review", notes="")], "notes obbligatorio")
        self.assert_rejected([row(1, finalUrl="")], "finalUrl obbligatorio")

    def test_cycle_day_uses_europe_rome(self):
        late = "2026-10-01T23:30:00Z"  # 2 ottobre, 01:30 a Roma
        self.assert_rejected([row(1, reviewedAt=late, cycleDay="1")], "giorno 2")
        code, _, err = self.run_cli(self.write_csv([row(1, reviewedAt=late, cycleDay="2")]))
        self.assertEqual(code, 0, err)
        self.assertEqual(self.load()["reviews"][0]["reviewedAt"], "2026-10-01T23:30:00+00:00")

    def test_formula_private_data_and_control_characters(self):
        self.assert_rejected([row(1, notes="=HYPERLINK(\"http://x\")")], "formula")
        self.assert_rejected([row(1, reviewedBy="@mario")], "formula")
        self.assert_rejected([row(1, reviewedBy="mario.rossi@example.org")], "indirizzo email")
        self.assert_rejected([row(1, reviewedBy="Mario\nRossi")], "una sola riga")
        self.assert_rejected([row(1, notes="a\x00b")], "caratteri di controllo")

    def test_header_must_match_schema(self):
        self.assert_rejected([], "intestazione", header=FIELDS[:-1])
        extra = FIELDS + ["Email revisore"]
        self.assert_rejected([dict(row(1), **{"Email revisore": "x"})], "non ammesse", header=extra)

    def test_overlapping_and_inconsistent_cycles(self):
        self.assert_rejected([row(1), row(2, cycleStartDate="2026-09-30", cycleDay="2")], "ha già data di inizio")
        self.assert_rejected([row(1), row(2, cycleId="2026-C02")], "si sovrappone")
        other = row(2, cycleId="2026-C02", cycleStartDate="2026-09-20", cycleDay="12")
        self.assert_rejected([row(1), other], "si sovrappone")
        previous = row(2, cycleId="2026-C00", cycleStartDate="2026-09-11", cycleDay="1",
                       reviewedAt="2026-09-11T10:00:00+02:00")
        code, _, err = self.run_cli(self.write_csv([row(1), previous]))
        self.assertEqual(code, 0, err)
        self.assertEqual([c["cycleId"] for c in self.load()["cycles"]], ["2026-C00", "2026-C01"])


class CsvParsingAndSafety(RegisterTestCase):
    def test_bom_quotes_commas_and_multiline_notes(self):
        notes = 'Reindirizza a "/en/", lingua inglese\nseconda riga, con virgola'
        code, _, err = self.run_cli(self.write_csv([row(1, notes=notes)], bom=True))
        self.assertEqual(code, 0, err)
        self.assertEqual(self.load()["reviews"][0]["notes"], notes)
        self.assertIn("&quot;/en/&quot;, lingua inglese<br>seconda riga", self.html.read_text(encoding="utf-8"))

    def test_html_is_escaped_and_plain_evidence_is_not_a_link(self):
        payload = '<img src=x onerror=alert(1)><script>alert(2)</script>'
        code, _, err = self.run_cli(self.write_csv([row(1, notes=payload, evidenceRef="Airtable rec<b>1</b>",
                                                        reviewedBy="M. <i>R</i>")]))
        self.assertEqual(code, 0, err)
        page = self.html.read_text(encoding="utf-8")
        self.assertNotIn("<img", page)
        self.assertNotIn("<script>", page)
        self.assertIn("&lt;img src=x onerror=alert(1)&gt;", page)
        self.assertIn("<td>Airtable rec&lt;b&gt;1&lt;/b&gt;</td>", page)
        self.assertIn('rel="noopener noreferrer nofollow"', page)

    def test_malformed_csv(self):
        path = self.tmp / "bad.csv"
        path.write_text(",".join(FIELDS) + '\n"unterminated,1\n', encoding="utf-8")
        before = self.snapshot()
        code, _, err = self.run_cli(path)
        self.assertEqual(code, 2)
        self.assertEqual(before, self.snapshot())
        path.write_bytes(b"\xff\xfe" + "authorityId".encode("utf-16-le"))
        code, _, err = self.run_cli(path)
        self.assertEqual(code, 2)
        self.assertIn("UTF-8", err)


class DuplicatesAndDeterminism(RegisterTestCase):
    def test_reimport_is_idempotent_and_conflicts_are_rejected(self):
        rows = [row(i, day=1 + i % 3) for i in range(9)]
        csv_path = self.write_csv(rows)
        self.assertEqual(self.run_cli(csv_path)[0], 0)
        first = self.snapshot()
        code, out, _ = self.run_cli(csv_path)
        self.assertEqual(code, 0)
        self.assertIn("nuove verifiche: 0; duplicati identici ignorati: 9", out)
        self.assertEqual(first, self.snapshot())
        self.assert_rejected([row(0, notes="Esito modificato")], "conflitto")
        code, out, _ = self.run_cli(self.write_csv(rows + rows, name="twice.csv"))
        self.assertEqual(code, 0)
        self.assertEqual(first, self.snapshot())

    def test_output_independent_of_row_order_and_import_batches(self):
        rows = [row(i, day=1 + i % 4) for i in range(12)]
        self.assertEqual(self.run_cli(self.write_csv(rows))[0], 0)
        expected = self.snapshot()
        self.register.unlink()
        self.html.write_text(
            "<main>\n      " + reg.BEGIN_MARKER + "\n      " + reg.END_MARKER + "\n</main>\n", encoding="utf-8")
        self.assertEqual(self.run_cli(self.write_csv(list(reversed(rows[6:])), name="b1.csv"))[0], 0)
        self.assertEqual(self.run_cli(self.write_csv(rows[:6], name="b2.csv"))[0], 0)
        self.assertEqual(expected, self.snapshot())

    def test_check_mode_does_not_write(self):
        before = self.snapshot()
        code, out, _ = self.run_cli(self.write_csv([row(1)]), "--check")
        self.assertEqual(code, 0)
        self.assertIn("Da aggiornare", out)
        self.assertEqual(before, self.snapshot())


class DerivedStatus(unittest.TestCase):
    HOME = "https://a01.example.org/"

    def review(self, at, status="verified", url=HOME):
        return {"authorityId": "x", "reviewedAt": at, "status": status, "checkedUrl": url}

    def test_ttl_boundary_and_states(self):
        start = reg.parse_datetime("2026-10-01T10:00:00+02:00")
        positive = self.review("2026-10-01T10:00:00+02:00")
        state = lambda reviews, now, home=self.HOME: reg.authority_status(reviews, home, now)[0]
        self.assertEqual(state([], start), "undocumented")
        self.assertEqual(state([positive], start + timedelta(days=20, seconds=-1)), "verified")
        self.assertEqual(state([positive], start + timedelta(days=20)), "renewal")
        self.assertEqual(state([positive], start, "https://changed.example.org/"), "warning")
        failed = self.review("2026-10-02T10:00:00+02:00", status="blocked")
        self.assertEqual(state([positive, failed], start + timedelta(days=2)), "warning")
        self.assertEqual(state([failed, positive], start + timedelta(days=2)), "warning")
        renewed = self.review("2026-10-03T10:00:00+02:00")
        self.assertEqual(state([positive, failed, renewed], start + timedelta(days=3)), "verified")

    def test_due_list_prioritises_undocumented_then_problems(self):
        authorities = {
            "a": {"name": "A", "country": "A", "homepage": "https://a.example.org/"},
            "b": {"name": "B", "country": "B", "homepage": "https://b.example.org/"},
            "c": {"name": "C", "country": "C", "homepage": "https://c.example.org/"},
            "d": {"name": "D", "country": "D", "homepage": ""},
        }
        reviews = [
            {"authorityId": "a", "reviewedAt": "2026-10-01T10:00:00+02:00", "status": "verified",
             "checkedUrl": "https://a.example.org/"},
            {"authorityId": "b", "reviewedAt": "2026-10-01T10:00:00+02:00", "status": "unreachable",
             "checkedUrl": "https://b.example.org/"},
        ]
        now = reg.parse_datetime("2026-10-02T10:00:00+02:00")
        due = reg.due_list({"reviews": reviews}, authorities, now, 10)
        self.assertEqual([(item[2], item[3]) for item in due], [("c", "undocumented"), ("b", "warning"),
                                                                ("a", "verified")])


class RepositoryRegister(unittest.TestCase):
    def test_published_register_and_static_html_are_consistent(self):
        out, err = io.StringIO(), io.StringIO()
        self.assertEqual(reg.run(["--check"], out=out, err=err), 0, out.getvalue() + err.getvalue())

    def test_template_is_header_only(self):
        template = ROOT / "verifica-prima di pagare" / "registro-verifiche-template.csv"
        self.assertEqual(template.read_text(encoding="utf-8").splitlines(), [",".join(FIELDS)])


if __name__ == "__main__":
    unittest.main()
