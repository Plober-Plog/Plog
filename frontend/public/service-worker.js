/* eslint-disable no-undef */
self.addEventListener('install', (event) => {
  // 서비스 워커가 설치될 때 즉시 활성화되도록 설정
  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  // 서비스 워커가 활성화될 때 기존 캐시를 모두 삭제
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cacheName) => {
          return caches.delete(cacheName);
        })
      );
    })
  );
  self.clients.claim();
});

self.addEventListener('fetch', (event) => {
  // 모든 네트워크 요청에 대해 캐시를 사용하지 않고 직접 네트워크에서 응답
  event.respondWith(
    fetch(event.request).catch(() => {
      // 네트워크 요청이 실패했을 때 (오프라인일 때 등)
      return new Response('Network request failed and no cache available.');
    })
  );
});