# Registro verifiche di coerenza home page Authorithy

Sezione pubblica del wrapper [`index.html`](index.html#registro-verifiche) di
«AMEV — Verifica prima di pagare». Documenta i **controlli manuali** con cui un
revisore confronta la homepage registrata per ciascuna Authority nel dataset
`financial_authorities_database.json` (fonte autorevole di `authorityId`, nome e
homepage) con il sito effettivamente raggiunto.

Il registro è vuoto finché non vengono importate verifiche reali: nessun dato di
esempio, nessuna data o spunta inventata.
Le 20 caselle restano raggiungibili con la tastiera anche senza dati o JavaScript;
il filtro è disponibile solo dopo l'importazione di un ciclo reale.

## File

| File | Ruolo |
|---|---|
| `registro-verifiche.json` | Registro pubblico (unica fonte delle verifiche pubblicate). Generato dall'importer, non va modificato a mano. |
| `registro-verifiche-template.csv` | Modello CSV vuoto (solo intestazione), scaricabile dalla pagina. |
| `registro-verifiche.js` | Calcolo nel browser di stati, griglia del ciclo e filtro della tabella. |
| `index.html` | Wrapper: il blocco tra `REGISTRO-VERIFICHE:STATIC:BEGIN/END` è rigenerato dall'importer, così i dati sono presenti anche nell'HTML servito inizialmente. |
| `../scripts/import_verification_register.py` | Importer offline (solo libreria standard Python ≥ 3.9; su Windows `zoneinfo` richiede il pacchetto `tzdata`). |
| `../scripts/test_import_verification_register.py` | Test dell'importer (`python -m unittest scripts/test_import_verification_register.py`). |
| `../test-amev-pages.html` | Test nel browser del wrapper, della guida e del registro. |

Il dataset delle Authority non cambia schema: il registro fa riferimento agli
`authorityId` esistenti. Il totale mostrato deriva sempre dal dataset (oggi 239
record); non è mai scritto a mano.

## Cicli, giorni e fuso orario

- Un ciclo dura **20 giorni**; le caselle `1/20` … `20/20` sono **giorni del ciclo**,
  non giorni del mese. Le date di calendario compaiono solo se un ciclo con data di
  inizio esplicita (`cycleStartDate`) è pubblicato.
- Fuso convenzionale: **Europe/Rome**. Il giorno del ciclo è
  `data locale di reviewedAt a Roma − cycleStartDate + 1` e deve coincidere con
  `cycleDay` (verificato dall'importer e di nuovo dal browser).
- `cycleId` identifica il ciclo (lettere, cifre, `.`, `_`, `-`; max 40; consigliato
  `AAAA-Cnn`, es. `2026-C01`). Uno stesso `cycleId` ha sempre la stessa data di
  inizio; cicli diversi non possono sovrapporsi. I cicli storici restano separati e
  selezionabili; le loro verifiche non vengono mai cancellate.
- Obiettivo: **12 controlli al giorno** (12 × 20 = 240 è aritmetica dell'obiettivo,
  non il numero di Authority). Le Authority ancora da controllare restano visibili
  come arretrato.

## Stati

Stato del giorno del ciclo (conta **Authority distinte**: un ricontrollo nello
stesso giorno non viene contato due volte; vale l'ultimo esito del giorno):

| Stato | Regola |
|---|---|
| Nessuna verifica documentata | nessun record |
| Parziale | da 1 a 11 Authority, tutte con esito positivo |
| Completato | almeno 12 Authority, tutte con esito positivo |
| Con criticità | almeno un ultimo esito non positivo |

«Completato» indica i **tentativi registrati**, non che tutte le Authority siano
verificate: la tabella distingue sempre esito e stato attuale.

Stato attuale della Authority (calcolato nel browser a ogni apertura e ogni minuto,
quindi scade senza nuovi commit):

| Stato | Regola |
|---|---|
| ✓ Verificata (verde) | ultimo controllo `verified`, più recente di 20 giorni (scade esattamente a 20 × 24 h) e `checkedUrl` uguale alla homepage **attuale** del dataset |
| Da rinnovare | ultimo controllo positivo, ma di 20 o più giorni fa |
| ⚠ Attenzione | ultimo controllo `needs_review`, `unreachable` o `blocked`, oppure URL cambiato nel dataset; l'ultimo esito positivo resta nello storico |
| Nessuna verifica documentata | nessun record |

## Schema CSV (export Airtable)

Intestazione obbligatoria, esattamente queste colonne (ordine libero, nessuna
colonna aggiuntiva: nascondere nella vista Airtable i campi interni):

```
authorityId,cycleId,cycleStartDate,cycleDay,reviewedAt,status,checkedUrl,finalUrl,reviewedBy,notes,evidenceRef
```

| Campo | Regole |
|---|---|
| `authorityId` | ID esistente in `financial_authorities_database.json` (es. `albania__boa`). |
| `cycleId` | vedi sopra. |
| `cycleStartDate` | `AAAA-MM-GG`, data reale. |
| `cycleDay` | intero 1–20, coerente con `reviewedAt` (Europe/Rome). |
| `reviewedAt` | ISO 8601 con fuso esplicito: `2026-10-06T10:15:00+02:00` o `…Z`; niente frazioni di secondo; mai nel futuro. In Airtable si può usare un campo formula: `DATETIME_FORMAT(SET_TIMEZONE({Data verifica}, 'Europe/Rome'), 'YYYY-MM-DDTHH:mm:ssZ')`. |
| `status` | `verified` (coerente), `needs_review` (da approfondire), `unreachable` (non raggiungibile), `blocked` (accesso bloccato, es. geoblocco o anti-bot). |
| `checkedUrl` | URL aperto: deve coincidere con la homepage del dataset (confronto su schema/host minuscoli, porta predefinita e `/` finale della radice). Solo `http`/`https`, senza credenziali. |
| `finalUrl` | URL dopo eventuali reindirizzamenti; obbligatorio per `verified`. Solo `http`/`https`. |
| `reviewedBy` | nome pubblico o sigla del revisore (max 80 caratteri, una riga, **niente email**). |
| `notes` | facoltativo per `verified`, obbligatorio negli altri casi; max 1000 caratteri; righe multiple ammesse. |
| `evidenceRef` | facoltativo: URL pubblico `http(s)` (es. copia Web Archive) o riferimento testuale (es. ID record Airtable). Altri schemi (`javascript:`, `data:` …) sono rifiutati. |

Formato file: UTF-8 (BOM ammesso), separatore virgola, campi tra virgolette con
virgole, virgolette raddoppiate e a capo gestiti dal modulo `csv` di Python. Le
righe completamente vuote sono ignorate. Per prevenire formule nei fogli di
calcolo, i testi che iniziano con `=`, `+`, `@` o `-` (salvo «- » da elenco) sono
rifiutati; i caratteri di controllo sono rifiutati.
Anche i record già pubblicati vengono ricontrollati per escludere date future;
nel browser date inesistenti, revisori mancanti, URL finali assenti per esiti
positivi e note mancanti per esiti problematici vengono scartati e segnalati.

Duplicati: una verifica è identificata da `authorityId` + istante `reviewedAt`.
Una riga identica a una già registrata è ignorata (reimportare lo stesso export
non cambia nulla); una riga con stessa chiave ma contenuto diverso è un
**conflitto** e blocca l'importazione: lo storico non viene mai sovrascritto.

## Sicurezza e privacy

- Tutto il testo importato è trattato come non attendibile: l'importer applica
  l'escape HTML, il browser usa solo `textContent`; i link sono creati solo per
  URL `http(s)` e aprono una nuova scheda con `rel="noopener noreferrer nofollow"`.
- Nessuna credenziale, API o integrazione Airtable: l'importer lavora su file
  locali. I visitatori del sito non possono modificare o caricare dati.
- Non inserire dati personali: niente email, telefoni o nomi completi non
  pubblici; le evidenze non devono essere screenshot con dati personali.
- Una base Airtable condivisa è solo un accesso facoltativo per persone: non è
  necessaria alla tabella pubblica e non ne è garantita l'indicizzazione.

## Procedura quotidiana

1. Elencare le Authority da controllare (prima quelle mai documentate, poi quelle
   con criticità, poi le verifiche più vecchie):
   `python scripts/import_verification_register.py --due 12`
2. Controllare manualmente le 12 homepage: aprire l'URL del dataset, verificare
   che il sito appartenga all'Authority indicata, annotare l'URL finale ed
   eventuali anomalie.
3. Registrare i controlli in Airtable con i campi dello schema.
4. Esportare la vista in CSV.
5. Validare senza scrivere: `python scripts/import_verification_register.py export.csv --check`
6. Importare: `python scripts/import_verification_register.py export.csv`
   (se la validazione fallisce, nessun file viene modificato). L'importer aggiorna
   `registro-verifiche.json` e il blocco statico di `index.html` con output
   deterministico.
7. Rivedere il diff, eseguire i test, fare commit e pubblicare con la pipeline
   esistente (GitHub Pages).

Dopo modifiche al dataset (es. nuova homepage o nuova Authority) eseguire
`python scripts/import_verification_register.py --render` per riallineare il
blocco statico; `--check` senza CSV termina con codice 1 se registro o HTML sono
da rigenerare.

## Limiti

- Una verifica positiva attesta la coerenza della homepage ufficiale al momento
  del controllo, non l'aggiornamento di tutti i contenuti esterni, né
  autorizzazioni di operatori finanziari, né certificazioni di Google o di altri
  motori di ricerca, né la permanenza di URL o indirizzi IP.
- L'export Airtable prova che la verifica è stata registrata da un revisore; non è
  una certificazione indipendente.
- I metadati di pubblicazione mostrati («Ultima verifica registrata») derivano dai
  record; la pagina non usa l'ora della visita come data di modifica del dataset.
- Fuori ambito per ora: spunte nei modal Authority della homepage e integrazione
  nell'App Android. Potranno riutilizzare `registro-verifiche.json` e le stesse
  regole di stato.
