package com.javgame.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.javgame.Integration.IntegrationManager;
import com.javgame.alipay.AliPay;
import com.javgame.app.UnityPlayerActivity;
import com.javgame.weixin.WXShare;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public class UnityHelper {

    private static Activity getActivity() {
        return UnityPlayerActivity.realActivity;
    }

    /**
     * 登录
     *
     * @param callObj  unity3d中接收消息的gamaObject的名称
     * @param callFunc unity3d中接收消息的函数名称
     * @param data     数据格式:json
     */
    public static void login(final String callObj, final String callFunc, final String data) {
        Log.d(TAG, " login  callObj: " + callObj + " callFunc :" + callFunc);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IntegrationManager.getInstance().login(getActivity(), callObj, callFunc, data);
            }
        });
    }

    /**
     * 支付方案统一入口
     *
     * @param paytype  支付类型
     * @param data
     * @param callObj
     * @param callFunc
     */
    public static void pay(final String paytype, final String callObj, final String callFunc, final String data) {
        LogUtil.d(TAG, "paytype:" + paytype + " Pay " + data + " callObj " + callObj + " callFunc " + callFunc);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IntegrationManager.getInstance().pay(paytype, callObj, callFunc, data);
            }
        });
    }

    /**
     * 微信分享
     */
    public static void wxShareFriends(final String callObj, final String callFunc, final String data) {
        LogUtil.d(TAG, " wxShareFriends: " + data + " callObj :" + callObj + " callFunc :" + callFunc);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WXShare.getInstance().wxShareFriends(callObj, callFunc, data);
            }
        });
    }

    public static void wxShareFriendsCircle(final String callObj, final String callFunc, final String data) {
        LogUtil.d(TAG, " wxShareFriendsCircle: " + data + " callObj :" + callObj + " callFunc :" + callFunc);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WXShare.getInstance().wxShareFriendsCircle(callObj, callFunc, data);
            }
        });
    }

    public static String getIsTest() {
        LogUtil.d(TAG, "getIsTest:" + AppInfoUtil.getIsTest(getActivity()));
        return AppInfoUtil.getIsTest(getActivity());
    }

    public static String getVersionName(){
        String version = AppInfoUtil.getVersionName(getActivity());
        LogUtil.d(TAG, version);
        return version;
    }

    public static String getChannelName(){
        String version = AppInfoUtil.getChannelName(getActivity());
        LogUtil.d(TAG, version);
        return version;
    }

    public static void downLoadApk(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IntegrationManager.getInstance().downLoadApk();
            }
        });
    }
}
