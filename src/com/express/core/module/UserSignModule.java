package com.express.core.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.express.core.bean.Jokes;
import com.express.core.bean.MessageItem;
import com.express.core.bean.User;
import com.express.core.bean.UserSign;
import com.express.core.extend.AjaxReturn;
import com.express.core.extend.Mapper;
import com.express.core.extend.Strings;
import com.express.core.extend.wechat.WechatTemplateMessage;
import com.express.core.service.BaseService;
import com.express.core.service.UserService;
import com.express.core.service.UserSignService;
import com.express.core.util.LoginUtil;
import com.express.core.util.MessageUtil;
import com.express.core.util.WebConfig;
import com.google.gson.Gson;

@Controller
@RequestMapping("/usign")
public class UserSignModule extends BasicModule<UserSign>{
	@Autowired
	private UserSignService userSignService;
	@Autowired
	private UserService userService;
	@Autowired
	private WebConfig webConfig;
	@Autowired
	private SystemModule systemModule;
	
	@Override
	protected BaseService<UserSign> getService() {
		return userSignService;
	}

	@Override
	public Object delete(Integer id) {
		return null;
	}

	@Override
	@Transactional
	public Object insert(final UserSign obj
			, BindingResult br) throws Exception{
		//新订单的空值判断
		if(Strings.isBlank(obj.getName())){
			throw new Exception("illegal order");
		}
		
		final User u = LoginUtil.getLoginUser(userService,request);
		boolean point4free = "true".equals(request.getParameter("point4free")); //使用积分换取免单标记
		if(null == u) return new AjaxReturn(false, "请先登录",-1);
		
		if(point4free && u.getPoint() < 200){
			return new AjaxReturn(false, "积分不足200");
		}
		obj.setUserid(u.getPhone());
		obj.setSign_time(new Date());
		obj.setState('S');
		obj.setName(u.getName());
		obj.setIs_free(point4free?'Y':'N');
		
		
		if(point4free){
			userService.update(Mapper.make("phone", u.getPhone()).put("point", -200).toHashMap());
			u.setPoint(u.getPoint()-200);
		}
		else{
			userService.update(Mapper.make("phone", u.getPhone()).put("point", 10).toHashMap());
			u.setPoint(u.getPoint()+10);
		}
		
		//设置profile
		if(u.getName() == null ){
			userService.update(Mapper.make("phone",u.getPhone()).put("name", obj.getSign_name()).toHashMap());
			u.setName(obj.getSign_name());
		}
		if(u.getAddr_region() == null){
			userService.update(Mapper.make("phone",u.getPhone()).put("addr_region", obj.getAddr_region()).toHashMap());
			u.setAddr_region(obj.getAddr_region());
		}
		if(u.getAddr_building() == null){
			userService.update(Mapper.make("phone",u.getPhone()).put("addr_building", obj.getAddr_building()).toHashMap());
			u.setAddr_building(obj.getAddr_building());
		}
		
		SystemModule.update_state(request);

		
		String successMsg = "订单提交成功。请等待快递送达。";
		if(!point4free) successMsg +="恭喜你获得10积分，到我的订单查看。";
		else successMsg += "恭喜你成功使用200积分换取免单机会。";
		
		if(systemModule.isFirstOrder(obj, SystemModule.FIRST_ORDER_DATE)){
			successMsg += "<br/>新学期首单免费哦~本单已为你免单。";
			obj.setIsFirst('Y');
		}
		else{
			obj.setIsFirst('N');
		}
		
		try{
			getLogger().debug(new Gson().toJson(obj));
			
			/*ReqRecord r = new ReqRecord();
			r.setOpt_time(new Date());
			r.setParams(new Gson().toJson(obj));
			r.setReq_url("NEWORDER");
			reqRecordService.insert(r);*/
			
			userSignService.insert(obj);
			successMsg +="<br/>"+Jokes.getJoke();
			
			new Thread(){
				public void run() {
					Pattern r = Pattern.compile(webConfig.get("pattern", "order"));
					Matcher m = r.matcher(obj.getRemark());
					if (m.find()) {
						obj.setRemark(m.group(0));
					}
					WechatTemplateMessage.sendTemplateMessageOrderSuccess(
							u.getOpenid(), 
							"#ffffff",
							obj.getRemark(),
							obj.getAddr_region()+"-"+obj.getAddr_building(),
							obj.getExpress()
							);
					
				};
			}.start();
			
			WebUtils.setSessionAttribute(request, ActivityModule.DRAW_TIMES, 1);
			return new AjaxReturn(true, successMsg);
		}
		
		catch(Exception e){
			//throw new Exception("请尝试登录或者减少输入字符再提交订单");
			getLogger().debug("order insert error:"+e.getMessage());
			return new AjaxReturn(false, "抱歉，订单提交失败了，失败信息："+e.getMessage()+"请联系客服或者重试。");
		}
		
	}
	
