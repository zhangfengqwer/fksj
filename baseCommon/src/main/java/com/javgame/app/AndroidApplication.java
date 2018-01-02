package com.javgame.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * @author czq
 * 自定义我们自己的Application，有的SDK 需要监听Application onCreate 。
 */
public class AndroidApplication extends Application {
    public static Context appContext ;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AndroidApplication", "onCreate");
        appContext = this;
    }
    
    @Override
    protected void attachBaseContext(Context base) {
    	super.attachBaseContext(base);
    }
}
