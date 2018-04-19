package com.javgame.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * @author zhangf
 * @date 2017/12/26
 */

public class InstallUtil {
    /**
     *
     * @param context
     * @param apkPath 要安装的APK
     * @param rootMode 是否是Root模式
     */
    public static void install(Context context, String apkPath, boolean rootMode){
        if (rootMode){
            installRoot(context,apkPath);
        }else {
            installNormal(context,apkPath);
        }
    }

    /**
     * 通过非Root模式安装
     * @param context
     * @param apkPath
     */
    public static void install(Context context,String apkPath){
        install(context,apkPath,false);
    }

    //普通安装
    private static void installNormal(Context context,String apkPath) {


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                    "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    //通过Root方式安装
    private static void installRoot(Context context, String apkPath) {

    }
}

