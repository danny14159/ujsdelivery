package com.express.core.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.express.core.bean.User;
import com.express.core.extend.AjaxReturn;
import com.express.core.extend.Mapper;
import com.express.core.service.BaseService;
import com.express.core.service.UserService;
import com.express.core.util.LoginUtil;

@Controller
@RequestMapping("/u")
public class UserModule extends BasicModule<User>{
	@Autowired
	private UserService userService;
	
	@Override
	protected BaseService<User> getService() {
		return userService;
	}

	@Override
	public Object delete(Integer id) {
		return null;
	}

	@Override
	public Object insert(User obj, BindingResult br) {
		/*int ret = (Integer) super.insert(obj, br);
		return AjaxReturn.ok(ret, "注册成功", "注册失败。请尝试不同的用户名");*/
		return null;
	}

	
	/* 
	 *这里面不允许修改积分
	 */
	public Object update(User obj, BindingResult br) {
		User u=LoginUtil.getLoginUser(userService,request);
		if(null == u) return 0;
		
		//代寄商家不允许改用户名
		if(u.isBusiness()){
			obj.setName(null);
		}
		obj.setPoint(null);
		obj.setPhone(u.getPhone());
		obj.setOpenid(u.getOpenid());
		if((Integer)super.update(obj, br)>0){
			WebUtils.setSessionAttribute(request, "me", u);
			return 1;
		}
		return 0;
	}
	
	
	@RequestMapping("/mp")
	@ResponseBody
	public Object minusPoint(@RequestParam String phone,
			@RequestParam Integer point){
		//只允许管理员访问
		/*User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || !u.isPointManager()) return new AjaxReturn(false, null);
		
		Object ret = 1;
		User user = null;
		if(point!=0){
			user = new User();
			user.setPhone(phone);
			user.setPoint(point);
			ret = super.update(user, null);
		}
		if((Integer)ret >0) {
			user = userService.load(Mapper.make("phone", phone).toHashMap());
			//SystemModule.invalidateUser(userService, request);
			return new AjaxReturn(true, user==null?"用户不存在":user.getPoint()+"");
		}*/
		return new AjaxReturn(false, null);
	}
	@RequestMapping("/mdpass")
	@ResponseBody
	public Object modifypass(@RequestParam String oldpass,
			@RequestParam String newpass){
		User u = LoginUtil.getLoginUser(userService,request);
		if(u.getPassword().equals(oldpass)){
			u.setPassword(newpass);
			return super.update(u, null);
		}
		return 0;
	}
	
	@Transactional
	public void test(){
		User u = new User();
		u.setName("denglei");
		u.setPhone("a12563");
		userService.insert(u);
		
		//int a = 5/0;
		u.setName("denglei");
		u.setPhone("b12563");
		userService.insert(u);
	}
}
