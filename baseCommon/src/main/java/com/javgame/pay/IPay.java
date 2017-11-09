package com.javgame.pay;


public interface IPay {

	/**
	 * 请求订单时需要的参数，由web服务端定义
	 * @return
	 */
	String getOrderKey();
	
	
	/**
	 * unity调用此支付接口，必须实现此接口才能支付
	 * @param order  关于订单的参数
	 */
	void pay(String data);

}
