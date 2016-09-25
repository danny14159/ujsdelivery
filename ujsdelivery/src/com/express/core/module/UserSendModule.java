package com.express.core.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.bean.User;
import com.express.core.bean.UserSend;
import com.express.core.service.BaseService;
import com.express.core.service.UserSendService;
import com.express.core.service.UserService;
import com.express.core.util.LoginUtil;

@Controller
@RequestMapping("/usend")
public class UserSendModule extends BasicModule<UserSend>{
	@Autowired
	private UserSendService userSendService;
	@Autowired
	private UserService userService;
	
	@Override
	protected BaseService<UserSend> getService() {
		return userSendService;
	}

	@Override
	public Object delete(Integer id) {
		return null;
	}

	@Override
	@Transactional
	public Object insert(UserSend obj, BindingResult br) throws Exception {
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u){
			return -1;
		}
		obj.setUserid(u.getPhone());
		
		return super.insert(obj, br);
		
	}
}
