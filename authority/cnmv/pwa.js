(() => {
  const button = document.getElementById('cnmv-install');
  const status = document.getElementById('cnmv-install-status');
  let deferredPrompt = null;

  if ('serviceWorker' in navigator && window.isSecureContext) {
    navigator.serviceWorker.register('/authority/cnmv-service-worker.js', {
      scope: '/authority/cnmv-orbital-webapp.html'
    }).catch(error => {
      console.error('CNMV guide service worker registration failed:', error);
      status.textContent = 'La configuración sin conexión no está disponible. Puedes consultar la guía en línea.';
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
        ? 'Instalación solicitada en el navegador.'
        : 'Instalación cancelada. Puedes volver a intentarlo desde el menú del navegador.';
    } catch (error) {
      console.error('CNMV guide installation prompt failed:', error);
      status.textContent = 'No se pudo iniciar la instalación. Inténtalo desde el menú del navegador.';
    }
  });

  window.addEventListener('appinstalled', () => {
    deferredPrompt = null;
    button.hidden = true;
    status.textContent = 'Guía CNMV instalada.';
  });
})();
