package com.tao8.app.domain;

import java.io.Serializable;
/**
 * 淘宝折扣客
 * @author Administrator
 *
 */
public class TaobaokeCouponItem extends SearchItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6551292721271868662L;
	/**
	 * 
	 */
	/**折扣比率*/
	private String coupon_rate;
	/**折扣价格*/
	private String coupon_price;
	/**折扣活动开始时间*/
	private String coupon_start_time;
	/**coupon_end_time*/
	private String coupon_end_time;
	/**店铺类型:B(商城),C(集市)*/
	private String shop_type;
	public String getCoupon_rate() {
		return coupon_rate;
	}
	public void setCoupon_rate(String coupon_rate) {
		this.coupon_rate = coupon_rate;
	}
	public String getCoupon_price() {
		return coupon_price;
	}
	public void setCoupon_price(String coupon_price) {
		this.coupon_price = coupon_price;
	}
	public String getCoupon_start_time() {
		return coupon_start_time;
	}
	public void setCoupon_start_time(String coupon_start_time) {
		this.coupon_start_time = coupon_start_time;
	}
	public String getCoupon_end_time() {
		return coupon_end_time;
	}
	public void setCoupon_end_time(String coupon_end_time) {
		this.coupon_end_time = coupon_end_time;
	}
	public String getShop_type() {
		return shop_type;
	}
	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
	}
	
	
	
}
