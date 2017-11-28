package com.javgame.alipay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.javgame.app.UnityPlayerActivity;
import com.javgame.utility.CommonUtils;
import com.javgame.utility.Constants;
import com.javgame.utility.LogUtil;
import com.unity3d.player.UnityPlayer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 重要说明:
 * <p>
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */
public class AliPay {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    static String callObj;
    static String callFunc;

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        LogUtil.d("msp","支付成功：{0}",resultInfo);

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        CommonUtils.showToast(UnityPlayerActivity.realActivity, "支付失败");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        CommonUtils.showToast(UnityPlayerActivity.realActivity, "授权成功\n" +
                                String.format("authCode:%s", authResult.getAuthCode()));

                    } else {
                        // 其他状态值则为授权失败
                        CommonUtils.showToast(UnityPlayerActivity.realActivity, "授权成功\n" +
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()));


                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    /**
     * 支付宝支付业务
     *
     * @param signData
     */
    public static void payV2(final String signData) {

//        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
//            new AlertDialog.Builder(UnityPlayerActivity.realActivity).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialoginterface, int i) {
//                            //
//                            UnityPlayerActivity.realActivity.finish();
//                        }
//                    }).show();
//            return;
//        }


        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         * orderInfo的获取必须来自服务端；
         */

//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID);
//
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//
//        final String orderInfo = orderParam + "&" + sign;
//
//
//        Log.i("msp", "orderInfo:" + orderInfo);
//        final String temp = "app_id%3d2017102309474098%26biz_content%3d%7b%22timeout_express%22%3a%2230m%22%2c%22product_code%22%3a%22QUICK_MSECURITY_PAY%22%2c%22total_amount%22%3a0.01%2c%22subject%22%3a%2210%e5%85%83%e5%ae%9d%22%2c%22out_trade_no%22%3a%2210162%22%7d%26charset%3dutf-8%26format%3djson%26method%3dalipay.trade.app.pay%26notify_url%3dhttp%3a%2f%2fmapi.javgame.com%3a14123%2fapi%2fmNotify%2fnotify_Aplipay%26sign_type%3dRSA2%26timestamp%3d2017-11-16+14%3a30%3a05%26version%3d1.0%26sign%3dF2YbB4d%2bqqun8vBGdy1oBfeJk5vJr6KtJvJgxRfuhvjWnUsNgV8vtKwCaAn6fAf87Ty4OUAKOHZPN7ihgIIYvMuP1nKXRouGkeTBmA1UvwdzBTjmquayceNXXqKqNzK%2fdD%2bHt8seHefTofLyLHyUqM%2bGRdg1vmxpaPAcPlV2TS9mbVjGLE1pQ2xQLWMoTC3OlfbSqFug5Q62uqRDyF4%2fjLvSt2XG4TLdTICJWi8X8icEyfbokxNkvT44d0phFp2mObRqR4S9jwai7hncvPhQAtvwe5FQV11gzI%2b1WiLImtuiGyaeD4cxXDDHF3sA75Wff1wH5SpFkAuQAkj01dmzEQ%3d%3d";
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {

                PayTask alipay = new PayTask(UnityPlayerActivity.realActivity);
                Map<String, String> result = alipay.payV2(signData, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();



    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(UnityPlayerActivity.realActivity);
        String version = payTask.getVersion();
        Toast.makeText(UnityPlayerActivity.realActivity, version, Toast.LENGTH_SHORT).show();
    }
}
