# DeepWall (cordova sdk)

* This package gives' wrapper methods for deepwall sdks. [iOS](https://github.com/Teknasyon-Teknoloji/deepwall-ios-sdk) - [Android](https://github.com/Teknasyon-Teknoloji/deepwall-android-sdk)

* Before implementing this package, you need to have **api_key** and list of **actions**.

* You can get api_key and actions from [DeepWall Dashboard](https://console.deepwall.com/)


---


## Getting started

`$ cordova plugin add cordova-plugin-deepwall`

* After adding plugin in the dependency section,
  add `AndroidXEnabled` as `true` in the `config.xml` of your application.
```xml
<preference name="AndroidXEnabled" value="true" />
```


### Installation Notes
- **IOS**
    - Set minimum ios version to 10.0 or higher in `platforms/ios/Podfile` like: `platform :ios, '10.0'`
    - Add `use_frameworks!` into `platforms/ios/Podfile` if not exists.
    - Run `$ cd platforms/ios && pod install`

- **ANDROID**
    - Set `minSdkVersion` to 21 or higher.


---


## Usage

### Let's start

- On application start you need to initialize sdk with api key and environment.
```javascript
cordova.DeepwallCordovaPlugin.initialize('{API_KEY}', Environments.PRODUCTION, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```

- Before requesting any paywall you need to set UserProperties (device uuid, country, language). [See all parameters](https://github.com/Teknasyon-Teknoloji/deepwall-ios-sdk#configuration)
```javascript
cordova.DeepwallCordovaPlugin.setUserProperties('UNIQUE_DEVICE_ID_HERE (UUID)','en-us','us', EnvironmentStyles.LIGHT, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```

- After setting userProperties, you are ready for requesting paywall with an action key. You can find action key in DeepWall dashboard.
```javascript
cordova.DeepwallCordovaPlugin.requestPaywall('{ACTION_KEY}',{}, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});

// You can send extra parameter if needed as below
cordova.DeepwallCordovaPlugin.requestPaywall('{ACTION_KEY}',{'sliderIndex': 2, 'title': 'Deepwall'}, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```

- You can also close paywall.
```javascript
cordova.DeepwallCordovaPlugin.closePaywall(function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```

- When any of userProperties is changed, you need to call updateUserProperties method. (For example if user changed application language)
```javascript
cordova.DeepwallCordovaPlugin.updateUserProperties('fr-fr','fr',EnvironmentStyles.LIGHT, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```

- You can validate receipts like below.
```javascript
cordova.DeepwallCordovaPlugin.validateReceipt(ValidateReceiptTypes.RESTORE, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```


### Events

- There is also bunch of events triggering before and after DeepWall Actions. You may listen any event like below.
```javascript
cordova.DeepwallCordovaPlugin.observeEvents(function(response){
    console.log(JSON.stringify(response));
    // access response.data
}, function(error){
    console.log(error);
})
```


### iOS Only Methods

- Requesting ATT Prompts
```javascript
cordova.DeepwallCordovaPlugin.requestAppTracking('{ACTION_KEY}',{}, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});

// You can send extra parameter if needed as below
cordova.DeepwallCordovaPlugin.requestAppTracking('{ACTION_KEY}', {appName: "My awesome app"}, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```

- Sending extra data to paywall while it's open.
```javascript
cordova.DeepwallCordovaPlugin.sendExtraDataToPaywall({appName: "My awesome app"}, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```


### Android Only Methods

- For consumable products, you need to mark the purchase as consumed for consumable product to be purchased again.
```javascript
cordova.DeepwallCordovaPlugin.consumeProduct('consumable_product_id', function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```

- Use `setProductUpgradePolicy` method to set the product upgrade policy for Google Play apps.
  * [enums/ProrationTypes.js](./www/enums/ProrationTypes.js)
  * [enums/PurchaseUpgradePolicy.js](./www/enums/PurchaseUpgradePolicy.js)
```javascript
cordova.DeepwallCordovaPlugin.setProductUpgradePolicy(
    ProrationTypes.IMMEDIATE_WITHOUT_PRORATION,
    PurchaseUpgradePolicy.ENABLE_ALL_POLICIES, 
    function(response){
      console.log(response);
    }, function(error){
      console.log(error);
    });
```

- Use `updateProductUpgradePolicy` method to update the product upgrade policy within the app workflow before requesting paywalls.
```javascript
cordova.DeepwallCordovaPlugin.setProductUpgradePolicy(
  ProrationTypes.IMMEDIATE_WITHOUT_PRORATION,
  PurchaseUpgradePolicy.ENABLE_ONLY_UPGRADE,
  function(response){
    console.log(response);
  }, function(error){
    console.log(error);
  });
```


---


## Notes
- You may find complete list of _events_ in [enums/Events.js](./www/enums/Events.js) or [Native Sdk Page](https://github.com/Teknasyon-Teknoloji/deepwall-ios-sdk#event-handling)
