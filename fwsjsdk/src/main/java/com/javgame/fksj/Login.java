package com.javgame.fksj;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.javgame.Integration.IActivityListener;
import com.javgame.login.IUser;
import com.javgame.login.UserSdk;
import com.javgame.utility.CommonUtils;
import com.javgame.utility.GameConfig;
import com.javgame.utility.LogUtil;
import com.javgame.weixin.WXShare;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public class Login implements IUser, IActivityListener {

    public IWXAPI wxApi;
    private UserInfo mInfo;
    private Tencent mTencent;
    private static String token;
    private static String expires;
    private static String openId;
    private String nickname;
    private String figureurl;
    private static HashMap<String, String> map;

    private Activity getActivity() {
        return UserSdk.getInstance().getActivity();
    }

    @Override
    public void onCreate() {
        mTencent = Tencent.createInstance(GameConfig.QQ_APP_ID, getActivity());

        //注册微信到app中
        wxApi = WXShare.getInstance().getWXApi();
    }


    @Override
    public void login(String data) {

        if ("weixin".equals(data)) {
            LogUtil.d(TAG, "点击wexin");
            send2Wx();
        } else if ("qq".equals(data)) {
            LogUtil.d(TAG, "点击qq");
            doQQLogin();
        }
    }

    private void send2Wx() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_tlj";
        wxApi.sendReq(req);
    }

    private void doQQLogin() {
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
            LogUtil.d(TAG, "设置openid");
            UserSdk.getInstance().loginResult(new JSONObject(map).toString());
            return;
        }
        mTencent.login(getActivity(), "all", loginListener);
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                CommonUtils.showToast(getActivity(), "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                CommonUtils.showToast(getActivity(), "登录失败");
                return;
            }
            CommonUtils.showToast(getActivity(), "登录成功");
            doComplete(jsonResponse);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            LogUtil.e(TAG, "error:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            CommonUtils.showToast(getActivity(), "取消登录");
        }
    }

    public IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d(TAG, "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            Log.d(TAG, values.toString());
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };

    private void handlerResult() {
        map = new HashMap<>();
        map.put("appid", GameConfig.QQ_APP_ID);
        map.put("code", openId);
        map.put("nickname", nickname);
        map.put("platform", 101 + "");
        map.put("figureurl", figureurl);
        Log.d(TAG, new JSONObject(map).toString());
        UserSdk.getInstance().loginResult(new JSONObject(map).toString());
    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    private void updateUserInfo() {
        mInfo = new UserInfo(getActivity(), mTencent.getQQToken());
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject jsonObject = (JSONObject) o;
                try {
                    nickname = jsonObject.getString("nickname");
                    figureurl = jsonObject.getString("figureurl_qq_2");
                    handlerResult();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, o.toString());
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }


    @Override
    public void onPause() {
        LogUtil.d(TAG, "onPause");
        UnityPlayer.UnitySendMessage("AndroidCallBack", "OnPauseCallBack", "");
    }

    @Override
    public void onStart() {
        LogUtil.d(TAG, "onStart");
    }

    @Override
    public void onRestart() {
        LogUtil.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        LogUtil.d(TAG, "onResume");
        UnityPlayer.UnitySendMessage("AndroidCallBack", "OnResumeCallBack", "");
    }

    @Override
    public void onStop() {
        LogUtil.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }

    @Override
    public void logout() {

    }

    @Override
    public void setLoginResponse(String data) {

    }
}
