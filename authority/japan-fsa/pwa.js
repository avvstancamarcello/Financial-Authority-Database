(() => {
  const button = document.getElementById('japan-install');
  const status = document.getElementById('japan-install-status');
  let deferredPrompt = null;

  if ('serviceWorker' in navigator && window.isSecureContext) {
    navigator.serviceWorker.register('/authority/japan-fsa-service-worker.js', {
      scope: '/authority/japan-fsa-orbital-webapp.html'
    }).catch(error => {
      console.error('Japan guide service worker registration failed:', error);
      status.textContent = 'Offline setup is unavailable. You can still read this guide online.';
    });
  }

  window.addEventListener('beforeinstallprompt', event => {
    event.preventDefault();
    deferredPrompt = event;
    button.hidden = false;
  });

  button.addEventListener('click', async () => {
    if (!deferredPrompt) return;
    const prompt = deferredPrompt;
    deferredPrompt = null;
    button.hidden = true;
    try {
      await prompt.prompt();
      const choice = await prompt.userChoice;
      status.textContent = choice.outcome === 'accepted'
        ? 'Installation requested in your browser.'
        : 'Installation cancelled. Use your browser menu to try again.';
    } catch (error) {
      console.error('Japan guide installation prompt failed:', error);
      status.textContent = 'Installation could not be started. Use your browser menu to try again.';
    }
  });

  window.addEventListener('appinstalled', () => {
    deferredPrompt = null;
    button.hidden = true;
    status.textContent = 'Japan guide installed.';
  });
})();
