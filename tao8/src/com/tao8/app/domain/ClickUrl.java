package com.tao8.app.domain;

import java.io.Serializable;

public class ClickUrl implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -8790007778834758035L;
public String click_url;

@Override
public String toString() {
	return "ClickUrl [click_url=" + click_url + "]";
}

public String getClick_url() {
	return click_url;
}

public void setClick_url(String click_url) {
	this.click_url = click_url;
}

}
