# Fonti e metodologia

Dataset locali usati dall'app:

- `financial_authorities_database.json`
- `data/international_financial_institutions.json`
- `data/international_institutions_categories.json`
- `data/international_institutions_types.json`
- `data/international_institutions_i18n.json`

Metodo:

1. Caricamento asset locali (offline-first)
2. Validazione base JSON e gestione errori leggibili
3. Costruzione relazioni tra authorityId e relatedInternationalInstitutionIds
4. Fallback lingua: scelta utente -> en -> it -> fallback UI
