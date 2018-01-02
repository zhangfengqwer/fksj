package com.javgame.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.javgame.utility.GameConfig;
import com.javgame.utility.LogUtil;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/12/26
 */

public class DownLoadApk {

    private static DownLoadApk instance;
    private DownloadService.DownloadBinder mDownloadBinder;
    private long downloadId;
    private ProgressDialog progressDialog;
    private Activity activity;

    public static DownLoadApk getInstance() {
        if (instance == null) {
            instance = new DownLoadApk();
        }
        return instance;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LogUtil.d(TAG, "complete!~! ");
                    String apkPath = DownloadService.mApkPaths.get(downloadId);
                    Log.d("DownloadFinishReceiver", apkPath);
                    if (!apkPath.isEmpty()) {
                        SystemManager.setPermission(apkPath);//提升读写权限,否则可能出现解析异常
                        InstallUtil.install(activity, apkPath, false);
                    } else {
                        Log.e("DownloadFinishReceiver", "apkPath is null");
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    break;

                case 2:
                    int progress = (int) msg.obj;
                    progressDialog.setProgress(progress);
                    break;
            }
        }
    };

    public void init(Activity activity) {
        this.activity = activity;
        Intent intent = new Intent(activity, DownloadService.class);
        LogUtil.d(TAG, "开启服务");
        activity.startService(intent);
        activity.getApplicationContext().bindService(intent, mConnection, activity.BIND_AUTO_CREATE);//绑定服务
    }

    private void startCheckProgress(final long downloadId) {
        new Thread(new Runnable() {
            int progress = 0;

            @Override
            public void run() {
                while (progress < 100) {
                    progress = mDownloadBinder.getProgress(downloadId);
                    Message msg = handler.obtainMessage();
                    msg.what = 2;
                    msg.obj = progress;
                    handler.sendMessage(msg);
                }
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadBinder = null;
        }
    };

    public void downLoadApk() {
        if (mDownloadBinder != null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("正在下载新版本");
            progressDialog.setCancelable(false);
            progressDialog.show();

            downloadId = mDownloadBinder.startDownload(GameConfig.APK_URL);
            LogUtil.d(TAG, GameConfig.APK_URL);
            startCheckProgress(downloadId);
        }
    }
}
