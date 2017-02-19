package com.express.core.bean;

import java.util.Date;

/**
 * @author dl
 *
 */
public class User  {

	/**
	 * 
	 */
	private String name;

	/**
	 * 
	 */
	private String address;

	/**
	 * 
	 */
	private String phone;

	/**
	 * 
	 */
	private String univ;

	/**
	 * 
	 */
	private String password;
	
	private Integer point;
	
	private String additional;
	
	private String addr_region;
	
	private String addr_building;
	
	/**
	 * 用户类型 Manager,Common,Business,Super,Empolyee,Point Manager
	 */
	private String type;
	
	/**
	 * 最后一次签到时间
	 */
	private Date last_sign;
	
	/**
	 * 用户openid
	 */
	private String openid;
	
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUniv() {
		return univ;
	}

	public void setUniv(String univ) {
		this.univ = univ;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getLast_sign() {
		return last_sign;
	}

	public void setLast_sign(Date last_sign) {
		this.last_sign = last_sign;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
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
	
	public boolean isPointManager(){
		return "P".equals(this.type);
	}
	
	public boolean isSuperManager(){
		return "S".equals(this.type);
	}
	public boolean isManager(){
		return "M".equals(this.type);
	}
	public boolean isCommonUser(){
		return "C".equals(this.type);
	}
	public boolean isBusiness(){
		return "B".equals(this.type);
	}
	public boolean isEmployee(){
		return "E".equals(this.type);
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

}