package com.emer.egou.app.util;

public class Config {
public static String BROWSER_ACTION = "com.emar.egou.browser";
/**查询淘客折扣商品。*/
public static final String TAOBAOKE_ITEMS_COUPON = "taobao.taobaoke.items.coupon.get";
/**taobao.taobaoke.items.get 查询淘宝客推广商品*/
public static final String TAOBAOKE_ITEMS = "taobao.taobaoke.items.get";
/**tmall.items.discount.search 天猫折扣精选商品库*/
public static final String TMALL_ITEMS_DISCOUNT = "tmall.items.discount.search";
/**taobao.taobaoke.widget.items.convert 淘客商品转换 天猫折扣精选商品库*/
public static final String TAOBAOKE_ITEMS_CONVERT = "taobao.taobaoke.widget.items.convert";

/**淘宝我的收藏*/
public static final String TAO_MYCOLLECTION = "http://fav.m.taobao.com/my_collect_list.htm?sid=";
/**淘宝我的订单待付款*/
public static final String TAO_ORDER = "http://m.taobao.com/trade/bought_item_lists.htm?sid=";
/**淘宝购物车*/
public static final String TAO_CAR = "http://cart.m.taobao.com/my_cart.htm?sid=";
/**淘宝物流*/
public static final String TAO_LOGISTICS = "http://tm.m.taobao.com/order_list.htm?statusId=5&sid=";//加上statusId=5的时候就引导到物流信息
/**最近三个月的订单列表*/
public static final String TAO_ORDER_RECENTLY_THREE_MONTH = "http://tm.m.taobao.com/order_list.htm?sid=";//到了最近三个月的订单列表
}
