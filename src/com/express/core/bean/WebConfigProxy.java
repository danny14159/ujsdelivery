package com.express.core.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**配置代理类
 * @author Danny
 *
 */
public class WebConfigProxy {

	public WebConfigProxy(Config config) {
		this.type = config.getType();
		this.config.put(config.getKey(),config.getValue());
	}

	/**获取key数组
	 * @return
	 */
	public String[] getKeys(){
		Set<String> keys = config.keySet();
		return keys.toArray(new String[0]);
	}
	
	private String type;
	
	private Map<String,String> config = new HashMap<String, String>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}
	
	public void put(String key,String value){
		config.put(key, value);
	}
	public String get(String key){
		return config.get(key);
	}
}
