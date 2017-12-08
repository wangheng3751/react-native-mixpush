package com.duanglink.rnmixpush;

import android.content.Context;

/**
 * Created by wangheng on 2017/11/22.
 */
public interface MixPushManager {

    void registerPush(Context context);

    void unRegisterPush(Context context);

    void setAlias(Context context, String alias);

    void unsetAlias(Context context, String alias);

    void setTags(Context context, String... tags);

    void unsetTags(Context context, String... tags);

    String getName();
}