var exec = require('cordova/exec');
var cordova = require('cordova');
var PLUGIN = "DeepwallCordovaPlugin";

const fireEventSuccess = function(response){
  cordova.fireWindowEvent("deepwallEventSuccess", response);
}

const fireEventError = function(response){
  console.log('PLUGIN'+JSON.stringify(response));
  cordova.fireWindowEvent("deepwallEventError", response);
}

exports.initialize = function (apiKey, environment, success, error){
    exec(success, error, PLUGIN, 'initialize', [apiKey, environment]);
};

exports.observeEvents = function(success, error){
    exec(success, error, PLUGIN, 'observeEvents', []);
};

exports.setUserProperties = function(
  uuid,
  country,
  language,
  environmentStyle = EnvironmentStyle.LIGHT,
  success,
  error,

  // adding new params
  debugAdvertiseAttributions = null,
  phoneNumber = null,
  emailAddress = null,
  firstName = null,
  lastName = null
){
    if (uuid.isEmpty) {
        throw new DeepwallException(ErrorCodes.USER_PROPERTIES_COUNTRY_REQUIRED);
    }
    if (country.isEmpty) {
        throw new DeepwallException(ErrorCodes.USER_PROPERTIES_COUNTRY_REQUIRED);
    }
    if (language.isEmpty) {
        throw new DeepwallException(ErrorCodes.USER_PROPERTIES_LANGUAGE_REQUIRED);
    }

    exec(success, error, PLUGIN, 'setUserProperties', [
      uuid,
      country,
      language,
      environmentStyle,
      debugAdvertiseAttributions,
      phoneNumber,
      emailAddress,
      firstName,
      lastName,
    ]);
};

exports.requestPaywall = function(actionKey, extraData = {}, success, error){
    exec(success, error, PLUGIN, 'requestPaywall', [actionKey, JSON.stringify(extraData)]);
};

exports.requestAppTracking = function(actionKey, extraData = {}, success, error){
  exec(success, error, PLUGIN, 'requestAppTracking', [actionKey, JSON.stringify(extraData)]);
};

exports.sendExtraDataToPaywall = function(extraData = {}, success, error){
  exec(success, error, PLUGIN, 'sendExtraDataToPaywall', [JSON.stringify(extraData)]);
};

exports.updateUserProperties = function(
  country,
  language,
  environmentStyle = EnvironmentStyle.LIGHT,
  debugAdvertiseAttributions = null,
  success,
  error,
  phoneNumber = null,
  emailAddress = null,
  firstName = null,
  lastName = null
){
    exec(success, error, PLUGIN, 'updateUserProperties', [
      country,
      language,
      environmentStyle,
      debugAdvertiseAttributions,
      phoneNumber,
      emailAddress,
      firstName,
      lastName,
    ]);
};

exports.closePaywall = function(success, error){
    exec(success, error, PLUGIN, 'closePaywall', []);
};

exports.hidePaywallLoadingIndicator = function(success, error){
    exec(success, error, PLUGIN, 'hidePaywallLoadingIndicator',[]);
};

exports.validateReceipt = function(validationType, success, error){
    if (!Object.values(ValidateReceiptTypes).includes(validationType)) {
        throw new DeepWallException(ErrorCodes.VALIDATE_RECEIPT_TYPE_NOT_VALID);
    }
    exec(success, error, PLUGIN, 'validateReceipt', [validationType]);
};

exports.consumeProduct = function(productId, success, error){
    exec(success, error, PLUGIN, 'consumeProduct', [productId]);
};

exports.setProductUpgradePolicy = function(prorationType, upgradePolicy, success, error){
    exec(success, error, PLUGIN, 'setProductUpgradePolicy', [prorationType, upgradePolicy]);
};

exports.updateProductUpgradePolicy = function(prorationType, upgradePolicy, success, error){
    exec(success, error, PLUGIN, 'updateProductUpgradePolicy', [prorationType, upgradePolicy]);
};
