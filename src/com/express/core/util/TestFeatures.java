package com.express.core.util;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.express.core.bean.UserSign;
import com.express.core.bean.WebConfigProxy;
import com.express.core.extend.HttpClientUtil;
import com.express.core.extend.Mapper;
import com.express.core.service.UserSignService;

public class TestFeatures {
	
	public static String getExpressFromRemark(String remark,String[] keys){
		for(String key:keys){
			//if(remark.index)
		}
		return null;
	}
	
	public  static void init(){
		ApplicationContext context =
			    new ClassPathXmlApplicationContext("dispatcher-servlet.xml");
		UserSignService service = context.getBean(UserSignService.class);
		WebConfig config = context.getBean(WebConfig.class);
		WebConfigProxy exp = config.getProxy("exp");
		String[] keys = exp.getKeys();
		
		List<UserSign> list = service.list(Mapper.pageTransfer(1, 100).toHashMap());
		for(UserSign i:list){
			System.out.println(i.getRemark());
			
		}
	}

	public static void main(String[] args) {
		
		//init();
		//HttpClientUtil.get("http://192.168.14.56:8090", null);
		Random r = new Random(new Date().getTime());
		String rand = "";
		for (int i = 0; i < 6; i++) {
			char c = (char) ('0' + r.nextInt(10));
			rand += c;
		}
		System.out.println(rand);
	}
}
