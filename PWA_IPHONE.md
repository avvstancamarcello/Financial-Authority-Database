# iPhone Safari WebApp / PWA

## URL di test
- Produzione: `https://tutelatruffe.it/`
- Manifest, service worker e icone usano percorsi dalla root `/` del dominio di produzione, non il sottopercorso GitHub Pages.
- Pagina canonica del progetto: repository-root `index.html`

## Test rapido su iPhone Safari
1. Apri la URL di produzione in **Safari** su iPhone.
2. Verifica che header, dialog, footer e pulsanti non finiscano sotto notch o home indicator.
3. Apri la guida integrata nella sezione app e controlla il blocco **iPhone / Safari WebApp-PWA**.
4. Tocca **Condividi** → **Aggiungi a Home**.
5. Conferma il nome **Financial Authority Database** e completa l’aggiunta.
6. Apri l’icona dalla Home Screen e verifica che il launch avvenga alla root `/` del dominio.
7. Dopo il primo caricamento, riapri l’app con rete disattivata per verificare l’offline dei contenuti già messi in cache.

## Android e regressione locale
- Su Android Chrome, verifica **Menu → Installa app** e il nome **Financial Authority Database**. Il browser decide quando mostrare il prompt automatico; Safari iPhone non supporta quel prompt.
- Verifica anche il nome nella scheda di condivisione di Safari. Le anteprime già memorizzate dal dispositivo possono richiedere un aggiornamento.
- Per il test locale: esegui `python3 -m http.server 8000` dalla root del repository e apri `http://localhost:8000/test-pwa.html`.
- Il test controlla manifest, metadata delle tre pagine, icone, mancata soppressione del prompt Android, scope `/` e precache della shell.
- In DevTools verifica il service worker attivo, abilita **Network → Offline** e ricarica `/` per controllare il fallback alla homepage memorizzata.

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
- Controllo coerenza dei percorsi di produzione `/`.
- Validazione JSON del manifest.
- Verifica statica locale delle principali risorse referenziate.

## Limiti ambiente
- In questa sandbox non è disponibile un iPhone reale né Safari iOS per un test end-to-end hardware.
- La validazione è stata quindi eseguita tramite revisione statica, controlli dei file e test locali da CLI.
