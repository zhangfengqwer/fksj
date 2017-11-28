package com.javgame.weixin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.javgame.app.R;
import com.javgame.utility.GameConfig;
import com.javgame.utility.LogUtil;
import com.javgame.utility.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author zhangf
 * @date 2017/11/14
 */

public class WXShare {
    private Activity activity;
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private int mTargetSceneQuan = SendMessageToWX.Req.WXSceneTimeline;
    private IWXAPI wxApi;

    private static WXShare instance;

    public static WXShare getInstance() {
        if (instance == null) {
            LogUtil.d("WXShare", "instance-----");
            instance = new WXShare();
        }
        return instance;
    }

    public void init(Activity activity) {
        this.activity = activity;
        //微信api
        wxApi = WXAPIFactory.createWXAPI(activity, GameConfig.WX_APP_ID,true);
        wxApi.registerApp(GameConfig.WX_APP_ID);
    }

    public IWXAPI getWXApi() {
        return wxApi;
    }

    public void wxShareFriendsCircle(String callObj, String callFunc, byte[] data) {
        try{
            sendImage(data);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(com.javgame.utility.Constants.TAG,e.getMessage());
        }

    }

    public void wxShareFriends(String callObj, String callFunc, String data) {
        sendWebPager(data);
    }

    public void sendWebPager(String data) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.qq.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "玩游戏就能赢奖，不信来试试！";
        msg.description = data;
        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.send_cup);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        wxApi.sendReq(req);
    }

    public void sendText(String text) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = mTargetScene;

        wxApi.sendReq(req);
    }

    public void sendImage(byte[] data) {
//        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.biaoti_denglujiangli);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetSceneQuan;
        wxApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
