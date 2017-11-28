package com.javgame.fksj;

import android.app.Activity;
import android.content.Intent;

import com.javgame.Integration.IActivityListener;
import com.javgame.alipay.AliPay;
import com.javgame.login.UserSdk;
import com.javgame.pay.IPay;
import com.javgame.pay.OrderResponse;
import com.javgame.pay.PaySdk;
import com.javgame.utility.Constants;
import com.javgame.utility.GameConfig;
import com.javgame.utility.LogUtil;
import com.javgame.utility.SignUtil;
import com.javgame.weixin.WXShare;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/10/25
 */
public class Pay implements IPay {

    private IWXAPI wxApi = WXShare.getInstance().getWXApi();

    @Override
    public String getOrderKey() {
        return null;
    }

    private Activity getActivity() {
        return UserSdk.getInstance().getActivity();
    }

    @Override
    public void pay(OrderResponse response) {
        LogUtil.d(TAG, "Trade_no:" + response.getData().getTrade_no());
        String payType = PaySdk.getInstance().getPayType();

        if (Constants.PAY_TYPE_WX.equals(payType)) {
            callWXPay(response);
        } else if (Constants.PAY_TYPE_ALIPAY.equals(payType)) {
            LogUtil.d(TAG, "orderInfo:" + response.getData().getSign());
            AliPay.payV2(response.getData().getSign());
        }
    }


    private void callWXPay(final OrderResponse response) {
        PayReq request = new PayReq();

        request.appId = GameConfig.WX_APP_ID;

        request.partnerId = response.getData().getPartnerid();

        request.prepayId = response.getData().getPrepayid();

        request.packageValue = response.getData().getPackageX();

        request.nonceStr = response.getData().getNoncestr();

        request.timeStamp = response.getData().getTimestamp();

        request.sign = response.getData().getSign().toUpperCase();

        wxApi.sendReq(request);
    }
}

