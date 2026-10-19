# Backup prima di FINMA CH

Copie byte-per-byte dal commit `a061852eb8091bb8ab76bf8c5fe79f976f6867b1`, create prima delle modifiche applicative.

SHA-256:

- `index.html`: `8ad241c84bff48b0cc2273fed5c13c1def857c1599fb2193ca0a98a745991dcd`
- `financial_authorities_database.json`: `3b9fd8fb5a0c5738287974ab5607f02d707543a0871034ce2a99a0a4b08d1b1c`

Per ripristinare, dalla radice del repository:

```sh
cp Backup/pre-finma-2026-10-19-a061852/index.html index.html
cp Backup/pre-finma-2026-10-19-a061852/financial_authorities_database.json financial_authorities_database.json
sha256sum index.html financial_authorities_database.json
```

Il backup HTML in questa sottocartella non è una pagina autonoma: i percorsi relativi funzionano dopo il ripristino nella posizione originale. Il database applicativo deve rimanere byte-identico; il ripristino di questi due file non rimuove le risorse FINMA aggiunte separatamente. Per servire subito la homepage ripristinata ai client PWA, incrementare anche `CACHE_NAME` in `service-worker.js`.
