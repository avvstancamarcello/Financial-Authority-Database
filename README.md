# Financial Authority Database – Regole di Progetto

Questo documento descrive le **Best Practice "Copyright Marcello Stanca"**
che hanno guidato la realizzazione della web page `index.html` e, più in generale,
la collaborazione tra Utente e AI in questo progetto.

Le regole sono espresse sia in linguaggio naturale sia in forma di
**asserzioni Prolog**, così da poterle utilizzare come base per motori di
regole, validatori automatici o strumenti di analisi statica.

---

## 1. Elenco sintetico delle 21 Regole

### Sezione 1 – Architettura e Visione

1. **Visione d'Insieme**  
   Definizione dell'architettura logica prima della stesura del codice.

2. **Astrazione Funzionale**  
   Separare i concetti di business dalla logica di esecuzione tecnica.

3. **Memoria del Successo**  
   Archiviare le soluzioni funzionanti per creare una base di conoscenza solida.

4. **Comunicazione Specifica**  
   Precisione assoluta nei termini tecnici per evitare ambiguità.

4.1 **Notifica di Aggiornamento**  
     Protocollo di conferma per ogni modifica apportata alla memoria.

4.2 **Comunicazione della Regola Applicata**  
     Ogni azione dell'AI deve citare la regola che la giustifica.

### Sezione 2 – Sicurezza e Validazione (Protocollo Web3)

5. **Sicurezza Nativa**  
   Difesa preventiva contro attacchi comuni (Reentrancy, Overflow, ecc.).

6. **Validazione Rigorosa degli Input**  
   Nessun dato esterno è considerato sicuro senza controllo.

7. **Garanzia di Continuità**  
   Verifica dello stato del sistema all'inizio di ogni nuova sessione.

8. **Modularità Estrema**  
   Scomposizione in micro-servizi o smart contract indipendenti.

### Sezione 3 – Testing e Qualità

9. **Testing Multi-Scenario**  
   Inclusione obbligatoria di edge case e scenari di errore.

10. **Iterazione Incrementale**  
    Sviluppo a piccoli passi con test di conferma intermedi.

11. **Trasparenza dell'Errore**  
    Analisi profonda dei fallimenti per l'ottimizzazione del codice.

12. **Ottimizzazione del Gas**  
    Scrittura di codice efficiente per minimizzare i costi on-chain.

### Sezione 4 – Logica Economica e Tokenomics

13. **Equilibrio di Distribuzione**  
    Applicazione della formula 55% NFT / 45% FT.

14. **Precisione Matematica**  
    Utilizzo di librerie/protocolli certificati per calcoli finanziari.

15. **Tracciabilità dei Flussi**  
    Ogni movimento di token deve essere emesso tramite eventi chiari.

### Sezione 5 – Filosofia Marcello Stanca (Resilienza)

16. **Perseveranza Metodica**  
    Approccio costante alla risoluzione di problemi complessi.

17. **Il Gas-Carburante (Il Riposo)**  
    Il riposo è parte integrante e necessaria della perseveranza.

18. **Evoluzione Continua**  
    Aggiornamento costante delle regole in base alle nuove scoperte tecnologiche.

19. **Integrità del Metodo**  
    Il framework deve essere applicato nella sua interezza per essere efficace.

Le seguenti formulazioni riassumono e completano le regole precedenti,
arrivando al set totale di 21 voci operative:

20. **Ingegneria Gestionale & Gestione Contestuale**  
    Guida della struttura della risposta, auto-modulazione dell'errore e
    applicazione trasparente della regola pertinente al contesto.

21. **Ottimizzazione e Tracciabilità**  
    Ottimizzazione della latenza e delle risorse, tracciabilità di ogni azione
    e garanzia di continuità sulla roadmap.

Concetti chiave collegati:
- *Memoria del Successo*: archiviare i risultati raggiunti per evitare ridondanze.  
- *Addomesticare*: creazione della relazione funzionale tra Utente e AI.  
- *Compito Primario*: facilitare e alleggerire il lavoro umano in ogni interazione.

---

## 2. Asserzioni Prolog

Di seguito un possibile modello di rappresentazione delle regole in Prolog.

### 2.1. Schema logico

Usiamo il predicato principale:

- `ms_rule/6`:
  - `ms_rule(Id, Numero, Sezione, Etichetta, Categoria, Descrizione).`

Dove:
- `Id` è un identificatore simbolico (`r1`, `r4_1`, ecc.).
- `Numero` è il numero umano della regola (1, 4.1, 13, ...).
- `Sezione` appartiene a: `architettura_visione`, `sicurezza_web3`,
  `testing_qualita`, `tokenomics`, `filosofia_resilienza`.
- `Categoria` è un tag sintetico che permette ricerche rapide.

### 2.2. Codifica delle regole

