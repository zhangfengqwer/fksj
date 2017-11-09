package com.javgame.pay;

public class OrderResponse {
    
    /**
     *   状态 0-异常,  1-正常
     */
    private int status;  
    
	/**
     * 正常返回订单ID，异常返回异常信息
     */
    private String  msg;
    
    /**
     * 扩展信息，自定义
     * 1、传给通知接口的验证串，Extra
     * 2、prepay_id（微信支付使用）
     */
    private String  expand;
    
    
    /**
     * 根据状态判断返回内容
     * @return
     */
    public String getErrorMessage() {
        return status == 0 ? msg : "";
   }
   
    /**
     * 根据状态判断返回订单id
     * @return
     */
   public String getOrderID() {
        return status == 1 ? msg : "";
   }
   
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getExpand() {
		return expand;
	}
	public void setExpand(String expand) {
		this.expand = expand;
	}
	
    
}
