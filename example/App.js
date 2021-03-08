// Wait for the deviceready event
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
  cordova.DeepwallCordovaPlugin.initialize('API_KEY', Environments.SANDBOX);
  cordova.DeepwallCordovaPlugin.setUserProperties(
    'deepwall-test-device',
    'en',
    'en-en',
    EnvironmentStyles.LIGHT
  );

  // Listen all events
  cordova.DeepwallCordovaPlugin.observeEvents(function (response) {
    console.log('Event SUCCESS: ', JSON.stringify(response));
  }, function (error) {
    console.log('Event ERROR: ', error);
  });

  // Open Paywall
  cordova.DeepwallCordovaPlugin.requestPaywall('Settings', extraData = {});
}
