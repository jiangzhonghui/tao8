package com.tao8.app.domain;

import java.io.Serializable;
/**
 * 天猫搜索数据结构
 * @author Administrator
 *
 */
public class TmallSearchItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7314676267840547078L;
	/**搜索宝贝的标题*/
	private String title;
	/**搜索宝贝的已售数量*/
	private String sold;
	/**搜索宝贝url*/
	private String url;
	/**搜索宝贝的图片url*/
	private String pic_path;
	/**搜索宝贝的一口价*/
	private String price;
	/**搜索宝贝的数字id*/
	private String tem_id;
	/**搜索宝贝的卖家昵称*/
	private String nick;
	/**搜索宝贝的宝贝所在地*/
	private String location;
	/**搜索宝贝的卖家所在地*/
	private String seller_loc;
	/**是否免邮*/
	private String shipping;
	/**搜索宝贝的spuid*/
	private String spu_id;
	public TmallSearchItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "TmallSearchItem [title=" + title + ", sold=" + sold + ", url="
				+ url + ", pic_path=" + pic_path + ", price=" + price
				+ ", tem_id=" + tem_id + ", nick=" + nick + ", location="
				+ location + ", seller_loc=" + seller_loc + ", shipping="
				+ shipping + ", spu_id=" + spu_id + ", is_cod=" + is_cod
				+ ", fast_post_fee=" + fast_post_fee + ", user_id=" + user_id
				+ ", is_promotion=" + is_promotion + ", price_with_rate="
				+ price_with_rate + "]";
	}

	public TmallSearchItem(String title, String sold, String url,
			String pic_path, String price, String tem_id, String nick,
			String location, String seller_loc, String shipping, String spu_id,
			String is_cod, String fast_post_fee, String user_id,
			String is_promotion, String price_with_rate) {
		super();
		this.title = title;
		this.sold = sold;
		this.url = url;
		this.pic_path = pic_path;
		this.price = price;
		this.tem_id = tem_id;
		this.nick = nick;
		this.location = location;
		this.seller_loc = seller_loc;
		this.shipping = shipping;
		this.spu_id = spu_id;
		this.is_cod = is_cod;
		this.fast_post_fee = fast_post_fee;
		this.user_id = user_id;
		this.is_promotion = is_promotion;
		this.price_with_rate = price_with_rate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSold() {
		return sold;
	}
	public void setSold(String sold) {
		this.sold = sold;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTem_id() {
		return tem_id;
	}
	public void setTem_id(String tem_id) {
		this.tem_id = tem_id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSeller_loc() {
		return seller_loc;
	}
	public void setSeller_loc(String seller_loc) {
		this.seller_loc = seller_loc;
	}
	public String getShipping() {
		return shipping;
	}
	public void setShipping(String shipping) {
		this.shipping = shipping;
	}
	public String getSpu_id() {
		return spu_id;
	}
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}
	public String getIs_cod() {
		return is_cod;
	}
	public void setIs_cod(String is_cod) {
		this.is_cod = is_cod;
	}
	public String getFast_post_fee() {
		return fast_post_fee;
	}
	public void setFast_post_fee(String fast_post_fee) {
		this.fast_post_fee = fast_post_fee;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getIs_promotion() {
		return is_promotion;
	}
	public void setIs_promotion(String is_promotion) {
		this.is_promotion = is_promotion;
	}
	public String getPrice_with_rate() {
		return price_with_rate;
	}
	public void setPrice_with_rate(String price_with_rate) {
		this.price_with_rate = price_with_rate;
	}
	/**是否货到付款*/
	private String is_cod;
	/**邮费*/
	private String fast_post_fee;
	/**搜索宝贝的卖家数字id*/
	private String user_id;
	/**是否折扣*/
	private String is_promotion;
	/**搜索宝贝的一口价折扣价*/
	private String price_with_rate;

}
