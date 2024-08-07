const CACHE_NAME = 'pwa-cache-v1';
const urlsToCache = [
  // '/',
  // '/index.html',
  // '/manifest.json'
];

self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => cache.addAll(urlsToCache))
  );
});


//fetch 이벤트에서 캐시된 파일 제공
self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request)
      .then(response => response || fetch(event.request))
  );
});