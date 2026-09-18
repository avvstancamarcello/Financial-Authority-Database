const CACHE_NAME = 'financial-authority-v7';
const FLAG_ICONS_CSS_URL = 'https://cdn.jsdelivr.net/gh/lipis/flag-icons@7.2.3/css/flag-icons.min.css';
const PWA_ICON_PATHS = [
  '/Financial-Authority-Database/icon-96.webp',
  '/Financial-Authority-Database/icon-96.png',
  '/Financial-Authority-Database/icon-192.png',
  '/Financial-Authority-Database/icon-384.webp',
  '/Financial-Authority-Database/icon-384.png',
  '/Financial-Authority-Database/icon-512.png'
];
const urlsToCache = [
  '/Financial-Authority-Database/',
  '/Financial-Authority-Database/index.html',
  '/Financial-Authority-Database/odissea.html',
  '/Financial-Authority-Database/db.enc',
  '/Financial-Authority-Database/logo_shield_financial_defense.svg',
  '/Financial-Authority-Database/logo_galaxy_yous.svg',
  ...PWA_ICON_PATHS,
  '/Financial-Authority-Database/manifest.json',
  FLAG_ICONS_CSS_URL
];

function isAssetStaleWhileRevalidate(requestUrl) {
  return requestUrl === FLAG_ICONS_CSS_URL || PWA_ICON_PATHS.some(path => requestUrl.endsWith(path));
}

// Installazione Service Worker
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('Cache aperta');
        return cache.addAll(urlsToCache);
      })
  );
});

// Fetch con strategia Cache First
self.addEventListener('fetch', event => {
  if (isAssetStaleWhileRevalidate(event.request.url)) {
    event.respondWith(
      caches.open(CACHE_NAME).then(cache =>
        cache.match(event.request).then(cachedResponse => {
          const networkUpdatePromise = fetch(event.request)
            .then(networkResponse => {
              if (networkResponse && (networkResponse.ok || networkResponse.type === 'opaque')) {
                cache.put(event.request, networkResponse.clone());
              }
              return networkResponse;
            })
            .catch(() => cachedResponse);

          if (cachedResponse) {
            event.waitUntil(networkUpdatePromise.then(() => undefined));
            return cachedResponse;
          }

          return networkUpdatePromise;
        })
      )
    );
    return;
  }

  event.respondWith(
    caches.match(event.request)
      .then(response => {
        // Cache hit - ritorna la risposta dalla cache
        if (response) {
          return response;
        }
        return fetch(event.request).then(
          response => {
            // Controlla se abbiamo ricevuto una risposta valida
            if (!response || response.status !== 200 || response.type !== 'basic') {
              return response;
            }

            // Clona la risposta
            const responseToCache = response.clone();

            caches.open(CACHE_NAME)
              .then(cache => {
                cache.put(event.request, responseToCache);
              });

            return response;
          }
        );
      })
  );
});

// Aggiornamento Service Worker
self.addEventListener('activate', event => {
  const cacheWhitelist = [CACHE_NAME];
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (cacheWhitelist.indexOf(cacheName) === -1) {
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
});
