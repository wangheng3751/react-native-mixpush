package com.duanglink.huaweipush;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.duanglink.rnmixpush.HttpHelpers;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * Created by wangheng on 2017/11/22.
 */
public class HuaweiPushManager {
    private static String appId="100147479";
    private static String appSecret="4fd1cf7269547184941d584ee6063f0d";
    public static String refreshToken() throws IOException, JSONException
    {
        String tokenUrl="https://login.vmall.com/oauth2/token";
        String msgBody = MessageFormat.format("grant_type=client_credentials&client_secret={0}&client_id={1}", URLEncoder.encode(appSecret, "UTF-8"), appId);
        String response = HttpHelpers.sendPost(tokenUrl, msgBody);
        JSONObject obj= new JSONObject(response);;
        String accessToken = obj.getString("access_token");
        return  accessToken;
    }

    //发送Push消息
    public static String sendPushMessage(String deviceToken) throws IOException,JSONException
    {
        String accessToken=refreshToken();
        String apiUrl="https://api.push.hicloud.com/pushsend.do";
        JSONArray deviceTokens = new JSONArray();//目标设备Token
        deviceTokens.put(deviceToken);
        //deviceTokens.put("0866936023001827300001100100CN01");//测试设备
        //deviceTokens.put("0A0000044270BAD0300001100100CN01");//测试设备

        JSONObject body = new JSONObject();//仅通知栏消息需要设置标题和内容，透传消息key和value为用户自定义
        body.put("title", "测试");//消息标题
        body.put("content", "测试华为通知");//消息内容体

        JSONObject param = new JSONObject();
        param.put("appPkgName", "com.mixpush");//定义需要打开的appPkgName

        JSONObject action = new JSONObject();
        action.put("type", 3);//类型3为打开APP，其他行为请参考接口文档设置
        action.put("param", param);//消息点击动作参数

        JSONObject msg = new JSONObject();
        msg.put("type", 3);//3: 通知栏消息，异步透传消息请根据接口文档设置
        msg.put("action", action);//消息点击动作
        msg.put("body", body);//通知栏消息body内容

        JSONObject ext = new JSONObject();//扩展信息，含BI消息统计，特定展示风格，消息折叠。
        ext.put("biTag", "Trump");//设置消息标签，如果带了这个标签，会在回执中推送给CP用于检测某种类型消息的到达率和状态
        ext.put("icon", "http://pic.qiantucdn.com/58pic/12/38/18/13758PIC4GV.jpg");//自定义推送消息在通知栏的图标,value为一个公网可以访问的URL
        JSONObject hps = new JSONObject();//华为PUSH消息总结构体
        hps.put("msg", msg);
        hps.put("ext", ext);

        JSONObject payload = new JSONObject();
        payload.put("hps", hps);

        String postBody = MessageFormat.format(
                "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}",
                URLEncoder.encode(accessToken,"UTF-8"),
                URLEncoder.encode("openpush.message.api.send","UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000),"UTF-8"),
                URLEncoder.encode(deviceTokens.toString(),"UTF-8"),
                URLEncoder.encode(payload.toString(),"UTF-8"));

        String postUrl = apiUrl + "?nsp_ctx=" + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + appId + "\"}", "UTF-8");
        String result= HttpHelpers.sendPost(postUrl, postBody);
        Log.i("HuaWeiPush", "PushResult:" + result);
        return result;
    }
}
