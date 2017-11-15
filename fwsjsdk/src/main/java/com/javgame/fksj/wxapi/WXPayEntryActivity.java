package com.javgame.fksj.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.javgame.utility.LogUtil;
import com.javgame.weixin.WXShare;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import static com.javgame.utility.Constants.TAG;

/**
 * @author zhangf
 * @date 2017/11/14
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI wxApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxApi = WXShare.getInstance().getWXApi();
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

    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.d(TAG,"pay返回type:{0}",resp.getType());
        finish();
    }
}
