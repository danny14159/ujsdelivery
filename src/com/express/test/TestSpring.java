package com.express.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.express.core.module.UserModule;

public class TestSpring {

	@Test
	public void test(){
		System.out.println(123);
		@SuppressWarnings("resource")
		ApplicationContext context =
			    new ClassPathXmlApplicationContext("dispatcher-servlet.xml");
		UserModule mod = context.getBean(UserModule.class);
		mod.test();
	}
}
