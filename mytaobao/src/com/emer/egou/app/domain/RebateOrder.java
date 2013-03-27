package com.emer.egou.app.domain;

import java.io.Serializable;
/**
 * 
 * 用户的返利订单
 */
public class RebateOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7967789609601418506L;
	/**订单号*/
	private String orderNo;
	/**商品金额*/
	private String price;
	/**返利额*/
	private String rebate;
	/**订单状态    正在审核    已发放     ...*/
	private String state;
	/**下单时间*/
	private String orderTime;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getRebate() {
		return rebate;
	}
	public void setRebate(String rebate) {
		this.rebate = rebate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public RebateOrder(String orderNo, String price, String rebate,
			String state, String orderTime) {
		super();
		this.orderNo = orderNo;
		this.price = price;
		this.rebate = rebate;
		this.state = state;
		this.orderTime = orderTime;
	}
	public RebateOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
