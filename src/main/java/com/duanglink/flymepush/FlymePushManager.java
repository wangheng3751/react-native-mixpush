package com.duanglink.flymepush;

import android.content.Context;

import com.duanglink.rnmixpush.MixPushManager;
import com.meizu.cloud.pushsdk.PushManager;

/**
 * Created by wangheng on 2017/11/22.
 */
public class FlymePushManager implements MixPushManager {
    public static final String NAME = "meizuPush";

    private String appId;
    private String appKey;

    public FlymePushManager(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    @Override
    public void registerPush(Context context) {
        PushManager.register(context, appId, appKey);
    }

    @Override
    public void unRegisterPush(Context context) {
        PushManager.unRegister(context, appId, appKey);
    }

    @Override
    public void setAlias(Context context, String alias) {
        PushManager.subScribeAlias(context, appId, appKey, PushManager.getPushId(context), alias);
    }

    @Override
    public void unsetAlias(Context context, String alias) {
        PushManager.unSubScribeAlias(context, appId, appKey, PushManager.getPushId(context), alias);
    }

    @Override
    public void setTags(Context context, String... tags) {
        for (String tag : tags) {
            PushManager.subScribeTags(context, appId, appKey, PushManager.getPushId(context), tag);
        }
    }

    @Override
    public void unsetTags(Context context, String... tags) {
        for (String tag : tags) {
            PushManager.unSubScribeTags(context, appId, appKey, PushManager.getPushId(context), tag);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static String sendPushMessage(String pushid){
        return "";
    }
}
