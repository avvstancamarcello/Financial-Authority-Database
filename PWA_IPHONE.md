# iPhone Safari WebApp / PWA

## URL di test
- GitHub Pages: `https://avvstancamarcello.github.io/Financial-Authority-Database/`
- Pagina canonica del progetto: repository-root `index.html`

## Test rapido su iPhone Safari
1. Apri la URL GitHub Pages in **Safari** su iPhone.
2. Verifica che header, dialog, footer e pulsanti non finiscano sotto notch o home indicator.
3. Apri la guida integrata nella sezione app e controlla il blocco **iPhone / Safari WebApp-PWA**.
4. Tocca **Condividi** → **Aggiungi a Home**.
5. Conferma il nome **FinAuthority** e completa l’aggiunta.
6. Apri l’icona dalla Home Screen e verifica che il launch avvenga sotto `/Financial-Authority-Database/`.
7. Dopo il primo caricamento, riapri l’app con rete disattivata per verificare l’offline dei contenuti già messi in cache.

## Aggiornare o sbloccare una WebApp installata
- Per forzare il controllo aggiornamenti: apri la pagina in Safari con rete attiva e attendi il refresh.
- Se la Home Screen app mostra contenuti vecchi, chiudila completamente e riaprila.
- Se serve un reset completo: iPhone **Impostazioni → Safari → Avanzate → Dati siti web** oppure rimuovi l’icona Home Screen e aggiungila di nuovo da Safari.
- Quando si modificano asset shell o strategia cache, incrementare `CACHE_NAME` in `service-worker.js`.

## Limitazioni rispetto a una app App Store
- Nessun file `.ipa` viene generato da questo repository.
- L’installazione iPhone dipende da **Safari** e dal comando **Aggiungi a Home**.
- Alcune funzioni native iOS restano limitate rispetto a una app Swift/App Store.
- L’offline copre la shell statica e i contenuti già memorizzati nella cache del service worker.

## Controlli eseguiti in questa verifica
- Verifica metadata/PWA in `index.html`, `manifest.json` e `service-worker.js`.
- Controllo coerenza path GitHub Pages `/Financial-Authority-Database/`.
- Validazione JSON del manifest.
- Verifica statica locale delle principali risorse referenziate.

## Limiti ambiente
- In questa sandbox non è disponibile un iPhone reale né Safari iOS per un test end-to-end hardware.
- La validazione è stata quindi eseguita tramite revisione statica, controlli dei file e test locali da CLI.