```prolog
% Sezione 1 – Architettura e Visione

ms_rule(r1, 1, architettura_visione,
        'Visione d\'Insieme',
        architettura,
        'Definizione dell\'architettura logica prima della stesura del codice.').

ms_rule(r2, 2, architettura_visione,
        'Astrazione Funzionale',
        astrazione_business,
        'Separare i concetti di business dalla logica di esecuzione tecnica.').

ms_rule(r3, 3, architettura_visione,
        'Memoria del Successo',
        memoria_successo,
        'Archiviare le soluzioni funzionanti per creare una base di conoscenza solida.').

ms_rule(r4, 4, architettura_visione,
        'Comunicazione Specifica',
        comunicazione_precisa,
        'Precisione assoluta nei termini tecnici per evitare ambiguità.').

ms_rule(r4_1, 4.1, architettura_visione,
        'Notifica di Aggiornamento',
        notifica_memoria,
        'Protocollo di conferma per ogni modifica apportata alla memoria.').

ms_rule(r4_2, 4.2, architettura_visione,
        'Comunicazione della Regola Applicata',
        regola_esplicita,
        'Ogni azione dell\'AI deve citare la regola che la giustifica.').


% Sezione 2 – Sicurezza e Validazione (Protocollo Web3)

ms_rule(r5, 5, sicurezza_web3,
        'Sicurezza Nativa',
        sicurezza_preventiva,
        'Difesa preventiva contro attacchi comuni (Reentrancy, Overflow, ecc.).').

ms_rule(r6, 6, sicurezza_web3,
        'Validazione Rigorosa degli Input',
        validazione_input,
        'Nessun dato esterno è considerato sicuro senza controllo.').

ms_rule(r7, 7, sicurezza_web3,
        'Garanzia di Continuità',
        continuita_stato,
        'Verifica dello stato del sistema all\'inizio di ogni nuova sessione.').

ms_rule(r8, 8, sicurezza_web3,
        'Modularità Estrema',
        modularita,
        'Scomposizione in micro-servizi o smart contract indipendenti.').


% Sezione 3 – Testing e Qualità

ms_rule(r9, 9, testing_qualita,
        'Testing Multi-Scenario',
        edge_case_testing,
        'Inclusione obbligatoria di edge case e scenari di errore.').

ms_rule(r10, 10, testing_qualita,
        'Iterazione Incrementale',
        sviluppo_incrementale,
        'Sviluppo a piccoli passi con test di conferma intermedi.').

ms_rule(r11, 11, testing_qualita,
        'Trasparenza dell\'Errore',
        analisi_errori,
        'Analisi profonda dei fallimenti per l\'ottimizzazione del codice.').

ms_rule(r12, 12, testing_qualita,
        'Ottimizzazione del Gas',
        efficienza_gas,
        'Scrittura di codice efficiente per minimizzare i costi on-chain.').


% Sezione 4 – Logica Economica e Tokenomics

ms_rule(r13, 13, tokenomics,
        'Equilibrio di Distribuzione',
        distribuzione_55_45,
        'Applicazione della formula 55% NFT / 45% FT.').

ms_rule(r14, 14, tokenomics,
        'Precisione Matematica',
        calcolo_sicuro,
        'Utilizzo di librerie o protocolli certificati per calcoli finanziari.').

ms_rule(r15, 15, tokenomics,
        'Tracciabilità dei Flussi',
        eventi_token,
        'Ogni movimento di token deve essere emesso tramite eventi chiari.').


% Sezione 5 – Filosofia Marcello Stanca (Resilienza)

ms_rule(r16, 16, filosofia_resilienza,
        'Perseveranza Metodica',
        perseveranza,
        'Approccio costante alla risoluzione di problemi complessi.').

ms_rule(r17, 17, filosofia_resilienza,
        'Il Gas-Carburante (Il Riposo)',
        riposo_strategico,
        'Il riposo è parte integrante e necessaria della perseveranza.').

ms_rule(r18, 18, filosofia_resilienza,
        'Evoluzione Continua',
        aggiornamento_regole,
        'Aggiornamento costante delle regole in base alle nuove scoperte tecnologiche.').

ms_rule(r19, 19, filosofia_resilienza,
        'Integrità del Metodo',
        metodo_olistico,
        'Il framework deve essere applicato nella sua interezza per essere efficace.').


% Regole di sintesi gestionale (20–21)

ms_rule(r20, 20, filosofia_resilienza,
        'Ingegneria Gestionale & Gestione Contestuale',
        gestione_contesto,
        'Guida della struttura della risposta, auto-modulazione dell\'errore e applicazione trasparente della regola pertinente al contesto.').

ms_rule(r21, 21, filosofia_resilienza,
        'Ottimizzazione e Tracciabilità',
        ottimizzazione_tracciabilita,
        'Ottimizzazione della latenza e delle risorse, tracciabilità di ogni azione e garanzia di continuità sulla roadmap.').


% Concetti ausiliari (memoria del successo, addomesticare, ecc.)

concetto(ms_memoria_successo,
         'Memoria del Successo',
         'Archiviare i risultati raggiunti per evitare ridondanze e guidare decisioni future.').

concetto(ms_addomesticare,
         'Addomesticare',
         'Creazione della relazione funzionale tra Utente e AI.').

concetto(ms_compito_primario,
         'Compito Primario',
         'Facilitare e alleggerire il lavoro umano in ogni interazione.').

concetto(ms_oculare_gioielliere,
         'Oculare del Gioielliere',
         'Analisi chirurgica su sintassi, codice e dati tecnici.').

concetto(ms_garanzia_continuita,
         'Garanzia di Continuità',
         'Focus costante sulla roadmap per evitare dispersioni.').
```

---

## 3. Esempi di Query Prolog

Esempi di utilizzo pratico nel motore Prolog:

```prolog
% Trovare tutte le regole di sicurezza Web3
?- ms_rule(Id, Num, sicurezza_web3, Etichetta, Categoria, Descrizione).

% Ottenere la descrizione della regola sulla Memoria del Successo
?- ms_rule(Id, _, _, 'Memoria del Successo', _, Descrizione).

% Elencare tutti i concetti ausiliari definiti
?- concetto(Id, Nome, Testo).
```

Questa codifica fornisce una base formale per:
- documentare il metodo usato nello sviluppo della web page,
- costruire strumenti automatici di verifica (linting semantico),
- guidare altre AI/agent nella replica dello stesso stile di lavoro.
