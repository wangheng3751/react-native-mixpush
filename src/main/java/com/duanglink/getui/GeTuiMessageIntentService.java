package com.duanglink.getui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.duanglink.rnmixpush.MixPushMoudle;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangheng on 2017/11/22.
 */
public class GeTuiMessageIntentService  extends GTIntentService {
    public GeTuiMessageIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        //Toast.makeText(context, "收到消息", Toast.LENGTH_SHORT).show();
        final String message = new String(msg.getPayload());
        Log.e(TAG,"收到透传消息："+message);
        //GetuiModule.sendEvent(GetuiModule.EVENT_RECEIVE_REMOTE_NOTIFICATION, param);
        //Toast.makeText(context, "sendEvent",Toast.LENGTH_SHORT).show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MixPushMoudle.sendEvent(MixPushMoudle.EVENT_RECEIVE_REMOTE_NOTIFICATION, message);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000);
        /*
        if (msg.getPayload() == null) {
            Log.e(TAG, "onReceiveMessageData -> " + "payload=null");
            return;
        }
        String data = new String(msg.getPayload());
        Log.e(TAG, "onReceiveMessageData -> " + "payload = " + data);
        try {
            JSONObject jsonObject = new JSONObject(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        //Toast.makeText(context,"onReceiveClientId -> " + "clientid = " + clientid, Toast.LENGTH_SHORT).show();
        MixPushMoudle.sendEvent(MixPushMoudle.EVENT_RECEIVE_CLIENTID,clientid);
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e(TAG, "onReceiveCommandResult -> " + "action = " + cmdMessage.getAction());
    }
}
