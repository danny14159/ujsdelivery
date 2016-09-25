package com.express.core.bean;

/**
 * @author 
 * 充值记录
 */
public class ChargeRecord  {

	/**
	 * 
	 */
	private Integer id;

	/**
	 * 
	 */
	@org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-dd-MM HH:mm:ss")
	private java.util.Date create_time;

	/**
	 * 
	 */
	private String create_user;

	/**
	 * 
	 */
	private Integer point;

	/**
	 * 
	 */
	private String target_user;

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return this.id;
	}

	public void setCreate_time(java.util.Date create_time){
		this.create_time=create_time;
	}

	public java.util.Date getCreate_time(){
		return this.create_time;
	}

	public void setCreate_user(String create_user){
		this.create_user=create_user;
	}

	public String getCreate_user(){
		return this.create_user;
	}

	public void setPoint(Integer point){
		this.point=point;
	}

	public Integer getPoint(){
		return this.point;
	}

	public void setTarget_user(String target_user){
		this.target_user=target_user;
	}

	public String getTarget_user(){
		return this.target_user;
	}

}