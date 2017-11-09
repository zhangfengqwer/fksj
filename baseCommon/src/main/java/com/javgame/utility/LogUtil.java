package com.javgame.utility;

import android.util.Log;

import java.text.MessageFormat;

public class LogUtil {

	private static int DEBUG_LOG = 1;
	private static final int INFO = DEBUG_LOG;
	private static final int DEBUG = DEBUG_LOG;
	private static final int WARNING = DEBUG_LOG;
	private static final int VERBOSE = DEBUG_LOG;
	private static final int ERROR = DEBUG_LOG;
	
	public static void i(String tag,String msg,Object... params){
		if(INFO > 0){
			Log.i(tag, MessageFormat.format(msg, params));
		}
	}
	
	public static void d(String tag,String msg,Object... params){
		if(DEBUG  > 0){
			Log.d(tag, MessageFormat.format(msg, params));
		}
	}

	public static void i(String tag,String msg){
		if(INFO > 0){
			Log.i(tag, msg);
		}
	}
	
    public static void d(String tag,String msg){
		if(DEBUG  > 0){
			Log.d(tag, msg);
		}
	}
	
	public static void v(String tag,String msg){
		if(VERBOSE > 0){
			Log.v(tag, msg);
		}
	}
	
	public static void e(String tag,String msg){
		if(ERROR > 0){
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag,String msg,Throwable t){
		if(ERROR > 0){
			Log.e(tag, msg, t);
		}
	}
	
	public static void w(String tag,String msg){
		if(WARNING > 0){
			Log.w(tag, msg);
		}
	}
	
	public static void w(String tag,Throwable t){
		if(WARNING > 0){
			Log.w(tag, t);
		}
	}
	
	
}
