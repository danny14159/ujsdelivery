package com.express.core.bean;

/**下单次数
 * @author lei.deng@newtouch.cn
 * @date 2016年4月27日
 *
 */
public class SignTimes {

	private String username;
	private String phone;
	private String addr_builiding;
	private String addr_region;
	
	/**
	 * 下单次数
	 */
	private Integer times;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddr_builiding() {
		return addr_builiding;
	}

	public void setAddr_builiding(String addr_builiding) {
		this.addr_builiding = addr_builiding;
	}

	public String getAddr_region() {
		return addr_region;
	}

	public void setAddr_region(String addr_region) {
		this.addr_region = addr_region;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}
}
