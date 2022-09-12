package deepwallcordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import io.reactivex.functions.Consumer;
import manager.eventbus.EventBus;
import manager.eventbus.EventModel;
import manager.purchasekit.ValidationType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import deepwall.core.DeepWall;
import deepwall.core.models.*;
import java.util.*;
import io.reactivex.disposables.Disposable;

/**
 * This class echoes a string called from JavaScript.
 */
public class DeepwallCordovaPlugin extends CordovaPlugin {

    private CallbackContext callback, eventCallback;
    private Disposable disposable;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("initialize")) {
            this.callback = callbackContext;
            this.initialize(args.getString(0), args.getInt(1));
            return true;
        }
        else if(action.equals("observeEvents")) {
            this.eventCallback = callbackContext;
            this.observeEvents();
            return true;
        }
        else if(action.equals("removeEventObserver")) {
            this.eventCallback = null;

            if (disposable != null) {
                disposable.dispose();
            }

            return true;
        }
        else if(action.equals("setUserProperties")) {
            this.callback = callbackContext;
            this.setUserProperties(
              args.getString(0), // uuid
              args.getString(1), // country
              args.getString(2), // language
              args.getInt(3), // environmentStyle
              args.getString(4), // debugAdvertiseAttributions
              args.getString(5), // phoneNumber
              args.getString(6), // emailAddress
              args.getString(7), // firstName
              args.getString(8) // lastName
            );

            return true;
        }
        else if(action.equals("requestPaywall")) {
            this.callback = callbackContext;
            JSONObject extra = new JSONObject(args.getString(1));
            this.requestPaywall(args.getString(0), extra);
            return true;
        }
        else if(action.equals("updateUserProperties")) {
            this.callback = callbackContext;
            this.updateUserProperties(
                args.getString(0), // country
                args.getString(1), // language
                args.getInt(2), // environmentStyle
                args.getString(3), // debugAdvertiseAttributions
                args.getString(4), // phoneNumber
                args.getString(5), // emailAddress
                args.getString(6), // firstName
                args.getString(7) // lastName
            );
            return true;
        }
        else if(action.equals("closePaywall")) {
            this.callback = callbackContext;
            this.closePayWall();
            return true;
        }
        else if(action.equals("hidePaywallLoadingIndicator")) {
            this.callback = callbackContext;
            this.hidePaywallLoadingIndicator();
            return true;
        }
        else if(action.equals("validateReceipt")) {
            this.callback = callbackContext;
            this.validateReceipt(args.getInt(0));
            return true;
        }
        else if(action.equals("consumeProduct")) {
            this.callback = callbackContext;
            this.consumeProduct(args.getString(0));
            return true;
        }
        else if(action.equals("setProductUpgradePolicy")) {
            this.callback = callbackContext;
            this.setProductUpgradePolicy(args.getInt(0), args.getInt(1));
            return true;
        }
        else if(action.equals("updateProductUpgradePolicy")) {
            this.callback = callbackContext;
            this.updateProductUpgradePolicy(args.getInt(0), args.getInt(1));
            return true;
        }
        else
            return false;
    }

    private void initialize(String apiKey, int environment){
        if (apiKey != null && apiKey.length() > 0) {
            DeepWallEnvironment deepWallEnvironment = environment == 1? DeepWallEnvironment.SANDBOX : DeepWallEnvironment.PRODUCTION;
            DeepWall.INSTANCE.initDeepWallWith(
                Platform.GOOGLE,
                this.cordova.getActivity(),
                apiKey,
                deepWallEnvironment
            );
            sendSuccess("Deepwall initialize success");
        } else {
            sendError("Expected non-empty string arguments.");
        }
    }


    private void setUserProperties(
        String uuid,
        String country,
        String language,
        int environmentStyle,
        String debugAdvertiseAttributions,
        String phoneNumber,
        String emailAddress,
        String firstName,
        String lastName
    ) {
        if (phoneNumber == null) {
            phoneNumber = "";
        }
        if (emailAddress == null) {
            emailAddress = "";
        }
        if (firstName == null) {
            firstName = "";
        }
        if (lastName == null) {
            lastName = "";
        }

        if (uuid != null && country != null && language != null) {
            DeepWallEnvironmentStyle theme = (environmentStyle == 0) ? DeepWallEnvironmentStyle.LIGHT : DeepWallEnvironmentStyle.DARK;
            DeepWall.INSTANCE.setUserProperties(
                uuid,
                country,
                language,
                phoneNumber,
                emailAddress,
                firstName,
                lastName,
                this::success,
                theme
            );
            sendSuccess("Deepwall setUserProperties success");
        } else {
            sendError("Expected non-empty string arguments.");
        }
    }

    private kotlin.Unit success(){return null;}

    private void requestPaywall(String actionKey, JSONObject extraData) throws JSONException {
        if (actionKey != null) {
            Bundle bundle = new Bundle();
            if(extraData != null){
                Iterator<String> iter = extraData.keys(); //This should be the iterator you want.
                while(iter.hasNext()){
                    String key = iter.next();
                    if(extraData.get(key) instanceof Boolean){
                        bundle.putBoolean(key, extraData.getBoolean(key));
                    }
                    else if(extraData.get(key) instanceof Integer){
                        bundle.putInt(key, extraData.getInt(key));
                    }
                    else if(extraData.get(key) instanceof Double){
                        bundle.putDouble(key, extraData.getDouble(key));
                    }
                    else if(extraData.get(key) instanceof String){
                        bundle.putString(key, extraData.getString(key));
                    }
                }
            }
            DeepWall.INSTANCE.showPaywall(this.cordova.getActivity(), actionKey, bundle, this::error, PaywallOrientation.PORTRAIT);
            sendSuccess("Deepwall requestPaywall success");
        } else {
            sendError("Expected non-empty string arguments.");
        }
    }

    private void requestAppTracking(String actionKey, JSONObject extraData) throws JSONException {
        // placeholder method !
    }

    private void sendExtraDataToPaywall(JSONObject extraData) throws JSONException {
        // placeholder method !
    }

    private kotlin.Unit error(String s){return null;}

    private void updateUserProperties(
        String country,
        String language,
        int environmentStyle,
        String debugAdvertiseAttributions,
        String phoneNumber,
        String emailAddress,
        String firstName,
        String lastName
    ){
        if (phoneNumber == null) {
            phoneNumber = "";
        }
        if (emailAddress == null) {
            emailAddress = "";
        }
        if (firstName == null) {
            firstName = "";
        }
        if (lastName == null) {
            lastName = "";
        }

        if (country != null && language != null) {
            DeepWallEnvironmentStyle theme = (environmentStyle == 0)? DeepWallEnvironmentStyle.LIGHT : DeepWallEnvironmentStyle.DARK;
            DeepWall.INSTANCE.updateUserProperties(
                country,
                language,
                theme,
                phoneNumber,
                emailAddress,
                firstName,
                lastName
            );
            sendSuccess("Deepwall updateUserProperties success");
        } else {
            sendError("Expected non-empty string arguments.");
        }
    }

    private void closePayWall(){
        DeepWall.INSTANCE.closePaywall();
        sendSuccess("Deepwall closePaywall success");
    }

    private void hidePaywallLoadingIndicator(){
        //DeepWall.hidePaywallLoadingIndicator();
        sendSuccess("Deepwall hidePaywallLoadingIndicator success");
    }

    private void validateReceipt(int validationType){
        ValidationType validation;
        switch (validationType) {
            case 1 : validation = ValidationType.PURCHASE; break;
            case 2 : validation = ValidationType.RESTORE; break;
            case 3 : validation = ValidationType.AUTOMATIC; break;
            default : validation = ValidationType.PURCHASE; break;
        }
        DeepWall.INSTANCE.validateReceipt(validation);
        sendSuccess("Deepwall validateReceipt success");
    }

    private void consumeProduct(String productId) {
        if (productId != null) {
            DeepWall.INSTANCE.consumeProduct(productId);
            sendSuccess("Deepwall consumeProduct success");
        } else {
            sendError("Expected non-empty string argument.");
        }
    }

    private void setProductUpgradePolicy(int prorationType, int upgradePolicy){
        ProrationType proration;
        switch (prorationType) {
            case 0 : proration = ProrationType.UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY; break;
            case 1 : proration = ProrationType.IMMEDIATE_WITH_TIME_PRORATION; break;
            case 2 : proration = ProrationType.IMMEDIATE_WITHOUT_PRORATION; break;
            case 3 : proration = ProrationType.IMMEDIATE_AND_CHARGE_PRORATED_PRICE; break;
            case 4 : proration = ProrationType.DEFERRED; break;
            case 5 : proration = ProrationType.NONE; break;
            default : proration = ProrationType.NONE; break;
        }
        PurchaseUpgradePolicy policy;
        switch (upgradePolicy) {
            case 0 : policy = PurchaseUpgradePolicy.DISABLE_ALL_POLICIES; break;
            case 1 : policy = PurchaseUpgradePolicy.ENABLE_ALL_POLICIES; break;
            case 2 : policy = PurchaseUpgradePolicy.ENABLE_ONLY_UPGRADE; break;
            case 3 : policy = PurchaseUpgradePolicy.ENABLE_ONLY_DOWNGRADE; break;
            default : policy = PurchaseUpgradePolicy.DISABLE_ALL_POLICIES; break;
        }
        DeepWall.INSTANCE.setProductUpgradePolicy(proration, policy);
        sendSuccess("Deepwall setProductUpgradePolicy success");

    }

    private void updateProductUpgradePolicy(int prorationType, int upgradePolicy){
        ProrationType proration;
        switch (prorationType) {
            case 0 : proration = ProrationType.UNKNOWN_SUBSCRIPTION_UPGRADE_DOWNGRADE_POLICY; break;
            case 1 : proration = ProrationType.IMMEDIATE_WITH_TIME_PRORATION; break;
            case 2 : proration = ProrationType.IMMEDIATE_WITHOUT_PRORATION; break;
            case 3 : proration = ProrationType.IMMEDIATE_AND_CHARGE_PRORATED_PRICE; break;
            case 4 : proration = ProrationType.DEFERRED; break;
            case 5 : proration = ProrationType.NONE; break;
            default : proration = ProrationType.NONE; break;
        }
        PurchaseUpgradePolicy policy;
        switch (upgradePolicy) {
            case 0 : policy = PurchaseUpgradePolicy.DISABLE_ALL_POLICIES; break;
            case 1 : policy = PurchaseUpgradePolicy.ENABLE_ALL_POLICIES; break;
            case 2 : policy = PurchaseUpgradePolicy.ENABLE_ONLY_UPGRADE; break;
            case 3 : policy = PurchaseUpgradePolicy.ENABLE_ONLY_DOWNGRADE; break;
            default : policy = PurchaseUpgradePolicy.DISABLE_ALL_POLICIES; break;
        }
        DeepWall.INSTANCE.updateProductUpgradePolicy(proration, policy);
        sendSuccess("Deepwall updateProductUpgradePolicy success");
    }

    private final void observeEvents() {
        disposable = EventBus.INSTANCE.subscribe((new Consumer() {
         // $FF: synthetic method
         // $FF: bridge method
         public void accept(Object var1) throws Exception {
            this.accept((EventModel)var1);
         }

         public final void accept(EventModel it) throws Exception {

            JSONObject map = new JSONObject();
            JSONObject modelData = new JSONObject();
            Object dataObject;
            int eventType = it.getType();
            if(eventType == DeepWallEvent.PAYWALL_OPENED.getValue()) {
                map = new JSONObject();
                dataObject = it.getData();
                if (dataObject == null) {
                    throw new Exception("null cannot be cast to non-null type deepwall.core.models.PaywallOpenedInfo");
                }

                PaywallOpenedInfo openedInfo = (PaywallOpenedInfo) dataObject;
                modelData = convertJson(openedInfo);
                map.put("data", modelData);
                map.put("event", "deepWallPaywallOpened");

                sendData(map);
            }
            else if(eventType == DeepWallEvent.DO_NOT_SHOW.getValue()) {
                 map = new JSONObject();
                 dataObject = it.getData();
                 if (dataObject == null) {
                     throw new Exception("null cannot be cast to non-null type deepwall.core.models.PaywallActionShowDisabledInfo");
                 }

                 PaywallActionShowDisabledInfo actionInfo = (PaywallActionShowDisabledInfo) dataObject;
                 modelData = convertJson(actionInfo);
                 map.put("data", modelData);
                 map.put("event", "deepWallPaywallActionShowDisabled");
                 sendData(map);
             }
             else if(eventType == DeepWallEvent.CLOSED.getValue()) {
                map = new JSONObject();
                dataObject = it.getData();
                if (dataObject == null) {
                    throw new Exception("null cannot be cast to non-null type deepwall.core.models.PaywallClosedInfo");
                }

                PaywallClosedInfo data = (PaywallClosedInfo) dataObject;
                modelData = convertJson(data);
                map.put("data", modelData);
                map.put("event", "deepWallPaywallClosed");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.PAYWALL_PURCHASING_PRODUCT.getValue()) {
                map = new JSONObject();
                dataObject = it.getData();
                if (dataObject == null) {
                    throw new Exception("null cannot be cast to non-null type deepwall.core.models.PaywallPurchasingProductInfo");
                }

                PaywallPurchasingProductInfo productInfo = (PaywallPurchasingProductInfo) dataObject;
                modelData = convertJson(productInfo);
                map.put("data", modelData);
                map.put("event", "deepWallPaywallPurchasingProduct");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.PAYWALL_PURCHASE_FAILED.getValue()) {
                map = new JSONObject();
                dataObject = it.getData();
                if (dataObject == null) {
                    throw new Exception("null cannot be cast to non-null type deepwall.core.models.SubscriptionErrorResponse");
                }

                SubscriptionErrorResponse errorResponse = (SubscriptionErrorResponse) dataObject;
                modelData = convertJson(errorResponse);
                map.put("data", modelData);
                map.put("event", "deepWallPaywallPurchaseFailed");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.PAYWALL_PURCHASE_SUCCESS.getValue()) {
                map = new JSONObject();
                dataObject = it.getData();
                if (dataObject == null) {
                    throw new Exception("null cannot be cast to non-null type deepwall.core.models.SubscriptionResponse");
                }

                SubscriptionResponse subResponse = (SubscriptionResponse) dataObject;
                modelData = convertJson(subResponse);
                map.put("data", modelData);
                map.put("event", "deepWallPaywallPurchaseSuccess");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.PAYWALL_RESPONSE_FAILURE.getValue()) {
                map = new JSONObject();
                dataObject = it.getData();
                if (dataObject == null) {
                    throw new Exception("null cannot be cast to non-null type deepwall.core.models.PaywallFailureResponse");
                }

                PaywallFailureResponse failureResponse = (PaywallFailureResponse) dataObject;
                modelData = convertJson(failureResponse);
                map.put("data", modelData);
                map.put("event", "deepWallPaywallResponseFailure");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.PAYWALL_RESTORE_SUCCESS.getValue()) {
                map = new JSONObject();
                map.put("data", String.valueOf(it.getData()));
                map.put("event", "deepWallPaywallRestoreSuccess");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.PAYWALL_RESTORE_FAILED.getValue()) {
                map = new JSONObject();
                map.put("data", String.valueOf(it.getData()));
                map.put("event", "deepWallPaywallRestoreFailed");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.EXTRA_DATA.getValue()) {
                dataObject = it.getData();
                if (dataObject != null) {
                    modelData = convertJson(dataObject);
                }

                map.put("data", modelData);
                map.put("event", "deepWallPaywallExtraDataReceived");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.PAYWALL_REQUESTED.getValue()) {
                map = new JSONObject();
                map.put("data", "");
                map.put("event", "deepWallPaywallRequested");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.PAYWALL_RESPONSE_RECEIVED.getValue()) {
                map = new JSONObject();
                map.put("data", "");
                map.put("event", "deepWallPaywallResponseReceived");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.CONSUME_SUCCESS.getValue()) {
                map = new JSONObject();
                map.put("data", "");
                map.put("event", "deepWallPaywallConsumeSuccess");
                sendData(map);
            }
            else if(eventType == DeepWallEvent.CONSUME_FAILURE.getValue()) {
                map = new JSONObject();
                map.put("data", "");
                map.put("event", "deepWallPaywallConsumeFailure");
                sendData(map);
            }
         }
      }));
    }

    private void sendSuccess(String data){
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, data);
        pluginResult.setKeepCallback(true);
        callback.sendPluginResult(pluginResult);
    }

    private void sendError(String data){
        PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, data);
        pluginResult.setKeepCallback(true);
        callback.sendPluginResult(pluginResult);
    }

    private void sendData(JSONObject map){
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, map);
        pluginResult.setKeepCallback(true);
        if (eventCallback != null) {
            eventCallback.sendPluginResult(pluginResult);
        }
    }

    private final HashMap convertJsonToMap(JSONObject object) throws JSONException {
        HashMap map = new HashMap();
        String key;
        Object value;
        for(Iterator keysItr = object.keys(); keysItr.hasNext(); map.put(key, value)) {
            key = (String)keysItr.next();
            value = object.get(key);
            if (value instanceof JSONArray) {
                value = this.convertJsonToArray((JSONArray)value);
            } else if (value instanceof JSONObject) {
                value = this.convertJsonToMap((JSONObject)value);
            }
        }

        return map;
    }

    private final JSONObject convertJson(Object model) throws JSONException {
        Gson gson = new Gson();
        String jsonInString = gson.toJson(model);
        return new JSONObject(jsonInString);
    }

    private final ArrayList convertJsonToArray(JSONArray array) throws JSONException {
        ArrayList list = new ArrayList();
        int i = 0;
        int var4 = array.length() - 1;
        if (i <= var4) {
            while(true) {
                Object value = array.get(i);
                if (value instanceof JSONArray) {
                    value = this.convertJsonToArray((JSONArray)value);
                } else if (value instanceof JSONObject) {
                    value = this.convertJsonToMap((JSONObject)value);
                }

                list.add(value);
                if (i == var4) {
                    break;
                }
                ++i;
            }
        }

        return list;
    }
}
