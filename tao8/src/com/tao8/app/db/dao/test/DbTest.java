package com.tao8.app.db.dao.test;

import android.test.AndroidTestCase;

import com.tao8.app.db.dao.TaoBaokeCouponDao;
import com.tao8.app.domain.SearchItem;

public class DbTest extends AndroidTestCase{

	
	public void TestAdd(){
		
		TaoBaokeCouponDao taoBaokeCouponDao = new TaoBaokeCouponDao(getContext());
		SearchItem taobaokeCouponItem = new SearchItem();
		taobaokeCouponItem.setClick_url("11111111111111111");
		taobaokeCouponItem.setCommission("222222222222");
		taobaokeCouponItem.setCommission_num("33333333333333333");
//		taobaokeCouponItem.setCoupon_end_time("sslkjfslfsl;");
//		taobaokeCouponItem.setCoupon_price("12.3元");
//		taobaokeCouponItem.setCoupon_rate("1折");
		taobaokeCouponItem.setTitle("手机大放血");
		taobaokeCouponItem.setNum_iid("12345678");
		taoBaokeCouponDao.insert(taobaokeCouponItem);
	}
	public void TestQuery(){
		TaoBaokeCouponDao taoBaokeCouponDao = new TaoBaokeCouponDao(getContext());
//		TaobaokeCouponItem query = taoBaokeCouponDao.query("12345678");
		//assertNotNull(taoBaokeCouponDao);
		
//		assertEquals("12345678",query.getNum_iid());
//		System.out.println(query);
	}
	public void testQuenyCount(){
		TaoBaokeCouponDao taoBaokeCouponDao = new TaoBaokeCouponDao(getContext());
		taoBaokeCouponDao.delAll();
		//assertEquals("0", taoBaokeCouponDao.queryCount());
	}
	
	public void testQueryAll(){
		TaoBaokeCouponDao taoBaokeCouponDao = new TaoBaokeCouponDao(getContext());
//		List<TaobaokeCouponItem> queryAll = taoBaokeCouponDao.queryAll();
//		System.out.println(queryAll.size());
//		assertEquals(expected, actual);
	}
	
	public void testQueryByKeyword(){
		TaoBaokeCouponDao taoBaokeCouponDao = new TaoBaokeCouponDao(getContext());
//		ArrayList<TaobaokeCouponItem> queryAllByKeyword = taoBaokeCouponDao.queryAllByKeywordFromTo("手机",1,20);
//		assertNotNull(queryAllByKeyword);
//		System.out.println(queryAllByKeyword.size());
//		assertEquals(queryAllByKeyword.size(), 20);
	}
}
