package com.javgame.utility;

import android.app.Activity;

import com.unity3d.player.UnityPlayer;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/11/6
 */

public class GameConfig {
    public static final String GAME_ID = "210";
    public static final String QQ_APP_ID = "101436232";
    public static final String WX_APP_ID = "wxa2c2802e8fedd592";
    public static final String ALI_APP_ID = "2017102309474098";
    public static final String HUAWEI_APP_ID = "100174463";
    public static String TEST_BASE_URL = "http://dmpay.51v.cn";
    public static String ONLINE_BASE_URL = "http://fkmpay.51v.cn";
    public static String BASE_URL;

    public static String TEST_BASE_FILE_URL = "http://fwdown.hy51v.com/test";
    public static String ONLINE_BASE_FILE_URL = "http://fwdown.hy51v.com/online";
    public static String BASE_FILE_URL;

    public static String WECHAT_LOGIN_URL;
    public static String WECHAT_PAY_URL;
    public static String ALI_PAY_URL;
    public static String Common_PAY_URL;

    public static String APK_URL;
    public static String SHARE_URL;
    public static String webpageUrl = "http://xyyl.hy51v.com/index.php?r=tools/download";


    public static boolean isTest = true;
    private static boolean isShowLog = true;

    public static void init(Activity activity){
        String isTestString = AppInfoUtil.getIsTest(activity);
        if("1".equals(isTestString)){
            isTest = true;
        }else {
            isTest = false;
        }

        if (!isShowLog) {
            LogUtil.DEBUG_LOG = 0;
            UnityPlayer.UnitySendMessage("AndroidCallBack", "SetLogIsShow", "0");
        }else{

        }

        if (!GameConfig.isTest) {
            GameConfig.BASE_URL = ONLINE_BASE_URL;
            GameConfig.BASE_FILE_URL = ONLINE_BASE_FILE_URL;
            LogUtil.d(TAG, " GameConfig.BASE_URL:" + GameConfig.BASE_URL);
//            UnityPlayer.UnitySendMessage("AndroidCallBack", "SetIsTest", "0");
        }else {
            GameConfig.BASE_URL = TEST_BASE_URL;
            GameConfig.BASE_FILE_URL = TEST_BASE_FILE_URL;
            LogUtil.d(TAG, " GameConfig.BASE_URL:" + GameConfig.BASE_URL);
//            UnityPlayer.UnitySendMessage("AndroidCallBack", "SetIsTest", "1");
        }

        WECHAT_LOGIN_URL = BASE_URL + "/mLogin/WechatLogin";
        WECHAT_PAY_URL = BASE_URL + "/mPay/wechatPay";
        ALI_PAY_URL = BASE_URL + "/mPay/aplipay";
        Common_PAY_URL = BASE_URL + "/mPay/trade";
        SHARE_URL = BASE_FILE_URL + "/file/share-5.jpg";

        //设置apk更新路径
        APK_URL = BASE_FILE_URL + "/apk/";
        String channelName = AndroidUtil.getMataData(activity, "UMENG_CHANNEL");
        GameConfig.APK_URL += "fksj_" + channelName + ".apk";
        LogUtil.d(TAG, " GameConfig.APK_URL:" + GameConfig.APK_URL);
        UnityPlayer.UnitySendMessage("AndroidCallBack", "SetVersionCode", AppInfoUtil.getVersionName(activity));
    }
}
