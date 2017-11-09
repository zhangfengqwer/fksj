package com.javgame.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author czq
 * android 工具类 ，供 其他android 类调用
 * 供unity 调用的类 在 UnityHelper
 * 由于共用包，所以不能直接用 R 类 访问资源 ，此类提供 多个不用R类 获取资源 的方法
 */

/**
 * @author apple
 */
public class AndroidUtil {


    public static String TAG = AndroidUtil.class.getSimpleName();


    /**
     * 判断是否sim卡正常
     *
     * @param context
     * @return
     */
    @SuppressLint("ServiceCast")
    public static boolean hasSimCard(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        return TelephonyManager.SIM_STATE_READY == manager.getSimState();
    }

    /**
     * 获取字符串 ，不用R类 获取资源
     */
    public static String getStringResource(Context activity, String name) {
        int id = activity.getResources().getIdentifier(name, "string",
                activity.getPackageName());
        if (id == 0) {
            Log.e(TAG, " Not found string resource " + name);
            return "";
        }
        String value = activity.getResources().getString(id);
        if (value != null) {
            value = value.trim();
        }
        return value;
    }

    /**
     * 获取字符串
     *
     * @param name
     * @return
     */
    public static int getStringResourceID(Context activity, String name) {
        int id = activity.getResources().getIdentifier(name, "string",
                activity.getPackageName());
        return id;
    }

    /**
     * 获取字符串数组资源id
     *
     * @param activity
     * @param name
     * @return
     */
    public static int getStringArrayResourceID(Context activity, String name) {
        Resources res = activity.getResources();
        int id = res.getIdentifier(name, "array", activity.getPackageName());
        return id;
    }

    /**
     * 获取字符串数组
     *
     * @param activity
     * @param name
     * @return
     */
    public static String[] getStringArray(Context activity, String name) {
        Resources res = activity.getResources();
        int id = res.getIdentifier(name, "array", activity.getPackageName());
        if (id == 0) return new String[0];
        return res.getStringArray(id);
    }


    /**
     * 获取Drawable
     *
     * @param name
     * @return
     */
    public static Drawable getDrawableResource(Context activity, String name) {
        int id = activity.getResources().getIdentifier(name, "javgame/com/fwsj/drawable",
                activity.getPackageName());
        if (id == 0) return null;
        return activity.getResources().getDrawable(id);
    }

    /**
     * 获取DrawableID
     *
     * @param name
     * @return
     */
    public static int getDrawableResourceID(Context activity, String name) {
        int id = activity.getResources().getIdentifier(name, "javgame/com/fwsj/drawable",
                activity.getPackageName());
        return id;
    }


    public static String encode(String string) {
        try {
            return URLEncoder.encode(string, "Utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string;
        }
    }


    public static int getIntMataData(Activity activity, String name) {
        try {
            ApplicationInfo appInfo = activity.getPackageManager()
                    .getApplicationInfo(activity.getPackageName(),
                            PackageManager.GET_META_DATA);
            int value = appInfo.metaData.getInt(name);
            return value;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.toString(), e);
            return 0;
        }
    }

    public static String getMataData(Activity activity, String name) {
        try {
            ApplicationInfo appInfo = activity.getPackageManager()
                    .getApplicationInfo(activity.getPackageName(),
                            PackageManager.GET_META_DATA);
            String value = appInfo.metaData.getString(name);
            return value;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.toString(), e);
            return "";
        }
    }

    public static String getAppName(Activity activity) {
        return activity.getString(activity.getApplicationInfo().labelRes);
    }


    public static boolean isCustomChannel(Context activity, String channelNameListKey) {
        String channelList = getStringResource(activity, channelNameListKey);
        if (TextUtils.isEmpty(channelList)) return false;
        String name = AppInfoUtil.getChannelName(activity);
        if (TextUtils.isEmpty(name)) return false;
        name = name.trim();
        String[] channels = channelList.split(",");
        for (String data : channels) {
            if (data.trim().equals(name)) {
                return true;
            }
        }

        return false;
    }
}
