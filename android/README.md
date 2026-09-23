# App Android nativa (Kotlin + Compose)

Questa directory contiene la prima versione Android nativa del progetto, separata dal codice web.

## Requisiti

- Android Studio recente (Koala o successivo)
- JDK 17
- Android SDK 35

## Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- DataStore Preferences
- Dataset locali in `app/src/main/assets`

## Dataset integrati (offline)

- `app/src/main/assets/financial_authorities_database.json`
- `app/src/main/assets/data/international_financial_institutions.json`
- `app/src/main/assets/data/international_institutions_categories.json`
- `app/src/main/assets/data/international_institutions_types.json`
- `app/src/main/assets/data/international_institutions_i18n.json`

## Moduli e struttura

- `data/`: parser JSON, repository, preferenze
- `domain/`: modelli normalizzati
- `navigation/`: route e scaffold principale
- `ui/`: schermate Compose (countries, international, search, favorites, about)

## Build e test

Dalla cartella `android/`:

```bash
gradle :app:assembleDebug
gradle :app:testDebugUnitTest
```

## Note ambiente CI/sandbox

In ambiente sandbox senza plugin Android scaricabili da repository remoti, la build completa può non partire.
Il progetto resta comunque strutturato per Android Studio con file Gradle e sorgenti compilabili in un ambiente Android completo.
