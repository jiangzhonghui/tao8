package com.emer.egou.app.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.emer.egou.app.domain.SearchItem;
import com.emer.egou.app.domain.TmallSearchItem;

public class TqlHelper {
	
/**
 * {select 
 * num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume,coupon_price,coupon_rate,coupon_start_time,coupon_end_time,shop_type 
 * from  taobao.taobaoke.items.coupon.get 
 * where cid = 11 and page_size = 3 and is_mobile = true}
 */
	/**
	 * 
	 * @param fields 需要返回的字段
	 * @param api	  请求的接口
	 * @param params 请求接口附带的参数
	 * @return 组装之后的tql语句
	 */
	public static String generateTql(List<String> fields,String api,Map<String,String> params){
		params.put("is_mobile", "true");
		String tql = "select ";
		for (int i = 0; i < fields.size(); i++) {
			if (i==fields.size()-1) {
				tql+=fields.get(i);
			}else {
				tql+=fields.get(i);
				tql+=",";
			}
		}
		tql+=" from "+api;
		tql+=" where ";
		Set<Entry<String, String>> entrySet = params.entrySet();
		int size = entrySet.size();
		for (Entry<String, String> entry : entrySet) {
			tql+=entry.getKey()+" = "+entry.getValue();
			size--;
			if (size<=0) {
				
			}else {
				tql+=" and ";
			}
		}
		return tql;
	}
	/**
	 * 查询淘客折扣商品的tql。
	 * @param fields
	 * @param params
	 * @return
	 */
	public static String generateTaoBaoKeCouponTql(List<String> fields,Map<String,String> params){
		return generateTql(fields, Config.TAOBAOKE_ITEMS_COUPON, params);
	}
	public static String generateTaoBaoKeTql(List<String> fields,Map<String,String> params){
		return generateTql(fields, "taobaoke.items", params);
	}
	public static String generateTaoBaoKeTql(Class<SearchItem> clazz,Map<String, String> params) {
		List<String> fields = new ArrayList<String>();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			fields.add(field.getName());
		}
		return generateTaoBaoKeTql(fields, params);
	}
	
	public static String generateTMallTql(List<String> fields,Map<String, String> params){
		return generateTql(fields, Config.TMALL_ITEMS_DISCOUNT, params);
	}
	public static String generateTiMailTql(Class<TmallSearchItem> clazz,Map<String, String> params){
		List<String> fields = new ArrayList<String>();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			fields.add(field.getName());
		}
		return generateTMallTql(fields, params);
	}
	
	public static String generateTaobaokeConvertTql(Class<SearchItem> clazz,Map<String,String> params){
		List<String> fields = new ArrayList<String>();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			fields.add(field.getName());
		}
		return generateTql(fields, Config.TAOBAOKE_ITEMS_CONVERT, params);
	}
	/**
	 * 将天猫精品的链接转换成淘客带佣金的链接
	 * @param clazz 搜索的
	 * @param whatIn num_iids商品的数字id
	 * @param TiMailTql 天猫精品的tql 如:select item_id from tmall.items.discount.search where q = %e6%89%8b%e6%9c%ba (手机)
	 * @return
	 */
	public static String generateTMallConvertToTaoKenestTql(Class<SearchItem> clazz,String whatIn,String tmallTql){
		Map<String, String> params = new HashMap<String, String>();
		return generateTaobaokeConvertTql(clazz, params)+" and "+whatIn+" in "+"("+tmallTql+")";
	}
}
