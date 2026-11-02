window.PublicDossier = (() => {
    const url = 'https://raw.githubusercontent.com/avvstancamarcello/Airtable-news/main/data/dossier.json';
    const list = document.getElementById('dossier-list');
    const status = document.getElementById('dossier-status');
    const copy = {
        it: ['Dossier', 'Caricamento dossier…', 'Nessun dossier pubblicato', 'Dossier non disponibili', 'Nota editoriale', 'Download', 'Traduzione non disponibile; lingua:'],
        en: ['Dossiers', 'Loading dossiers…', 'No published dossiers', 'Dossiers unavailable', 'Editorial note', 'Downloads', 'Translation unavailable; language:'],
        fr: ['Dossiers', 'Chargement des dossiers…', 'Aucun dossier publié', 'Dossiers indisponibles', 'Note éditoriale', 'Téléchargements', 'Traduction indisponible ; langue :'],
        es: ['Dosieres', 'Cargando dosieres…', 'No hay dosieres publicados', 'Dosieres no disponibles', 'Nota editorial', 'Descargas', 'Traducción no disponible; idioma:'],
        de: ['Dossiers', 'Dossiers werden geladen…', 'Keine veröffentlichten Dossiers', 'Dossiers nicht verfügbar', 'Redaktioneller Hinweis', 'Downloads', 'Übersetzung nicht verfügbar; Sprache:'],
        pt: ['Dossiês', 'A carregar dossiês…', 'Nenhum dossiê publicado', 'Dossiês indisponíveis', 'Nota editorial', 'Transferências', 'Tradução indisponível; idioma:']
    };
    let records = [];
    let state = 'loading';
    let language = 'it';

    const object = value => value !== null && typeof value === 'object' && !Array.isArray(value);
    const text = value => typeof value === 'string' && value.trim();

    function validate(payload) {
        if (!Array.isArray(payload) || payload.some(record => {
            if (!object(record) || !text(record.id)) return true;
            if (['title', 'author', 'source_type', 'notebook_url', 'file_links', 'editorial_note']
                .some(key => record[key] != null && typeof record[key] !== 'string')) return true;
            if (['titles', 'texts'].some(key => record[key] != null
                && (!object(record[key]) || Object.values(record[key]).some(value => typeof value !== 'string')))) return true;
            return ['tags', 'languages'].some(key => record[key] != null
                && (!Array.isArray(record[key]) || record[key].some(value => typeof value !== 'string')));
        })) throw new Error('Invalid dossier feed');
        return payload;
    }

    function select(values = {}) {
        values = values || {};
        const code = [language, 'en', 'it', ...Object.keys(values)].find(key => text(values[key]));
        return code ? { lang: code, value: values[code] } : null;
    }

    function appendText(parent, tag, value, lang) {
        if (!text(value)) return;
        const element = document.createElement(tag);
        element.textContent = value;
        if (lang) element.lang = lang;
        element.dir = lang === 'ar' ? 'rtl' : 'auto';
        parent.append(element);
    }

    function appendLink(parent, value, label) {
        const href = PublicNews.safeUrl(value);
        if (!href) return;
        const link = document.createElement('a');
        link.className = 'news-link';
        link.href = href;
        link.target = '_blank';
        link.rel = 'noopener noreferrer';
        link.textContent = label;
        parent.append(link);
    }

    function card(record, messages) {
        const article = document.createElement('article');
        article.className = 'news-card dossier-card';
        article.dataset.dossierId = record.id;
        const title = select(record.titles);
        const body = select(record.texts);
        const usedLanguages = new Set([title?.lang, body?.lang].filter(code => code && code !== language));
        appendText(article, 'h3', title?.value || record.title || messages[0], title?.lang);
        appendText(article, 'p', [record.author, record.source_type].filter(Boolean).join(' · '));
        appendText(article, 'p', body?.value, body?.lang);
        for (const code of usedLanguages) {
            appendText(article, 'p', `${messages[6]} ${code}`, language);
        }
        appendText(article, 'p', record.tags?.join(' · '));
        if (text(record.editorial_note)) {
            appendText(article, 'h4', messages[4], language);
            appendText(article, 'p', record.editorial_note);
        }
        appendLink(article, record.notebook_url, 'Notebook');
        if (text(record.file_links)) {
            appendText(article, 'h4', messages[5], language);
            appendText(article, 'p', record.file_links);
            const links = record.file_links.match(/https?:\/\/[^\s<>"\])]+/g) || [];
            for (const link of new Set(links)) appendLink(article, link, link);
        }
        return article;
    }

    function render(lang = language) {
        language = lang;
        const messages = copy[language] || copy.en;
        document.getElementById('dossier-title').textContent = messages[0];
        list.replaceChildren(...records.map(record => card(record, messages)));
        list.setAttribute('aria-busy', String(state === 'loading'));
        status.textContent = messages[state === 'loading' ? 1 : state === 'error' ? 3 : 2];
        status.classList.toggle('is-visible', state !== 'loaded' || !records.length);
    }

    async function load() {
        state = 'loading';
        records = [];
        render();
        try {
            const response = await fetch(url, { cache: 'no-cache', headers: { Accept: 'application/json' } });
            if (!response.ok) throw new Error(`Dossier request failed (HTTP ${response.status})`);
            records = validate(await response.json());
            state = 'loaded';
        } catch (error) {
            state = 'error';
            console.error('Dossier feed could not be loaded:', error);
        }
        render();
    }

    return { load, render };
})();
