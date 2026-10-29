(() => {
  'use strict';

  const ids = [
    'austria', 'benin', 'bulgaria', 'ethiopia-ecma', 'ethiopia-guide', 'france',
    'germany', 'iosco', 'italy', 'nunavut', 'polynesia', 'uk', 'romania',
    'spain', 'usa', 'switzerland-it', 'switzerland', 'eu'
  ];
  const uiKeys = [
    'skip', 'home', 'guide', 'eyebrow', 'intro', 'count', 'updated',
    'directoryEyebrow', 'directoryTitle', 'languageLabel', 'searchLabel',
    'searchPlaceholder', 'reset', 'noResults', 'officialAuthority',
    'regionalAuthority', 'consumerGuide', 'internationalSource',
    'territorialGuide', 'noticeTitle', 'noticeBody', 'footer', 'backHome'
  ];
  const makeDictionary = (ui, regions, descriptions) => {
    const dict = { ...ui };
    for (const id of ids) {
      dict[`region-${id}`] = regions[id];
      dict[`description-${id}`] = descriptions[id];
    }
    return dict;
  };
  const countries = {
    it: {
      austria: 'Austria', benin: 'Benin · area BCEAO', bulgaria: 'Bulgaria',
      'ethiopia-ecma': 'Etiopia', 'ethiopia-guide': 'Etiopia', france: 'Francia',
      germany: 'Germania', iosco: 'Internazionale · IOSCO', italy: 'Italia',
      nunavut: 'Nunavut · Canada', polynesia: 'Polinesia francese',
      uk: 'Regno Unito', romania: 'Romania', spain: 'Spagna', usa: 'Stati Uniti',
      'switzerland-it': 'Svizzera · pagina in inglese', switzerland: 'Svizzera',
      eu: 'Unione europea'
    },
    en: {
      austria: 'Austria', benin: 'Benin · BCEAO region', bulgaria: 'Bulgaria',
      'ethiopia-ecma': 'Ethiopia', 'ethiopia-guide': 'Ethiopia', france: 'France',
      germany: 'Germany', iosco: 'International · IOSCO', italy: 'Italy',
      nunavut: 'Nunavut · Canada', polynesia: 'French Polynesia',
      uk: 'United Kingdom', romania: 'Romania', spain: 'Spain', usa: 'United States',
      'switzerland-it': 'Switzerland · English page', switzerland: 'Switzerland',
      eu: 'European Union'
    },
    fr: {
      austria: 'Autriche', benin: 'Bénin · zone BCEAO', bulgaria: 'Bulgarie',
      'ethiopia-ecma': 'Éthiopie', 'ethiopia-guide': 'Éthiopie', france: 'France',
      germany: 'Allemagne', iosco: 'International · IOSCO', italy: 'Italie',
      nunavut: 'Nunavut · Canada', polynesia: 'Polynésie française',
      uk: 'Royaume-Uni', romania: 'Roumanie', spain: 'Espagne', usa: 'États-Unis',
      'switzerland-it': 'Suisse · page en anglais', switzerland: 'Suisse',
      eu: 'Union européenne'
    },
    de: {
      austria: 'Österreich', benin: 'Benin · BCEAO-Region', bulgaria: 'Bulgarien',
      'ethiopia-ecma': 'Äthiopien', 'ethiopia-guide': 'Äthiopien', france: 'Frankreich',
      germany: 'Deutschland', iosco: 'International · IOSCO', italy: 'Italien',
      nunavut: 'Nunavut · Kanada', polynesia: 'Französisch-Polynesien',
      uk: 'Vereinigtes Königreich', romania: 'Rumänien', spain: 'Spanien',
      usa: 'Vereinigte Staaten', 'switzerland-it': 'Schweiz · englische Seite',
      switzerland: 'Schweiz', eu: 'Europäische Union'
    },
    es: {
      austria: 'Austria', benin: 'Benín · región BCEAO', bulgaria: 'Bulgaria',
      'ethiopia-ecma': 'Etiopía', 'ethiopia-guide': 'Etiopía', france: 'Francia',
      germany: 'Alemania', iosco: 'Internacional · IOSCO', italy: 'Italia',
      nunavut: 'Nunavut · Canadá', polynesia: 'Polinesia Francesa',
      uk: 'Reino Unido', romania: 'Rumanía', spain: 'España', usa: 'Estados Unidos',
      'switzerland-it': 'Suiza · página en inglés', switzerland: 'Suiza',
      eu: 'Unión Europea'
    },
    pt: {
      austria: 'Áustria', benin: 'Benim · região BCEAO', bulgaria: 'Bulgária',
      'ethiopia-ecma': 'Etiópia', 'ethiopia-guide': 'Etiópia', france: 'França',
      germany: 'Alemanha', iosco: 'Internacional · IOSCO', italy: 'Itália',
      nunavut: 'Nunavut · Canadá', polynesia: 'Polinésia Francesa',
      uk: 'Reino Unido', romania: 'Roménia', spain: 'Espanha', usa: 'Estados Unidos',
      'switzerland-it': 'Suíça · página em inglês', switzerland: 'Suíça',
      eu: 'União Europeia'
    },
    sq: {
      austria: 'Austri', benin: 'Benin · rajoni BCEAO', bulgaria: 'Bullgari',
      'ethiopia-ecma': 'Etiopi', 'ethiopia-guide': 'Etiopi', france: 'Francë',
      germany: 'Gjermani', iosco: 'Ndërkombëtare · IOSCO', italy: 'Itali',
      nunavut: 'Nunavut · Kanada', polynesia: 'Polinezia Franceze',
      uk: 'Mbretëria e Bashkuar', romania: 'Rumani', spain: 'Spanjë',
      usa: 'Shtetet e Bashkuara', 'switzerland-it': 'Zvicër · faqe në anglisht',
      switzerland: 'Zvicër', eu: 'Bashkimi Evropian'
    },
    ro: {
      austria: 'Austria', benin: 'Benin · regiunea BCEAO', bulgaria: 'Bulgaria',
      'ethiopia-ecma': 'Etiopia', 'ethiopia-guide': 'Etiopia', france: 'Franța',
      germany: 'Germania', iosco: 'Internațional · IOSCO', italy: 'Italia',
      nunavut: 'Nunavut · Canada', polynesia: 'Polinezia Franceză',
      uk: 'Regatul Unit', romania: 'România', spain: 'Spania', usa: 'Statele Unite',
      'switzerland-it': 'Elveția · pagină în engleză', switzerland: 'Elveția',
      eu: 'Uniunea Europeană'
    },
    nl: {
      austria: 'Oostenrijk', benin: 'Benin · BCEAO-regio', bulgaria: 'Bulgarije',
      'ethiopia-ecma': 'Ethiopië', 'ethiopia-guide': 'Ethiopië', france: 'Frankrijk',
      germany: 'Duitsland', iosco: 'Internationaal · IOSCO', italy: 'Italië',
      nunavut: 'Nunavut · Canada', polynesia: 'Frans-Polynesië',
      uk: 'Verenigd Koninkrijk', romania: 'Roemenië', spain: 'Spanje',
      usa: 'Verenigde Staten', 'switzerland-it': 'Zwitserland · Engelse pagina',
      switzerland: 'Zwitserland', eu: 'Europese Unie'
    },
    el: {
      austria: 'Αυστρία', benin: 'Μπενίν · περιοχή BCEAO', bulgaria: 'Βουλγαρία',
      'ethiopia-ecma': 'Αιθιοπία', 'ethiopia-guide': 'Αιθιοπία', france: 'Γαλλία',
      germany: 'Γερμανία', iosco: 'Διεθνής · IOSCO', italy: 'Ιταλία',
      nunavut: 'Νούναβουτ · Καναδάς', polynesia: 'Γαλλική Πολυνησία',
      uk: 'Ηνωμένο Βασίλειο', romania: 'Ρουμανία', spain: 'Ισπανία',
      usa: 'Ηνωμένες Πολιτείες', 'switzerland-it': 'Ελβετία · σελίδα στα αγγλικά',
      switzerland: 'Ελβετία', eu: 'Ευρωπαϊκή Ένωση'
    },
    bs: {
      austria: 'Austrija', benin: 'Benin · regija BCEAO', bulgaria: 'Bugarska',
      'ethiopia-ecma': 'Etiopija', 'ethiopia-guide': 'Etiopija', france: 'Francuska',
      germany: 'Njemačka', iosco: 'Međunarodno · IOSCO', italy: 'Italija',
      nunavut: 'Nunavut · Kanada', polynesia: 'Francuska Polinezija',
      uk: 'Ujedinjeno Kraljevstvo', romania: 'Rumunija', spain: 'Španija',
      usa: 'Sjedinjene Američke Države', 'switzerland-it': 'Švicarska · stranica na engleskom',
      switzerland: 'Švicarska', eu: 'Evropska unija'
    },
    ar: {
      austria: 'النمسا', benin: 'بنين · منطقة البنك المركزي لدول غرب أفريقيا', bulgaria: 'بلغاريا',
      'ethiopia-ecma': 'إثيوبيا', 'ethiopia-guide': 'إثيوبيا', france: 'فرنسا',
      germany: 'ألمانيا', iosco: 'دولي · IOSCO', italy: 'إيطاليا',
      nunavut: 'نونافوت · كندا', polynesia: 'بولينيزيا الفرنسية',
      uk: 'المملكة المتحدة', romania: 'رومانيا', spain: 'إسبانيا',
      usa: 'الولايات المتحدة', 'switzerland-it': 'سويسرا · صفحة بالإنجليزية',
      switzerland: 'سويسرا', eu: 'الاتحاد الأوروبي'
    },
    zh: {
      austria: '奥地利', benin: '贝宁 · 西非国家中央银行区域', bulgaria: '保加利亚',
      'ethiopia-ecma': '埃塞俄比亚', 'ethiopia-guide': '埃塞俄比亚', france: '法国',
      germany: '德国', iosco: '国际 · IOSCO', italy: '意大利',
      nunavut: '努纳武特 · 加拿大', polynesia: '法属波利尼西亚',
      uk: '英国', romania: '罗马尼亚', spain: '西班牙', usa: '美国',
      'switzerland-it': '瑞士 · 英文页面', switzerland: '瑞士', eu: '欧盟'
    },
    hi: {
      austria: 'ऑस्ट्रिया', benin: 'बेनिन · BCEAO क्षेत्र', bulgaria: 'बुल्गारिया',
      'ethiopia-ecma': 'इथियोपिया', 'ethiopia-guide': 'इथियोपिया', france: 'फ़्रांस',
      germany: 'जर्मनी', iosco: 'अंतरराष्ट्रीय · IOSCO', italy: 'इटली',
      nunavut: 'नुनावुत · कनाडा', polynesia: 'फ़्रेंच पोलिनेशिया',
      uk: 'यूनाइटेड किंगडम', romania: 'रोमानिया', spain: 'स्पेन', usa: 'संयुक्त राज्य',
      'switzerland-it': 'स्विट्ज़रलैंड · अंग्रेज़ी पृष्ठ', switzerland: 'स्विट्ज़रलैंड',
      eu: 'यूरोपीय संघ'
    },
    vi: {
      austria: 'Áo', benin: 'Benin · khu vực BCEAO', bulgaria: 'Bulgaria',
      'ethiopia-ecma': 'Ethiopia', 'ethiopia-guide': 'Ethiopia', france: 'Pháp',
      germany: 'Đức', iosco: 'Quốc tế · IOSCO', italy: 'Ý',
      nunavut: 'Nunavut · Canada', polynesia: 'Polynesia thuộc Pháp',
      uk: 'Vương quốc Anh', romania: 'Romania', spain: 'Tây Ban Nha',
      usa: 'Hoa Kỳ', 'switzerland-it': 'Thụy Sĩ · trang tiếng Anh',
      switzerland: 'Thụy Sĩ', eu: 'Liên minh châu Âu'
    }
  };

  const ui = {
    it: {
      pageTitle: 'Autorità finanziarie — Sezioni e settori per la tutela del consumatore e la prevenzione delle frodi',
      skip: 'Vai all’elenco delle autorità', home: 'Pagina iniziale', guide: 'Verifica prima di pagare',
      eyebrow: 'FONTI ISTITUZIONALI · DIRECTORY INFORMATIVA',
      intro: 'Esplora fonti ufficiali e guide indipendenti organizzate per Paese, territorio e ambito. Seleziona una scheda per consultare i riferimenti e i limiti specifici della relativa pagina.',
      count: '18 pagine informative', updated: 'Selezione geografica e internazionale',
      directoryEyebrow: 'ESPLORA LE SCHEDE', directoryTitle: 'Autorità e fonti per area',
      languageLabel: 'Lingua', searchLabel: 'Cerca per Paese, autorità o settore',
      searchPlaceholder: 'Cerca per Paese, autorità o settore', reset: 'Azzera',
      noResults: 'Nessuna scheda corrisponde alla ricerca.', resultCount: '{count} risultati',
      officialAuthority: 'Autorità nazionale', regionalAuthority: 'Fonte regionale',
      consumerGuide: 'Guida indipendente', internationalSource: 'Fonte internazionale',
      territorialGuide: 'Guida territoriale', noticeTitle: 'Indipendenza editoriale e limiti',
      noticeBody: 'Questa directory è una raccolta editoriale indipendente di collegamenti e contenuti informativi. La citazione di un’autorità, di un registro o di un servizio non verifica licenze, intermediari, beneficiari o transazioni e non costituisce approvazione, certificazione o garanzia di sicurezza. L’assenza di avvisi o segnalazioni non equivale ad autorizzazione né prova affidabilità. I dati sono informativi: verifica sempre condizioni e recapiti aggiornati direttamente sulle fonti ufficiali.',
      footer: 'Directory informativa indipendente · AMEV Financial Defense',
      backHome: 'Torna alla pagina iniziale'
    },
    en: {
      pageTitle: 'Financial authorities — Sections and sectors for consumer protection and fraud prevention',
      skip: 'Skip to authority list', home: 'Home', guide: 'Check before you pay',
      eyebrow: 'INSTITUTIONAL SOURCES · INFORMATION DIRECTORY',
      intro: 'Explore official sources and independent guides by country, territory and subject. Open a card for the references and limitations specific to that page.',
      count: '18 information pages', updated: 'Country and international coverage',
      directoryEyebrow: 'EXPLORE THE DIRECTORY', directoryTitle: 'Authorities and sources by area',
      languageLabel: 'Language', searchLabel: 'Search by country, authority or sector',
      searchPlaceholder: 'Search by country, authority or sector', reset: 'Reset',
      noResults: 'No pages match your search.', resultCount: '{count} results',
      officialAuthority: 'National authority', regionalAuthority: 'Regional source',
      consumerGuide: 'Independent guide', internationalSource: 'International source',
      territorialGuide: 'Territorial guide', noticeTitle: 'Editorial independence and limits',
      noticeBody: 'This directory is an independent editorial collection of links and informational material. Listing an authority, register or service does not verify licences, intermediaries, beneficiaries or transactions and is not an endorsement, certification or safety guarantee. The absence of warnings or reports does not mean authorization or prove reliability. This information is for reference: always confirm current terms and contact details directly with official sources.',
      footer: 'Independent information directory · AMEV Financial Defense', backHome: 'Back to home'
    },
    fr: {
      pageTitle: 'Autorités financières — Sections et secteurs pour la protection des consommateurs et la prévention de la fraude',
      skip: 'Accéder à la liste des autorités', home: 'Accueil', guide: 'Vérifier avant de payer',
      eyebrow: 'SOURCES INSTITUTIONNELLES · ANNUAIRE INFORMATIF',
      intro: 'Consultez les sources officielles et les guides indépendants par pays, territoire et domaine. Ouvrez une fiche pour connaître ses références et ses limites.',
      count: '18 pages informatives', updated: 'Sélection nationale et internationale',
      directoryEyebrow: 'PARCOURIR LES FICHES', directoryTitle: 'Autorités et sources par zone',
      languageLabel: 'Langue', searchLabel: 'Rechercher par pays, autorité ou secteur',
      searchPlaceholder: 'Rechercher par pays, autorité ou secteur', reset: 'Effacer',
      noResults: 'Aucune fiche ne correspond à la recherche.', resultCount: '{count} résultats',
      officialAuthority: 'Autorité nationale', regionalAuthority: 'Source régionale',
      consumerGuide: 'Guide indépendant', internationalSource: 'Source internationale',
      territorialGuide: 'Guide territorial', noticeTitle: 'Indépendance éditoriale et limites',
      noticeBody: 'Cet annuaire est une sélection éditoriale indépendante de liens et de contenus informatifs. La mention d’une autorité, d’un registre ou d’un service ne vérifie ni les licences, ni les intermédiaires, bénéficiaires ou transactions et ne constitue pas une approbation, une certification ou une garantie de sécurité. L’absence d’alerte ne vaut pas autorisation et ne prouve pas la fiabilité. Ces informations sont indicatives : vérifiez toujours les conditions et coordonnées à jour auprès des sources officielles.',
      footer: 'Annuaire informatif indépendant · AMEV Financial Defense', backHome: 'Retour à l’accueil'
    },
    de: {
      pageTitle: 'Finanzbehörden — Bereiche und Sektoren zum Verbraucherschutz und zur Betrugsprävention',
      skip: 'Zur Behördenliste springen', home: 'Startseite', guide: 'Vor dem Bezahlen prüfen',
      eyebrow: 'INSTITUTIONELLE QUELLEN · INFORMATIONSDIREKTORIUM',
      intro: 'Entdecken Sie offizielle Quellen und unabhängige Leitfäden nach Land, Gebiet und Thema. Die jeweilige Karte enthält Hinweise und Grenzen der Seite.',
      count: '18 Informationsseiten', updated: 'Länder und internationale Auswahl',
      directoryEyebrow: 'SEITEN ENTDECKEN', directoryTitle: 'Behörden und Quellen nach Gebiet',
      languageLabel: 'Sprache', searchLabel: 'Nach Land, Behörde oder Bereich suchen',
      searchPlaceholder: 'Nach Land, Behörde oder Bereich suchen', reset: 'Zurücksetzen',
      noResults: 'Keine Seite entspricht der Suche.', resultCount: '{count} Ergebnisse',
      officialAuthority: 'Nationale Behörde', regionalAuthority: 'Regionale Quelle',
      consumerGuide: 'Unabhängiger Leitfaden', internationalSource: 'Internationale Quelle',
      territorialGuide: 'Gebietsleitfaden', noticeTitle: 'Redaktionelle Unabhängigkeit und Grenzen',
      noticeBody: 'Dieses Verzeichnis ist eine unabhängige redaktionelle Sammlung von Links und Informationsmaterial. Die Nennung einer Behörde, eines Registers oder eines Dienstes bestätigt weder Lizenzen noch Vermittler, Begünstigte oder Transaktionen und ist keine Empfehlung, Zertifizierung oder Sicherheitsgarantie. Das Fehlen von Warnungen bedeutet keine Zulassung und belegt keine Zuverlässigkeit. Die Angaben dienen nur der Information: Prüfen Sie aktuelle Bedingungen und Kontaktdaten direkt bei offiziellen Quellen.',
      footer: 'Unabhängiges Informationsverzeichnis · AMEV Financial Defense', backHome: 'Zur Startseite'
    },
    es: {
      pageTitle: 'Autoridades financieras — Secciones y sectores para la protección del consumidor y la prevención del fraude',
      skip: 'Ir a la lista de autoridades', home: 'Inicio', guide: 'Verifica antes de pagar',
      eyebrow: 'FUENTES INSTITUCIONALES · DIRECTORIO INFORMATIVO',
      intro: 'Consulta fuentes oficiales y guías independientes por país, territorio y ámbito. Abre cada ficha para conocer sus referencias y limitaciones.',
      count: '18 páginas informativas', updated: 'Selección geográfica e internacional',
      directoryEyebrow: 'EXPLORAR LAS FICHAS', directoryTitle: 'Autoridades y fuentes por zona',
      languageLabel: 'Idioma', searchLabel: 'Buscar por país, autoridad o sector',
      searchPlaceholder: 'Buscar por país, autoridad o sector', reset: 'Restablecer',
      noResults: 'Ninguna ficha coincide con la búsqueda.', resultCount: '{count} resultados',
      officialAuthority: 'Autoridad nacional', regionalAuthority: 'Fuente regional',
      consumerGuide: 'Guía independiente', internationalSource: 'Fuente internacional',
      territorialGuide: 'Guía territorial', noticeTitle: 'Independencia editorial y límites',
      noticeBody: 'Este directorio es una recopilación editorial independiente de enlaces e información. La inclusión de una autoridad, registro o servicio no verifica licencias, intermediarios, beneficiarios ni transacciones, y no constituye respaldo, certificación ni garantía de seguridad. La ausencia de alertas no equivale a autorización ni demuestra fiabilidad. La información es orientativa: confirma siempre las condiciones y los contactos actuales directamente con las fuentes oficiales.',
      footer: 'Directorio informativo independiente · AMEV Financial Defense', backHome: 'Volver al inicio'
    },
    pt: {
      pageTitle: 'Autoridades financeiras — Secções e setores para a proteção do consumidor e a prevenção de fraudes',
      skip: 'Ir para a lista de autoridades', home: 'Início', guide: 'Verifique antes de pagar',
      eyebrow: 'FONTES INSTITUCIONAIS · DIRETÓRIO INFORMATIVO',
      intro: 'Explore fontes oficiais e guias independentes por país, território e área. Abra cada ficha para consultar as referências e os limites da página.',
      count: '18 páginas informativas', updated: 'Seleção geográfica e internacional',
      directoryEyebrow: 'EXPLORAR AS FICHAS', directoryTitle: 'Autoridades e fontes por área',
      languageLabel: 'Idioma', searchLabel: 'Pesquisar por país, autoridade ou setor',
      searchPlaceholder: 'Pesquisar por país, autoridade ou setor', reset: 'Limpar',
      noResults: 'Nenhuma ficha corresponde à pesquisa.', resultCount: '{count} resultados',
      officialAuthority: 'Autoridade nacional', regionalAuthority: 'Fonte regional',
      consumerGuide: 'Guia independente', internationalSource: 'Fonte internacional',
      territorialGuide: 'Guia territorial', noticeTitle: 'Independência editorial e limites',
      noticeBody: 'Este diretório é uma recolha editorial independente de ligações e conteúdos informativos. A referência a uma autoridade, registo ou serviço não verifica licenças, intermediários, beneficiários ou transações e não constitui aprovação, certificação ou garantia de segurança. A ausência de alertas não equivale a autorização nem comprova fiabilidade. A informação é indicativa: confirme sempre as condições e os contactos atualizados diretamente nas fontes oficiais.',
      footer: 'Diretório informativo independente · AMEV Financial Defense', backHome: 'Voltar ao início'
    },
    sq: {
      pageTitle: 'Autoritetet financiare — Seksione dhe sektorë për mbrojtjen e konsumatorit dhe parandalimin e mashtrimeve',
      skip: 'Kalo te lista e autoriteteve', home: 'Kreu', guide: 'Verifiko para se të paguash',
      eyebrow: 'BURIME INSTITUCIONALE · DREJTORI INFORMUES',
      intro: 'Shfletoni burime zyrtare dhe udhëzues të pavarur sipas shtetit, territorit dhe fushës. Hapni një kartë për referencat dhe kufizimet përkatëse.',
      count: '18 faqe informuese', updated: 'Përzgjedhje gjeografike dhe ndërkombëtare',
      directoryEyebrow: 'SHFLETO FAQET', directoryTitle: 'Autoritetet dhe burimet sipas zonës',
      languageLabel: 'Gjuha', searchLabel: 'Kërko sipas shtetit, autoritetit ose sektorit',
      searchPlaceholder: 'Kërko sipas shtetit, autoritetit ose sektorit', reset: 'Pastro',
      noResults: 'Asnjë faqe nuk përputhet me kërkimin.', resultCount: '{count} rezultate',
      officialAuthority: 'Autoritet kombëtar', regionalAuthority: 'Burim rajonal',
      consumerGuide: 'Udhëzues i pavarur', internationalSource: 'Burim ndërkombëtar',
      territorialGuide: 'Udhëzues territorial', noticeTitle: 'Pavarësia editoriale dhe kufizimet',
      noticeBody: 'Ky drejtor është një përmbledhje editoriale e pavarur e lidhjeve dhe materialeve informuese. Përmendja e një autoriteti, regjistri ose shërbimi nuk verifikon licenca, ndërmjetës, përfitues ose transaksione dhe nuk është miratim, certifikim apo garanci sigurie. Mungesa e paralajmërimeve nuk nënkupton autorizim dhe nuk provon besueshmëri. Informacioni është orientues: verifikoni kushtet dhe kontaktet aktuale drejtpërdrejt te burimet zyrtare.',
      footer: 'Drejtor informues i pavarur · AMEV Financial Defense', backHome: 'Kthehu në kreu'
    },
    ro: {
      pageTitle: 'Autorități financiare — Secțiuni și sectoare pentru protecția consumatorilor și prevenirea fraudelor',
      skip: 'Salt la lista autorităților', home: 'Pagina principală', guide: 'Verifică înainte de plată',
      eyebrow: 'SURSE INSTITUȚIONALE · DIRECTOR INFORMATIV',
      intro: 'Explorați surse oficiale și ghiduri independente după țară, teritoriu și domeniu. Deschideți o fișă pentru referințele și limitele paginii respective.',
      count: '18 pagini informative', updated: 'Selecție geografică și internațională',
      directoryEyebrow: 'EXPLORAȚI FIȘELE', directoryTitle: 'Autorități și surse pe regiuni',
      languageLabel: 'Limba', searchLabel: 'Caută după țară, autoritate sau sector',
      searchPlaceholder: 'Caută după țară, autoritate sau sector', reset: 'Resetează',
      noResults: 'Nicio fișă nu corespunde căutării.', resultCount: '{count} rezultate',
      officialAuthority: 'Autoritate națională', regionalAuthority: 'Sursă regională',
      consumerGuide: 'Ghid independent', internationalSource: 'Sursă internațională',
      territorialGuide: 'Ghid teritorial', noticeTitle: 'Independență editorială și limite',
      noticeBody: 'Acest director este o colecție editorială independentă de linkuri și materiale informative. Menționarea unei autorități, a unui registru sau a unui serviciu nu verifică licențe, intermediari, beneficiari ori tranzacții și nu constituie aprobare, certificare sau garanție de siguranță. Lipsa avertismentelor nu înseamnă autorizare și nu dovedește fiabilitatea. Informațiile sunt orientative: verificați condițiile și datele de contact actuale direct la sursele oficiale.',
      footer: 'Director informativ independent · AMEV Financial Defense', backHome: 'Înapoi la pagina principală'
    },
    nl: {
      pageTitle: 'Financiële autoriteiten — Afdelingen en sectoren voor consumentenbescherming en fraudepreventie',
      skip: 'Ga naar de autoriteitenlijst', home: 'Startpagina', guide: 'Controleer vóór betaling',
      eyebrow: 'INSTITUTIONELE BRONNEN · INFORMATIEDIRECTORY',
      intro: 'Bekijk officiële bronnen en onafhankelijke gidsen per land, gebied en onderwerp. Open een kaart voor de bronnen en beperkingen van die pagina.',
      count: '18 informatiepagina’s', updated: 'Geografische en internationale selectie',
      directoryEyebrow: 'BEKIJK DE PAGINA’S', directoryTitle: 'Autoriteiten en bronnen per gebied',
      languageLabel: 'Taal', searchLabel: 'Zoeken op land, autoriteit of sector',
      searchPlaceholder: 'Zoeken op land, autoriteit of sector', reset: 'Wissen',
      noResults: 'Geen pagina’s gevonden voor deze zoekopdracht.', resultCount: '{count} resultaten',
      officialAuthority: 'Nationale autoriteit', regionalAuthority: 'Regionale bron',
      consumerGuide: 'Onafhankelijke gids', internationalSource: 'Internationale bron',
      territorialGuide: 'Gebiedsgids', noticeTitle: 'Redactionele onafhankelijkheid en beperkingen',
      noticeBody: 'Deze directory is een onafhankelijke redactionele verzameling links en informatieve inhoud. De vermelding van een autoriteit, register of dienst controleert geen vergunningen, tussenpersonen, begunstigden of transacties en is geen goedkeuring, certificering of veiligheidsgarantie. Het ontbreken van waarschuwingen betekent geen vergunning en bewijst geen betrouwbaarheid. De informatie is uitsluitend informatief: controleer actuele voorwaarden en contactgegevens rechtstreeks bij officiële bronnen.',
      footer: 'Onafhankelijke informatiedirectory · AMEV Financial Defense', backHome: 'Terug naar startpagina'
    },
    el: {
      pageTitle: 'Χρηματοπιστωτικές αρχές — Ενότητες και τομείς για την προστασία των καταναλωτών και την πρόληψη της απάτης',
      skip: 'Μετάβαση στη λίστα αρχών', home: 'Αρχική σελίδα', guide: 'Έλεγχος πριν από την πληρωμή',
      eyebrow: 'ΘΕΣΜΙΚΕΣ ΠΗΓΕΣ · ΕΝΗΜΕΡΩΤΙΚΟΣ ΚΑΤΑΛΟΓΟΣ',
      intro: 'Εξερευνήστε επίσημες πηγές και ανεξάρτητους οδηγούς ανά χώρα, επικράτεια και τομέα. Ανοίξτε μια κάρτα για τις πηγές και τους περιορισμούς της.',
      count: '18 ενημερωτικές σελίδες', updated: 'Γεωγραφική και διεθνής επιλογή',
      directoryEyebrow: 'ΕΞΕΡΕΥΝΗΣΤΕ ΤΙΣ ΣΕΛΙΔΕΣ', directoryTitle: 'Αρχές και πηγές ανά περιοχή',
      languageLabel: 'Γλώσσα', searchLabel: 'Αναζήτηση ανά χώρα, αρχή ή τομέα',
      searchPlaceholder: 'Αναζήτηση ανά χώρα, αρχή ή τομέα', reset: 'Επαναφορά',
      noResults: 'Δεν βρέθηκαν σελίδες για αυτή την αναζήτηση.', resultCount: '{count} αποτελέσματα',
      officialAuthority: 'Εθνική αρχή', regionalAuthority: 'Περιφερειακή πηγή',
      consumerGuide: 'Ανεξάρτητος οδηγός', internationalSource: 'Διεθνής πηγή',
      territorialGuide: 'Εδαφικός οδηγός', noticeTitle: 'Συντακτική ανεξαρτησία και περιορισμοί',
      noticeBody: 'Αυτός ο κατάλογος είναι ανεξάρτητη συντακτική συλλογή συνδέσμων και ενημερωτικού υλικού. Η αναφορά σε αρχή, μητρώο ή υπηρεσία δεν επαληθεύει άδειες, διαμεσολαβητές, δικαιούχους ή συναλλαγές και δεν αποτελεί έγκριση, πιστοποίηση ή εγγύηση ασφάλειας. Η απουσία προειδοποιήσεων δεν σημαίνει αδειοδότηση ούτε αποδεικνύει αξιοπιστία. Οι πληροφορίες είναι ενδεικτικές: επιβεβαιώνετε τους ισχύοντες όρους και τα στοιχεία επικοινωνίας στις επίσημες πηγές.',
      footer: 'Ανεξάρτητος ενημερωτικός κατάλογος · AMEV Financial Defense', backHome: 'Επιστροφή στην αρχική'
    },
    bs: {
      pageTitle: 'Finansijski organi — Odjeljci i sektori za zaštitu potrošača i sprečavanje prevara',
      skip: 'Pređi na listu nadležnih organa', home: 'Početna stranica', guide: 'Provjerite prije plaćanja',
      eyebrow: 'INSTITUCIONALNI IZVORI · INFORMATIVNI DIREKTORIJ',
      intro: 'Pregledajte službene izvore i nezavisne vodiče prema državi, teritoriji i oblasti. Otvorite karticu za izvore i ograničenja određene stranice.',
      count: '18 informativnih stranica', updated: 'Geografski i međunarodni izbor',
      directoryEyebrow: 'PREGLEDAJTE STRANICE', directoryTitle: 'Organi i izvori po području',
      languageLabel: 'Jezik', searchLabel: 'Pretraži prema državi, organu ili sektoru',
      searchPlaceholder: 'Pretraži prema državi, organu ili sektoru', reset: 'Poništi',
      noResults: 'Nijedna stranica ne odgovara pretrazi.', resultCount: '{count} rezultata',
      officialAuthority: 'Državni organ', regionalAuthority: 'Regionalni izvor',
      consumerGuide: 'Nezavisni vodič', internationalSource: 'Međunarodni izvor',
      territorialGuide: 'Teritorijalni vodič', noticeTitle: 'Urednička nezavisnost i ograničenja',
      noticeBody: 'Ovaj direktorij je nezavisna urednička zbirka linkova i informativnog sadržaja. Navođenje organa, registra ili usluge ne provjerava licence, posrednike, korisnike ili transakcije i ne predstavlja preporuku, certifikat ni garanciju sigurnosti. Odsustvo upozorenja ne znači odobrenje niti dokazuje pouzdanost. Informacije su informativne: aktuelne uslove i kontakte uvijek provjerite direktno kod službenih izvora.',
      footer: 'Nezavisni informativni direktorij · AMEV Financial Defense', backHome: 'Nazad na početnu stranicu'
    },
    ar: {
      pageTitle: 'السلطات المالية — أقسام وقطاعات لحماية المستهلك ومنع الاحتيال',
      skip: 'انتقل إلى قائمة السلطات', home: 'الصفحة الرئيسية', guide: 'تحقق قبل الدفع',
      eyebrow: 'مصادر مؤسسية · دليل معلوماتي',
      intro: 'استكشف المصادر الرسمية والأدلة المستقلة حسب البلد والإقليم والمجال. افتح بطاقة للاطلاع على المراجع والحدود الخاصة بكل صفحة.',
      count: '١٨ صفحة معلوماتية', updated: 'اختيار جغرافي ودولي',
      directoryEyebrow: 'استكشف الصفحات', directoryTitle: 'السلطات والمصادر حسب المنطقة',
      languageLabel: 'اللغة', searchLabel: 'ابحث حسب البلد أو السلطة أو القطاع',
      searchPlaceholder: 'ابحث حسب البلد أو السلطة أو القطاع', reset: 'إعادة ضبط',
      noResults: 'لا توجد صفحات تطابق البحث.', resultCount: '{count} نتائج',
      officialAuthority: 'سلطة وطنية', regionalAuthority: 'مصدر إقليمي',
      consumerGuide: 'دليل مستقل', internationalSource: 'مصدر دولي',
      territorialGuide: 'دليل إقليمي', noticeTitle: 'الاستقلال التحريري والحدود',
      noticeBody: 'هذا الدليل مجموعة تحريرية مستقلة من الروابط والمواد المعلوماتية. إدراج سلطة أو سجل أو خدمة لا يتحقق من التراخيص أو الوسطاء أو المستفيدين أو المعاملات، ولا يشكل تزكية أو اعتماداً أو ضماناً للسلامة. عدم وجود تحذيرات لا يعني الترخيص ولا يثبت الموثوقية. المعلومات للاسترشاد فقط: تحقق دائماً من الشروط وبيانات الاتصال المحدثة مباشرة من المصادر الرسمية.',
      footer: 'دليل معلوماتي مستقل · AMEV Financial Defense', backHome: 'العودة إلى الصفحة الرئيسية'
    },
    zh: {
      pageTitle: '金融监管机构 — 消费者保护与防范欺诈的领域和部门',
      skip: '跳转至监管机构列表', home: '首页', guide: '付款前先核查',
      eyebrow: '机构来源 · 信息目录',
      intro: '按国家、地区和领域浏览官方来源与独立指南。打开卡片可查看该页面的参考资料及其适用限制。',
      count: '18 个信息页面', updated: '国家及国际信息精选',
      directoryEyebrow: '浏览页面', directoryTitle: '按地区查看机构与来源',
      languageLabel: '语言', searchLabel: '按国家、机构或领域搜索',
      searchPlaceholder: '按国家、机构或领域搜索', reset: '重置',
      noResults: '没有符合搜索条件的页面。', resultCount: '{count} 个结果',
      officialAuthority: '国家监管机构', regionalAuthority: '区域来源',
      consumerGuide: '独立指南', internationalSource: '国际来源',
      territorialGuide: '地区指南', noticeTitle: '编辑独立性与信息限制',
      noticeBody: '本目录是独立编辑整理的链接和信息资料。列出某个机构、登记册或服务，并不代表已核实其牌照、中介、受益人或交易，也不构成背书、认证或安全保证。没有警示或举报并不代表获得授权，也不能证明可靠性。内容仅供参考：请直接向官方来源核实最新条件和联系方式。',
      footer: '独立信息目录 · AMEV Financial Defense', backHome: '返回首页'
    },
    hi: {
      pageTitle: 'वित्तीय प्राधिकरण — उपभोक्ता संरक्षण और धोखाधड़ी की रोकथाम के लिए अनुभाग और क्षेत्र',
      skip: 'प्राधिकरण सूची पर जाएँ', home: 'मुखपृष्ठ', guide: 'भुगतान से पहले जाँचें',
      eyebrow: 'संस्थागत स्रोत · जानकारी निर्देशिका',
      intro: 'देश, क्षेत्र और विषय के अनुसार आधिकारिक स्रोत तथा स्वतंत्र मार्गदर्शिकाएँ देखें। किसी पृष्ठ के संदर्भ और सीमाएँ जानने के लिए उसका कार्ड खोलें।',
      count: '18 जानकारी पृष्ठ', updated: 'भौगोलिक और अंतरराष्ट्रीय चयन',
      directoryEyebrow: 'पृष्ठ देखें', directoryTitle: 'क्षेत्र के अनुसार प्राधिकरण और स्रोत',
      languageLabel: 'भाषा', searchLabel: 'देश, प्राधिकरण या क्षेत्र से खोजें',
      searchPlaceholder: 'देश, प्राधिकरण या क्षेत्र से खोजें', reset: 'रीसेट',
      noResults: 'खोज से मेल खाने वाला कोई पृष्ठ नहीं मिला।', resultCount: '{count} परिणाम',
      officialAuthority: 'राष्ट्रीय प्राधिकरण', regionalAuthority: 'क्षेत्रीय स्रोत',
      consumerGuide: 'स्वतंत्र मार्गदर्शिका', internationalSource: 'अंतरराष्ट्रीय स्रोत',
      territorialGuide: 'क्षेत्रीय मार्गदर्शिका', noticeTitle: 'संपादकीय स्वतंत्रता और सीमाएँ',
      noticeBody: 'यह निर्देशिका लिंक और जानकारीपूर्ण सामग्री का स्वतंत्र संपादकीय संकलन है। किसी प्राधिकरण, रजिस्टर या सेवा का उल्लेख लाइसेंस, मध्यस्थ, लाभार्थी या लेनदेन का सत्यापन नहीं करता और यह समर्थन, प्रमाणन या सुरक्षा की गारंटी नहीं है। चेतावनी का अभाव प्राधिकरण नहीं दर्शाता और विश्वसनीयता सिद्ध नहीं करता। जानकारी केवल संदर्भ के लिए है: अद्यतन शर्तें और संपर्क विवरण सीधे आधिकारिक स्रोतों से जाँचें।',
      footer: 'स्वतंत्र जानकारी निर्देशिका · AMEV Financial Defense', backHome: 'मुखपृष्ठ पर लौटें'
    },
    vi: {
      pageTitle: 'Cơ quan tài chính — Các mục và lĩnh vực bảo vệ người tiêu dùng và phòng chống gian lận',
      skip: 'Chuyển đến danh sách cơ quan', home: 'Trang chủ', guide: 'Kiểm tra trước khi thanh toán',
      eyebrow: 'NGUỒN CƠ QUAN · DANH BẠ THÔNG TIN',
      intro: 'Khám phá nguồn chính thức và hướng dẫn độc lập theo quốc gia, lãnh thổ và lĩnh vực. Mở thẻ để xem nguồn tham khảo và giới hạn của từng trang.',
      count: '18 trang thông tin', updated: 'Tuyển chọn theo khu vực và quốc tế',
      directoryEyebrow: 'KHÁM PHÁ CÁC TRANG', directoryTitle: 'Cơ quan và nguồn theo khu vực',
      languageLabel: 'Ngôn ngữ', searchLabel: 'Tìm theo quốc gia, cơ quan hoặc lĩnh vực',
      searchPlaceholder: 'Tìm theo quốc gia, cơ quan hoặc lĩnh vực', reset: 'Đặt lại',
      noResults: 'Không có trang phù hợp với tìm kiếm.', resultCount: '{count} kết quả',
      officialAuthority: 'Cơ quan quốc gia', regionalAuthority: 'Nguồn khu vực',
      consumerGuide: 'Hướng dẫn độc lập', internationalSource: 'Nguồn quốc tế',
      territorialGuide: 'Hướng dẫn lãnh thổ', noticeTitle: 'Tính độc lập biên tập và giới hạn',
      noticeBody: 'Danh bạ này là tuyển tập liên kết và nội dung thông tin do biên tập độc lập. Việc nêu tên cơ quan, sổ đăng ký hoặc dịch vụ không xác minh giấy phép, trung gian, người thụ hưởng hay giao dịch, và không phải là sự chứng thực, chứng nhận hoặc bảo đảm an toàn. Không có cảnh báo không đồng nghĩa được cấp phép và không chứng minh độ tin cậy. Thông tin chỉ mang tính tham khảo: hãy xác nhận điều kiện và thông tin liên hệ mới nhất trực tiếp từ nguồn chính thức.',
      footer: 'Danh bạ thông tin độc lập · AMEV Financial Defense', backHome: 'Trở về trang chủ'
    }
  };
  const descriptionTemplates = {
    it: {
      authority: 'Fonti ufficiali e informazioni sulla vigilanza finanziaria in {country}.',
      guide: 'Guida indipendente per i consumatori con riferimenti istituzionali di {country}.',
      regional: 'Riferimenti regionali e risorse ufficiali pertinenti per {country}.',
      international: 'Fonti, standard e strumenti internazionali per gli investitori.',
      territory: 'Guida territoriale indipendente e fonti pubbliche pertinenti per {country}.'
    },
    en: {
      authority: 'Official sources and information on financial supervision in {country}.',
      guide: 'Independent consumer guide with institutional references for {country}.',
      regional: 'Regional references and relevant official resources for {country}.',
      international: 'International sources, standards and investor resources.',
      territory: 'Independent territorial guide and public sources for {country}.'
    },
    fr: {
      authority: 'Sources officielles et informations sur la supervision financière en {country}.',
      guide: 'Guide indépendant destiné aux consommateurs avec des références institutionnelles pour {country}.',
      regional: 'Références régionales et ressources officielles concernant {country}.',
      international: 'Sources, normes et ressources internationales pour les investisseurs.',
      territory: 'Guide territorial indépendant et sources publiques concernant {country}.'
    },
    de: {
      authority: 'Offizielle Quellen und Informationen zur Finanzaufsicht in {country}.',
      guide: 'Unabhängiger Verbraucherleitfaden mit institutionellen Quellen für {country}.',
      regional: 'Regionale Hinweise und einschlägige offizielle Quellen für {country}.',
      international: 'Internationale Quellen, Standards und Informationen für Anleger.',
      territory: 'Unabhängiger Gebietsleitfaden und öffentliche Quellen für {country}.'
    },
    es: {
      authority: 'Fuentes oficiales e información sobre la supervisión financiera en {country}.',
      guide: 'Guía independiente para consumidores con referencias institucionales de {country}.',
      regional: 'Referencias regionales y recursos oficiales pertinentes para {country}.',
      international: 'Fuentes, normas y recursos internacionales para los inversores.',
      territory: 'Guía territorial independiente y fuentes públicas de {country}.'
    },
    pt: {
      authority: 'Fontes oficiais e informações sobre a supervisão financeira em {country}.',
      guide: 'Guia independente para consumidores com referências institucionais de {country}.',
      regional: 'Referências regionais e recursos oficiais pertinentes para {country}.',
      international: 'Fontes, normas e recursos internacionais para investidores.',
      territory: 'Guia territorial independente e fontes públicas de {country}.'
    },
    sq: {
      authority: 'Burime zyrtare dhe informacione për mbikëqyrjen financiare në {country}.',
      guide: 'Udhëzues i pavarur për konsumatorët me burime institucionale për {country}.',
      regional: 'Referenca rajonale dhe burime zyrtare përkatëse për {country}.',
      international: 'Burime, standarde dhe informacione ndërkombëtare për investitorët.',
      territory: 'Udhëzues i pavarur territorial dhe burime publike për {country}.'
    },
    ro: {
      authority: 'Surse oficiale și informații despre supravegherea financiară din {country}.',
      guide: 'Ghid independent pentru consumatori cu referințe instituționale pentru {country}.',
      regional: 'Referințe regionale și resurse oficiale relevante pentru {country}.',
      international: 'Surse, standarde și resurse internaționale pentru investitori.',
      territory: 'Ghid teritorial independent și surse publice pentru {country}.'
    },
    nl: {
      authority: 'Officiële bronnen en informatie over financieel toezicht in {country}.',
      guide: 'Onafhankelijke consumentengids met institutionele verwijzingen voor {country}.',
      regional: 'Regionale verwijzingen en relevante officiële bronnen voor {country}.',
      international: 'Internationale bronnen, normen en informatie voor beleggers.',
      territory: 'Onafhankelijke gebiedsgids en openbare bronnen voor {country}.'
    },
    el: {
      authority: 'Επίσημες πηγές και πληροφορίες για τη χρηματοπιστωτική εποπτεία στην {country}.',
      guide: 'Ανεξάρτητος οδηγός καταναλωτών με θεσμικές αναφορές για την {country}.',
      regional: 'Περιφερειακές αναφορές και σχετικές επίσημες πηγές για την {country}.',
      international: 'Διεθνείς πηγές, πρότυπα και πληροφορίες για επενδυτές.',
      territory: 'Ανεξάρτητος εδαφικός οδηγός και δημόσιες πηγές για την {country}.'
    },
    bs: {
      authority: 'Službeni izvori i informacije o finansijskom nadzoru u području: {country}.',
      guide: 'Nezavisni vodič za potrošače s institucionalnim izvorima za područje: {country}.',
      regional: 'Regionalne reference i relevantni službeni resursi za područje: {country}.',
      international: 'Međunarodni izvori, standardi i informacije za investitore.',
      territory: 'Nezavisni teritorijalni vodič i javni izvori za područje: {country}.'
    },
    ar: {
      authority: 'مصادر رسمية ومعلومات عن الرقابة المالية في {country}.',
      guide: 'دليل مستقل للمستهلكين يتضمن مراجع مؤسسية تخص {country}.',
      regional: 'مراجع إقليمية وموارد رسمية ذات صلة بـ {country}.',
      international: 'مصادر ومعايير وموارد دولية للمستثمرين.',
      territory: 'دليل إقليمي مستقل ومصادر عامة تخص {country}.'
    },
    zh: {
      authority: '关于{country}金融监管的官方来源与信息。',
      guide: '面向消费者的独立指南，包含{country}的机构参考资料。',
      regional: '与{country}相关的区域参考资料和官方资源。',
      international: '面向投资者的国际来源、标准与资源。',
      territory: '关于{country}的独立地区指南与公共来源。'
    },
    hi: {
      authority: '{country} में वित्तीय निगरानी से संबंधित आधिकारिक स्रोत और जानकारी।',
      guide: '{country} के संस्थागत संदर्भों सहित उपभोक्ताओं के लिए स्वतंत्र मार्गदर्शिका।',
      regional: '{country} के लिए क्षेत्रीय संदर्भ और प्रासंगिक आधिकारिक संसाधन।',
      international: 'निवेशकों के लिए अंतरराष्ट्रीय स्रोत, मानक और संसाधन।',
      territory: '{country} के सार्वजनिक स्रोतों सहित स्वतंत्र क्षेत्रीय मार्गदर्शिका।'
    },
    vi: {
      authority: 'Nguồn chính thức và thông tin về giám sát tài chính tại {country}.',
      guide: 'Hướng dẫn độc lập cho người tiêu dùng với nguồn tham khảo cơ quan tại {country}.',
      regional: 'Thông tin khu vực và nguồn chính thức liên quan đến {country}.',
      international: 'Nguồn, tiêu chuẩn và tài nguyên quốc tế dành cho nhà đầu tư.',
      territory: 'Hướng dẫn lãnh thổ độc lập và nguồn công khai về {country}.'
    }
  };
  const descriptionKinds = {
    austria: 'authority', benin: 'regional', bulgaria: 'authority',
    'ethiopia-ecma': 'authority', 'ethiopia-guide': 'guide', france: 'authority',
    germany: 'authority', iosco: 'international', italy: 'authority',
    nunavut: 'territory', polynesia: 'territory', uk: 'authority',
    romania: 'guide', spain: 'authority', usa: 'authority',
    'switzerland-it': 'authority', switzerland: 'authority', eu: 'regional'
  };
  const dictionaries = {};
  for (const lang of Object.keys(ui)) {
    const localizedDescriptions = {};
    for (const id of ids) {
      localizedDescriptions[id] = descriptionTemplates[lang][descriptionKinds[id]]
        .replace('{country}', countries[lang][id.replace(/-(ecma|guide|it)$/, '')] || countries[lang][id]);
    }
    dictionaries[lang] = makeDictionary(ui[lang], countries[lang], localizedDescriptions);
  }
  dictionaries.xk = dictionaries.sq;

  const localeTags = {
    it: 'it', en: 'en', fr: 'fr', de: 'de', es: 'es', pt: 'pt',
    sq: 'sq-XK', xk: 'sq-XK', ro: 'ro-RO', nl: 'nl', el: 'el-GR',
    bs: 'bs-BA', ar: 'ar', zh: 'zh-CN', hi: 'hi', vi: 'vi'
  };
  const aliases = {
    it: 'it', en: 'en', fr: 'fr', de: 'de', es: 'es', pt: 'pt',
    sq: 'sq', 'sq-xk': 'xk', xk: 'xk', ro: 'ro', 'ro-ro': 'ro',
    nl: 'nl', el: 'el', 'el-gr': 'el', bs: 'bs', 'bs-ba': 'bs',
    ar: 'ar', zh: 'zh', 'zh-cn': 'zh', hi: 'hi', vi: 'vi'
  };
  const normalize = value => String(value ?? '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLocaleLowerCase();
  const cards = [...document.querySelectorAll('.authority-card[data-id]')];
  const input = document.getElementById('authority-search');
  const reset = document.getElementById('reset-search');
  const status = document.getElementById('results-status');
  const selector = document.getElementById('language');
  const emptyState = document.getElementById('empty-state');
  let activeLanguage = 'it';

  function resolveLanguage(value) {
    const normalized = String(value || '').trim().replace(/_/g, '-').toLowerCase();
    return aliases[normalized] || aliases[normalized.split('-')[0]] || 'it';
  }
  function queryLanguage() {
    return new URLSearchParams(window.location.search).get('lang') || '';
  }
  function getDictionaryKey(id) {
    return `region-${id}`;
  }
  function applyLanguage(language, updateUrl = false) {
    activeLanguage = dictionaries[language] ? language : 'it';
    const dict = dictionaries[activeLanguage];
    const locale = localeTags[activeLanguage];
    document.documentElement.lang = locale;
    document.documentElement.dir = activeLanguage === 'ar' ? 'rtl' : 'ltr';
    document.title = `${dict.directoryTitle} · AMEV`;
    const metaDescription = document.querySelector('meta[name="description"]');
    if (metaDescription) metaDescription.content = dict.intro;
    for (const node of document.querySelectorAll('[data-i18n]')) {
      const key = node.dataset.i18n;
      if (dict[key] !== undefined) node.textContent = dict[key];
    }
    input.placeholder = dict.searchPlaceholder;
    input.setAttribute('aria-label', dict.searchLabel);
    selector.setAttribute('aria-label', dict.languageLabel);
    for (const card of cards) {
      const id = card.dataset.id;
      const region = dict[getDictionaryKey(id)];
      const description = dict[`description-${id}`];
      if (region) card.querySelector('[data-country-label]').textContent = region;
      if (description) card.querySelector('[data-description]').textContent = description;
    }
    const collator = new Intl.Collator(locale, { sensitivity: 'base', numeric: true });
    const grid = document.getElementById('authority-list');
    cards.sort((a, b) => collator.compare(
      a.querySelector('[data-country-label]').textContent,
      b.querySelector('[data-country-label]').textContent
    ) || collator.compare(a.querySelector('.authority-name').textContent, b.querySelector('.authority-name').textContent));
    cards.forEach(card => grid.append(card));
    selector.value = activeLanguage;
    filterCards();
    if (updateUrl) {
      const url = new URL(window.location.href);
      url.searchParams.set('lang', activeLanguage);
      window.history.replaceState(null, '', url);
    }
  }
  function filterCards() {
    const needle = normalize(input.value);
    let visible = 0;
    for (const card of cards) {
      const matches = !needle || normalize([
        card.textContent,
        card.dataset.terms,
        card.dataset.country,
        card.querySelector('.authority-name').textContent
      ].join(' ')).includes(needle);
      card.hidden = !matches;
      if (matches) visible++;
    }
    status.textContent = visible === 18
      ? dictionaries[activeLanguage].count
      : dictionaries[activeLanguage].resultCount.replace('{count}', new Intl.NumberFormat(localeTags[activeLanguage]).format(visible));
    emptyState.hidden = visible !== 0;
  }
  function search(query) {
    input.value = String(query ?? '');
    filterCards();
  }
  function resetSearch() {
    input.value = '';
    filterCards();
  }

  input.disabled = false;
  selector.disabled = false;
  reset.disabled = false;
  input.addEventListener('input', filterCards);
  reset.addEventListener('click', resetSearch);
  selector.addEventListener('change', () => applyLanguage(resolveLanguage(selector.value), true));

  const flagsLink = document.querySelector('link[href*="flag-icons@7.2.3"]');
  const markFallbackFlags = () => document.documentElement.classList.add('flag-icons-unavailable');
  if (flagsLink) {
    flagsLink.addEventListener('error', markFallbackFlags, { once: true });
    if (!flagsLink.sheet) window.setTimeout(() => {
      if (!flagsLink.sheet) markFallbackFlags();
    }, 1200);
  }

  applyLanguage(resolveLanguage(queryLanguage()));

  const publicApi = {
    get language() { return activeLanguage; },
    getLinks() {
      return Object.freeze([...document.querySelectorAll('a[data-authority-link]')].map(link => link.getAttribute('href')));
    },
    search,
    reset: resetSearch,
    setLanguage(value) {
      const resolved = resolveLanguage(value);
      applyLanguage(resolved, true);
      return activeLanguage;
    },
    getVisibleCount() { return cards.filter(card => !card.hidden).length; },
    normalize
  };
  Object.defineProperty(window, 'authorityConsumer', {
    value: Object.freeze(publicApi),
    enumerable: true,
    configurable: false,
    writable: false
  });
})();
