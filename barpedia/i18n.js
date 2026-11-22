(function () {
  'use strict';

  var STORAGE_KEY = 'barpediaLang';
  var COUNTRY_KEYS = ['uk', 'ch', 'es', 'de', 'it', 'us'];
  var IT_COUNTRIES = { 'Regno Unito': 'uk', 'Svizzera': 'ch', 'Spagna': 'es', 'Germania': 'de', 'Italia': 'it', 'USA': 'us' };

  // Titoli, navigazione e avviso sono tradotti; il testo dei paragrafi resta in italiano.
  var T = {
    en: {
      name: 'English',
      c: ['United Kingdom', 'Switzerland', 'Spain', 'Germany', 'Italy', 'USA'],
      notice: 'Titles and navigation are translated. The full text of these pages is in Italian.',
      back: '← Back to Bar-pedia – Lawyers Worldwide', home: 'Back to the AMEV home page',
      equiv: 'AMEV Equivalence page', paese: 'AMEV country page',
      method: 'AMEV methodological note', todo: 'What to do if the lawyer is not listed',
      todoContacts: 'What to do if the lawyer is not listed, or the contacts do not match',
      perCountry: 'Verification by country – official Bar / Register',
      fifty: 'The 50 US States – where all the links are',
      grid: 'Go to the grid – Financial Authority Database – 50 US States →',
      brand: 'AMEV – Lawyers Worldwide Effective Verification',
      second: 'Have you been scammed? Beware the second scam.',
      scam: 'Warning: the fake lawyer scam – {c}', red: '5 red flags of the fake lawyer – {c}',
      official: 'Official verification – direct links to sources: {c}',
      steps: 'Effective Verification {k} – {n} steps'
    },
    es: {
      name: 'Español',
      c: ['Reino Unido', 'Suiza', 'España', 'Alemania', 'Italia', 'EE. UU.'],
      notice: 'Los títulos y la navegación están traducidos. El texto completo de estas páginas está en italiano.',
      back: '← Volver a Bar-pedia – Abogados en el Mundo', home: 'Volver a la página de inicio AMEV',
      equiv: 'Página de equivalencia AMEV', paese: 'Ficha país AMEV',
      method: 'Nota metodológica AMEV', todo: 'Qué hacer si el abogado no figura',
      todoContacts: 'Qué hacer si el abogado no figura o los contactos no coinciden',
      perCountry: 'Verificación por país – Colegio / Registro oficial',
      fifty: 'Los 50 Estados de EE. UU. – dónde están todos los enlaces',
      grid: 'Ir a la cuadrícula – Financial Authority Database – 50 Estados de EE. UU. →',
      brand: 'AMEV – Abogados en el Mundo Verificación Efectiva',
      second: '¿Te han estafado? Cuidado con la segunda estafa.',
      scam: 'Atención: la estafa del falso abogado – {c}', red: '5 señales de alerta del falso abogado – {c}',
      official: 'Verificación oficial – enlaces directos a las fuentes: {c}',
      steps: 'Verificación Efectiva {k} – {n} pasos'
    },
    fr: {
      name: 'Français',
      c: ['Royaume-Uni', 'Suisse', 'Espagne', 'Allemagne', 'Italie', 'États-Unis'],
      notice: 'Les titres et la navigation sont traduits. Le texte complet de ces pages est en italien.',
      back: '← Retour à Bar-pedia – Avocats dans le Monde', home: "Retour à la page d'accueil AMEV",
      equiv: "Page d'équivalence AMEV", paese: 'Fiche pays AMEV',
      method: 'Note méthodologique AMEV', todo: "Que faire si l'avocat n'apparaît pas",
      todoContacts: "Que faire si l'avocat n'apparaît pas ou si les coordonnées ne correspondent pas",
      perCountry: 'Vérification par pays – Barreau / registre officiel',
      fifty: 'Les 50 États américains – où trouver tous les liens',
      grid: 'Aller à la grille – Financial Authority Database – 50 États américains →',
      brand: 'AMEV – Avocats dans le Monde Vérification Effective',
      second: 'Vous avez été arnaqué ? Méfiez-vous de la deuxième arnaque.',
      scam: "Attention : l'arnaque du faux avocat – {c}", red: "5 signaux d'alerte du faux avocat – {c}",
      official: 'Vérification officielle – liens directs vers les sources : {c}',
      steps: 'Vérification effective {k} – {n} étapes'
    },
    de: {
      name: 'Deutsch',
      c: ['Vereinigtes Königreich', 'Schweiz', 'Spanien', 'Deutschland', 'Italien', 'USA'],
      notice: 'Titel und Navigation sind übersetzt. Der vollständige Text dieser Seiten ist auf Italienisch.',
      back: '← Zurück zu Bar-pedia – Anwälte weltweit', home: 'Zurück zur AMEV-Startseite',
      equiv: 'AMEV-Äquivalenzseite', paese: 'AMEV-Länderseite',
      method: 'Methodischer Hinweis AMEV', todo: 'Was tun, wenn der Anwalt nicht aufgeführt ist',
      todoContacts: 'Was tun, wenn der Anwalt nicht aufgeführt ist oder die Kontaktdaten nicht übereinstimmen',
      perCountry: 'Prüfung nach Land – offizielle Kammer / Verzeichnis',
      fifty: 'Die 50 US-Bundesstaaten – wo alle Links zu finden sind',
      grid: 'Zum Raster – Financial Authority Database – 50 US-Bundesstaaten →',
      brand: 'AMEV – Anwälte weltweit Effektive Verifizierung',
      second: 'Wurden Sie betrogen? Vorsicht vor dem zweiten Betrug.',
      scam: 'Achtung: der Betrug mit dem falschen Anwalt – {c}', red: '5 Warnsignale des falschen Anwalts – {c}',
      official: 'Offizielle Prüfung – Direktlinks zu den Quellen: {c}',
      steps: 'Effektive Verifizierung {k} – {n} Schritte'
    },
    pt: {
      name: 'Português',
      c: ['Reino Unido', 'Suíça', 'Espanha', 'Alemanha', 'Itália', 'EUA'],
      notice: 'Os títulos e a navegação estão traduzidos. O texto completo destas páginas está em italiano.',
      back: '← Voltar à Bar-pedia – Advogados no Mundo', home: 'Voltar à página inicial AMEV',
      equiv: 'Página de equivalência AMEV', paese: 'Ficha do país AMEV',
      method: 'Nota metodológica AMEV', todo: 'O que fazer se o advogado não constar',
      todoContacts: 'O que fazer se o advogado não constar ou os contactos não coincidirem',
      perCountry: 'Verificação por país – Ordem / registo oficial',
      fifty: 'Os 50 Estados dos EUA – onde estão todos os links',
      grid: 'Ir para a grelha – Financial Authority Database – 50 Estados dos EUA →',
      brand: 'AMEV – Advogados no Mundo Verificação Efetiva',
      second: 'Você foi enganado? Cuidado com o segundo golpe.',
      scam: 'Atenção: o golpe do falso advogado – {c}', red: '5 sinais de alerta do falso advogado – {c}',
      official: 'Verificação oficial – links diretos para as fontes: {c}',
      steps: 'Verificação Efetiva {k} – {n} passos'
    },
    nl: {
      name: 'Nederlands',
      c: ['Verenigd Koninkrijk', 'Zwitserland', 'Spanje', 'Duitsland', 'Italië', 'VS'],
      notice: 'Titels en navigatie zijn vertaald. De volledige tekst van deze pagina’s is in het Italiaans.',
      back: '← Terug naar Bar-pedia – Advocaten wereldwijd', home: 'Terug naar de AMEV-startpagina',
      equiv: 'AMEV-equivalentiepagina', paese: 'AMEV-landenpagina',
      method: 'Methodologische noot AMEV', todo: 'Wat te doen als de advocaat niet vermeld staat',
      todoContacts: 'Wat te doen als de advocaat niet vermeld staat of de contactgegevens niet kloppen',
      perCountry: 'Verificatie per land – officiële balie / register',
      fifty: 'De 50 Amerikaanse staten – waar alle links staan',
      grid: 'Naar het raster – Financial Authority Database – 50 Amerikaanse staten →',
      brand: 'AMEV – Advocaten wereldwijd Effectieve Verificatie',
      second: 'Bent u opgelicht? Pas op voor de tweede oplichting.',
      scam: 'Let op: de oplichting met de nepadvocaat – {c}', red: '5 waarschuwingssignalen van de nepadvocaat – {c}',
      official: 'Officiële verificatie – directe links naar de bronnen: {c}',
      steps: 'Effectieve verificatie {k} – {n} stappen'
    },
    pl: {
      name: 'Polski',
      c: ['Wielka Brytania', 'Szwajcaria', 'Hiszpania', 'Niemcy', 'Włochy', 'USA'],
      notice: 'Tytuły i nawigacja są przetłumaczone. Pełny tekst tych stron jest po włosku.',
      back: '← Powrót do Bar-pedii – Prawnicy na Świecie', home: 'Powrót do strony głównej AMEV',
      equiv: 'Strona równoważności AMEV', paese: 'Strona kraju AMEV',
      method: 'Nota metodologiczna AMEV', todo: 'Co zrobić, jeśli adwokat nie figuruje w rejestrze',
      todoContacts: 'Co zrobić, jeśli adwokat nie figuruje w rejestrze lub dane kontaktowe się nie zgadzają',
      perCountry: 'Weryfikacja według kraju – oficjalna izba / rejestr',
      fifty: '50 stanów USA – gdzie są wszystkie linki',
      grid: 'Przejdź do siatki – Financial Authority Database – 50 stanów USA →',
      brand: 'AMEV – Prawnicy na Świecie Skuteczna Weryfikacja',
      second: 'Zostałeś oszukany? Uważaj na drugie oszustwo.',
      scam: 'Uwaga: oszustwo fałszywego adwokata – {c}', red: '5 sygnałów ostrzegawczych fałszywego adwokata – {c}',
      official: 'Oficjalna weryfikacja – bezpośrednie linki do źródeł: {c}',
      steps: 'Skuteczna weryfikacja {k} – {n} kroki'
    },
    ro: {
      name: 'Română',
      c: ['Regatul Unit', 'Elveția', 'Spania', 'Germania', 'Italia', 'SUA'],
      notice: 'Titlurile și navigarea sunt traduse. Textul complet al acestor pagini este în italiană.',
      back: '← Înapoi la Bar-pedia – Avocați în Lume', home: 'Înapoi la pagina principală AMEV',
      equiv: 'Pagina de echivalență AMEV', paese: 'Pagina de țară AMEV',
      method: 'Notă metodologică AMEV', todo: 'Ce să faci dacă avocatul nu apare în registru',
      todoContacts: 'Ce să faci dacă avocatul nu apare în registru sau datele de contact nu coincid',
      perCountry: 'Verificare pe țară – barou / registru oficial',
      fifty: 'Cele 50 de state SUA – unde găsești toate linkurile',
      grid: 'Mergi la grilă – Financial Authority Database – 50 de state SUA →',
      brand: 'AMEV – Avocați în Lume Verificare Efectivă',
      second: 'Ai fost înșelat? Atenție la a doua înșelătorie.',
      scam: 'Atenție: înșelătoria falsului avocat – {c}', red: '5 semnale de alarmă ale falsului avocat – {c}',
      official: 'Verificare oficială – linkuri directe către surse: {c}',
      steps: 'Verificare efectivă {k} – {n} pași'
    }
  };

  var page = (location.pathname.split('/').pop() || 'index.html').replace('.html', '');
  var pageKey = COUNTRY_KEYS.indexOf(page) >= 0 ? page : null;
  var lang = 'it';

  function countryName(L, key) { return L.c[COUNTRY_KEYS.indexOf(key)]; }

  function translate(L, src) {
    var m;
    if (src === 'AMEV - Avvocati nel Mondo Effettiva Verifica') return L.brand;
    if (src === 'Sei stato truffato? Attento alla seconda truffa.') return L.second;
    if (src === 'Nota metodologica AMEV') return L.method;
    if (src === 'Cosa fare se non risulta') return L.todo;
    if (src === 'Cosa fare se non risulta, o se i contatti non coincidono') return L.todoContacts;
    if (src === 'Verifica per Paese - Bar / Albo Ufficiale') return L.perCountry;
    if (src === 'I 50 Stati USA - Dove sono tutti i link') return L.fifty;
    if (src === 'Vai alla griglia - Financial Authority Database - 50 Stati USA →') return L.grid;
    if (src === '← Torna a Bar-pedia - Avvocati nel Mondo') return L.back;
    if (src === 'Torna alla pagina iniziale AMEV') return L.home;
    if (src === 'Pagina Equivalenza AMEV' || src === 'Scheda Equivalenza AMEV') return L.equiv;
    if (src === 'Scheda Paese AMEV') return L.paese;
    if (src === 'Financial Authority Database - Autorità Finanziarie') return 'Financial Authority Database';
    if (pageKey && /^AMEV Bar-pedia - /.test(src)) return 'AMEV Bar-pedia - ' + countryName(L, pageKey);
    if (pageKey && /^Attenzione:/.test(src)) return L.scam.replace('{c}', countryName(L, pageKey));
    if (pageKey && /^5 segnali rossi/.test(src)) return L.red.replace('{c}', countryName(L, pageKey));
    if (pageKey && /^Verifica Ufficiale/.test(src)) return L.official.replace('{c}', countryName(L, pageKey));
    if ((m = src.match(/^Effettiva Verifica (\w+) - (\d) passi/))) return L.steps.replace('{k}', m[1]).replace('{n}', m[2]);
    if ((m = src.match(/^← Scheda (\w+) - /))) return '← ' + m[1] + ' – ' + countryName(L, m[1].toLowerCase());
    if ((m = src.match(/^(Regno Unito|Svizzera|Spagna|Germania|Italia|USA) - (.*)$/))) {
      return countryName(L, IT_COUNTRIES[m[1]]) + ' - ' + m[2];
    }
    return null;
  }

  var items = [];
  Array.prototype.forEach.call(document.querySelectorAll('h1, h2, a[href]'), function (el) {
    var href = el.getAttribute('href') || '';
    if (el.tagName === 'A' && /^https?:/.test(href)) return;
    if (el.children.length) return;
    items.push({ el: el, src: el.textContent.trim() });
  });

  var notice = document.createElement('p');
  var bar = document.createElement('nav');
  bar.setAttribute('aria-label', 'Language');
  var buttons = {};

  function addButton(code, label) {
    var b = document.createElement('button');
    b.type = 'button';
    b.textContent = label;
    b.lang = code;
    b.addEventListener('click', function () { apply(code); });
    buttons[code] = b;
    bar.appendChild(b);
    bar.appendChild(document.createTextNode(' '));
  }

  function apply(code) {
    var L = T[code];
    lang = L ? code : 'it';
    items.forEach(function (it) {
      var out = L ? translate(L, it.src) : null;
      it.el.textContent = out === null ? it.src : out;
    });
    document.documentElement.lang = lang;
    notice.textContent = L ? L.notice : '';
    notice.hidden = !L;
    Object.keys(buttons).forEach(function (c) { buttons[c].setAttribute('aria-pressed', String(c === lang)); });
    try { localStorage.setItem(STORAGE_KEY, lang); } catch (e) { /* storage non disponibile */ }
  }

  addButton('it', 'Italiano');
  Object.keys(T).forEach(function (c) { addButton(c, T[c].name); });
  document.body.insertBefore(notice, document.body.firstChild);
  document.body.insertBefore(bar, document.body.firstChild);

  var wanted = null;
  try {
    wanted = (new URLSearchParams(location.search).get('lang') || localStorage.getItem(STORAGE_KEY) || '').toLowerCase();
  } catch (e) { wanted = null; }
  apply(wanted && (wanted === 'it' || T[wanted]) ? wanted : 'it');
})();
