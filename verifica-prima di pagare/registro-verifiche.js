/* Registro verifiche di coerenza home page Authorithy — logica client.
 * Dati: ./registro-verifiche.json (generato da scripts/import_verification_register.py)
 * e ../financial_authorities_database.json (fonte autorevole di ID, nomi e homepage).
 * Tutto il testo importato è trattato come non attendibile: solo textContent e link http(s).
 */
(function () {
  'use strict';

  const CYCLE_LENGTH = 20;
  const DAILY_TARGET = 12;
  const VALIDITY_DAYS = 20;
  const DAY_MS = 86400000;
  const TABLE_COLUMNS = ['Authority', 'Homepage nel dataset', 'URL controllato', 'URL finale', 'Data e ora',
    'Ciclo / giorno', 'Esito', 'Revisore', 'Note', 'Evidenza pubblica', 'Stato attuale Authority'];
  const TIMEZONE = 'Europe/Rome';
  const STATUSES = ['verified', 'needs_review', 'unreachable', 'blocked'];
  const OUTCOME_LABELS = {
    verified: 'Coerente (verifica positiva)',
    needs_review: 'Da approfondire',
    unreachable: 'Non raggiungibile',
    blocked: 'Accesso bloccato'
  };
  const SLOT_LABELS = {
    unreviewed: 'Nessuna verifica documentata',
    partial: 'Parziale',
    completed: 'Completato',
    problematic: 'Con criticità'
  };
  const AUTHORITY_LABELS = {
    verified: '✓ Verificata',
    renewal: 'Da rinnovare',
    warning: '⚠ Attenzione',
    undocumented: 'Nessuna verifica documentata'
  };
  const DATE_RE = /^\d{4}-\d{2}-\d{2}$/;
  const DATETIME_RE = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}(:\d{2})?(Z|[+-]\d{2}:\d{2})$/;
  const CYCLE_ID_RE = /^[A-Za-z0-9][A-Za-z0-9_.-]{0,39}$/;
  const romeDateFormat = new Intl.DateTimeFormat('en-CA', {
    timeZone: TIMEZONE, year: 'numeric', month: '2-digit', day: '2-digit'
  });
  const itDateFormat = new Intl.DateTimeFormat('it-IT', {
    timeZone: 'UTC', day: 'numeric', month: 'short', year: 'numeric'
  });
  const itDateTimeFormat = new Intl.DateTimeFormat('it-IT', {
    timeZone: TIMEZONE, day: 'numeric', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit'
  });

  function normalizeUrl(value) {
    if (typeof value !== 'string' || !value || value.length > 2048
      || /[\s\u0000-\u001f\u007f\u202a-\u202e\u2066-\u2069]/.test(value)) return null;
    let url;
    try { url = new URL(value); } catch (error) { return null; }
    if ((url.protocol !== 'http:' && url.protocol !== 'https:') || url.username || url.password
      || !url.hostname) return null;
    return url.protocol + '//' + url.host + url.pathname + url.search;
  }

  function parseInstant(value) {
    if (typeof value !== 'string' || !DATETIME_RE.test(value)) return null;
    if (dateToUtc(value.slice(0, 10)) === null) return null;
    const ms = Date.parse(value);
    return Number.isFinite(ms) ? ms : null;
  }

  function dateToUtc(isoDate) {
    if (typeof isoDate !== 'string' || !DATE_RE.test(isoDate)) return null;
    const [y, m, d] = isoDate.split('-').map(Number);
    const ms = Date.UTC(y, m - 1, d);
    const check = new Date(ms);
    return check.getUTCFullYear() === y && check.getUTCMonth() === m - 1 && check.getUTCDate() === d ? ms : null;
  }

  function romeDate(ms) {
    const parts = {};
    romeDateFormat.formatToParts(new Date(ms)).forEach(part => { parts[part.type] = part.value; });
    return parts.year + '-' + parts.month + '-' + parts.day;
  }

  function addDays(isoDate, days) {
    return new Date(dateToUtc(isoDate) + days * DAY_MS).toISOString().slice(0, 10);
  }

  function formatDate(isoDate) { return itDateFormat.format(new Date(dateToUtc(isoDate))); }
  function formatDateTime(ms) { return itDateTimeFormat.format(new Date(ms)) + ' (ora di Roma)'; }

  /* Converte registro + dataset in un modello validato; i record non validi sono scartati e contati. */
  function prepare(register, dataset) {
    const authorities = new Map();
    Object.keys(dataset || {}).forEach(country => {
      const record = dataset[country] || {};
      const fa = record.financial_authority || {};
      if (typeof fa.authorityId === 'string' && !authorities.has(fa.authorityId)) {
        authorities.set(fa.authorityId, {
          id: fa.authorityId,
          name: typeof fa.name === 'string' && fa.name ? fa.name : fa.authorityId,
          country: typeof record.country_name === 'string' ? record.country_name : country,
          homepage: typeof fa.homepage === 'string' ? fa.homepage : ''
        });
      }
    });
    const cycles = new Map();
    (Array.isArray(register && register.cycles) ? register.cycles : []).forEach(cycle => {
      if (cycle && typeof cycle.cycleId === 'string' && CYCLE_ID_RE.test(cycle.cycleId)
        && dateToUtc(cycle.startDate) !== null && !cycles.has(cycle.cycleId)) {
        cycles.set(cycle.cycleId, {
          cycleId: cycle.cycleId, startDate: cycle.startDate, endDate: addDays(cycle.startDate, CYCLE_LENGTH - 1)
        });
      }
    });
    const reviews = [];
    let rejected = 0;
    (Array.isArray(register && register.reviews) ? register.reviews : []).forEach(raw => {
      const cycle = raw && cycles.get(raw.cycleId);
      const instant = raw && parseInstant(raw.reviewedAt);
      const valid = cycle && instant !== null && typeof raw.authorityId === 'string'
        && STATUSES.includes(raw.status) && Number.isInteger(raw.cycleDay)
        && raw.cycleDay >= 1 && raw.cycleDay <= CYCLE_LENGTH
        && raw.cycleDay === Math.round((dateToUtc(romeDate(instant)) - dateToUtc(cycle.startDate)) / DAY_MS) + 1
        && normalizeUrl(raw.checkedUrl) !== null
        && typeof raw.reviewedBy === 'string' && raw.reviewedBy.trim()
        && (raw.status !== 'verified' || normalizeUrl(raw.finalUrl) !== null)
        && (!raw.finalUrl || normalizeUrl(raw.finalUrl) !== null)
        && (raw.status === 'verified' || (typeof raw.notes === 'string' && raw.notes.trim()));
      if (!valid) { rejected += 1; return; }
      const text = key => (typeof raw[key] === 'string' ? raw[key] : '');
      reviews.push({
        authorityId: raw.authorityId, cycleId: raw.cycleId, cycleDay: raw.cycleDay, instant: instant,
        reviewedAt: raw.reviewedAt, status: raw.status, checkedUrl: raw.checkedUrl, finalUrl: text('finalUrl'),
        reviewedBy: text('reviewedBy'), notes: text('notes'), evidenceRef: text('evidenceRef')
      });
    });
    const lastRecorded = parseInstant(register && register.lastRecordedReviewAt);
    return {
      authorities: authorities,
      cycles: Array.from(cycles.values()).sort((a, b) => dateToUtc(a.startDate) - dateToUtc(b.startDate)),
      reviews: reviews,
      rejected: rejected,
      lastRecordedReviewAt: lastRecorded
    };
  }

  /* Stato corrente: verde solo con ultimo esito positivo, < 20 giorni e URL uguale al dataset. */
  function authorityStatus(reviews, homepage, now) {
    if (!reviews.length) return { state: 'undocumented', latest: null, lastPositive: null };
    const ordered = reviews.slice().sort((a, b) => a.instant - b.instant);
    const latest = ordered[ordered.length - 1];
    const positives = ordered.filter(review => review.status === 'verified');
    const lastPositive = positives.length ? positives[positives.length - 1] : null;
    let state;
    if (latest.status !== 'verified' || latest.instant > now) state = 'warning';
    else if (normalizeUrl(latest.checkedUrl) !== normalizeUrl(homepage)) state = 'warning';
    else if (now - latest.instant >= VALIDITY_DAYS * DAY_MS) state = 'renewal';
    else state = 'verified';
    return { state: state, latest: latest, lastPositive: lastPositive };
  }

  function statusDetail(result, authority) {
    const latest = result.latest;
    if (!latest) return 'Nessun controllo registrato.';
    if (result.state === 'verified') {
      return 'Valida fino al ' + formatDateTime(latest.instant + VALIDITY_DAYS * DAY_MS) + '.';
    }
    if (result.state === 'renewal') {
      return 'Ultima verifica positiva oltre ' + VALIDITY_DAYS + ' giorni fa: da ripetere.';
    }
    if (latest.status !== 'verified') {
      const previous = result.lastPositive
        ? ' Ultima verifica positiva storica: ' + formatDateTime(result.lastPositive.instant) + '.' : '';
      return 'Ultimo controllo: ' + OUTCOME_LABELS[latest.status] + '.' + previous;
    }
    if (!authority) return 'Authority non presente nel dataset attuale.';
    return 'L\u2019URL controllato non coincide più con la homepage attuale del dataset.';
  }

  function cycleSlots(cycle, reviews) {
    const slots = [];
    for (let day = 1; day <= CYCLE_LENGTH; day++) {
      const latestByAuthority = new Map();
      if (cycle) {
        reviews.forEach(review => {
          if (review.cycleId !== cycle.cycleId || review.cycleDay !== day) return;
          const current = latestByAuthority.get(review.authorityId);
          if (!current || review.instant > current.instant) latestByAuthority.set(review.authorityId, review);
        });
      }
      const reviewed = latestByAuthority.size;
      let positive = 0;
      latestByAuthority.forEach(review => { if (review.status === 'verified') positive += 1; });
      const problems = reviewed - positive;
      const state = !reviewed ? 'unreviewed' : problems ? 'problematic'
        : reviewed >= DAILY_TARGET ? 'completed' : 'partial';
      slots.push({
        day: day, date: cycle ? addDays(cycle.startDate, day - 1) : null,
        reviewed: reviewed, positive: positive, problems: problems, state: state
      });
    }
    return slots;
  }

  function el(tag, attributes, children) {
    const node = document.createElement(tag);
    Object.keys(attributes || {}).forEach(name => node.setAttribute(name, attributes[name]));
    (children || []).forEach(child => {
      node.appendChild(typeof child === 'string' ? document.createTextNode(child) : child);
    });
    return node;
  }

  function safeLink(url) {
    if (!url) return document.createTextNode('—');
    if (!normalizeUrl(url)) return document.createTextNode(url);
    return el('a', { href: url, target: '_blank', rel: 'noopener noreferrer nofollow' }, [url]);
  }

  /* Disegna il registro dentro container. options.now() restituisce l'istante corrente (ms). */
  function render(container, register, dataset, options) {
    const settings = options || {};
    const now = settings.now || (() => Date.now());
    const model = prepare(register, dataset);
    const byAuthority = new Map();
    model.reviews.forEach(review => {
      if (!byAuthority.has(review.authorityId)) byAuthority.set(review.authorityId, []);
      byAuthority.get(review.authorityId).push(review);
    });
    const total = model.authorities.size;
    const selection = {
      cycle: model.cycles.length ? model.cycles[model.cycles.length - 1] : null,
      day: null
    };

    const summaryText = model.reviews.length
      ? 'Verifiche pubblicate: ' + model.reviews.length + ' · Authority distinte controllate: '
        + byAuthority.size + ' su ' + total + ' nel dataset'
        + (model.lastRecordedReviewAt !== null
          ? ' · Ultima verifica registrata: ' + formatDateTime(model.lastRecordedReviewAt) : '') + '.'
      : 'Nessuna verifica documentata. Authority nel dataset: ' + total + '; nessun ciclo avviato.';
    const overviewList = el('ul', { class: 'authority-overview' });
    const overviewNote = el('p', { class: 'register-note' });
    const cycleTitle = el('h3', { id: 'registro-cycle-title' });
    const cycleProgress = el('p', { class: 'register-note' });
    const grid = el('ol', { class: 'cycle-grid', 'aria-labelledby': 'registro-cycle-title' });
    const filterNote = el('p', { class: 'register-note', id: 'registro-filter-note' });
    const clearButton = el('button', { type: 'button', class: 'register-button' }, ['Mostra tutte le verifiche']);
    const tbody = el('tbody');
    const statusCells = [];
    let slotButtons = [];

    function computeStatuses(at) {
      const counts = { verified: 0, renewal: 0, warning: 0, undocumented: 0 };
      const results = new Map();
      model.authorities.forEach(authority => {
        const result = authorityStatus(byAuthority.get(authority.id) || [], authority.homepage, at);
        results.set(authority.id, result);
        counts[result.state] += 1;
      });
      return { counts: counts, results: results };
    }

    function statusFor(authorityId, at, computed) {
      if (computed.results.has(authorityId)) return computed.results.get(authorityId);
      return authorityStatus(byAuthority.get(authorityId) || [], '', at);
    }

    function fillStatusCell(cell, result, authority) {
      cell.className = 'authority-state state-' + result.state;
      cell.replaceChildren(el('strong', {}, [AUTHORITY_LABELS[result.state]]), el('br'),
        el('span', {}, [statusDetail(result, authority)]));
    }

    function refresh() {
      const at = now();
      const computed = computeStatuses(at);
      overviewList.replaceChildren.apply(overviewList, ['verified', 'renewal', 'warning', 'undocumented'].map(state =>
        el('li', { class: 'state-' + state }, [
          el('strong', {}, [AUTHORITY_LABELS[state] + ': ']), String(computed.counts[state])
        ])));
      overviewNote.textContent = 'Da verificare o rinnovare: ' + (total - computed.counts.verified) + ' su '
        + total + '. Stato calcolato in questo browser il ' + formatDateTime(at)
        + '; viene ricalcolato a ogni apertura e ogni minuto.';
      statusCells.forEach(item => fillStatusCell(item.cell, statusFor(item.authorityId, at, computed),
        model.authorities.get(item.authorityId)));
      const today = romeDate(at);
      slotButtons.forEach(item => {
        const isToday = item.slot.date === today;
        if (isToday) item.button.setAttribute('aria-current', 'date');
        else item.button.removeAttribute('aria-current');
        item.today.textContent = isToday ? 'Oggi' : '';
      });
      return computed;
    }

    function renderTable() {
      const at = now();
      const computed = computeStatuses(at);
      statusCells.length = 0;
      const rows = model.reviews.filter(review => !selection.day
        || (review.cycleId === selection.cycle.cycleId && review.cycleDay === selection.day))
        .sort((a, b) => b.instant - a.instant || (a.authorityId < b.authorityId ? -1 : 1));
      if (selection.day) {
        const slot = cycleSlots(selection.cycle, model.reviews)[selection.day - 1];
        filterNote.textContent = 'Filtro attivo: giorno ' + selection.day + '/' + CYCLE_LENGTH + ' del ciclo '
          + selection.cycle.cycleId + ' (' + formatDate(slot.date) + ') — ' + rows.length + ' verifiche.';
        clearButton.hidden = false;
      } else {
        filterNote.textContent = 'Mostrate tutte le verifiche pubblicate (' + rows.length
          + '). Seleziona un giorno del ciclo per filtrare la tabella.';
        clearButton.hidden = true;
      }
      if (!rows.length) {
        tbody.replaceChildren(el('tr', {}, [el('td', { colspan: String(TABLE_COLUMNS.length), class: 'empty-state' }, [
          selection.day ? 'Nessuna verifica documentata per questo giorno del ciclo'
            : 'Nessuna verifica documentata'])]));
        return;
      }
      tbody.replaceChildren.apply(tbody, rows.map(review => {
        const authority = model.authorities.get(review.authorityId);
        const statusCell = el('td');
        fillStatusCell(statusCell, statusFor(review.authorityId, at, computed), authority);
        statusCells.push({ cell: statusCell, authorityId: review.authorityId });
        const notes = el('td', { class: 'notes' }, [review.notes || '—']);
        return el('tr', {}, [
          el('th', { scope: 'row' }, [
            el('span', { class: 'authority-name' }, [authority
              ? authority.name + ' (' + authority.country + ')' : 'ID non presente nel dataset attuale']),
            el('code', {}, [review.authorityId])
          ]),
          el('td', {}, [authority ? safeLink(authority.homepage) : '—']),
          el('td', {}, [safeLink(review.checkedUrl)]),
          el('td', {}, [safeLink(review.finalUrl)]),
          el('td', {}, [el('time', { datetime: review.reviewedAt }, [formatDateTime(review.instant)])]),
          el('td', {}, [review.cycleId + ' · ' + review.cycleDay + '/' + CYCLE_LENGTH]),
          el('td', { class: 'outcome-' + review.status }, [OUTCOME_LABELS[review.status]]),
          el('td', {}, [review.reviewedBy || '—']),
          notes,
          el('td', {}, [safeLink(review.evidenceRef)]),
          statusCell
        ]);
      }));
    }

    function renderGrid() {
      const cycle = selection.cycle;
      if (cycle) {
        cycleTitle.textContent = 'Ciclo ' + cycle.cycleId + ': dal ' + formatDate(cycle.startDate)
          + ' al ' + formatDate(cycle.endDate);
        const inCycle = model.reviews.filter(review => review.cycleId === cycle.cycleId);
        const distinct = new Set(inCycle.map(review => review.authorityId));
        const positive = new Set();
        distinct.forEach(id => {
          const last = inCycle.filter(review => review.authorityId === id).sort((a, b) => b.instant - a.instant)[0];
          if (last.status === 'verified') positive.add(id);
        });
        cycleProgress.textContent = 'In questo ciclo: ' + distinct.size + ' Authority distinte controllate, '
          + positive.size + ' con ultimo esito positivo. Obiettivo: ' + DAILY_TARGET + ' controlli al giorno ('
          + DAILY_TARGET + ' × ' + CYCLE_LENGTH + ' = ' + (DAILY_TARGET * CYCLE_LENGTH)
          + '); Authority nel dataset: ' + total + '.';
      } else {
        cycleTitle.textContent = 'Ciclo non ancora avviato: giorni numerati senza date di calendario';
        cycleProgress.textContent = 'Le date compariranno quando sarà pubblicato un ciclo con data di inizio '
          + 'esplicita. Obiettivo: ' + DAILY_TARGET + ' controlli al giorno (' + DAILY_TARGET + ' × '
          + CYCLE_LENGTH + ' = ' + (DAILY_TARGET * CYCLE_LENGTH) + '); Authority nel dataset: ' + total + '.';
      }
      slotButtons = cycleSlots(cycle, model.reviews).map(slot => {
        const today = el('span', { class: 'slot-today' });
        const button = el('button', {
          type: 'button', class: 'cycle-slot slot-' + slot.state, 'aria-pressed': 'false',
          'aria-controls': 'registro-table'
        }, [
          el('span', { class: 'slot-label' }, [el('span', { class: 'visually-hidden' }, ['Giorno ']),
            slot.day + '/' + CYCLE_LENGTH]),
          el('span', { class: 'slot-date' }, [slot.date ? formatDate(slot.date) : 'Data non assegnata']),
          el('span', { class: 'slot-state' }, [SLOT_LABELS[slot.state]]),
          el('span', { class: 'slot-count' }, [slot.reviewed + '/' + DAILY_TARGET + ' controlli · '
            + slot.positive + ' positivi · ' + slot.problems + ' criticità']),
          today
        ]);
        if (!cycle) button.setAttribute('aria-disabled', 'true');
        button.addEventListener('click', () => {
          if (!cycle) return;
          selection.day = selection.day === slot.day ? null : slot.day;
          slotButtons.forEach(item => item.button.setAttribute('aria-pressed',
            String(item.slot.day === selection.day)));
          renderTable();
        });
        return { slot: slot, button: button, today: today };
      });
      grid.replaceChildren.apply(grid, slotButtons.map(item => el('li', {}, [item.button])));
    }

    clearButton.addEventListener('click', () => {
      selection.day = null;
      slotButtons.forEach(item => item.button.setAttribute('aria-pressed', 'false'));
      renderTable();
    });

    const children = [
      el('p', { class: 'register-summary' }, [summaryText]),
      el('h3', {}, ['Stato attuale delle Authority']), overviewList, overviewNote
    ];
    if (model.cycles.length > 1) {
      const select = el('select', { id: 'registro-cycle-select' });
      model.cycles.slice().reverse().forEach(cycle => {
        select.appendChild(el('option', { value: cycle.cycleId }, [cycle.cycleId + ' (dal '
          + formatDate(cycle.startDate) + ')']));
      });
      select.addEventListener('change', () => {
        selection.cycle = model.cycles.find(cycle => cycle.cycleId === select.value);
        selection.day = null;
        renderGrid();
        renderTable();
        refresh();
      });
      children.push(el('p', { class: 'register-note' }, [
        el('label', { for: 'registro-cycle-select' }, ['Ciclo da visualizzare (i cicli storici restano separati): ']),
        select]));
    }
    children.push(cycleTitle, cycleProgress, grid,
      el('h3', { id: 'registro-table-title' }, ['Verifiche pubblicate']), filterNote, clearButton,
      el('div', { class: 'table-scroll', role: 'region', 'aria-labelledby': 'registro-table-title', tabindex: '0' }, [
        el('table', { class: 'register-table', id: 'registro-table' }, [
          el('thead', {}, [el('tr', {}, TABLE_COLUMNS.map(label => el('th', { scope: 'col' }, [label])))]),
          tbody
        ])
      ]));
    if (model.rejected) {
      children.unshift(el('p', { class: 'register-error', role: 'alert' }, [model.rejected
        + ' record del registro non sono validi e non sono mostrati: segnalare ai manutentori.']));
    }
    renderGrid();
    renderTable();
    container.replaceChildren.apply(container, children);
    refresh();
    return { refresh: refresh, model: model, selectDay: day => slotButtons[day - 1].button.click() };
  }

  function fetchJson(url) {
    return fetch(url, { cache: 'no-cache', credentials: 'same-origin' }).then(response => {
      if (!response.ok) throw new Error(url + ': HTTP ' + response.status);
      return response.json();
    });
  }

  const liveRefresh = { timer: null, controller: null };
  document.addEventListener('visibilitychange', () => {
    if (document.visibilityState === 'visible' && liveRefresh.controller) liveRefresh.controller.refresh();
  });
  window.addEventListener('pagehide', () => clearInterval(liveRefresh.timer));

  function load() {
    const section = document.getElementById('registro-verifiche');
    const container = document.getElementById('registro-data');
    const status = document.getElementById('registro-load-status');
    if (!section || !container || !status) return Promise.resolve(null);
    status.textContent = 'Caricamento del registro e del dataset delle Authority…';
    return Promise.all([
      fetchJson(section.getAttribute('data-register-src')),
      fetchJson(section.getAttribute('data-dataset-src'))
    ]).then(([register, dataset]) => {
      const controller = render(container, register, dataset);
      status.textContent = '';
      clearInterval(liveRefresh.timer);
      liveRefresh.controller = controller;
      liveRefresh.timer = setInterval(controller.refresh, 60000);
      return controller;
    }).catch(error => {
      status.textContent = 'Impossibile caricare i dati aggiornati del registro (connessione assente o errore '
        + 'del server). Restano visibili i dati statici pubblicati; lo stato attuale delle Authority non è '
        + 'stato calcolato.';
      status.classList.add('register-error');
      console.error(error);
      return null;
    });
  }

  window.AmevRegister = {
    CYCLE_LENGTH: CYCLE_LENGTH, DAILY_TARGET: DAILY_TARGET, VALIDITY_DAYS: VALIDITY_DAYS, TIMEZONE: TIMEZONE,
    normalizeUrl: normalizeUrl, romeDate: romeDate, prepare: prepare, authorityStatus: authorityStatus,
    cycleSlots: cycleSlots, render: render, load: load
  };
  window.AmevRegister.ready = document.readyState === 'loading'
    ? new Promise(resolve => document.addEventListener('DOMContentLoaded', () => resolve(load())))
    : load();
}());
