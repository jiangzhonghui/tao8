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
        "city": "��ƽ",
        "city_en": "changping",
        "date_y": "2013��4��25��",
        "date": "",
        "week": "������",
        "fchh": "11",
        "cityid": "101010700",
        "temp1": "23��~7��",
        "temp2": "23��~9��",
        "temp3": "22��~10��",
        "temp4": "21��~14��",
        "temp5": "25��~12��",
        "temp6": "26��~12��",
        "tempF1": "73.4�H~44.6�H",
        "tempF2": "73.4�H~48.2�H",
        "tempF3": "71.6�H~50�H",
        "tempF4": "69.8�H~57.2�H",
        "tempF5": "77�H~53.6�H",
        "tempF6": "78.8�H~53.6�H",
        "weather1": "��",
        "weather2": "��ת����",
        "weather3": "����ת��",
        "weather4": "����ת��",
        "weather5": "��",
        "weather6": "��",
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
        "img_title1": "��",
        "img_title2": "��",
        "img_title3": "��",
        "img_title4": "����",
        "img_title5": "����",
        "img_title6": "��",
        "img_title7": "����",
        "img_title8": "��",
        "img_title9": "��",
        "img_title10": "��",
        "img_title11": "��",
        "img_title12": "��",
        "img_title_single": "��",
        "wind1": "����3-4��ת΢��",
        "wind2": "΢��",
        "wind3": "΢��",
        "wind4": "΢��ת����3-4��",
        "wind5": "΢��",
        "wind6": "΢��",
        "fx1": "����",
        "fx2": "΢��",
        "fl1": "3-4��תС��3��",
        "fl2": "С��3��",
        "fl3": "С��3��",
        "fl4": "С��3��ת3-4��",
        "fl5": "С��3��",
        "fl6": "С��3��",
        "index": "����",
        "index_d": "�����ż��¼ӱ���ë���ȴ����װ�����������ż��¼���ë��������ҹ�²�ϴ�ע�������·���",
        "index48": "����",
        "index48_d": "�����ż��¼ӱ���ë���ȴ����װ�����������ż��¼���ë��������ҹ�²�ϴ�ע�������·���",
        "index_uv": "�е�",
        "index48_uv": "�е�",
        "index_xc": "����",
        "index_tr": "������",
        "index_co": "����",
        "st1": "22",
        "st2": "8",
        "st3": "22",
        "st4": "11",
        "st5": "22",
        "st6": "9",
        "index_cl": "������",
        "index_ls": "����",
        "index_ag": "���׷�"
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
