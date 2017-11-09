package com.javgame.pay;

import java.io.Serializable;


public class Order implements Serializable {
    
    
    /**
     * 
     */
    public int sortid;

    /**
     * 充值渠道id
     */
    public int codepayid;


    /**
     * 游戏id
     */
    public int gameid;


    /**
     * 充值项id
     */
    public int itemid;


    /**
     * 商品名称
     */
    public String name;


    /**
     * 商品价格
     */
    public int price;


    /**
     * 是否热门
     */
    public boolean hot;


    /**
     * 汇率 ，一元可以换多少游戏币
     */
    public String rate;
    
    
    /**
     *  产品ID ，一般用作 购买点ID 
     */
    public String productID ;
    
    /**
     * 用户 ID
     */
    public String uid ;


    /**
     * 购买数量
     */
    public int count;
    
    /**
     *  没有解析的商品信息字符串 ，需要解析后才能用
     */
    public String orderInfo ;
    
    /**
     * 订单ID（服务器自己生成的）
     */
    public String  orderID ;

    /**
     * 支付类型字符串
     */
    public String payTypeString ;

    /**
     * 订单注释，额外信息
     */
    public String remark;

    
    /**
     * 订单扩展信息
     */
    public String expand;
    


    /**
     * @return 取订单Url 格式 
     */
    public String getOrderPara() {
        return "?systype=android&paytype=" + payTypeString + "&uid=" + uid + "&sortid=" + sortid
                + "&codepayid=" + codepayid + "&gameid=" + gameid + "&itemid=" + itemid
                + "&chargecount=" + 1 + "&amount=" + price;
    }
    
    /**
     * @return 新的，简化后的取订单Url 格式 。
     */
    public String getOrderPara2() {
        return  "?uid="      +uid+
                "&itemid="      +itemid+
                "&chargecount="   +1+
                "&amount="       +price  ;
    }

    
    
    /**
     * @return 通知服务器购买成功格式
     */
    public String getNotifyPara() {
        return   "?systype=android&paytype="+payTypeString+ 
                "&sign=" ;

    }

    public String getExtInfo() {
        return orderID;
    }

}
