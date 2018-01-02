package com.javgame.utility;

/**
 * @author zhangf
 * @date 2017/11/6
 */

public class GameConfig {
    public static final String GAME_ID = "210";
    public static final String QQ_APP_ID = "101436232";
    public static final String WX_APP_ID = "wxa2c2802e8fedd592";
    public static final String ALI_APP_ID = "2017102309474098";
    public static String BASE_URL = "http://mapi.javgame.com:14123";
    public static String WECHAT_LOGIN_URL;
    public static String WECHAT_PAY_URL;
    public static String ALI_PAY_URL;

    public static String SHARE_BASE_URL = "http://hatest.d51v.com";
    public static String APK_URL;
    public static String SHARE_URL;

    public static void init(){
        WECHAT_LOGIN_URL = BASE_URL + "/api/mlogin/WechatLogin";
        WECHAT_PAY_URL = BASE_URL + "/api/mpay/wechatPay";
        ALI_PAY_URL = BASE_URL + "/api/mpay/aplipay";

        APK_URL = "http://fwdown.hy51v.com/";
        SHARE_URL = SHARE_BASE_URL + "/static/game/share-5.jpg";
    }
}
