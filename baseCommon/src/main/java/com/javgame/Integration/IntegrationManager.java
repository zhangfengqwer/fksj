package com.javgame.Integration;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.javgame.login.UserSdk;
import com.javgame.pay.PaySdk;
import com.javgame.update.DownLoadApk;
import com.javgame.update.DownloadService;
import com.javgame.update.InstallUtil;
import com.javgame.utility.LogUtil;
import com.javgame.weixin.WXShare;
import com.unity3d.player.UnityPlayer;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public class IntegrationManager {

    public static final String TAG = IntegrationManager.class.getSimpleName();
    private static IntegrationManager mIntegrationManager;
    private Activity activity;



    public static IntegrationManager getInstance() {
        if (mIntegrationManager == null) {
            mIntegrationManager = new IntegrationManager();
        }
        return mIntegrationManager;
    }

    /**
     * 第三方支付需要的通知 ，Android Application onCreate 的时候，通知第三方程序
     * 如果 非第三方支付 方法为空
     *
     * @param application
     */
    public void iniApplication(Application application) {
//        PaySdk.getInstance().initApplication(application);
    }

    public void attachBaseContext(Application base) {
//        PaySdk.getInstance().attachBaseContext(base);
    }

    /**
     * Activity OnCreate 的时候初始化
     *
     * @param activity
     */


    public void init(Activity activity) {
        this.activity = activity;
        WXShare.getInstance().init(activity);
        ComponentFactory.getInstance().init(activity);
        UserSdk.getInstance().init(activity);
        PaySdk.getInstance().Init(activity);
        DownLoadApk.getInstance().init(activity);
    }



    public void login(Activity activity, String callObj, String callFunc, String data) {
        UserSdk.getInstance().login(callObj, callFunc, data);
    }

    public void onDestroy() {
        if (UserSdk.getInstance().getActivityListener() != null) {
            UserSdk.getInstance().getActivityListener().onDestroy();
        }
        if (PaySdk.getInstance().getActivityListener() != null) {
            PaySdk.getInstance().getActivityListener().onDestroy();
        }
    }

    public void onStart() {
        if (UserSdk.getInstance().getActivityListener() != null) {
            UserSdk.getInstance().getActivityListener().onStart();
        }
        if (PaySdk.getInstance().getActivityListener() != null) {
            PaySdk.getInstance().getActivityListener().onStart();
        }
    }

    public void onStop() {
        if (UserSdk.getInstance().getActivityListener() != null) {
            UserSdk.getInstance().getActivityListener().onStop();
        }
        if (PaySdk.getInstance().getActivityListener() != null) {
            PaySdk.getInstance().getActivityListener().onStop();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (UserSdk.getInstance().getActivityListener() != null) {
            UserSdk.getInstance().getActivityListener().onActivityResult(requestCode, resultCode, data);
        }
        if (PaySdk.getInstance().getActivityListener() != null) {
            PaySdk.getInstance().getActivityListener().onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onResume() {
        if (UserSdk.getInstance().getActivityListener() != null) {
            UserSdk.getInstance().getActivityListener().onResume();
        }
        if (PaySdk.getInstance().getActivityListener() != null) {
            PaySdk.getInstance().getActivityListener().onResume();
        }
    }

    public void onPause() {
        if (UserSdk.getInstance().getActivityListener() != null) {
            UserSdk.getInstance().getActivityListener().onPause();
        }
        if (PaySdk.getInstance().getActivityListener() != null) {
            PaySdk.getInstance().getActivityListener().onPause();
        }
    }

    public void onRestart() {
        if (UserSdk.getInstance().getActivityListener() != null) {
            UserSdk.getInstance().getActivityListener().onRestart();
        }
        if (PaySdk.getInstance().getActivityListener() != null) {
            PaySdk.getInstance().getActivityListener().onRestart();
        }
    }

    public void pay(String paytype, String callObj, String callFunc, String data) {
        PaySdk.getInstance().pay(paytype,callObj, callFunc, data);
    }

    public void downLoadApk() {
        DownLoadApk.getInstance().downLoadApk();
    }
}
