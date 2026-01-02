const CACHE_NAME = 'financial-authority-v1';
const urlsToCache = [
  '/Financial-Authority-Database/',
  '/Financial-Authority-Database/index.html',
  '/Financial-Authority-Database/financial_authorities_database.json',
  '/Financial-Authority-Database/icon-192.png',
  '/Financial-Authority-Database/icon-512.png'
];

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
