package com.express.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import com.express.core.bean.User;
import com.express.core.extend.AjaxReturn;
import com.express.core.extend.Mapper;
import com.express.core.extend.Strings;
import com.express.core.extend.wechat.WeChatPay;
import com.express.core.service.UserService;

/**登录工具类
 * @author Danny
 *
 */
public class LoginUtil {
	
	/**ajax登录
	 * @param u
	 * @param userService
	 * @param request
	 * @return
	 */
	public static Object ajaxlogin(User u,UserService userService,HttpServletRequest request){
		if(Strings.isEmpty(u.getPhone())) return new AjaxReturn(false, "手机号不能为空。");
		User user = userService.load(Mapper.make("phone",u.getPhone()).toHashMap());
		if(null != user && user.getPassword()!=null && user.getPassword().equals(u.getPassword())){
			WebUtils.setSessionAttribute(request, "me", user);
			return new AjaxReturn(true, null);
		}
		else return new AjaxReturn(false, "用户名或密码不正确，请重新登录！");
	}
	
	/**提取cookie中保存的用户名密码登录
	 * @param request
	 * @return 返回是否登录成功,null表示失败
	 */
	private static User loginFromCookie(UserService userService,HttpServletRequest request){
		
		User u = new User();
		Cookie YHM = WebUtils.getCookie(request, "YHM"),
				MIM = WebUtils.getCookie(request, "MIM");
		if( null == YHM || MIM == null) return null;
		
		if(Strings.isBlank(YHM.getValue()) || Strings.isBlank(MIM.getValue())){
			return null;
		}
		u.setPhone(YHM.getValue());
		u.setPassword(MIM.getValue());
		
		return ((AjaxReturn)ajaxlogin(u,userService,request)).getOk()?u:null;
	}
	
	/**获取用户的session登录信息，如果未登录则从cookie中寻找用户名和密码
	 * @param UserService 传null则不会在cookie中找
	 * @return
	 */
	public static User getLoginUser(UserService userService,HttpServletRequest request){
		User sesUser = (User) WebUtils.getSessionAttribute(request, "me");
		
		if(null == sesUser) return null;
		
		return sesUser;
	}
	
	/**根据微信的code码获取openid，然后登录
	 * @param code
	 * @return
	 */
	public static User login(HttpServletRequest request,String code,UserService userService){
		String openid = WeChatPay.getWechatOpenId(request, code);
		if(Strings.isBlank(openid)) return null;
		User u = userService.load(Mapper.make("openid", openid).toHashMap());
		if(null == u) return null;
		
		WebUtils.setSessionAttribute(request, "me", u);
		return u;
	}
	
}
