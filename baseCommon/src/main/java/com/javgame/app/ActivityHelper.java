package com.javgame.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.javgame.Integration.IntegrationManager;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public class ActivityHelper {

    public void onCreate(Activity activity, Bundle savedInstanceState) {
        IntegrationManager.getInstance().init(activity);
    }

    public void onResume() {IntegrationManager.getInstance().onResume();}

    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        IntegrationManager.getInstance().onActivityResult(requestCode,resultCode,data);
    }

    public void onPause() {IntegrationManager.getInstance().onPause();}

    public void onDestroy() {
        IntegrationManager.getInstance().onDestroy();
    }


    public void onStart() {
        IntegrationManager.getInstance().onStart();
    }

    public void onStop() { IntegrationManager.getInstance().onStop();

    }
    public void onRestart(){
        IntegrationManager.getInstance().onRestart();
    }
}
