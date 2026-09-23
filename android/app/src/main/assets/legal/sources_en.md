# Sources and methodology

Local datasets used by the app:

- `financial_authorities_database.json`
- `data/international_financial_institutions.json`
- `data/international_institutions_categories.json`
- `data/international_institutions_types.json`
- `data/international_institutions_i18n.json`

Method:

1. Local asset loading (offline-first)
2. Basic JSON validation with readable error handling
3. Relationship construction across authorityId and relatedInternationalInstitutionIds
4. Language fallback: user choice -> en -> it -> UI fallback
