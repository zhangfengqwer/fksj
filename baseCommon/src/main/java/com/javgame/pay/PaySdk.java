package com.javgame.pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.javgame.Integration.ComponentFactory;
import com.javgame.Integration.IActivityListener;
import com.javgame.utility.AppInfoUtil;
import com.javgame.utility.CommonUtils;
import com.javgame.utility.GameConfig;
import com.javgame.utility.LogUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

    private String shareFriendsCallObj;
    private String shareFriendsCallFunc;

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
                    OrderResponse response = new Gson().fromJson(orderResponse, OrderResponse.class);
                    if(response.getCode() != 1) {
                        CommonUtils.showToast(activity,"订单返回失败");
                        return;
                    }
                    if (mPay != null) {
                        mPay.pay(response);
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
        }
    }

    public Object getPayObject() {
        return ComponentFactory.getInstance().getPay();
    }

    public IActivityListener getActivityListener() {
        return activityListener;
    }

    public void pay(String callObj, String callFunc, String data) {
        objCallBack = callObj;
        funCallBack = callFunc;

        dialog = ProgressDialog.show(activity, "", "订单生成中...");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        try {
            JSONObject jsonObject = new JSONObject(data);
            String uid = jsonObject.getString("uid");
            String goods_id = jsonObject.getString("goods_id");
            String goods_num = jsonObject.getString("goods_num");
            String goods_name = jsonObject.getString("goods_name");
            String price = jsonObject.getString("price");
            price = "0.01";
            Map<String, String> map = new HashMap<>();
            map.put("userId", uid);
            map.put("gameId", GameConfig.GAME_ID);
            map.put("appId", GameConfig.WX_APP_ID);
            map.put("ProductId", goods_id);
            map.put("ProductName", goods_name);
            map.put("ProductNum", goods_num);
            map.put("ProductDesc", goods_name);
            map.put("price", price);
            map.put("total_amount", price);
            map.put("version", AppInfoUtil.getVersionName(activity));
            map.put("PhoneModel", Build.MODEL);
            map.put("expand", "");
            LogUtil.d(TAG, "pay:" + new JSONObject(map).toString());
            OkHttpUtils
                    .postString()
                    .url(GameConfig.WECHAT_PAY_URL)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .content(new JSONObject(map).toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
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
}
