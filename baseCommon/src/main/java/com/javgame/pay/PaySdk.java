package com.javgame.pay;

import android.app.Activity;

import com.javgame.Integration.ComponentFactory;
import com.javgame.utility.LogUtil;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public class PaySdk {

    private static PaySdk paySdk;
    private Activity mContext;

    /**
     * unity回调对象
     */
    public String objCallBack;
    /**
     * unity回调方法
     */
    public String funCallBack;

    private String shareFriendsCallObj;
    private String shareFriendsCallFunc;


    private IPay mPay;
    private IWXShare mWxShare;

    public static PaySdk getInstance(){
        if(paySdk == null){
            paySdk = new PaySdk();
        }
        return paySdk;
    }

    public void Init(Activity act){
        mContext = act;
        Object obj = getPayObject();
        if(obj != null){
            if (obj instanceof IPay) {
                mPay = (IPay) obj;
                LogUtil.i(TAG, "IPay create");
            }
            if(obj instanceof IWXShare){
                mWxShare = (IWXShare) obj;
                LogUtil.i(TAG, "IWXShare create");
            }
        }
    }

    public Object getPayObject(){
        return ComponentFactory.getInstance().getPay();
    }

    public void pay(String callObj, String callFunc,String data) {
        if (mPay != null) {
            objCallBack = callObj;
            funCallBack = callFunc;
            mPay.pay(data);
        }
    }

    public void wxShareFriends(String callObj, String callFunc, String data) {
        if(mWxShare !=null){
            shareFriendsCallObj = callObj;
            shareFriendsCallFunc = callFunc;
            mWxShare.shareFriend(data);
        }
    }
}
