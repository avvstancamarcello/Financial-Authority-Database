#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

INTL = ROOT / "data" / "international_financial_institutions.json"
CATS = ROOT / "data" / "international_institutions_categories.json"
TYPES = ROOT / "data" / "international_institutions_types.json"
I18N = ROOT / "data" / "international_institutions_i18n.json"
NATIONAL_FILES = [
    ROOT / "financial_authorities_database.json",
    ROOT / "APP" / "financial_authorities_database.json",
    ROOT / "DEPLOY_REGISTER" / "financial_authorities_database.json",
]


def load_json(path: Path):
    with path.open("r", encoding="utf-8") as f:
        return json.load(f)


def collect_authorities(data):
    out = {}
    for country, rec in data.items():
        if not isinstance(rec, dict):
            continue
        fa = rec.get("financial_authority")
        if not isinstance(fa, dict):
            continue
        aid = fa.get("authorityId")
        if not aid:
            raise AssertionError(f"Missing authorityId for country '{country}'")
        if aid in out:
            raise AssertionError(f"Duplicate authorityId: {aid}")
        rel = fa.get("relatedInternationalInstitutionIds")
        if not isinstance(rel, list):
            raise AssertionError(f"relatedInternationalInstitutionIds must be a list for authorityId '{aid}'")
        out[aid] = rel
    return out


def main():
    intl = load_json(INTL)
    cats = load_json(CATS)
    types = load_json(TYPES)
    i18n = load_json(I18N)
    national = [load_json(p) for p in NATIONAL_FILES]

    assert intl.get("metadata", {}).get("lastVerified") == "2026-09-23", "metadata.lastVerified must be 2026-09-23"

    cat_ids = {c["id"] for c in cats["categories"]}
    type_ids = {t["id"] for t in types["institutionTypes"]}
    region_ids = {r["id"] for r in intl["regions"]}
    strings = i18n.get("strings", {})

    institutions = intl["institutions"]
    ids = [x["id"] for x in institutions]
    if len(ids) != len(set(ids)):
        raise AssertionError("Duplicate institution ids found")
    id_set = set(ids)

    for inst in institutions:
        if inst["category"] not in cat_ids:
            raise AssertionError(f"Unknown category '{inst['category']}' in {inst['id']}")
        if inst["institutionType"] not in type_ids:
            raise AssertionError(f"Unknown institutionType '{inst['institutionType']}' in {inst['id']}")
        if inst["region"] not in region_ids:
            raise AssertionError(f"Unknown region '{inst['region']}' in {inst['id']}")

        dkey = inst["descriptionKey"]
        if dkey not in strings:
            raise AssertionError(f"Missing i18n key '{dkey}'")
        for locale in ("it", "en"):
            if locale not in strings[dkey] or not strings[dkey][locale]:
                raise AssertionError(f"Missing locale '{locale}' for '{dkey}'")

        for rid in inst.get("relatedInstitutionIds", []):
            if rid not in id_set:
                raise AssertionError(f"Unresolved relatedInstitutionId '{rid}' in {inst['id']}")

    # category/type labels in i18n
    for c in cats["categories"]:
        for k in (c["labelKey"], c["descriptionKey"]):
            if k not in strings:
                raise AssertionError(f"Missing i18n key '{k}'")
            for locale in ("it", "en"):
                if locale not in strings[k] or not strings[k][locale]:
                    raise AssertionError(f"Missing {locale} translation for '{k}'")

    for t in types["institutionTypes"]:
        k = t["labelKey"]
        if k not in strings:
            raise AssertionError(f"Missing i18n key '{k}'")
        for locale in ("it", "en"):
            if locale not in strings[k] or not strings[k][locale]:
                raise AssertionError(f"Missing {locale} translation for '{k}'")

    roots = [collect_authorities(d) for d in national]

    # ensure mirrors keep same relation field content
    base = roots[0]
    for idx, r in enumerate(roots[1:], start=1):
        if r != base:
            raise AssertionError(f"National dataset mirror mismatch in {NATIONAL_FILES[idx]}")

    # ensure relatedAuthorityIds resolve
    for inst in institutions:
        for aid in inst.get("relatedAuthorityIds", []):
            if aid not in base:
                raise AssertionError(f"Unresolved relatedAuthorityId '{aid}' in {inst['id']}")

    # ensure national links point to valid institutions
    for aid, rel in base.items():
        for iid in rel:
            if iid not in id_set:
                raise AssertionError(f"Authority '{aid}' references unknown international institution '{iid}'")

    print("Validation passed:")
    print(f"- institutions: {len(institutions)}")
    print(f"- authority records with IDs: {len(base)}")


if __name__ == "__main__":
    main()
