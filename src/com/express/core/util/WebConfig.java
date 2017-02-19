package com.express.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.express.core.bean.Config;
import com.express.core.bean.WebConfigProxy;
import com.express.core.extend.SpringContextHolder;
import com.express.core.service.ConfigService;

@Repository
public class WebConfig {
	
	private ConfigService configService;
	
	private static final Map<String,WebConfigProxy> config = new HashMap<String, WebConfigProxy>();

	public WebConfig() {
		configService =(ConfigService) SpringContextHolder.getBean("configService");
		List<Config> configarr = configService.list(null);
		for(Config con:configarr){
			WebConfigProxy proxy = config.get(con.getType());
			if(null != proxy){
				proxy.put(con.getKey(), con.getValue());
			}
			else{
				config.put(con.getType(), new WebConfigProxy(con));
			}
		}
	}
	
	/**根据key拿到代理对象
	 * @param type
	 * @return
	 */
	public WebConfigProxy getProxy(String type){
		
		return config.get(type);
	}
	
	public String get(String type,String key){
		WebConfigProxy proxy = getProxy(type);
		return proxy == null?null:proxy.get(key);
	}
}
