package com.javgame.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.javgame.utility.AndroidUtil;
import com.javgame.utility.AppInfoUtil;
import com.javgame.utility.GameConfig;
import com.javgame.utility.LogUtil;
import com.javgame.utility.UnityHelper;
import com.unity3d.player.UnityPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.javgame.utility.Constants.TAG;


public class UnityPlayerActivity extends Activity {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    public static Activity realActivity; //unity获取该变量

    private ActivityHelper activityHelper = new ActivityHelper();

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        realActivity = this;
        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();

        GameConfig.init(this);

        File rootFile = Environment.getExternalStorageDirectory();
        String path = rootFile.getPath();
        File file = new File(rootFile.getPath(),"tlj_config.txt");
        if (file.exists()){
            try {
                FileInputStream stream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                StringBuilder stringBuilder = new StringBuilder();
                int i;
                while ((i = stream.read(bytes,0,bytes.length)) > 0){
                    stringBuilder.append(new String(bytes,0,i));
                }
                LogUtil.d(TAG,stringBuilder.toString());
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LogUtil.d(TAG,path);
        activityHelper.onCreate(this, savedInstanceState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
        activityHelper.onDestroy();
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        UnityPlayer.UnitySendMessage("AndroidCallBack", "OnPauseCallBack", "");
        mUnityPlayer.pause();
        activityHelper.onPause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        UnityPlayer.UnitySendMessage("AndroidCallBack", "OnResumeCallBack", "");
        mUnityPlayer.resume();
        activityHelper.onResume();
    }

    // Low Memory Unity
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
}
