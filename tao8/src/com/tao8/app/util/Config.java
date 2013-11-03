package com.tao8.app.util;

public class Config {
public static String BROWSER_ACTION = "com.emar.egou.browser";
/**查询淘客折扣商品。*/
//public static final String TAOBAOKE_ITEMS_COUPON = "taobao.taobaoke.items.coupon.get";
/**taobao.taobaoke.items.get 查询淘宝客推广商品*/
public static final String TAOBAOKE_ITEMS = "taobao.tbk.items.get";
/**taobao.tbk.mobile.items.convert 手机淘宝转换*/
public static final String TAOBAOKE_CLICK_CONVERT = "taobao.tbk.mobile.items.convert";
/**tmall.items.discount.search 天猫折扣精选商品库*/
public static final String TMALL_ITEMS_DISCOUNT = "tmall.items.discount.search";
/**taobao.taobaoke.widget.items.convert 淘客商品转换 天猫折扣精选商品库*/
public static final String TAOBAOKE_ITEMS_CONVERT = "taobao.taobaoke.widget.items.convert";
public static final String COIN_URL  = "http://h5.m.taobao.com/vip/index.htm";
/**淘宝我的收藏*/
public static final String TAO_MYCOLLECTION = "http://h5.m.taobao.com/fav/index.htm?sprefer=p23590&sid=";
/**淘宝我的店铺收藏*/
public static final String TAO_MYCOLLECTION_SHOP = "http://h5.m.taobao.com/fav/index.htm?&sid=";
/**淘宝我的淘宝*/
public static final String TAO_ORDER = "http://h5.m.taobao.com/awp/mtb/mtb.htm?sid=";
/**淘宝购物车*/
public static final String TAO_CAR = "http://h5.m.taobao.com/awp/base/cart.htm?sid=";
/**淘宝物流*/
public static final String TAO_LOGISTICS = "http://h5.m.taobao.com/awp/mtb/mtb.htm#!/awp/mtb/olist.htm?sta=5&sid=";//加上statusId=5的时候就引导到物流信息
/**最近三个月的订单列表*/
public static final String TAO_ORDER_RECENTLY_THREE_MONTH = "http://tm.m.taobao.com/order_list.htm?sid=";//到了最近三个月的订单列表
//http://wwc.taobaocdn.com/avatar/get_avatar.do?userId=362114751&width=100&height=100&type=sns//头像
//http://a.tbcdn.cn/mw/s/hi/tbtouch/icons/rank/b_3_3.gif//星级
//http://a.tbcdn.cn/mw/base/styles/component/icon/images/c_1.png
}
