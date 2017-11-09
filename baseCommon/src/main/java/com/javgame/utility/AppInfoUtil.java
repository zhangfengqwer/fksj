package com.javgame.utility;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author czq
 * 提供系统消息供debug 
 */
public class AppInfoUtil {
    
	
	 /**
		 * 获取版本名称
		 */
		public static String getVersionName(Context context) {
			String versionName = "";
			try {
				versionName = context.getPackageManager().getPackageInfo(
						context.getApplicationInfo().packageName, 0).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return versionName;
		}
		
		/**
		 * 获取versionCode
		 * 
		 * @return
		 */
		public static int getVersionCode(Context context) {
			int versionCode = 0;
			try {
				versionCode = context.getPackageManager().getPackageInfo(
						context.getApplicationInfo().packageName, 0).versionCode;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return versionCode;
		}
		
		
		/**
		 * 获取包名
		 * @param activity
		 * @return
		 */
		public static String getPackageName(Context activity) {
	        return  activity.getApplicationInfo().packageName;
	    }
		
		public static String getChannelName(Context activity) {
	        return AndroidUtil.getStringResource(activity, "channelName");
	    }
		
		public static int getGameID(Activity activity) {
	        return AndroidUtil.getIntMataData(activity, "javGameID");
	    }
		
		/**
		 * 获取单个支付类型，如果有2种类型以上，则返回空,只有单个支付类型才会要求接入账号
		 * @param activity
		 * @return
		 */
		public static String getPayType(Activity activity){
			String[] payArray = AndroidUtil.getStringArray(activity, "payType");
			if(payArray.length == 1){
				String  payString = payArray[0];
				String[] array = payString.split(",");
				return array[1];
			}
			return "";
		}
		
	/**
	 * 检测是否安装微信
	 * @param activity
	 * @return
	 */
	public static boolean hasInstallWXApp(Activity activity){
		try {
			if (activity != null) {
				String packageName = "com.tencent.mm";
				ApplicationInfo info = activity.getPackageManager()
						.getApplicationInfo(packageName,0);
				if (info != null)
					return true;
			}
		} catch (NameNotFoundException e) {
			Log.e("AppInfoUtil", "cann't find package name -- com.tencent.mm");
		}
		return false;
		
	}
	
	
	
    public static void showAppInfo(Activity activity,String unityInfo) {
        Builder builder = new Builder(activity);
        builder.setMessage(getAppInfo(activity,unityInfo));
        builder.setPositiveButton("ok", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    
 public static String getAppInfo(Context context ,String unityInfo) {
     StringBuffer sb = new StringBuffer();
     try {
         appendInfo(sb ,"UnityInfo" , "");
         sb.append(unityInfo);
         appendInfo(sb ,"AndroidInfo" , "");
         String packageName = context.getPackageName();
         PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                 packageName, 0);
        
         appendInfo(sb ,"versionName" , packageInfo.versionName);
         appendInfo(sb ,"versionCode" , ""+packageInfo.versionCode);
         appendInfo(sb ,"package" , packageName);
         appendInfo(sb ,"channelName" ,  getChannelName(context));
         appendInfo(sb ,"updateFile" ,  AndroidUtil.getStringResource(context,"update_file"));
         sb.append("\n");
         appendInfo(sb ,"MetaData" , "");
         ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
         sb.append(applicationInfo.metaData.toString());
         Bundle bundle = applicationInfo.metaData ;
         Set<String> keySet = bundle.keySet();
         for (String key : keySet) {   
             appendInfo(sb ,key, bundle.get(key).toString());      
         } 
         sb.append("\n");
         appendInfo(sb ,"BuilderInfo" , "");
         Field[] fields = Build.class.getDeclaredFields();   
         for (Field field : fields) {   
             field.setAccessible(true);   
             appendInfo(sb ,field.getName(), field.get(null).toString());      
         } 
         
     } catch (Exception e) {
         e.printStackTrace();
     }
     return sb.toString();
 }

 private static void appendInfo(StringBuffer sb, String name, String value) {
     sb.append(name);
     sb.append(": ");
     sb.append(value);
     sb.append("\n");
 }
}
