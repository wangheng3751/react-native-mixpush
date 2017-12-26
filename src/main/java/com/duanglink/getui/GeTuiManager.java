package com.duanglink.getui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.duanglink.rnmixpush.HttpHelpers;
import com.duanglink.rnmixpush.MixPushManager;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangheng on 2017/11/22.
 */
public class GeTuiManager implements MixPushManager {
    public static final String NAME = "getui";
    private static String appId="4PrFb29HZA7ROkO1wHNXB8";
    private static String appKey="W5s9oBaCLYAV6dPRFbBa1";
    private static String masterSecret="FYzRODdJOaALrUYFlHb2V9";
    @Override
    public void registerPush(Context context) {
        PushManager.getInstance().initialize(context, null);
        PushManager.getInstance().registerPushIntentService(context, GeTuiMessageIntentService.class);
    }

    @Override
    public void unRegisterPush(Context context) {
        PushManager.getInstance().stopService(context);
    }

    @Override
    public void setAlias(Context context, String alias) {
        PushManager.getInstance().bindAlias(context, alias);
    }

    @Override
    public void unsetAlias(Context context, String alias) {
        PushManager.getInstance().unBindAlias(context, alias, false);
    }

    @Override
    public void setTags(Context context, String... tags) {
        Tag[] temps = new Tag[tags.length];
        for (int i = 0; i < tags.length; i++) {
            Tag tag = new Tag();
            tag.setName(tags[i]);
            temps[i] = tag;
        }
        PushManager.getInstance().setTag(context, temps, null);
    }

    @Override
    public void unsetTags(Context context, String... tags) {
        PushManager.getInstance().setTag(context, new Tag[0], null);
    }

    @Override
    public String getClientId(Context context) {
        return  PushManager.getInstance().getClientid(context);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static String sendPushMessage(String cid) throws IOException,JSONException{
        String auth_sign=authSign();
        if(auth_sign=="")
            return "";
        String push_url=MessageFormat.format("https://restapi.getui.com/v1/{0}/push_single",appId);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar c = Calendar.getInstance();

        JSONObject message = new JSONObject();
        message.put("appkey",appKey);
        message.put("is_offline",false);
        message.put("msgtype","notification");

        JSONObject notification = new JSONObject();
        JSONObject style = new JSONObject();
        style.put("type",0);
        style.put("text","你收到一条来自个推的推送通知");
        style.put("title","个推测试");
        style.put("logo","push.png");
        style.put("logourl","");
        style.put("is_ring",true);
        style.put("is_vibrate",true);
        style.put("is_clearable",true);
        notification.put("style",style);
        notification.put("transmission_type",true);
        notification.put("transmission_content","这是透传的内容");
        notification.put("duration_begin",formater.format(c.getTime()));
        c.add(Calendar.DAY_OF_MONTH,1);//加一天
        notification.put("duration_end",formater.format(c.getTime()));

        Timestamp timestamp = new Timestamp(new Date().getTime());
        String postBody = MessageFormat.format(
                "message={0}&notification={1}&cid={2}&requestid={3}",
                message.toString(),notification.toString(),cid,timestamp.toString());
        String result= HttpHelpers.sendPost(push_url, postBody);
        return  result;
    }

    public static String authSign() throws  JSONException {
        String time=new Date().getTime()+"";;
        String signUrl= MessageFormat.format("https://restapi.getui.com/v1/{0}/auth_sign",appId);
        String sign= HttpHelpers.getSHA256(appKey+time+masterSecret);
        String postBody = MessageFormat.format("sign={0}&timestamp={1}&appkey={2}",sign ,time ,appKey);
        Log.i("GeTui", "postBody:"+postBody);
        String result= HttpHelpers.sendPost(signUrl, postBody);
        JSONObject obj= new JSONObject(result);;
        String ok = obj.getString("result");
        if(ok=="ok"){
            return  obj.getString("auth_token");
        }
        Log.i("GeTui", "个推鉴权失败,code:"+ok);
        return "";
    }
}
