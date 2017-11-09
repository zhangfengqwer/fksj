package com.javgame.login;

public interface IUser {

	
	/**
	 * unity调用，如果需要第三方登录，需实现此接口
	 * @param data
	 */
	void login(String data);
	
	
	/**
	 * 此方法被调用时间：
	 * 1、unity切换账号
	 * 2、收到第三方切换账号的通知然后跳转到start界面
	 */
	void logout();
	
	/**
	 * 登录成功后unity返回给android的参数
	 */
	void setLoginResponse(String data);
	
}
