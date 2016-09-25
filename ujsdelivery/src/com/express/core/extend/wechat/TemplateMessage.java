package com.express.core.extend.wechat;

import java.util.HashMap;
import java.util.Map;

/**模板消息实体类
 * @author Danny
 *
 */
public class TemplateMessage {

	private String touser;
	
	private String template_id;
	private String url;
	private String topcolor;
	private Map<String,TplMessageData> data = new HashMap<>();
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTopcolor() {
		return topcolor;
	}
	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}
	public Map<String, TplMessageData> getData() {
		return data;
	}
	public void setData(Map<String, TplMessageData> data) {
		this.data = data;
	}
	
	
}
class TplMessageData{
	private String value;
	private String color;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public TplMessageData(String value, String color) {
		super();
		this.value = value;
		this.color = color;
	}
}