package com.javgame.fksj;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.javgame.Integration.IActivityListener;
import com.javgame.login.UserSdk;
import com.javgame.pay.IPay;
import com.javgame.pay.IWXShare;
import com.javgame.pay.PayHelper;
import com.javgame.utility.LogUtil;
import com.javgame.utility.SignUtil;
import com.javgame.utility.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/10/25
 */
public class Pay implements IPay,IWXShare,IActivityListener {
    public static final String ORDER_URL_ALIPAY = "http://mpay.51v.cn/pay/trade_alipayv3.aspx";
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private int mTargetSceneQuan = SendMessageToWX.Req.WXSceneTimeline;
    private IWXAPI wxApi;
    @Override
    public String getOrderKey() {
        return null;
    }

    private Activity getActivity(){
        return UserSdk.getInstance().getActivity();
    }

    @Override
    public void onCreate() {
        wxApi = WXAPIFactory.createWXAPI(getActivity(), GameConfig.WX_APP_ID);
        wxApi.registerApp(GameConfig.WX_APP_ID);
    }
    @Override
    public void pay(String data) {
        LogUtil.d(TAG, data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String uid = jsonObject.getString("uid");
            String itemid = jsonObject.getString("itemid");
            String chargecount = jsonObject.getString("chargecount");
            String amount = jsonObject.getString("amount");
            String expand = PayHelper.getOrderExpand(getActivity());
            String sign = "uid=" + uid + "&itemid=" + itemid + "&chargecount=" + chargecount + "&amount=" + amount + "&key=" + "alipay_javgame_@#$";
            String md5 = SignUtil.md5(sign);

            LogUtil.d(TAG, md5);
//
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .get()
//                    .url(ORDER_URL_ALIPAY)
//                    .header("uid", uid)
//                    .header("goodsid", itemid)
//                    .header("price", chargecount)
//                    .header("num", amount)
//                    .header("gameid", 210+"")
//                    .header("sign", md5)
//                    .header("expand", expand)
//                    .build();
//
//            LogUtil.d(TAG, request.url().toString());
//            Call call = client.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    LogUtil.d(TAG, call.toString());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    LogUtil.d(TAG, response.body().string());
//                }
//            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void shareFriend(String data) {
        LogUtil.d(TAG,data);
        try {
//            sendImage();
            sendWebPager(data);
//            getActivity().finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendWebPager(String data) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.qq.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "玩游戏就能赢奖，不信来试试！";
        msg.description = data;
        Bitmap bmp = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.send_cup);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        wxApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void sendText(String text){
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

    public void sendImage(){
        Bitmap bmp = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.send_music_thumb);
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

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}

