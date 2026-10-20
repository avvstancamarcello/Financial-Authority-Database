/* Public GitHub export only; publication dates are displayed consistently in UTC. */
window.PublicNews = (() => {
    const url = 'https://raw.githubusercontent.com/avvstancamarcello/Airtable-news/main/news.json';
    const languages = Object.freeze({
        it: { label: 'Italiano', flag: '🇮🇹' },
        en: { label: 'English', flag: '🇬🇧' },
        me: { label: 'Crnogorski', flag: '🇲🇪' },
        ar: { label: 'العربية', flag: '🌍' },
        hi: { label: 'हिन्दी', flag: '🇮🇳' },
        sq: { label: 'Shqip', flag: '🇦🇱' },
        ro: { label: 'Română', flag: '🇷🇴' },
        et: { label: 'Eesti', flag: '🇪🇪' },
        lt: { label: 'Lietuvių', flag: '🇱🇹' },
        bs: { label: 'Bosanski', flag: '🇧🇦' },
        nl: { label: 'Nederlands', flag: '🇳🇱' },
        sv: { label: 'Svenska', flag: '🇸🇪' },
        el: { label: 'Ελληνικά', flag: '🇬🇷' },
        fr: { label: 'Français', flag: '🇫🇷' }
    });
    function language(value) {
        return Object.hasOwn(languages, value) ? languages[value] : { label: 'Unknown language', flag: '🌍' };
    }
    const copy = {
        it: ['NEWS PUBBLICATE', 'Caricamento notizie…', 'Nessuna notizia pubblicata', 'Notizie non disponibili', 'Traduzione non disponibile; lingua:'],
        en: ['NEWS PUBLISHED', 'Loading news…', 'No published news', 'News unavailable', 'Translation unavailable; language:'],
        fr: ['ACTUALITÉS PUBLIÉES', 'Chargement des actualités…', 'Aucune actualité publiée', 'Actualités indisponibles', 'Traduction indisponible ; langue :'],
        de: ['NEWS VERÖFFENTLICHT', 'Nachrichten werden geladen…', 'Keine veröffentlichten Nachrichten', 'Nachrichten nicht verfügbar', 'Übersetzung nicht verfügbar; Sprache:'],
        es: ['NOTICIAS PUBLICADAS', 'Cargando noticias…', 'No hay noticias publicadas', 'Noticias no disponibles', 'Traducción no disponible; idioma:'],
        pt: ['NOTÍCIAS PUBLICADAS', 'A carregar notícias…', 'Nenhuma notícia publicada', 'Notícias indisponíveis', 'Tradução indisponível; idioma:'],
        nl: ['GEPUBLICEERD NIEUWS', 'Nieuws laden…', 'Geen gepubliceerd nieuws', 'Nieuws niet beschikbaar'],
        ro: ['ȘTIRI PUBLICATE', 'Se încarcă știrile…', 'Nu există știri publicate', 'Știri indisponibile'],
        bs: ['OBJAVLJENE VIJESTI', 'Učitavanje vijesti…', 'Nema objavljenih vijesti', 'Vijesti nisu dostupne'],
        sq: ['LAJME TË PUBLIKUARA', 'Duke ngarkuar lajmet…', 'Nuk ka lajme të publikuara', 'Lajmet nuk janë të disponueshme'],
        el: ['ΔΗΜΟΣΙΕΥΜΕΝΕΣ ΕΙΔΗΣΕΙΣ', 'Φόρτωση ειδήσεων…', 'Δεν υπάρχουν δημοσιευμένες ειδήσεις', 'Οι ειδήσεις δεν είναι διαθέσιμες'],
        ar: ['الأخبار المنشورة', 'جارٍ تحميل الأخبار…', 'لا توجد أخبار منشورة', 'الأخبار غير متاحة'],
        zh: ['已发布新闻', '正在加载新闻…', '暂无已发布新闻', '新闻暂不可用'],
        hi: ['समाचार प्रकाशित', 'समाचार लोड हो रहे हैं…', 'कोई प्रकाशित समाचार नहीं', 'समाचार उपलब्ध नहीं हैं'],
        vi: ['TIN TỨC ĐÃ ĐĂNG', 'Đang tải tin tức…', 'Chưa có tin tức được đăng', 'Tin tức không khả dụng']
    };
    function messages(lang) {
        return copy[lang === 'xk' ? 'sq' : lang.split('-')[0].toLowerCase()] || copy.en;
    }
    function timestamp(value) {
        if (typeof value !== 'string' || !/^\d{4}-\d{2}-\d{2}(?:T\d{2}:\d{2}(?::\d{2}(?:\.\d+)?)?(?:Z|[+-]\d{2}:\d{2}))?$/.test(value)) return NaN;
        const day = Date.parse(value.slice(0, 10));
        if (!Number.isFinite(day) || new Date(day).toISOString().slice(0, 10) !== value.slice(0, 10)) return NaN;
        if (value.includes('T') && (Number(value.slice(11, 13)) > 23 || Number(value.slice(14, 16)) > 59
            || (value[16] === ':' && Number(value.slice(17, 19)) > 59))) return NaN;
        return Date.parse(value);
    }
    function formatDate(value, lang) {
        return new Intl.DateTimeFormat(lang === 'en' ? 'en-GB' : lang === 'xk' || lang === 'sq-XK' ? 'sq' : lang, {
            day: '2-digit', month: '2-digit', year: 'numeric', timeZone: 'UTC'
        }).format(new Date(value));
    }
    function latest(records) {
        return records.reduce((date, record) => Math.max(date, timestamp(record.published_at)), -Infinity);
    }
    function label(records, state, lang) {
        const text = messages(lang);
        const date = latest(records);
        return state === 'loaded' && Number.isFinite(date)
            ? `${text[0]} · ${formatDate(date, lang)}`
            : text[state === 'loading' ? 1 : state === 'error' ? 3 : 2];
    }
    function safeUrl(value) {
        try {
            const parsed = new URL(value);
            return ['https:', 'http:'].includes(parsed.protocol) && !parsed.username && !parsed.password ? parsed.href : null;
        } catch { return null; }
    }
    async function load() {
        const response = await fetch(url, { cache: 'no-cache', headers: { Accept: 'application/json' } });
        if (!response.ok) throw new Error('News request failed');
        const payload = await response.json();
        const records = Array.isArray(payload) ? payload : payload && (payload.records || payload.news);
        if (!Array.isArray(records) || records.some(record => !record || typeof record !== 'object' || Array.isArray(record))) {
            throw new Error('Invalid news feed');
        }
        return records.filter(record => record.status === 'published' && Number.isFinite(timestamp(record.published_at))
            && ['news_id', 'lang', 'title'].every(key => typeof record[key] === 'string' && record[key].trim())
            && ['summary', 'body_text', 'theme', 'source_url'].every(key => record[key] == null || typeof record[key] === 'string'));
    }
    return { load, timestamp, formatDate, latest, label, safeUrl, messages, languages, language };
})();

const homepageNewsDate = document.getElementById('homepage-news-date');
if (homepageNewsDate) {
    let records = [];
    let state = 'loading';
    const update = () => {
        const lang = document.documentElement.lang || 'it';
        homepageNewsDate.textContent = PublicNews.label(records, state, lang);
        const link = document.querySelector('.language-hint-link');
        link.setAttribute('aria-label', PublicNews.messages(lang)[0]);
    };
    new MutationObserver(update).observe(document.documentElement, { attributes: true, attributeFilter: ['lang'] });
    update();
    PublicNews.load().then(data => { records = data; state = 'loaded'; })
        .catch(() => { state = 'error'; }).finally(update);
}
