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
import com.unity3d.player.UnityPlayer;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 *  重要说明:
 *
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */
public class AliPay{

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017102309474098";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDK9XE3oqQ5ZyUd+D9/SlfCO5n5eHi3gOSZU/T6k5mTR9Sb3yXCWg7AsQ/23w3OX9DuEbqPn0KBPMXIV4fi382qbwY9tlkfiOQxxaBw/+2+IA69RJD3Of3qNMZSLzcESHwwGeFBVZ8NhP0qCznU3T7SjULYnjNeQCq2TYfmpUWa8J2WnjUX1TIO4VHHwJJEcmR1Yj3L0NT0zBBHFfUN3E5dj0LlXVn8bEj3m7CduvlDU9pm2mtBhJU6Zvq10xLFSbDvhvYvusR2XxX44QKsHZ2htVNrYQ+pTeVDtKKLnaivbpLDscDu831WFY3YXc4W/2Da4vw2pVgEzRLg+lbVfpu1AgMBAAECggEBAKZJKES8Ba9eWePPJD8navWhnYru8IDwotsZFYtD6TzpSkGXN7mfzZyEtZ6/q/EdQfH8Jo1Ln9KBN07ooFX6pefw5P0k7KpQnx5EXCqZFXGDlG/vwJI+OlJsgNyPAJWKye0jFxJPk2whPMb4wZ4s0y7xPAZ8v3uGOTh6F1GasYe6DMULT6B0SUSJz+QkkhPiYr8Jlr8NxygoTtJ4uiXPSj2GiYMoM9gNdx5ROddrAdKvtZWA+6uEf/YOI+ErsRs5ugl4P/crg9IZA/qmzjHjCECp2tt2CMqn0qzPYm/wzw+GLQhYHOwfvRuexYmHKmpNkaaBQ4FtlWtfIFKPtSX4uwECgYEA6tpzkBikEL3T6Q4mITPgkWvhL8GVK8UTP2lv5zhtnJEt2Xp2dKYYSMA+rXJuLdyhf4NeRsuPZ3UjtnjzglcZPUOG225frGwlNycrZW/CQ7N/Aekjixlk9OOjIADF1iN2Lepqdu9fCKFfh8I6zP4xYLNqRkYT5RyetkuxpXyqVHUCgYEA3TvL9JWsnTXJI4b7lPVVoSkX1VqlloQ7Pr9Ron3qO/AMaxoesldMnDJFErpNWyD3jTvzeysBH9FVggojRy9j7B8P3UYx8u9dpE/ae7rk+9Pk5f8NuvLMSKBnLdL3mVVIs+J6uK29XbvKpeDUshIO+rk0jVNOYMVdby7tPZ91QkECgYA0wbHoEsD4ScxKtDT4jHDL+hHx6miaFoFGY2cR5+knnK1SB2KIva8C2Ly7tdLuVnuo61fIS34BXZ0SJoV9KBexXXPz4w127CxIAXKMLNjU4IONaFPlsWSuZlyEmefXPMwVcG1OHmOYyrdtBcKzvf5VnLgo5SEe/JjilopnhGO26QKBgGXbyDXeS9E+GFORLHgS1NAUuXJz/9VjIFvtfkqQKq5aAX22UvfNleo8guzydfdFIHUYaywESso5eWMcA84clab7TjSUwx6U8spaMb/R9uezUapLWij+7OtrXtYMUg944rZfyh0JcSyc79qbv5IVGmx5pSaEeou3kyNDudsrdbOBAoGATHgqSgWxGZNjai8e1jIzAQWpuG1xdyDBhnE7787qUXc2BvkE6vUArjhe2/SfwXBar9JaqJY5laLK3sdWs9VVs0EXs/dKAkVD+jJWftYjHNiF9+Lqsk8U88Qs1i/FfEqdWklj9b0ot+/KvFxgnHqyS3zcrFBSHy2UdMJuLpu4Tjc=";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    static String callObj ;
    static String callFunc ;

    @SuppressLint("HandlerLeak")
    private  static Handler mHandler = new Handler() {
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
                        CommonUtils.showToast(UnityPlayerActivity.realActivity,"支付成功");
                        UnityPlayer.UnitySendMessage(callObj, callFunc, "支付成功");

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        CommonUtils.showToast(UnityPlayerActivity.realActivity,"支付失败");
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
                        CommonUtils.showToast(UnityPlayerActivity.realActivity,"授权成功\n" +
                                String.format("authCode:%s", authResult.getAuthCode()));

                    } else {
                        // 其他状态值则为授权失败
                        CommonUtils.showToast(UnityPlayerActivity.realActivity,"授权成功\n" +
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()));


                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    /**
     * 支付宝支付业务
     *
     *
     */
    public static void payV2() {

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
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);

        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;

        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);

        final String orderInfo = orderParam + "&" + sign;



        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(UnityPlayerActivity.realActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
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
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(UnityPlayerActivity.realActivity);
        String version = payTask.getVersion();
        Toast.makeText(UnityPlayerActivity.realActivity, version, Toast.LENGTH_SHORT).show();
    }


    public static void payV2(String Obj, String Func) {
        callObj = Obj;
        callFunc =Func;
        payV2();
    }
}
