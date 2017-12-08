package com.duanglink.huaweipush;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.duanglink.rnmixpush.MixPushMoudle;
import com.huawei.hms.support.api.push.PushReceiver;
import com.igexin.sdk.PushManager;

/**
 * Created by wangheng on 2017/11/22.
 */
public class HuaweiPushMessageReceiver extends PushReceiver {
    public static final String TAG = "HuaweiPushRevicer";

    public static final String ACTION_UPDATEUI = "action.updateUI";
    @Override
    public void onToken(Context context, String token, Bundle extras) {
        String belongId = extras.getString("belongId");
        /*调试信息
        Log.i(TAG, "belongId为:" + belongId);
        Log.i(TAG, "Token为:" + token);
        Toast.makeText(context, "Token为:" + token, Toast.LENGTH_SHORT).show();
        */
        MixPushMoudle.sendEvent(MixPushMoudle.EVENT_RECEIVE_CLIENTID, token);
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATEUI);
        intent.putExtra("type", 1);
        intent.putExtra("token", token);
        context.sendBroadcast(intent);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            //CP可以自己解析消息内容，然后做相应的处理
            String content = new String(msg, "UTF-8");
            Toast.makeText(context, "收到PUSH透传消息,消息内容为:" + content, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "收到PUSH透传消息,消息内容为:" + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            Log.i(TAG, "收到通知栏消息点击事件,notifyId:" + notifyId);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
        }
        String message = extras.getString(BOUND_KEY.pushMsgKey);
        super.onEvent(context, event, extras);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        Log.i("TAG", "Push连接状态为:" + pushState);
        //Toast.makeText(context, "Push连接状态为:" + pushState, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATEUI);
        intent.putExtra("type", 2);
        intent.putExtra("pushState", pushState);
        context.sendBroadcast(intent);
    }
}
