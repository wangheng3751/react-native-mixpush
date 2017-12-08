/**
 * Created by wangheng on 2017/11/22.
 */
package com.duanglink.mipush;

import android.content.Context;

import com.duanglink.rnmixpush.MixPushManager;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class MiPushManager implements MixPushManager {
    public static final String NAME = "mipush";
    private String appId;
    private String appKey;

    public MiPushManager(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    @Override
    public void registerPush(Context context) {
        MiPushClient.registerPush(context.getApplicationContext(), appId, appKey);
    }

    @Override
    public void unRegisterPush(Context context) {
        unsetAlias(context, null);
        MiPushClient.unregisterPush(context.getApplicationContext());
    }

    @Override
    public void setAlias(Context context, String alias) {
        if (!MiPushClient.getAllAlias(context).contains(alias)) {
            MiPushClient.setAlias(context, alias, null);
        }
    }

    @Override
    public void unsetAlias(Context context, String alias) {
        List<String> allAlias = MiPushClient.getAllAlias(context);
        for (int i = 0; i < allAlias.size(); i++) {
            MiPushClient.unsetAlias(context, allAlias.get(i), null);
        }
    }

    @Override
    public void setTags(Context context, String... tags) {
        for (String tag : tags){
            MiPushClient.subscribe(context, tag, null);
        }

    }

    @Override
    public void unsetTags(Context context, String... tags) {
        for (String tag : tags) {
            MiPushClient.unsubscribe(context, tag, null);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
