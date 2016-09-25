package com.express.core.bean;

import java.util.Date;

/**
 * @author dl
 *
 */
public class UserSign  {
	
	private Integer id;
	/**
	 * 
	 */
	private String userid;

	private String sign_name;
	/**
	 * 
	 */
	private String express;

	/**
	 * 
	 */
	private String remark;

	/**
	 * 
	 */
	private String phone;
	private String name;

	/**
	 * 
	 */
	private String address;

	/**
	 * 
	 */
	private Date sign_time;

	/**
	 * 下单->已提交 Submited(83)
	 * 打印->取件中I(73)
	 * 入库->已取件 Processed(80)
	 * 发短信->派送中E(69)
	 * 派完件->已完成 Finished(70)
	 * 
	 * 已删除 Deleted(68)
	 * 用户取消 Cancel(67)
	 */
	private Character state;
	/**
	 * 判断是否是首单
	 */
	private Character isFirst;
	
	/**
	 * 是否免单，是Y(89)
	 */
	private Character is_free;
	
	private String addr_region;
	
	private String addr_building;
	
	
	/**
	 * 送件时间
	 */
	private String send_time;
	/**
	 * 用户评论
	 */
	private String comment;
	
	/**
	 * 系统备注
	 */
	private String sys_remark;
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSign_name() {
		return sign_name;
	}

	public void setSign_name(String sign_name) {
		this.sign_name = sign_name;
	}

	public Date getSign_time() {
		return sign_time;
	}
	public long getSign_time_value() {
		return sign_time.getTime();
	}

	public void setSign_time(Date sign_time) {
		this.sign_time = sign_time;
	}

	public Character getState() {
		return state;
	}

	public void setState(Character state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Character isFirst) {
		this.isFirst = isFirst;
	}

	public Character getIs_free() {
		return is_free;
	}

	public void setIs_free(Character is_free) {
		this.is_free = is_free;
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
	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((UserSign)obj).id);
	}

	public UserSign(String id) {
		this.setId(id);
	}

	public UserSign() {
		super();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public String getSys_remark() {
		return sys_remark;
	}

	public void setSys_remark(String sys_remark) {
		this.sys_remark = sys_remark;
	}

	public String getId() {
		return id+"";
	}
	
	public String getSecretId() {
		return Integer.toString(id, 36).toUpperCase();
	}
	
	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}


}