	/**
	 * 下单->已提交 Submited(83)
	 * 打印->取件中I(73)
	 * 入库->已取件 Processed(80)
	 * 发短信->派送中E(69)
	 * 派完件->已完成 Finished(70)
	 * 
	 * 已删除 Deleted(68)
	 * 用户取消 Cancel(67)
	 */
	
	/**下一个订单状态
	 * @param id
	 * @return
	 */
	@RequestMapping("/nexts")
	@ResponseBody
	public Object nextState(String id){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || !u.isManager()) return 0;
		
		UserSign dataObj = userSignService.load(Mapper.make("id", id).toHashMap());
		if(null == dataObj) return 0;
		
		Character nextState = null;
		switch(dataObj.getState()){
			case 'S' :nextState='I';break;
			case 'I' :nextState='P';break;
			case 'P' :nextState='E';break;
			case 'E' :nextState='F';break;
			default:return 0;
		}
		
		UserSign param = new UserSign();
		param.setId(dataObj.getId());
		param.setState(nextState);
		return super.update(param, null);
	}
	
	/**上一个订单状态
	 * @param id
	 * @return
	 */
	@RequestMapping("/prevs")
	@ResponseBody
	public Object prevState(String id){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || !u.isManager()) return 0;
		
		UserSign dataObj = userSignService.load(Mapper.make("id", id).toHashMap());
		if(null == dataObj) return 0;
		
		Character prevState = null;
		switch(dataObj.getState()){
			case 'I' :prevState='S';break;
			case 'P' :prevState='I';break;
			case 'E' :prevState='P';break;
			case 'F' :prevState='E';break;
			case 'D' :prevState='S';break;
			default:return 0;
		}
		
		UserSign param = new UserSign(dataObj.getId()+"");
		param.setState(prevState);
		return super.update(param, null);
	}

	/**删除订单，扣除相应积分或者归还相应积分
	 * @param id
	 * @return
	 */
	@RequestMapping("/delorder")
	@ResponseBody
	public Object deleteOrder(String id){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || !u.isManager()) return 0;
		
		UserSign order = userSignService.load(Mapper.make("id", id).toHashMap());
		if(order.getIs_free()!=null && order.getIs_free()=='Y'){
			//这单是用户用200积分换来的。
			userService.update(Mapper.make("point", 200)
					.put("phone", order.getUserid())
					.toHashMap());
			//u.setPoint(u.getPoint()+200);
		}
		else{
			userService.update(Mapper.make("point", -10)
					.put("phone", order.getUserid())
					.toHashMap());
			//u.setPoint(u.getPoint()-20);
		}
		
		UserSign param = new UserSign(id);
		param.setState('D');
		return super.update(param, null);
	}
	
	/**取消订单，扣除相应积分或者归还相应积分，只能取消自己的订单
	 * @param id
	 * @return
	 */
	@RequestMapping("/cancelorder")
	@ResponseBody
	public Object cancelOrder(String id){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u) return new AjaxReturn(false, "登录信息已失效，无法继续操作");
		
		UserSign order = userSignService.load(Mapper.make("id", id).toHashMap());
		if(!order.getUserid().equals(u.getPhone())) return new AjaxReturn(false, "只能取消自己下的单哦") ;//判断是不是自己下的单
		
		if(order.getIs_free()!=null && order.getIs_free()=='Y'){
			//这单是用户用200积分换来的。
			userService.update(Mapper.make("point", 200)
					.put("phone", order.getUserid())
					.toHashMap());
			u.setPoint(u.getPoint()+200);
		}
		else{
			userService.update(Mapper.make("point", -10)
					.put("phone", order.getUserid())
					.toHashMap());
			u.setPoint(u.getPoint()-10);
		}
		
		UserSign param = new UserSign(id);
		param.setState('C');
		return AjaxReturn.ok((int)super.update(param, null), null, "系统异常，请稍后重试或联系客服");
	}
	
	@Override
	public Object update(UserSign obj, BindingResult br) {
		/*//普通用户只能修改自己的订单为状态C，管理员则无限制
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u) return 0;
		UserSign dataObj = userSignService.load(Mapper.make("id", obj.getId()).toHashMap());
		if(null == dataObj) return 0;
		if(u.getType() == 'M' || (u.getType()=='C' && obj.getState()== 'C' && u.getPhone().equals(dataObj.getUserid()))){
			super.update(obj, br);
			
			//如果是取消订单或者删除，扣除相应积分或者归还相应积分
			if(obj.getState() == 'C' || obj.getState()=='D'){
				UserSign order = userSignService.load(Mapper.make("id", obj.getId()).toHashMap());
				if(order.getIs_free()!=null && order.getIs_free()=='Y'){
					//这单是用户用200积分换来的。
					userService.update(Mapper.make("point", 200)
							.put("phone", obj.getUserid())
							.toHashMap());
				}
				else{
					userService.update(Mapper.make("point", -20)
							.put("phone", obj.getUserid())
							.toHashMap());
				}
				SystemModule.invalidateUser(userService, request);
				
				//检查用户最早的订单是不是首单
				UserSign earlist = userSignService.getEarliestSubmitedOrder(Mapper.make("userid", order.getUserid()).toHashMap());
				if(null != earlist){
					if(systemModule.isFirstOrder(earlist, SystemModule.FIRST_ORDER_DATE)){
						userSignService.update(Mapper.make("id", earlist.getId()).put("isFirst", 'Y').toHashMap());
					}
				}
			}
		}
		else{
			return obj.getState()+","+u.getType()+","+u.getPhone()+","+obj.getUserid();
		}
		return 1;*/
		return 0;
	}
	
	/**批量更新操作，管理员无限制，商家只能处理自己的
	 * @param ids
	 * @param state
	 * @return
	 */
	@RequestMapping("/batch")
	@ResponseBody
	public Object batchUpdate(@RequestParam("ids[]") String[] ids,@RequestParam Character state){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || u.isCommonUser()) return 0;
		//员工只能入库操作
		if(u.isEmployee() && state!='P'){
			return 0;
		}
		
		int bsuccess = 1;
		Map<String,Object> param = Mapper.make("state", state)
				.toHashMap();
		if(u.isBusiness()){
			param.put("express", u.getName());
		}
		for(String id:ids){
			param.put("id", id);
			bsuccess *=userSignService.update(param);
		}
		return bsuccess;
	}
	
	/**评论，只能评论已完成的单，必须是自己的，如果评论的就不加积分了
	 * @param id
	 * @param comment
	 * @return
	 */
	@RequestMapping("/comment")
	@ResponseBody
	public Object comment(String id,String comment){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u) return 0;
		UserSign dataObj = userSignService.load(Mapper.make("id", id).toHashMap());
		
		if(null == dataObj || dataObj.getState()!='F' || !dataObj.getUserid().equals(u.getPhone())) return 0;
		
		if( userSignService.update(Mapper.make("id", id).put("comment", comment).toHashMap()) >0 ){
			if(dataObj.getComment()==null){
				userService.update(Mapper.make("point", 10)
						.put("phone", u.getPhone())
						.toHashMap());
				u.setPoint(u.getPoint()+10);
			}
			return 1;
		}
		
		return 0;
	}
	
	@RequestMapping("/syscmt")
	@ResponseBody
	public Object sys_comment(Integer id,String comment){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || u.isBusiness() || u.isCommonUser()) return 0;
		
		return userSignService.update(Mapper.make("sys_remark", comment).put("id",id).toHashMap());
	}
	
	/**派件时的群发短信，发送的短信会让订单状态变成E
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/sendmsgs")
	@ResponseBody
	public Object sendMsgs(){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || u.isBusiness() || u.isCommonUser()) return 0;
		
		final List<UserSign> collected = (List<UserSign>) WebUtils.getSessionAttribute(request, "collected");
		Integer beginIndex = (Integer) WebUtils.getSessionAttribute(request, "beginIndex");
		if(null == collected) return 0;
		List<String> strs = new ArrayList<String>();
		strs.add("A区");
		strs.add("B区");
		strs.add("F区");
		
		List<MessageItem<UserSign>> items = new ArrayList<MessageItem<UserSign>>();
		for(int i = 0;i<collected.size();i++){
			UserSign sign=collected.get(i);
			
			if(strs.contains(sign.getAddr_region())) sign.setAddr_building("");
			
			items.add(new MessageItem<UserSign>(i+1+beginIndex, sign));
		}
		
		new MessageUtil.ThPut(items).run();//一个生产者线程，阻塞执行
		//5个消费者线程
		for(int i = 0;i<5;i++)
			new MessageUtil.ThTake(userSignService,webConfig).start();
		
		return new AjaxReturn(true, null);
	}
	
	/**单条短信发送
	 * @param index collected下标，从0开始
	 * @return
	 * @throws InterruptedException 
	 */
	/*	@SuppressWarnings("unchecked")
	@RequestMapping("/sendmsg")
	@ResponseBody
	public Object sendMsg(Integer index) throws InterruptedException{
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || u.isBusiness() || u.isCommonUser()) return 0;
		
		List<UserSign> collected = (List<UserSign>) WebUtils.getSessionAttribute(request, "collected");
		Integer beginIndex = (Integer) WebUtils.getSessionAttribute(request, "beginIndex");
		if(null == collected) return 0;
		List<String> strs = new ArrayList<String>();
		strs.add("A区");
		strs.add("B区");
		strs.add("F区");
		strs.add("C区");
		
		UserSign sign=collected.get(index);
		
		if(strs.contains(sign.getAddr_region())) sign.setAddr_building("");
		
		if( MessageUtil.sendMessage(sign.getPhone(), sign.getAddr_region(),
				sign.getAddr_building(),index+1+beginIndex)){
			userSignService.update(Mapper.make("id", sign.getId()).put("state", 'E').toHashMap());
			//collected.remove(sign);
			return new AjaxReturn(true, null);
		}
		
		return new AjaxReturn(false, sign.getId());
	}*/
	
	/**批量入库，入库订单的状态必须是
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/batchstore")
	@ResponseBody
	public Object batchStore(){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || u.isBusiness() || u.isCommonUser()) return 0;
		
		List<UserSign> collected = (List<UserSign>) WebUtils.getSessionAttribute(request, "collected");

		List<UserSign> failQueue = new ArrayList<UserSign>();
		int successCount = 0,failureCount = 0;
		for(UserSign sign:collected){
			if(sign.getState()=='I'){
				successCount++;
				userSignService.update(Mapper.make("id", sign.getId()).put("state", 'P').toHashMap());
			}
			else{
				failQueue.add(sign);
				failureCount++;
			}
		}
		WebUtils.setSessionAttribute(request, "collected", failQueue);
		return new AjaxReturn(true, "成功"+successCount+"条，失败"+failureCount+"条。");
	}
	
	@ExceptionHandler
	@ResponseBody
	public Object exceptionHandler(Exception exception){
		exception.printStackTrace();
		return new AjaxReturn(false, "抱歉服务其发生了异常，异常说明："+exception.getMessage()+"，请尝试重新操作或者联系客服。");
	}
}
