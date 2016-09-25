package com.express.core.module;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.express.core.bean.Config;
import com.express.core.bean.User;
import com.express.core.extend.AjaxReturn;
import com.express.core.extend.Mapper;
import com.express.core.extend.wechat.WechatTemplateMessage;
import com.express.core.service.ConfigService;
import com.express.core.service.UserService;
import com.express.core.service.UserSignService;
import com.express.core.util.LoginUtil;

@Controller
@RequestMapping("/app/act")
public class ActivityModule{
	
	public static final String DRAW_TIMES = "drawTimes";
	
	int[] rewards = new int[]{5,18,30};
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserSignService userSignService;
	@Autowired
	private ConfigService configService;

//	@RequestMapping("/1111")
	public String doubleEleven(HttpServletRequest request){
		return "core/1111";
	}
	
	/**抽奖机会
	 * @return
	 */
/*	@RequestMapping("/draw")
	@ResponseBody*/
	synchronized public Object draw(HttpSession session,HttpServletRequest request){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u) return new AjaxReturn(false, "请先登录");
		
		//先去session里面看看有没有抽奖机会
		Integer drawTimes = (Integer) WebUtils.getSessionAttribute(request, DRAW_TIMES);
		if(null == drawTimes || drawTimes <= 0){
			return new AjaxReturn(false, "抱歉，您暂时没有抽奖机会！");
		}
		
		//看看是不是今天的第一单
		if(userSignService.countUserSign(u.getPhone()) > 1){
			return new AjaxReturn(false, "Sorry，每人当日抽奖次数仅限一次！");
		}
		drawTimes--;
		WebUtils.setSessionAttribute(request, DRAW_TIMES,drawTimes);
		
		//计算各奖品概率
		Random r = new Random();
		int rate = r.nextInt(100);
		AjaxReturn ret = null;
		if(rate < 10 ){
			//一等奖
			ret = win(u, 100);
		}
		else if(rate < 30){
			//二等奖
			ret = win(u, 80);
		}
		else if(rate < 50){
			//三等奖
			ret = win(u, 50);
		}
		else{
			//幸运奖
			ret = win(u, 20);
		}
		return ret;
	}
	
	public AjaxReturn win(User u,Integer point){
		userService.update(Mapper.make("phone", u.getPhone()).put("point", point).toHashMap());
		WechatTemplateMessage.sendTemplateMsgReward(u.getOpenid(), "5·20幸运大转盘", u.getName(),"幸运奖"+point+"积分");
		return new AjaxReturn(true, "恭喜你抽到幸运奖"+point+"积分");
	}
	
	@Deprecated
	public boolean winReward(String key,String phone){
		int keyNum = Integer.parseInt(key);
		int maxNum = rewards[keyNum];
		int existNum = configService.count(Mapper.make("type", "act").put("key", key).toHashMap());
		if(existNum >= maxNum){
			 
			return false;
		}
		else{
			Config config = new Config();
			config.setType("act");
			config.setKey(key);
			config.setValue(phone);
			configService.insert(config);
			return true;
		}
	}
}
