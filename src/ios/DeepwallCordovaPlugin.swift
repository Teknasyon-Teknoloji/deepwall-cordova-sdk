import UIKit
import DeepWall
import Foundation

@objc(DeepwallCordovaPlugin) class DeepwallCordovaPlugin : CDVPlugin, DeepWallNotifierDelegate {

    var eventsCallbackCommand : CDVInvokedUrlCommand? = nil

    @objc(initialize:)
    func initialize(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (initialize)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [Any]{
            let apiKey = myArgs[0] as? String
            let environment = myArgs[1] as? Int
            var deepWallEnvironment : DeepWallEnvironment;
            if (environment == 1){
                deepWallEnvironment = .sandbox
            }
            else{
                deepWallEnvironment = .production
            }
            DeepWall.initialize(apiKey: apiKey!, environment: deepWallEnvironment)
            let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "DeepWall Initialize success")
            result?.setKeepCallbackAs(true)
            self.commandDelegate.send(result, callbackId: command.callbackId)
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (initialize)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(observeEvents:)
    func observeEvents(command : CDVInvokedUrlCommand)
    {
        self.eventsCallbackCommand = command
        DeepWall.shared.observeEvents(for: self)
    }

    @objc(setUserProperties:)
    func setUserProperties(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (setUserProperties)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [Any]{
            let uuid = myArgs[0] as? String
            let country = myArgs[1] as? String
            let language = myArgs[2] as? String
            let properties = DeepWallUserProperties(uuid: uuid!, country: country!, language: language!)
            DeepWall.shared.setUserProperties(properties)
            let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "DeepWall setUserProperties success")
            result?.setKeepCallbackAs(true)
            self.commandDelegate.send(result, callbackId: command.callbackId)
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (setUserProperties)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(requestPaywall:)
    func requestPaywall(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (requestPaywall)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [Any]{
            let actionKey = myArgs[0] as? String
            let extra = myArgs[1] as? String
            if let extraData = convertToDictionary(text: extra) {
                DeepWall.shared.requestPaywall(action: actionKey!, in: self.viewController, extraData: extraData)
            }
            else{
                DeepWall.shared.requestPaywall(action: actionKey!, in: self.viewController, extraData: [:])
            }
            let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "DeepWall requestPaywall success")
            result?.setKeepCallbackAs(true)
            self.commandDelegate.send(result, callbackId: command.callbackId)
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (requestPaywall)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(requestAppTracking:)
    func requestAppTracking(command : CDVInvokedUrlCommand)
    {
        guard #available(iOS 14.0, *) else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "(requestAppTracking) method is only available in iOS 14 or newer")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }

        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (requestAppTracking)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [Any]{
            let actionKey = myArgs[0] as? String
            let extra = myArgs[1] as? String
            if let extraData = convertToDictionary(text: extra) {
                DeepWall.shared.requestAppTracking(action: actionKey!, in: self.viewController, extraData: extraData)
            }
            else{
                DeepWall.shared.requestAppTracking(action: actionKey!, in: self.viewController, extraData: [:])
            }
            let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "DeepWall requestAppTracking success")
            result?.setKeepCallbackAs(true)
            self.commandDelegate.send(result, callbackId: command.callbackId)
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (requestAppTracking)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(updateUserProperties:)
    func updateUserProperties(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (updateUserProperties)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [Any]{
            let country = myArgs[0] as? String
            let language = myArgs[1] as? String
            let environmentStyle = myArgs[2] as? Int
            let debugAdvertiseAttributions = myArgs[3]
            let theme: DeepWallEnvironmentStyle = (environmentStyle == 0) ? .light : .dark
            DeepWall.shared.updateUserProperties(country:country, language:language, environmentStyle:theme,debugAdvertiseAttributions:debugAdvertiseAttributions as? [String : String])
            let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "DeepWall updateUserProperties success")
            result?.setKeepCallbackAs(true)
            self.commandDelegate.send(result, callbackId: command.callbackId)
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (updateUserProperties)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(closePaywall:)
    func closePaywall(command : CDVInvokedUrlCommand)
    {
        DeepWall.shared.closePaywall()
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "DeepWall closePaywall success")
        result?.setKeepCallbackAs(true)
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }

    @objc(sendExtraDataToPaywall:)
    func sendExtraDataToPaywall(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (sendExtraDataToPaywall)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [Any] {
            let extra = myArgs[0] as? String
            if let extraData = convertToDictionary(text: extra) {
                DeepWall.shared.sendExtraData(toPaywall: extraData)
            }
            else{
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (sendExtraDataToPaywall)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            }
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (sendExtraDataToPaywall)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(hidePaywallLoadingIndicator:)
    func hidePaywallLoadingIndicator(command : CDVInvokedUrlCommand)
    {
        DeepWall.shared.hidePaywallLoadingIndicator()
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "DeepWall hidePaywallLoadingIndicator success")
        result?.setKeepCallbackAs(true)
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }

    @objc(validateReceipt:)
    func validateReceipt(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (validateReceipt)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [String: Any]{
            var validationType = myArgs["validationType"] as? Int
            var validation:PloutosValidationType;
            switch (validationType) {
            case 1: validation = PloutosValidationType.purchase; break;
                case 2: validation = PloutosValidationType.restore; break;
                case 3: validation = PloutosValidationType.automatic; break
                default: validation = PloutosValidationType.purchase; break;
            }
            DeepWall.shared.validateReceipt(for: validation)
            let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "DeepWall validateReceipt success")
            result?.setKeepCallbackAs(true)
            self.commandDelegate.send(result, callbackId: command.callbackId)
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (validateReceipt)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(consumeProduct:)
    func consumeProduct(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (consumeProduct)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [String: Any]{
            // TODO Implementation of consumeProduct method
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (consumeProduct)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(setProductUpgradePolicy:)
    func setProductUpgradePolicy(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (setProductUpgradePolicy)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [String: Any]{
            // TODO Implementation of setProductUpgradePolicy method
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (setProductUpgradePolicy)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    @objc(updateProductUpgradePolicy:)
    func updateProductUpgradePolicy(command : CDVInvokedUrlCommand)
    {
        guard let args = command.arguments else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Could not recognize cordova arguments in method: (updateProductUpgradePolicy)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
            return ()
        }
        if let myArgs = args as? [String: Any]{
            // TODO Implementation of updateProductUpgradePolicy method
        } else {
            let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "iOS could not extract " +
                "cordova arguments in method: (updateProductUpgradePolicy)")
            self.commandDelegate.send(result, callbackId: command.callbackId)
        }
    }

    public func deepWallPaywallRequested() -> Void {
        //print("event:deepWallPaywallRequested");
        var mapData = [String: Any]()
        mapData["data"] =  ""
        mapData["event"] = "deepWallPaywallRequested"
        sendData(state: mapData)
    }

    public func deepWallPaywallResponseReceived() -> Void {
        //print("event:deepWallPaywallResponseReceived");
        var mapData = [String: Any]()
        mapData["data"] =  ""
        mapData["event"] = "deepWallPaywallResponseReceived"
        sendData(state: mapData)
    }

    public func deepWallPaywallOpened(_ model: DeepWallPaywallOpenedInfoModel) -> Void {
        //print("event:deepWallPaywallOpened");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["pageId"] = model.pageId
        mapData["data"] = modelMap
        mapData["event"] =  "deepWallPaywallOpened"
        sendData(state: mapData)
    }

    public func deepWallPaywallNotOpened(_ model: DeepWallPaywallNotOpenedInfoModel) -> Void {
        //print("event:deepWallPaywallNotOpened");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["pageId"] = model.pageId
        modelMap["reason"] = model.reason
        modelMap["errorCode"] = model.errorCode
        mapData["data"] =  modelMap
        mapData["event"] =  "deepWallPaywallNotOpened"
        sendData(state: mapData)
    }
    public func deepWallPaywallClosed(_ model: DeepWallPaywallClosedInfoModel) -> Void {
        //print("event:deepWallPaywallClosed");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["pageId"] = model.pageId
        mapData["data"] =  modelMap
        mapData["event"] =  "deepWallPaywallClosed"
        sendData(state: mapData)
    }
    public func deepWallPaywallActionShowDisabled(_ model: DeepWallPaywallActionShowDisabledInfoModel) -> Void {
        //print("event:deepWallPaywallActionShowDisabled");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["pageId"] = model.pageId
        mapData["data"] =  modelMap
        mapData["event"] =  "deepWallPaywallActionShowDisabled"
        sendData(state: mapData)
    }
    public func deepWallPaywallResponseFailure(_ model: DeepWallPaywallResponseFailedModel) -> Void {
        //print("event:deepWallPaywallResponseFailure");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["errorCode"] = model.errorCode
        modelMap["reason"] = model.reason
        mapData["data"] =  modelMap
        mapData["event"] =  "deepWallPaywallResponseFailure"
        sendData(state: mapData)
    }
    public func deepWallPaywallPurchasingProduct(_ model: DeepWallPaywallPurchasingProduct) -> Void {
        //print("event:deepWallPaywallPurchasingProduct");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["productCode"] = model.productCode
        mapData["data"] =  modelMap
        mapData["event"] =  "deepWallPaywallPurchasingProduct"
        sendData(state: mapData)
    }
    public func deepWallPaywallPurchaseSuccess(_ model:  DeepWallValidateReceiptResult) -> Void {
        //print("event:deepWallPaywallPurchaseSuccess");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["type"] = model.type.rawValue
        modelMap["result"] = model.result?.toDictionary() as? [String: Any];
        mapData["data"] =  modelMap
        mapData["event"] =  "deepWallPaywallPurchaseSuccess"
        sendData(state: mapData)
    }
    public func deepWallPaywallPurchaseFailed(_ model: DeepWallPurchaseFailedModel) -> Void {
        //print("event:deepWallPaywallPurchaseFailed");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["productCode"] = model.productCode
        modelMap["reason"] = model.reason
        modelMap["errorCode"] = model.errorCode
        modelMap["isPaymentCancelled"] = model.isPaymentCancelled
        mapData["data"] =  modelMap
        mapData["event"] =  "deepWallPaywallPurchaseFailed"
        sendData(state: mapData)
    }
    public func deepWallPaywallRestoreSuccess() -> Void {
        //print("event:deepWallPaywallRestoreSuccess");
        var map = [String: Any]()
        map["data"] =  ""
        map["event"] = "deepWallPaywallRestoreSuccess"
        sendData(state: map)
    }
    public func deepWallPaywallRestoreFailed(_ model: DeepWallRestoreFailedModel) -> Void {
        //print("event:deepWallPaywallRestoreFailed");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["reason"] = model.reason.rawValue
        modelMap["errorCode"] = model.errorCode
        modelMap["errorText"] = model.errorText
        modelMap["isPaymentCancelled"] = model.isPaymentCancelled
        mapData["data"] =  modelMap
        mapData["event"] = "deepWallPaywallRestoreFailed"
        sendData(state: mapData)
    }
    public func deepWallPaywallExtraDataReceived(_ model: [AnyHashable : Any]) -> Void {
        //print("event:deepWallPaywallExtraDataReceived");
        var mapData = [String: Any]()
        var modelMap = [String: Any]()
        modelMap["extraData"] = model as? [String: Any]
        mapData["data"] =  modelMap
        mapData["event"] = "deepWallPaywallExtraDataReceived"
        sendData(state: mapData)
    }

    public func deepWallATTStatusChanged() {
        //print("event:deepWallATTStatusChanged");
        var map = [String: Any]()
        map["data"] =  ""
        map["event"] = "deepWallATTStatusChanged"
        sendData(state: map)
    }


    func sendData(state: Dictionary<String,Any>) {
        print(state)
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: state)
        result?.setKeepCallbackAs(true)
        self.commandDelegate.send(result, callbackId: self.eventsCallbackCommand?.callbackId)
    }

    func convertToDictionary(text: String?) -> [String: Any]? {
        if let data = text?.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }

}
