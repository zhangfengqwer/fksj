package com.javgame.Integration;

import android.content.Intent;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public interface IActivityListener {
    void onCreate();
    void onPause();
    void onStart();
    void onRestart();
    void onResume();
    void onStop();
    void onDestroy();
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
