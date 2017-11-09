package com.javgame.login;

import android.app.Activity;

import com.javgame.Integration.ComponentFactory;
import com.javgame.Integration.IActivityListener;
import com.javgame.utility.LogUtil;
import com.unity3d.player.UnityPlayer;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public class UserSdk {
    private static UserSdk userSdk;
    private  String loginCallObj;
    private  String loginCallFunc;

    private Activity mActivity;
    IUser user;
    IActivityListener activityListener;


    public static  void sendUnityMessage(String callObj, String callFunc, String data){
        UnityPlayer.UnitySendMessage(callObj, callFunc, data);
    }
    public void init(Activity activity) {
        mActivity = activity;
        Object obj = ComponentFactory.getInstance().getLoginUser(activity);
        if(obj instanceof IUser){
            user = (IUser) obj;
            LogUtil.i(TAG, "IUser create");
        }
        if(obj instanceof IActivityListener){
            activityListener = (IActivityListener) obj;
            activityListener.onCreate();
            LogUtil.i(TAG, "IActivityListener create");
        }
    }
    public Activity getActivity() {
        return mActivity;
    }

    public static UserSdk getInstance() {
        if (userSdk == null) {
            userSdk = new UserSdk();
        }
        return userSdk;
    }
    public IActivityListener getActivityListener() {
        return activityListener;
    }

    public void login(String callObj, String callFunc, String data) {
        if (user != null) {
            loginCallObj = callObj;
            loginCallFunc = callFunc;
            user.login(data);
        }
    }

    public void loginResult(String result) {
        // 使用userToken向游戏服务端执行登录，初始化游戏
        sendUnityMessage(loginCallObj,loginCallFunc,result);
    }
}
