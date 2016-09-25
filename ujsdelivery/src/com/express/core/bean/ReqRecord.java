
package com.express.core.bean;

import java.util.Date;

/**
 * @author 
 *
 */
public class ReqRecord  {

    /**
     * 
     */
    private String userid;

    /**
     * 
     */
    private String req_url;

    /**
     * 
     */
    private String params;

    /**
     * 
     */
    private String target_ip;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private Date opt_time;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getReq_url() {
		return req_url;
	}

	public void setReq_url(String req_url) {
		this.req_url = req_url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getTarget_ip() {
		return target_ip;
	}

	public void setTarget_ip(String target_ip) {
		this.target_ip = target_ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getOpt_time() {
		return opt_time;
	}

	public void setOpt_time(Date opt_time) {
		this.opt_time = opt_time;
	}

}