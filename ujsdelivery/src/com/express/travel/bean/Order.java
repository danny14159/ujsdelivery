
package com.express.travel.bean;

import com.express.core.bean.User;

/**
 * @author 
 *
 */
public class Order  {

    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String userid;

    /**
     * 联系方式
     */
    private String connection_methods;

    /**
     * 
     */
    private Integer goods_id;

    /**
     * 商品总价
     */
    private Float total_price;

    /**
     * 订单状态 S：已提交，P:已付款，F已完成，D:已删除：C:已取消
     */
    private String status;

    /**
     * 下单时间
     */
    private java.util.Date create_time;
    
    /**
     * 导游费
     */
    private Integer guide_level;
    
	/**
	 * 车费
	 */
	private Integer fare_level;
	
	/**
	 * 门票费
	 */
	private Integer ticket_level;
	
	/**
	 * 下单用户
	 */
	private User user;
	
	/**
	 * 下单的商品
	 */
	private Goods goods;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getConnection_methods() {
		return connection_methods;
	}

	public void setConnection_methods(String connection_methods) {
		this.connection_methods = connection_methods;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Float getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Float total_price) {
		this.total_price = total_price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public java.util.Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(java.util.Date create_time) {
		this.create_time = create_time;
	}

	public Integer getGuide_level() {
		return guide_level;
	}

	public void setGuide_level(Integer guide_level) {
		this.guide_level = guide_level;
	}

	public Integer getFare_level() {
		return fare_level;
	}

	public void setFare_level(Integer fare_level) {
		this.fare_level = fare_level;
	}

	public Integer getTicket_level() {
		return ticket_level;
	}

	public void setTicket_level(Integer ticket_level) {
		this.ticket_level = ticket_level;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

}