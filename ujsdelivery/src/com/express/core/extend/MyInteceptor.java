package com.express.core.extend;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.express.core.bean.ReqRecord;
import com.express.core.bean.User;
import com.express.core.service.ReqRecordService;
import com.express.core.util.LoginUtil;

public class MyInteceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		//在这里记录下所有的请求，静态资源会被过滤
		String uri = request.getRequestURI();
		
		if(!uri.startsWith("/static") && !uri.startsWith("/app")){
			Enumeration<String> paramNames = request.getParameterNames();
			String paramValus = "";
			while(paramNames.hasMoreElements()){
				String name = paramNames.nextElement();
				String[] values = request.getParameterValues(name);
				for(String value:values){
					paramValus += name+"="+value+";";
				}
			}
			ReqRecordService reqRecordService = SpringContextHolder.getBean("reqRecordService");
			
			User u = LoginUtil.getLoginUser(null,request);
			ReqRecord record = new ReqRecord();
			record.setOpt_time(new Date());
			record.setReq_url(uri);
			record.setTarget_ip(RequestUtils.getRemoteAddr(request));
			if(null !=u){
				record.setUsername(u.getName());
				record.setUserid(u.getPhone());
			}
			record.setParams(paramValus);
			reqRecordService.insert(record);
		}
		
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		super.postHandle(request, response, handler, modelAndView);
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	
}
