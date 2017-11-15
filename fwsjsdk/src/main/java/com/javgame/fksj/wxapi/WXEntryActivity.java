package com.javgame.fksj.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.javgame.login.UserSdk;
import com.javgame.utility.CommonUtils;
import com.javgame.utility.Constants;
import com.javgame.utility.GameConfig;
import com.javgame.utility.LogUtil;
import com.javgame.weixin.WXShare;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/11/6
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private String nickname;
    private String unionid;
    private static HashMap<String, String> map;
    private IWXAPI wxApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxApi = WXAPIFactory.createWXAPI(this, GameConfig.WX_APP_ID, false);
        wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtil.d(Constants.TAG, "req" + baseReq.openId);
    }

    @Override
    public void onResp(final BaseResp resp) {
        LogUtil.d(Constants.TAG, "返回：" + "Type:" + resp.getType() + "\nstate:" + resp.errCode);

        //登录返回
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            loginHandler(resp);
        }
        //分享返回
        else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {

        }

        finish();
    }

    private void payHandler(BaseResp resp) {
        int errorCode = resp.errCode;
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                break;
            default:
                break;
        }
    }

    private void loginHandler(BaseResp resp) {
        int errorCode = resp.errCode;
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                String code = ((SendAuth.Resp) resp).code;

                Map<String, String> map = new HashMap<>();
                map.put("gameId", GameConfig.GAME_ID);
                map.put("appId", GameConfig.WX_APP_ID);
                map.put("code", code);
                map.put("openId", "");
                OkHttpUtils
                        .postString()
                        .url(GameConfig.WECHAT_LOGIN_URL)
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .content(new JSONObject(map).toString())
                        .build()
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Request request, Exception e) {
                                LogUtil.d(TAG, "onError" + request.toString() + "\ne" + e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                LogUtil.d(TAG, "onResponse" + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int code = jsonObject.getInt("code");
                                    if (code != 1) {
                                        CommonUtils.showToast(WXEntryActivity.this, "微信登录web返回失败");
                                        return;
                                    }
                                    String data = jsonObject.getString("data");
                                    JSONObject dataJson = new JSONObject(data);
                                    nickname = dataJson.getString("name");
                                    unionid = dataJson.getString("expand");
                                    handlerResult();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                break;
            default:
                break;
        }
    }

    private void handlerResult() {
        CommonUtils.showToast(UserSdk.getInstance().getActivity(), "登录成功");
        map = new HashMap<>();
        map.put("appid", GameConfig.WX_APP_ID);
        map.put("code", unionid);
        map.put("nickname", nickname);
        map.put("platform", 102 + "");
        map.put("figureurl", "");
        Log.d(TAG, new JSONObject(map).toString());
        UserSdk.getInstance().loginResult(new JSONObject(map).toString());

    }
}
