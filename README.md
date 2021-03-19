# DeepWall (deepwall-cordova-plugin)
* This package gives wrapper methods for deepwall sdks. [iOS](https://github.com/Teknasyon-Teknoloji/deepwall-ios-sdk) - [Android](https://github.com/Teknasyon-Teknoloji/deepwall-android-sdk)
  
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
    - Set minimum ios version to `10.0` in `platforms/ios/Podfile` like: `platform :ios, '10.0'`
    - Add `use_frameworks!` into `platforms/ios/Podfile` if not exists.
    - Run `$ cd platforms/ios && pod install`

- **ANDROID**
    - Set `minSdkVersion` to `21 in` `platforms/android/app/build.gradle`
    - Add `maven { url 'https://raw.githubusercontent.com/Teknasyon-Teknoloji/deepwall-android-sdk/master/' }` into `platforms/android/app/build.gradle` (Add into repositories under allprojects)

---

## Usage

### 1. Initialize DeepWall SDK
- On application start you need to initialize sdk with api key and environment.
```javascript
cordova.DeepwallCordovaPlugin.initialize("API_KEY", Environments.PRODUCTION, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
```

### 2. set User Properties
- Before requesting any paywall you need to set UserProperties (device uuid, country, language). [See all parameters](https://github.com/Teknasyon-Teknoloji/deepwall-ios-sdk#configuration)
````javascript
cordova.DeepwallCordovaPlugin.setUserProperties('UNIQUE_DEVICE_ID_HERE (UUID)','en-us','us', EnvironmentStyles.LIGHT, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
````

### 3. request Paywall
- After setting userProperties, you are ready for requesting paywall with an action name. You can find action name in DeepWall dashboard. You can send extra paramteres as well.
````javascript
cordova.DeepwallCordovaPlugin.requestPaywall('AppLaunch',{}, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
````

### 4. close Paywall
- You can also close paywall using:
````javascript
cordova.DeepwallCordovaPlugin.closePaywall(function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
````

### 5. update userProperties
- If any of userProperties is changed you need to call updateUserProperties method. (For example if user changed application language)
````javascript
cordova.DeepwallCordovaPlugin.updateUserProperties('fr-fr','fr',EnvironmentStyles.LIGHT, function(response){
    console.log(response);
}, function(error){
    console.log(error);
});
````

### 6. listen all Deepwall Events
- There is also bunch of events triggering before and after DeepWall Actions. You may listen any action like below.
````javascript
cordova.DeepwallCordovaPlugin.observeEvents(function(response){
    console.log(JSON.stringify(response));
    // access response.data
}, function(error){
    console.log(error);
})
````

## Notes
- You may find complete list of _events_ in [enums/Events.js](./www/enums/Events.js) or [Native Sdk Page](https://github.com/Teknasyon-Teknoloji/deepwall-ios-sdk#event-handling)
