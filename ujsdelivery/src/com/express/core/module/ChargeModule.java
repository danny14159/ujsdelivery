package com.express.core.module;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.express.core.bean.ChargeRecord;
import com.express.core.bean.User;
import com.express.core.extend.AjaxReturn;
import com.express.core.extend.Mapper;
import com.express.core.service.ChargeRecordService;
import com.express.core.service.UserService;
import com.express.core.util.LoginUtil;

@Controller
@RequestMapping("/app/back/charge")
public class ChargeModule {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserService userService;

	@Autowired
	private ChargeRecordService chargeRecordService;

	@RequestMapping(value = "/ins")
	@ResponseBody
	@Transactional
	public Object insert(ChargeRecord record) {

		if (Strings.isEmpty(record.getTarget_user())) {
			return new AjaxReturn(false, "input target user");
		}
		if (record.getPoint() == null) {
			return new AjaxReturn(false, "input point");
		}

		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || !u.isPointManager()) {
			return new AjaxReturn(false, "login");
		}
		record.setCreate_time(new Date());
		record.setCreate_user(u.getName());

		userService.update(Mapper.make("phone", record.getTarget_user()).put("point", record.getPoint()).toHashMap());

		User target = userService.load(Mapper.make("phone", record.getTarget_user()).toHashMap());
		if (target != null) {

			record.setTarget_user(record.getTarget_user() + "-" + target.getName());
		}
		else{
			return new AjaxReturn(false, "用户不存在");
		}
		return AjaxReturn.ok(chargeRecordService.insert(record), target.getPoint()+"", "插入失败");
	}

	@RequestMapping("")
	public String list(ChargeRecord record, Model model) {
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || !u.isPointManager()) {
			return "redirect:/app/login";
		}
		
		model.addAttribute("list", chargeRecordService.list(record));
		model.addAttribute("target_user", record.getTarget_user());
		return "back/chargeManager";
	}
	
	@RequestMapping("/get")
	@ResponseBody
	public Object getPoint(@RequestParam 
			String phone){
		
		User target = userService.load(Mapper.make("phone", phone).toHashMap());
		if(null == target) return new AjaxReturn(false, "用户不存在");
		return new AjaxReturn(true, target.getPoint()+"");
	}
}
