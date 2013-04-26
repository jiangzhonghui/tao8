package com.tao8.weather.domain;

import java.io.Serializable;

public class Weather implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -5218576851722140853L;
/**
 * {
    "weatherinfo": {
        "city": "昌平",
        "city_en": "changping",
        "date_y": "2013年4月25日",
        "date": "",
        "week": "星期四",
        "fchh": "11",
        "cityid": "101010700",
        "temp1": "23℃~7℃",
        "temp2": "23℃~9℃",
        "temp3": "22℃~10℃",
        "temp4": "21℃~14℃",
        "temp5": "25℃~12℃",
        "temp6": "26℃~12℃",
        "tempF1": "73.4℉~44.6℉",
        "tempF2": "73.4℉~48.2℉",
        "tempF3": "71.6℉~50℉",
        "tempF4": "69.8℉~57.2℉",
        "tempF5": "77℉~53.6℉",
        "tempF6": "78.8℉~53.6℉",
        "weather1": "晴",
        "weather2": "晴转多云",
        "weather3": "多云转阴",
        "weather4": "阵雨转阴",
        "weather5": "晴",
        "weather6": "晴",
        "img1": "0",
        "img2": "99",
        "img3": "0",
        "img4": "1",
        "img5": "1",
        "img6": "2",
        "img7": "3",
        "img8": "2",
        "img9": "0",
        "img10": "99",
        "img11": "0",
        "img12": "99",
        "img_single": "0",
        "img_title1": "晴",
        "img_title2": "晴",
        "img_title3": "晴",
        "img_title4": "多云",
        "img_title5": "多云",
        "img_title6": "阴",
        "img_title7": "阵雨",
        "img_title8": "阴",
        "img_title9": "晴",
        "img_title10": "晴",
        "img_title11": "晴",
        "img_title12": "晴",
        "img_title_single": "晴",
        "wind1": "北风3-4级转微风",
        "wind2": "微风",
        "wind3": "微风",
        "wind4": "微风转北风3-4级",
        "wind5": "微风",
        "wind6": "微风",
        "fx1": "北风",
        "fx2": "微风",
        "fl1": "3-4级转小于3级",
        "fl2": "小于3级",
        "fl3": "小于3级",
        "fl4": "小于3级转3-4级",
        "fl5": "小于3级",
        "fl6": "小于3级",
        "index": "温凉",
        "index_d": "建议着夹衣加薄羊毛衫等春秋服装。体弱者宜着夹衣加羊毛衫。但昼夜温差较大，注意增减衣服。",
        "index48": "温凉",
        "index48_d": "建议着夹衣加薄羊毛衫等春秋服装。体弱者宜着夹衣加羊毛衫。但昼夜温差较大，注意增减衣服。",
        "index_uv": "中等",
        "index48_uv": "中等",
        "index_xc": "适宜",
        "index_tr": "很适宜",
        "index_co": "舒适",
        "st1": "22",
        "st2": "8",
        "st3": "22",
        "st4": "11",
        "st5": "22",
        "st6": "9",
        "index_cl": "较适宜",
        "index_ls": "适宜",
        "index_ag": "极易发"
    }
}
 */
	public String city;
	public String date_y;
	public String week;
	public String cityid;
	public String temp1;
	public String temp2;
	public String temp3;
	public String temp4;
	public String temp5;
	public String temp6;
	public String weather1;
	public String weather2;
	public String weather3;
	public String weather4;
	public String weather5;
	public String weather6;
	public String wind1;
	public String wind2;
	public String wind3;
	public String wind4;
	public String wind5;
	public String wind6;
	@Override
	public String toString() {
		return "Weather [city=" + city + ", date_y=" + date_y + ", week="
				+ week + ", cityid=" + cityid + ", temp1=" + temp1 + ", temp2="
				+ temp2 + ", temp3=" + temp3 + ", temp4=" + temp4 + ", temp5="
				+ temp5 + ", temp6=" + temp6 + ", weather1=" + weather1
				+ ", weather2=" + weather2 + ", weather3=" + weather3
				+ ", weather4=" + weather4 + ", weather5=" + weather5
				+ ", weather6=" + weather6 + ", wind1=" + wind1 + ", wind2="
				+ wind2 + ", wind3=" + wind3 + ", wind4=" + wind4 + ", wind5="
				+ wind5 + ", wind6=" + wind6 + "]";
	}
	
}
