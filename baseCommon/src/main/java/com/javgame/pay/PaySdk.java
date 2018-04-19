package com.javgame.pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.javgame.Integration.ComponentFactory;
import com.javgame.Integration.IActivityListener;
import com.javgame.Integration.IThirdMethod;
import com.javgame.utility.AndroidUtil;
import com.javgame.utility.AppInfoUtil;
import com.javgame.utility.CommonUtils;
import com.javgame.utility.Constants;
import com.javgame.utility.GameConfig;
import com.javgame.utility.LogUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.utils.Exceptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public class PaySdk {

    private static PaySdk paySdk;
    private Activity activity;

    /**
     * unity回调对象
     */
    public String objCallBack;
    /**
     * unity回调方法
     */
    public String funCallBack;

    private String payType;

    private String shareFriendsCallObj;
    private String shareFriendsCallFunc;
    private IThirdMethod thirdListener = null;

    IActivityListener activityListener;
    private IPay mPay;

    private final int ORDER_FLAG = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog != null) dialog.dismiss();
            switch (msg.what) {
                case ORDER_FLAG:
                    String orderResponse = (String) msg.obj;

                    if (Constants.PAY_TYPE_WX.equals(payType)) {
                        try {
                            OrderResponse response = new Gson().fromJson(orderResponse, OrderResponse.class);
                            if (response.getCode() != 1) {
                                CommonUtils.showToast(activity, "订单返回失败:" + response.getMessage());
                                return;
                            }
                            if (mPay != null) {
                                mPay.pay(response);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } else if (Constants.PAY_TYPE_ALIPAY.equals(payType)) {
                        try {
                            JSONObject jsonObject = new JSONObject(orderResponse);
                            String data = jsonObject.getString("data");
                            String message = jsonObject.getString("msg");
                            JSONObject dataJson = new JSONObject(data);
                            int code = dataJson.getInt("_code");
                            int expand = dataJson.getInt("expand");
                            String signData = dataJson.getString("Data");
                            if(code != 1){
                                CommonUtils.showToast(activity, "订单返回失败:" + message);
                                return;
                            }

                            OrderResponse response = new OrderResponse();
                            response.setCode(code);
                            response.setMessage(message);

                            OrderResponse.DataBean dataBean = new OrderResponse.DataBean();
                            dataBean.setSign(signData);
                            dataBean.setTrade_no(expand+"");
                            response.setData(dataBean);

                            if (mPay != null) {
                                mPay.pay(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        LogUtil.e(TAG, "未知的paytype:" + payType);
                    }

                    break;
            }
        }
    };
    private ProgressDialog dialog;


    public static PaySdk getInstance() {
        if (paySdk == null) {
            paySdk = new PaySdk();
        }
        return paySdk;
    }

    public void Init(Activity act) {
        activity = act;
        Object obj = getPayObject();
        if (obj != null) {
            if (obj instanceof IPay) {
                mPay = (IPay) obj;
                LogUtil.i(TAG, "IPay create");
            }

            if (obj instanceof IActivityListener) {
                activityListener = (IActivityListener) obj;
                activityListener.onCreate();
                LogUtil.i(TAG, "IActivityListener create");
            }

            if (obj instanceof IThirdMethod) {
                thirdListener = (IThirdMethod) obj;
            }
        }
    }

    public Object getPayObject() {
        return ComponentFactory.getInstance().getPay();
    }

    public IActivityListener getActivityListener() {
        return activityListener;
    }

    public String getPayType(){return payType;}

    public void pay(String paytype, String callObj, String callFunc, String data) {
        objCallBack = callObj;
        funCallBack = callFunc;
        payType = paytype;

        dialog = ProgressDialog.show(activity, "", "订单生成中...");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        if (Constants.PAY_TYPE_WX.equals(paytype)) {
            getOrder(data,GameConfig.WX_APP_ID, GameConfig.Common_PAY_URL,"10020");
        } else if (Constants.PAY_TYPE_ALIPAY.equals(paytype)) {
            getOrder(data,GameConfig.ALI_APP_ID, GameConfig.Common_PAY_URL,"10001");
        } else if(Constants.PAY_TYPE_HUAWEI.equals(paytype)){
            OrderResponse response = new OrderResponse();
            response.setMessage(data);
            mPay.pay(response);
            dialog.dismiss();
        }else {
            LogUtil.d(TAG, "未知的paytype:" + paytype);
        }
    }

    private void getOrder(String data, String appId, String url,String codePayId) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String uid = jsonObject.getString("uid");
            String goods_id = jsonObject.getString("goods_id");
            String goods_num = jsonObject.getString("goods_num");
            String goods_name = jsonObject.getString("goods_name");
            String price = jsonObject.getString("price");
            Map<String, String> map = new HashMap<>();
            map.put("userId", uid);
            map.put("gameId",GameConfig.GAME_ID);
            map.put("appId", appId);
            map.put("ProductId", goods_id);
            map.put("ProductName", goods_name);
            map.put("ProductNum", goods_num);
            map.put("ProductDesc", goods_name);
            map.put("codePayId", codePayId);
            map.put("price", price);
            map.put("total_amount", price);
            map.put("version", AppInfoUtil.getVersionName(activity));
            map.put("PhoneModel", Build.MODEL);
            map.put("expand", "");
            LogUtil.d(TAG, "pay:" + new JSONObject(map).toString());
            LogUtil.d(TAG, "url:" + url);

            OkHttpUtils
                    .postString()
                    .url(url)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .content(new JSONObject(map).toString())
                    .build()
                    .connTimeOut(10000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            if (dialog != null) dialog.dismiss();
                            CommonUtils.showToast(activity, "请求订单失败，请重新请求。");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response) {
                            LogUtil.d(TAG, "response:" + response);
                            Message message = handler.obtainMessage();
                            message.what = ORDER_FLAG;
                            message.obj = response;
                            handler.sendMessage(message);
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isThirdSDKQuit() {
        String[] channels = AndroidUtil.getStringArray(activity, "thirdSdkQuitChannel");
        List<String> list = Arrays.asList(channels);
        return list.contains(AppInfoUtil.getChannelName(activity));
    }

    public void thirdQuit(String data) {
        if (thirdListener != null) {
            thirdListener.quit();
        }
    }
}
