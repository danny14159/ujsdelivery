package com.express.core.bean;
/**
 * @author dl
 *
 */
public class UserSend  {
	
	private String id;

	/**
	 * 
	 */
	private String userid;
	private String name;
	private String address;
	private String state;
	
	private String addr_region;
	private String addr_building;

	/**
	 * 
	 */
	private String sign_time;

	/**
	 * 
	 */
	private String remark;

	/**
	 * 
	 */
	private String express;
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSign_time() {
		return sign_time;
	}

	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddr_region() {
		return addr_region;
	}

	public void setAddr_region(String addr_region) {
		this.addr_region = addr_region;
	}

	public String getAddr_building() {
		return addr_building;
	}

	public void setAddr_building(String addr_building) {
		this.addr_building = addr_building;
	}

}