//package com.tao8.app.domain;
//
//import java.io.Serializable;
//
///**
// * 淘宝折扣客
// * 
// * @author Administrator
// * 
// */
//public class TaobaokeCouponItem  implements Serializable {
//
//	/**
//	 * 
//	 */
//	public static final long serialVersionUID = -6551292721271868662L;
//	/**
//	 * 
//	 */
//	/**
//	 * num_iid,title,nick,pic_url,price,click_url,commission,
//	 * commission_rate,commission_num,
//	 * commission_volume,shop_click_url,seller_credit_score,item_location,volume
//	 */
//	/** 淘宝客商品数字id */
//	public String num_iid;
//	/** 商品title 宝贝名称 */
//	public String title;
//	/** 卖家昵称 */
//	public String nick;
//	/** 图片url */
//	public String pic_url;
//	/** 商品价格 */
//	public String price;
//	/** 推广点击url */
//	public String click_url;
//	/** 淘宝客佣金 */
//	public String commission;
//	/** 淘宝客佣金比率，比如：1234.00代表12.34% */
//	public String commission_rate;
//	/** 累计成交量.注：返回的数据是30天内累计推广量 */
//	public String commission_num;
//	/** 累计总支出佣金量 */
//	public String commission_volume;
//	/** 商品所在店铺的推广点击url */
//	public String shop_click_url;
//	/** 卖家信用等级 */
//	public String seller_credit_score;
//	/** 商品所在地 */
//	public String item_location;
//	/**30天内交易量*/
//	public String volume;
//	
//	
//	
//	
//	public String getVolume() {
//		return volume;
//	}
//	public void setVolume(String volume) {
//		this.volume = volume;
//	}
//	/** 折扣比率 */
//	public String coupon_rate;
//	/** 折扣价格 */
//	public String coupon_price;
//	/** 折扣活动开始时间 */
//	public String coupon_start_time;
//	/** coupon_end_time */
//	public String coupon_end_time;
//	/** 店铺类型:B(商城),C(集市) */
//	public String shop_type;
//	
//	public String getNum_iid() {
//		return num_iid;
//	}
//	public void setNum_iid(String num_iid) {
//		this.num_iid = num_iid;
//	}
//	public String getTitle() {
//		return title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
//	public String getNick() {
//		return nick;
//	}
//	public void setNick(String nick) {
//		this.nick = nick;
//	}
//	public String getPic_url() {
//		return pic_url;
//	}
//	public void setPic_url(String pic_url) {
//		this.pic_url = pic_url;
//	}
//	public String getPrice() {
//		return price;
//	}
//	public void setPrice(String price) {
//		this.price = price;
//	}
//	public String getClick_url() {
//		return click_url;
//	}
//	public void setClick_url(String click_url) {
//		this.click_url = click_url;
//	}
//	public String getCommission() {
//		return commission;
//	}
//	public void setCommission(String commission) {
//		this.commission = commission;
//	}
//	public String getCommission_rate() {
//		return commission_rate;
//	}
//	public void setCommission_rate(String commission_rate) {
//		this.commission_rate = commission_rate;
//	}
//	public String getCommission_num() {
//		return commission_num;
//	}
//	public void setCommission_num(String commission_num) {
//		this.commission_num = commission_num;
//	}
//	public String getCommission_volume() {
//		return commission_volume;
//	}
//	public void setCommission_volume(String commission_volume) {
//		this.commission_volume = commission_volume;
//	}
//	public String getShop_click_url() {
//		return shop_click_url;
//	}
//	public void setShop_click_url(String shop_click_url) {
//		this.shop_click_url = shop_click_url;
//	}
//	public String getSeller_credit_score() {
//		return seller_credit_score;
//	}
//	public void setSeller_credit_score(String seller_credit_score) {
//		this.seller_credit_score = seller_credit_score;
//	}
//	public String getItem_location() {
//		return item_location;
//	}
//	public void setItem_location(String item_location) {
//		this.item_location = item_location;
//	}
//	public String getCoupon_rate() {
//		return coupon_rate;
//	}
//	public void setCoupon_rate(String coupon_rate) {
//		this.coupon_rate = coupon_rate;
//	}
//	public String getCoupon_price() {
//		return coupon_price;
//	}
//	public void setCoupon_price(String coupon_price) {
//		this.coupon_price = coupon_price;
//	}
//	public String getCoupon_start_time() {
//		return coupon_start_time;
//	}
//	public void setCoupon_start_time(String coupon_start_time) {
//		this.coupon_start_time = coupon_start_time;
//	}
//	public String getCoupon_end_time() {
//		return coupon_end_time;
//	}
//	public void setCoupon_end_time(String coupon_end_time) {
//		this.coupon_end_time = coupon_end_time;
//	}
//	public String getShop_type() {
//		return shop_type;
//	}
//	public void setShop_type(String shop_type) {
//		this.shop_type = shop_type;
//	}
//	@Override
//	public String toString() {
//		return "TaobaokeCouponItem [num_iid=" + num_iid + ", title=" + title
//				+ ", nick=" + nick + ", pic_url=" + pic_url + ", price="
//				+ price + ", click_url=" + click_url + ", commission="
//				+ commission + ", commission_rate=" + commission_rate
//				+ ", commission_num=" + commission_num + ", commission_volume="
//				+ commission_volume + ", shop_click_url=" + shop_click_url
//				+ ", seller_credit_score=" + seller_credit_score
//				+ ", item_location=" + item_location + ", volume=" + volume
//				+ ", coupon_rate=" + coupon_rate + ", coupon_price="
//				+ coupon_price + ", coupon_start_time=" + coupon_start_time
//				+ ", coupon_end_time=" + coupon_end_time + ", shop_type="
//				+ shop_type + "]";
//	}
//
//
//}
