package com.express.core.bean;

import java.util.Date;

/**评论记录，excel导出时用
 * @author Danny
 *
 */
public class CommentRecord {

	private Integer id;
	
	private String comment;
	
	private Date sign_time;
	
	private String name;
	
	private String addr_region;
	
	private String addr_building;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getSign_time() {
		return sign_time;
	}

	public void setSign_time(Date sign_time) {
		this.sign_time = sign_time;
	}
}
