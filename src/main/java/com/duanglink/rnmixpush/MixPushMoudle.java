package com.duanglink.rnmixpush;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.duanglink.flymepush.FlymePushManager;
import com.duanglink.getui.GeTuiManager;
import com.duanglink.huaweipush.HuaweiPushManager;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * Created by wangheng on 2017/12/4.
 */
public class MixPushMoudle extends ReactContextBaseJavaModule {
    public static final String EVENT_RECEIVE_REMOTE_NOTIFICATION = "receiveRemoteNotification";
    public static final String EVENT_TYPE_PAYLOAD = "payload";
    public static final String EVENT_RECEIVE_CLIENTID ="receiveClientId";
    private static ReactApplicationContext mRAC;
    public static MixPushManager pushManager;
    public MixPushMoudle(ReactApplicationContext reactContext) {
        super(reactContext);
        mRAC=reactContext;
    }
    @Override
    public String getName() {
        return "MixPushModule";
    }

    @Override
    public boolean canOverrideExistingModule() {
        return true;
    }

    public static void sendEvent(String eventName, @Nullable WritableMap params){
        mRAC.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public static void sendEvent(String eventName, @Nullable String params){
        mRAC.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @ReactMethod
    public void  setAlias(String alias){
        if(pushManager!=null){
            pushManager.setAlias(mRAC,alias);
        }
    }

    @ReactMethod
    public void  unsetAlias(String alias){
        if(pushManager!=null){
            pushManager.unsetAlias(mRAC,alias);
        }
    }

    @ReactMethod
    public void  setTags(String tags){
        if(pushManager!=null){
            pushManager.setTags(mRAC,tags);
        }
    }

    @ReactMethod
    public void  unsetTags(String tags){
        if(pushManager!=null){
            pushManager.unsetTags(mRAC,tags);
        }
    }

    @ReactMethod
    public void  getClientId(final Callback callback){
        String clientId="";
        if(pushManager!=null){
            clientId= pushManager.getClientId(mRAC);
        }
        callback.invoke(clientId);
    }

    @ReactMethod
    public void getDeviceInfo(final Callback callback) throws JSONException{
        JSONObject info = new JSONObject();
        info.put("BRAND", Build.BRAND);
        info.put("DEVICE", Build.DEVICE);
        info.put("MODEL", Build.MODEL);
        info.put("TAGS", Build.TAGS);
        callback.invoke(info.toString());
    }

    //华为推送测试
    @ReactMethod
    public void sendHuaWeiPushMessage(String deviceToken) throws IOException,JSONException
    {
        try {
            String result=HuaweiPushManager.sendPushMessage(deviceToken);
            JSONObject obj= new JSONObject(result);;
            String code = obj.getString("code");
            if(code=="80000000"){
                showToast("消息发送成功");
            }else{
                showToast("消息发送失败,code:"+code);
            }
        }catch (Exception e) {
            showToast("消息发送异常");
        }
    }

    //个推推送测试
    @ReactMethod
    public void sendGeTuiPushMessage(String cid) throws IOException,JSONException
    {
        try {
            String result=GeTuiManager.sendPushMessage(cid);
            JSONObject obj= new JSONObject(result);;
            String code = obj.getString("result");
            if(code=="ok"){
                showToast("消息发送成功");
            }else{
                showToast("消息发送失败,"+obj.getString("desc"));
            }
        }catch (Exception e) {
            showToast("消息发送异常");
        }
    }

    //魅族推送测试
    @ReactMethod
    public void sendMeiZuPushMessage(String cid) throws IOException,JSONException
    {
        try {
            String result= FlymePushManager.sendPushMessage(cid);
            JSONObject obj= new JSONObject(result);;
            String code = obj.getString("result");
            if(code=="200"){
                showToast("消息发送成功");
            }else{
                showToast("消息发送失败,"+obj.getString("message"));
            }
        }catch (Exception e) {
            showToast("消息发送异常");
        }
    }

    //小米推送测试
    @ReactMethod
    public void sendXiaoMiPushMessage(String cid) throws IOException,JSONException{

    }

    private void showToast(String msg){
        Toast.makeText(mRAC,  msg , Toast.LENGTH_SHORT).show();
    }
}
