package com.express.core.module;


import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.express.core.extend.Mapper;
import com.express.core.service.BaseService;


public abstract class BasicModule<T> {
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected DataSourceTransactionManager transactionManager;
	
	static{
		org.apache.ibatis.logging.LogFactory.useLog4J2Logging();
	}
	public BasicModule() {
		
	}

	private Logger logger = null;
	/**获取logger对象
	 * @return
	 */
	protected Logger getLogger(){
		if(logger==null)logger = LogManager.getLogger(this.getClass().getName());
		return logger;
	}
	
	/**获取基础服务类
	 * @return
	 */
	protected abstract BaseService<T> getService();
	
/*	@RequestMapping("/load/{id}")
	@ResponseBody
	public Object load(@PathVariable(value="id") Integer id){
		Object obj = null;
		try{
			obj = getService().load(Mapper.make("id", id).toHashMap());
			return obj;
		}
		catch(Exception e){
			
			return obj;
		}
	}*/
	
	@RequestMapping("/ins")
	@ResponseBody
	public Object insert(T obj,BindingResult br) throws Exception{
		if(br!=null && br.hasErrors()){
			return 0;
		}
		try{
			return getService().insert(obj);
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	@RequestMapping("/del/{id}")
	@ResponseBody
	public Object delete(@PathVariable("id")Integer id){
		try{
			return getService().delete(Mapper.make("id", id).toHashMap());
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	@RequestMapping("/upd")
	@ResponseBody
	public Object update(@ModelAttribute  T obj,BindingResult br){
		if(br!=null && br.hasErrors()) return 0;
		try{
			return getService().update(obj);
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
/*	@RequestMapping("/lst")
	@ResponseBody
	public Object list(T obj){
		try{
			return getService().list(obj);
		}
		catch(Exception e){
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}*/
	
/*	@RequestMapping("/lstp")
	@ResponseBody
	public Object listByPage(T obj,
			@RequestParam("ps")Integer ps,
			@RequestParam("pn")Integer pn){
		try{
			List<T> list = getService().listByPage(
					Mapper.make("offset", ps*(pn-1)).put("rows", ps).joinBean(obj).toHashMap()
					);
			Pager pager = new Pager();
			pager.setPageNumber(pn);
			pager.setPageSize(ps);
			pager.setRecordCount(getService().count(obj));
			QueryResult qr = new QueryResult(list, pager);
			
			return qr;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}*/
	
	@RequestMapping("/cnt")
	@ResponseBody
	public Object count(T obj){
		try{
			return getService().count(obj);
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}
