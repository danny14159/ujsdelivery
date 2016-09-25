
package com.express.travel.bean;
/**
 * @author 
 *
 */
public class GoodsAppendixParam  {

    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String name;
    
    /**
     * 父级id
     */
    private Integer pid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

}