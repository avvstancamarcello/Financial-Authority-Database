const CACHE_NAME = 'financial-authority-v30';
const BASE_PATH = '';
const FLAG_ICONS_CSS_URL = 'https://cdn.jsdelivr.net/gh/lipis/flag-icons@7.2.3/css/flag-icons.min.css';
const PWA_ICON_PATHS = [
  `${BASE_PATH}/icon-96.webp`,
  `${BASE_PATH}/icon-96.png`,
  `${BASE_PATH}/icon-192.png`,
  `${BASE_PATH}/icon-384.webp`,
  `${BASE_PATH}/icon-384.png`,
  `${BASE_PATH}/icon-512.png`
];
const CORE_APP_SHELL_URLS = [
  `${BASE_PATH}/`,
  `${BASE_PATH}/index.html`,
  `${BASE_PATH}/scripts/news-feed.js`,
  `${BASE_PATH}/odissea.html`,
  `${BASE_PATH}/db.enc`,
  `${BASE_PATH}/logo_shield_financial_defense.svg`,
  `${BASE_PATH}/logo_galaxy_yous.svg`,
  `${BASE_PATH}/manifest.json`,
  ...PWA_ICON_PATHS
];
const OPTIONAL_CACHE_URLS = [FLAG_ICONS_CSS_URL];

function isCacheableResponse(response) {
  return Boolean(response) && (response.ok || response.type === 'opaque');
}

function isAppNavigationRequest(request) {
  if (request.mode === 'navigate') {
    return true;
  }
  const acceptHeader = request.headers.get('accept') || '';
  return request.destination === 'document' || acceptHeader.includes('text/html');
}

function isSameOriginAppRequest(url) {
  return url.origin === self.location.origin
    && (url.pathname === BASE_PATH || url.pathname.startsWith(`${BASE_PATH}/`));
}

function isAppShellDocumentPath(pathname) {
  return pathname === BASE_PATH || pathname === `${BASE_PATH}/` || pathname === `${BASE_PATH}/index.html`;
}

function isAssetStaleWhileRevalidate(requestUrl) {
  const url = new URL(requestUrl);
  return requestUrl === FLAG_ICONS_CSS_URL || PWA_ICON_PATHS.includes(url.pathname);
}

async function cacheUrl(cache, url) {
  const response = await fetch(url);
  if (!isCacheableResponse(response)) {
    throw new Error(`Unexpected response while caching ${url}: ${response?.status}`);
  }
  await cache.put(url, response.clone());
  return response;
}

async function installCoreShell() {
  const cache = await caches.open(CACHE_NAME);
  for (const url of CORE_APP_SHELL_URLS) {
    await cacheUrl(cache, url);
  }
  await Promise.allSettled(OPTIONAL_CACHE_URLS.map(url => cacheUrl(cache, url)));
}

async function handleNavigationRequest(request) {
  const cache = await caches.open(CACHE_NAME);
  try {
    const networkResponse = await fetch(request);
    const requestUrl = new URL(request.url);
    if (request.method === 'GET' && isCacheableResponse(networkResponse) && isSameOriginAppRequest(requestUrl)) {
      await cache.put(request, networkResponse.clone());
      if (isAppShellDocumentPath(requestUrl.pathname)) {
        await Promise.all([
          cache.put(`${BASE_PATH}/index.html`, networkResponse.clone()),
          cache.put(`${BASE_PATH}/`, networkResponse.clone())
        ]);
      }
    }
    return networkResponse;
  } catch (error) {
    const cachedResponse = await cache.match(request, { ignoreSearch: true });
    if (cachedResponse) {
      return cachedResponse;
    }
    const cachedIndexResponse = await cache.match(`${BASE_PATH}/index.html`);
    if (cachedIndexResponse) {
      return cachedIndexResponse;
    }
    return cache.match(`${BASE_PATH}/`);
  }
}

async function handleStaleWhileRevalidate(request, event) {
  const cache = await caches.open(CACHE_NAME);
  const cachedResponse = await cache.match(request, { ignoreSearch: true });
  const networkUpdatePromise = fetch(request)
    .then(async networkResponse => {
      if (request.method === 'GET' && isCacheableResponse(networkResponse)) {
        await cache.put(request, networkResponse.clone());
      }
      return networkResponse;
    })
    .catch(() => cachedResponse);

  if (cachedResponse) {
    if (event) {
      event.waitUntil(networkUpdatePromise.then(() => undefined));
    }
    return cachedResponse;
  }

  return networkUpdatePromise;
}

async function handleCacheFirst(request) {
  const cachedResponse = await caches.match(request, { ignoreSearch: true });
  if (cachedResponse) {
    return cachedResponse;
  }

  const networkResponse = await fetch(request);
  if (request.method === 'GET' && isCacheableResponse(networkResponse) && isSameOriginAppRequest(new URL(request.url))) {
    const cache = await caches.open(CACHE_NAME);
    await cache.put(request, networkResponse.clone());
  }
  return networkResponse;
}

self.addEventListener('install', event => {
  event.waitUntil(
    installCoreShell()
      .then(() => self.skipWaiting())
  );
});

self.addEventListener('fetch', event => {
  if (event.request.method !== 'GET') {
    return;
  }

  // Let the browser revalidate the public news export on every page load.
  if (new URL(event.request.url).origin === 'https://raw.githubusercontent.com'
      && new URL(event.request.url).pathname === '/avvstancamarcello/Airtable-news/main/news.json') {
    return;
  }

  if (isAppNavigationRequest(event.request) && isSameOriginAppRequest(new URL(event.request.url))) {
    event.respondWith(handleNavigationRequest(event.request));
    return;
  }

  if (isAssetStaleWhileRevalidate(event.request.url)) {
    event.respondWith(handleStaleWhileRevalidate(event.request, event));
    return;
  }

  event.respondWith(
    handleCacheFirst(event.request).catch(() => caches.match(event.request, { ignoreSearch: true }))
  );
});

self.addEventListener('activate', event => {
  event.waitUntil((async () => {
    const cacheNames = await caches.keys();
    await Promise.all(
      cacheNames
        .filter(cacheName => cacheName !== CACHE_NAME)
        .map(cacheName => caches.delete(cacheName))
    );
    await self.clients.claim();
  })());
});
