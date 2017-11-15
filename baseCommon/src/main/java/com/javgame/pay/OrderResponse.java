package com.javgame.pay;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {

	/**
	 * code : 1
	 * message :
	 * data : {"appId":"wxa2c2802e8fedd592","noncestr":"56b4a9e63eee4305a810c3498b33526a","package":"Sign=WXPay","partnerid":"1491388672","prepayid":"wx20171114140045ab81876d6a0019750920","sign":"0ddfbd5a38aba5f3c5a045a7d2775744","timestamp":"1510639235","trade_no":"10014"}
	 */

	private int code;
	private String message;
	private DataBean data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * appId : wxa2c2802e8fedd592
		 * noncestr : 56b4a9e63eee4305a810c3498b33526a
		 * package : Sign=WXPay
		 * partnerid : 1491388672
		 * prepayid : wx20171114140045ab81876d6a0019750920
		 * sign : 0ddfbd5a38aba5f3c5a045a7d2775744
		 * timestamp : 1510639235
		 * trade_no : 10014
		 */

		private String appId;
		private String noncestr;
		@SerializedName("package")
		private String packageX;
		private String partnerid;
		private String prepayid;
		private String sign;
		private String timestamp;
		private String trade_no;

		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		public String getNoncestr() {
			return noncestr;
		}

		public void setNoncestr(String noncestr) {
			this.noncestr = noncestr;
		}

		public String getPackageX() {
			return packageX;
		}

		public void setPackageX(String packageX) {
			this.packageX = packageX;
		}

		public String getPartnerid() {
			return partnerid;
		}

		public void setPartnerid(String partnerid) {
			this.partnerid = partnerid;
		}

		public String getPrepayid() {
			return prepayid;
		}

		public void setPrepayid(String prepayid) {
			this.prepayid = prepayid;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getTrade_no() {
			return trade_no;
		}

		public void setTrade_no(String trade_no) {
			this.trade_no = trade_no;
		}
	}
}
