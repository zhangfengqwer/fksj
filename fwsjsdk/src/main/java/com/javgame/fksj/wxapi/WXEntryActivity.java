package com.javgame.fksj.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.javgame.fksj.GameConfig;
import com.javgame.fksj.Login;
import com.javgame.login.UserSdk;
import com.javgame.utility.CommonUtils;
import com.javgame.utility.Constants;
import com.javgame.utility.LogUtil;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/11/6
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private String nickname;
    private String unionid;
    private static HashMap<String, String> map;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Login.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtil.d(Constants.TAG, "req" + baseReq.openId);
    }

    @Override
    public void onResp(final BaseResp resp) {

        int errorCode = resp.errCode;
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                String code = ((SendAuth.Resp) resp).code;
                LogUtil.d(Constants.TAG, "code:" + code + "type:" + resp.getType() + "state:" + ((SendAuth.Resp) resp).state);

                OkHttpUtils
                        .get()
                        .url(GameConfig.WX_ACCESS_TOKEN_URL)
                        .addParams("appid", GameConfig.WX_APP_ID)
                        .addParams("secret", GameConfig.WX_APP_SECRET)
                        .addParams("code", code)
                        .addParams("grant_type", "authorization_code")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                            }

                            @Override
                            public void onResponse(String response) {
                                LogUtil.d(Constants.TAG, "response:" + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String access_token = jsonObject.getString("access_token");
                                    String openid = jsonObject.getString("openid");
                                    OkHttpUtils
                                            .get()
                                            .url(GameConfig.WX_USERINFO_URL)
                                            .addParams("access_token", access_token)
                                            .addParams("openid", openid)
                                            .build()
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onError(Request request, Exception e) {

                                                }

                                                @Override
                                                public void onResponse(String response) {
                                                    LogUtil.d(Constants.TAG, response);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        nickname = jsonObject.getString("nickname");
                                                        unionid = jsonObject.getString("unionid");
                                                        handlerResult();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
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
        finish();
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
