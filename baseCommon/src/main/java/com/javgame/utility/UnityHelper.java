package com.javgame.utility;

import android.app.Activity;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.javgame.Integration.IntegrationManager;
import com.javgame.app.UnityPlayerActivity;
import com.javgame.weixin.WXShare;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static String getVersionName() {
        String version = AppInfoUtil.getVersionName(getActivity());
        LogUtil.d(TAG, version);
        return version;
    }

    public static String getChannelName() {
        String version = AppInfoUtil.getChannelName(getActivity());
        LogUtil.d(TAG, version);
        return version;
    }

    public static void downLoadApk() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IntegrationManager.getInstance().downLoadApk();
            }
        });
    }

    public static boolean isThirdSDKQuit() {
        LogUtil.d(TAG, IntegrationManager.getInstance().isThirdSDKQuit() + "");
        return IntegrationManager.getInstance().isThirdSDKQuit();
    }

    /**
     * 通知第三方SDK 的退出
     *
     * @param callObj
     * @param callFunc
     * @param data
     */
    public static void thirdSDKQuit(final String callObj,
                                    final String callFunc, final String data) {
        LogUtil.d(TAG, " thirdSDKQuit  callObj " + callObj + " callFunc " + callFunc);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IntegrationManager.getInstance().thirdSDKQuit(
                        getActivity(), callObj, callFunc, data);
            }
        });
    }

    public static boolean isThirdLogin() {
        String name = AndroidUtil.getStringResource(getActivity(), "packageLogin");
        if (name != null) return true;
        return false;
    }

    public static String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return "";
        } else {
            return deviceId;
        }
    }

    public static String getUniqueId() {
        String androidID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }
}
