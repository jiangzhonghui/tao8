package com.tao8.app.domain;

import java.io.Serializable;

public class AccountDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3597490741401820423L;
	public AccountDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AccountDetail(String itemName, String uri, String action) {
		super();
		this.itemName = itemName;
		this.uri = uri;
		this.action = action;
	}
	private String itemName;
	private String uri;
	private String action;
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Override
	public String toString() {
		return "AccountDetail [itemName=" + itemName + ", uri=" + uri
				+ ", action=" + action + "]";
	}
	
	
}
