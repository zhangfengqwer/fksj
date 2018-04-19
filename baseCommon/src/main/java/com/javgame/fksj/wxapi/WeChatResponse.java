package com.javgame.fksj.wxapi;

/**
 * @author zhangf
 * @date 2018/1/2
 */

public final class WeChatResponse {

    /**
     * code : 1
     * name : 张峰
     * nick : 张峰
     * tId : o_6Wn0Zuj36JXiMq75TdpvsUKzWM
     * url : http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eofpg5atgccSZJcJic85QRJS3vLc3w19EIhjfapt9MEnQUHiaSk4WyQYf1MnRP5Dc0CL0NOqrYP607g/132
     * tgId : null
     * msg : null
     * expand : {"unionid":"odLBW1SQoLN2yGyixMn660yZacZw","access_token":"5_ajNP324VyojbmxULrZGQPUBYmAYGBco9JAbP9D9J8aE6TucDnYYxKcArsGQl8w-4ZDSVgehsAhC03YYjT8hmcoV40FjeaDumIlCyP6C81TA","refresh_token":"5_apWS4NTv3UJwRITPcSSLfcBovX50YLMqHM6K3ygT7jCHRJfcj9eLaiQ4K-ovrJUpCDTVdzR8LP4N8vF0Nf2lkYB8i4yasgdH95ZlEbCRPU0"}
     */

    private int code;
    private String name;
    private String nick;
    private String tId;
    private String url;
    private Object tgId;
    private Object msg;
    private ExpandBean expand;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTId() {
        return tId;
    }

    public void setTId(String tId) {
        this.tId = tId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getTgId() {
        return tgId;
    }

    public void setTgId(Object tgId) {
        this.tgId = tgId;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public ExpandBean getExpand() {
        return expand;
    }

    public void setExpand(ExpandBean expand) {
        this.expand = expand;
    }

    public static class ExpandBean {
        /**
         * unionid : odLBW1SQoLN2yGyixMn660yZacZw
         * access_token : 5_ajNP324VyojbmxULrZGQPUBYmAYGBco9JAbP9D9J8aE6TucDnYYxKcArsGQl8w-4ZDSVgehsAhC03YYjT8hmcoV40FjeaDumIlCyP6C81TA
         * refresh_token : 5_apWS4NTv3UJwRITPcSSLfcBovX50YLMqHM6K3ygT7jCHRJfcj9eLaiQ4K-ovrJUpCDTVdzR8LP4N8vF0Nf2lkYB8i4yasgdH95ZlEbCRPU0
         */

        private String unionid;
        private String access_token;
        private String refresh_token;

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }
    }
}
