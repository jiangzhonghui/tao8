package com.tao8.app.domain;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchItem implements Serializable{
/**
	 * 
	 * 
num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,
commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume 
	 */
	public static final long serialVersionUID = 903907914676661343L;
@Override
	public String toString() {
		return "SearchItem [num_iid=" + num_iid + ", title=" + title
				+ ", nick=" + nick + ", pic_url=" + pic_url + ", price="
				+ price + ", click_url=" + click_url + ", commission="
				+ commission + ", commission_rate=" + commission_rate
				+ ", commission_num=" + commission_num + ", commission_volume="
				+ commission_volume + ", shop_click_url=" + shop_click_url
				+ ", seller_credit_score=" + seller_credit_score
				+ ", item_location=" + item_location + ", volume=" + volume
				+ "]";
	}
/**
 * num_iid,title,nick,pic_url,price,click_url,commission,
 * commission_rate,commission_num,
 * commission_volume,shop_click_url,seller_credit_score,item_location,volume
 */
	/**淘宝客商品数字id*/
	public String num_iid;
	/**商品title 宝贝名称*/
	public String title;
	/**卖家昵称*/
	public String nick;
	/**图片url*/
	public String pic_url;
	/**商品价格*/
	public String price;
	/**推广点击url*/
	public String click_url;
	/**淘宝客佣金*/
	public String commission;
	/**淘宝客佣金比率，比如：1234.00代表12.34%*/
	public String commission_rate;
	/**累计成交量.注：返回的数据是30天内累计推广量*/
	public String commission_num;
	/**累计总支出佣金量*/
	public String commission_volume;
	/**商品所在店铺的推广点击url*/
	public String shop_click_url;
	/**卖家信用等级*/
	public String seller_credit_score;
	/**商品所在地*/
	public String item_location;
	/**30天内交易量*/
	public String volume;
	public String getNum_iid() {
		return num_iid;
	}
	public void setNum_iid(String num_iid) {
		this.num_iid = num_iid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getClick_url() {
		return click_url;
	}
	public void setClick_url(String click_url) {
		this.click_url = click_url;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getCommission_rate() {
		return commission_rate;
	}
	public void setCommission_rate(String commission_rate) {
		this.commission_rate = commission_rate;
	}
	public String getCommission_num() {
		return commission_num;
	}
	public void setCommission_num(String commission_num) {
		this.commission_num = commission_num;
	}
	public String getCommission_volume() {
		return commission_volume;
	}
	public void setCommission_volume(String commission_volume) {
		this.commission_volume = commission_volume;
	}
	public String getShop_click_url() {
		return shop_click_url;
	}
	public void setShop_click_url(String shop_click_url) {
		this.shop_click_url = shop_click_url;
	}
	public String getSeller_credit_score() {
		return seller_credit_score;
	}
	public void setSeller_credit_score(String seller_credit_score) {
		this.seller_credit_score = seller_credit_score;
	}
	public String getItem_location() {
		return item_location;
	}
	public void setItem_location(String item_location) {
		this.item_location = item_location;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	
}
