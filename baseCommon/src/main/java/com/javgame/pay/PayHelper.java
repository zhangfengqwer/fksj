/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.javgame.pay;


import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.javgame.utility.AndroidUtil;
import com.javgame.utility.AppInfoUtil;
import com.javgame.utility.XMLhelper;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 * 
 */
public class PayHelper {

    public static final int PAY_RESULT = 5;
    public static final int GET_ORDER = 6;
    public static final int PAY_NOTIFY = 7;
    public static final int LOGIN_RESULT = 14;
    public static final int QUIT_RESULT = 15;
    public static final int LOGOUT_RESULT = 16;
	public static final String Codepayid = "codepayid";
	public static final String Systype = "systype";
	public static final String Paytype = "paytype";
	public static final String OrderID = "orderid";
	public static final String ALI_TYPE = "alipay";
	public static final String Anzhi_TYPE = "anzhi";
	public static final String Android = "android";
	public static final String Status = "status";
    public static String amount = "amount";
	public static final  boolean DEBUG = true ;
	
	private static final String TAG = PayHelper.class.getSimpleName();


    private static HashMap<String, String> parserString(String orderInfo) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            URL url = new URL("http://www.pay.com/test.asp?" + orderInfo);
            List<NameValuePair> valus = URLEncodedUtils.parse(url.toURI(), "utf-8");
            for (NameValuePair nameValuePair : valus) {
                map.put(nameValuePair.getName(), nameValuePair.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Order parserOrder(String orderInfo , String payTypeString) {
        Order order = new Order();
        HashMap<String, String> map =  PayHelper.parserString(orderInfo);
        Log.d(TAG," parserOrder map= " + map);
        order.price =Integer.parseInt(map.get("price"));
        order.name = map.get("name");
        order.codepayid = Integer.parseInt(map.get("codepayid"));
        order.gameid =  Integer.parseInt(map.get("gameid"));
        order.sortid =  Integer.parseInt(map.get("sortid"));
        order.itemid =  Integer.parseInt(map.get("itemid"));
        order.uid =  map.get("uid");
        parserSubinfo(order ,map.get("subinfo"));
        Log.d(TAG," pay productID " + order.productID);
        order.payTypeString = payTypeString;
        return order ;
    }



    private static void parserSubinfo(Order order, String string) {
        try {
            if (TextUtils.isEmpty(string)){
                return ;
            }
            string = URLDecoder.decode(string,"utf-8");
            JSONObject jsonObject =  new JSONObject( string);
            order.productID = jsonObject.optString("productid");
        } catch (Exception e) {
            Log.e(TAG," pay parserSubinfo "+ e.toString() ,e);
        }
    }


    public static OrderResponse parserOrderResponse(String response, Order order) {
        OrderResponse orderResponse = XMLhelper.parserOrderResponse(response);
        order.orderID = orderResponse.getOrderID();
        order.expand = orderResponse.getExpand();
        return orderResponse ;
    }
	

    public static String encode(String string) {
        try {
            return URLEncoder.encode(string,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string ;
        }
    }

    /**
	 * 获取 订单的额外参数 ，现在主要是传入版本号和手机信号提供跟踪功能
	 * 
	 * @return
	 */
	public static String getOrderExpand(Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", AppInfoUtil.getVersionName(context));
		map.put("phonemodule", Build.MODEL);
		JSONObject jSONObject = new JSONObject(map);
		String jsonString = jSONObject.toString();
		String jsonStringExp = "&expand="+ AndroidUtil.encode(jsonString);
		Log.d(TAG, " jsonStringExp " + jsonStringExp);
		return jsonStringExp;
	}

    

}