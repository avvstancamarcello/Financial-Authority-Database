# International Financial Institutions Data Model

This folder introduces a structured dataset for international/supranational AML, FIU, supervision, investigation, and consumer-protection institutions, designed for Android/Kotlin + Jetpack Compose integration while preserving existing HTML pages.

## Files

- `international_financial_institutions.json` — canonical institution records.
- `international_institutions_categories.json` — category dictionary.
- `international_institutions_types.json` — institution type dictionary.
- `international_institutions_i18n.json` — i18n strings (currently `it` and `en`) for category/type labels and all institution descriptions.

## Canonical schema (`international_financial_institutions.json`)

Top-level keys:

- `schemaVersion`
- `generatedFrom`
- `metadata` (includes `lastVerified`, dictionary references, and unresolved references)
- `categories`
- `institutionTypes`
- `regions`
- `institutions`

Each institution record uses stable IDs and includes:

- `id`, `name`, `shortName`, `abbreviation`
- optional `italianName`
- `category`, `institutionType`
- `level` (`global`, `supranational`, `regional`, `national`)
- `region`, `country`
- `homepage` and optional official links (`mandateUrl`, `recommendationsUrl`, `membersUrl`, `listsUrl`, `alertsUrl`, `aboutUrl`, `contactsUrl`, `reportsUrl`)
- `descriptionKey` (must resolve in i18n)
- `relatedInstitutionIds` (must resolve to existing international IDs)
- `relatedAuthorityIds` (must resolve to national authority IDs)
- optional `scope`, `powers`, `source`

## ID conventions

### International institutions

- Lowercase snake_case IDs (for example: `fatf_gafi`, `egmont_group`, `iosco_i_scan`).
- IDs are intended to be stable and safe for app navigation/deep links.

### National authority cross-reference IDs

`financial_authorities_database.json` (root + mirrors) now includes on each `financial_authority` object:

- `authorityId`
- `relatedInternationalInstitutionIds`

`authorityId` convention:

- `{country_key_slug}__{authority_abbreviation_or_name_slug}`
- Example: `italy__consob`, `united_states_of_america__sec`

These are additive fields and do not remove or rename existing fields.

## Relationship semantics

- Links are **cross-references for navigation and context**.
- They are **not** automatic claims of legal hierarchy/subordination unless explicitly documented by source.
- FSRBs are represented as a **regional layer** between FATF/GAFI and national authorities for vertical global→regional→national navigation.

## Governance notes required by product scope

- **FATF/GAFI** is encoded as an intergovernmental standard-setting/evaluation body and explicitly not as a supranational legislature, police authority, or direct seizure/investigative authority.
- **Egmont Group** is encoded as a global FIU cooperation network.
- **UIF Italy** is encoded as Italy’s national FIU (not a global single FIU authority).
- Other countries have their own FIUs.

## Source provenance and verification

- Dataset seeded from `referenze.html` institutions and user-requested additions.
- Each institution includes a `source` object with `primary` URL and `lastVerified` date.
- `metadata.lastVerified` is fixed to `2026-09-23` for this release.

## Unresolved references

Some requested national links could not be resolved to current national dataset IDs and are listed in `metadata.unresolvedAuthorityReferences`.

Current unresolved items:

- Bank of Italy supervisory authority record (not present as standalone authority in `financial_authorities_database.json`) — official site: https://www.bancaditalia.it/
- Guardia di Finanza authority record (not present as standalone authority in `financial_authorities_database.json`) — official site: https://www.gdf.gov.it/it

## Synchronization rule (active mirrors)

Keep these files synchronized for national authority data changes:

- `/financial_authorities_database.json`
- `/APP/financial_authorities_database.json`
- `/DEPLOY_REGISTER/financial_authorities_database.json`

For this update, all three were updated with the same additive `authorityId` and `relatedInternationalInstitutionIds` fields.

## i18n fallback behavior for Android

Recommended string resolution order for `descriptionKey` and labels:

1. selected locale
2. English (`en`)
3. Italian (`it`)
4. a safe built-in fallback literal (if present in UI layer)

## Android consumption guidance

1. Load category/type dictionaries and i18n strings first.
2. Load institutions and resolve:
   - `category` against categories dictionary
   - `institutionType` against type dictionary
   - `descriptionKey` against i18n strings
3. Build graph edges from:
   - `relatedInstitutionIds`
   - `relatedAuthorityIds`
4. Use `level` + `region` to render vertical hierarchy:
   - global → regional/supranational → national
5. Read national authority links from `relatedInternationalInstitutionIds` in the national dataset.

## Validation command

Run:

```bash
python scripts/validate_international_data.py
```

The script verifies JSON parsing, uniqueness, cross-file references, i18n coverage (`it`/`en`), and national mirror consistency for cross-reference fields.
