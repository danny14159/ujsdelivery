package com.express.core.module;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.RestTest;
import com.express.core.bean.SignTimes;
import com.express.core.bean.User;
import com.express.core.bean.UserSign;
import com.express.core.extend.AjaxReturn;
import com.express.core.extend.ExpressQuery;
import com.express.core.extend.Mapper;
import com.express.core.extend.Strings;
import com.express.core.extend.Webs;
import com.express.core.extend.wechat.Config;
import com.express.core.extend.wechat.WeChatPay;
import com.express.core.extend.wechat.WechatTemplateMessage;
import com.express.core.service.ReqRecordService;
import com.express.core.service.UserSendService;
import com.express.core.service.UserService;
import com.express.core.service.UserSignService;
import com.express.core.util.LoginUtil;
import com.google.gson.Gson;

@Controller
@RequestMapping("/app")
public class SystemModule {

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private UserService userService;
	@Autowired
	private UserSendService userSendService;
	@Autowired
	private UserSignService userSignService;
	@Autowired
	private ReqRecordService reqRecordService;
	/*
	 * @Autowired private WebConfig webConfig;
	 */

	ThreadLocal<String> msgstore = new ThreadLocal<String>();

	private static final String REFRESH_STATE = "REFRESH_STATE";

	/**
	 * 首单开始计算的时间
	 */
	private static final String FIRST_ORDER_FROM = "2016-09-01";

	public static Date FIRST_ORDER_DATE = null;

	static {
		try {
			FIRST_ORDER_DATE = new SimpleDateFormat("yyyy-MM-dd").parse(FIRST_ORDER_FROM);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新服务器状态
	 */
	public static void update_state(HttpServletRequest request) {
		ServletContext application = request.getServletContext();
		application.setAttribute(REFRESH_STATE, new Date().getTime());
	}

	/**
	 * 重新获取session中的User并返回
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * public static User invalidateUser(UserService userService,
	 * HttpServletRequest request) { User u =
	 * LoginUtil.getLoginUser(userService, request); if (null == u) return null;
	 * User newUser = userService.load(u.getPhone());
	 * WebUtils.setSessionAttribute(request, "me", newUser); return newUser; }
	 */

	@RequestMapping(value = "")
	public String defaultindex() {
		return "../index";
	}

	/**
	 * 代寄页面
	 * 
	 * @return
	 */

	@RequestMapping(value = "/sendinternal", method = RequestMethod.GET)
	public String send(Model model, String code) {
		if (Strings.isBlank(code)) {
			return "core/404";
		}
		if (isSuspend()) {
			return "suspend";
		}
		String openid = WeChatPay.getWechatOpenId(request, code);
		User u = LoginUtil.getLoginUser(userService, request);

		if (null != u && u.getOpenid() == null && Strings.isNotBlank(openid)) {
			userService.update(Mapper.make("phone", u.getPhone()).put("openid", openid).toHashMap());
		}
		u = LoginUtil.login(request, code, userService);
		if (null == u) {
			return "core/bindPhone";
		}
		model.addAttribute("bacco", userService.listBusinessAccount());

		return "core/send";
	}

	/**
	 * 判断网站是否暂停服务
	 * 
	 * @return
	 */
	private Boolean isSuspend() {
		Boolean suspend = (Boolean) (request.getServletContext().getAttribute("suspend"));
		if (null == suspend)
			return false;
		return suspend;
	}

	@RequestMapping("/sign")
	public String signForWechat() {
		if (isSuspend()) {
			return "suspend";
		}
		return getWechatUrl("signinternal");
	}

	@RequestMapping("/send")
	public String sendForWechat() {
		if (isSuspend()) {
			return "suspend";
		}
		return getWechatUrl("sendinternal");
	}

	/**
	 * 代取页面 内部处理。若没有带code（即由微信打开，则跳转404）
	 * 
	 * @return
	 */
	@RequestMapping("/signinternal")
	public String sign(Model model, String code) {
		User u = LoginUtil.getLoginUser(userService, request);
		if(null!=u && u.isManager()){
		}
		else{
			
			if (isSuspend()) {
				return "suspend";
			}
			// 1.如果没有code，跳转至404
			// 2.拿到code，拿到openid，并把openid存到session中
			// 3.查找cookie中的用户名，如果有则并将用户名与openid相关联
			// 如果没有则跳转到完善信息页面
			if (Strings.isBlank(code)) {
				return "core/404";
			}
			String openid = WeChatPay.getWechatOpenId(request, code);
			if (null != u && u.getOpenid() == null && Strings.isNotBlank(openid)) {
				userService.update(Mapper.make("phone", u.getPhone()).put("openid", openid).toHashMap());
			}
			u = LoginUtil.login(request, code, userService);
			if (null == u) {
				return "core/bindPhone";
			}
		}
		return "core/sign";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginpage(String redirect, Boolean decode, Model model) {
		model.addAttribute("redirect", redirect);
		return "../login";
	}

	@RequestMapping("/logout")
	public String logout() {
		request.getSession().invalidate();
		return "redirect:/app/?info=1002";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(User u, String redirect) {
		if (Strings.isEmpty(u.getPhone()))
			return "redirect:../login.jsp";
		User user = userService.load(Mapper.make("phone", u.getPhone()).toHashMap());
		if (null != user && user.getPassword().equals(u.getPassword())) {
			WebUtils.setSessionAttribute(request, "me", user);
			if (!"/".equals(redirect)) {
				return "redirect:" + redirect;
			}
			if (user.isCommonUser())
				return "redirect:/index.jsp";
			else if (user.isManager() || user.isBusiness())
				return "redirect:/app/back";
			else if (user.isPointManager())
				return "redirect:/app/back/charge";
			else
				return "redirect:../login.jsp";
		} else
			return "redirect:../login.jsp";
	}

	@RequestMapping(value = "/ajaxlogin")
	@ResponseBody
	public Object ajaxlogin(User u) {
		if (Strings.isEmpty(u.getPhone()))
			return new AjaxReturn(false, "手机号不能为空。");
		User user = userService.load(u.getPhone());
		if (null != user && user.getPassword().equals(u.getPassword())) {
			WebUtils.setSessionAttribute(request, "me", user);
			return new AjaxReturn(true, null);
		} else
			return new AjaxReturn(false, "用户名或密码不正确，请重新登录！");
	}

	/*
	 * @RequestMapping(value = "/signup", method = RequestMethod.GET) public
	 * String signup() { return "forward:/signup.jsp"; }
	 */

	/**
	 * 注册表单提交地址POST
	 * 
	 * @param u
	 * @return
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@Transactional
	public String signup(User u, Model model) {
		// 判断是否已经登录，已登录则修改个人信息
		User me = LoginUtil.getLoginUser(userService, request);
		// 已登录
		if (me != null) {

			User param = new User();
			param.setPhone(me.getPhone());
			param.setName(u.getName());
			param.setAddr_region(u.getAddr_region());
			param.setAddr_building(u.getAddr_building());
			userService.update(param);

			model.addAttribute("info", "用户信息修改成功！");
			return "forward:/app";
		}
		// 未登录

		if (null != userService.load(Mapper.make("phone", u.getPhone()).toHashMap())) {
			model.addAttribute("info", "很抱歉该手机号已经被注册了哦~请检查号码是否输错或者尝试");
			model.addAttribute("phone", u.getPhone());
			return "core/bindPhone";
		}
		String openid = WeChatPay.getWechatOpenId(request, null);
		if (Strings.isBlank(openid)) {
			return "core/404";
		}

		u.setOpenid(openid);
		u.setPoint(null);
		// 在数据库里找这条记录，如果有则修改，没有则插入
		if (userService.count(Mapper.make("openid", openid).toHashMap()) > 0) {
			userService.update(u);
			WebUtils.setSessionAttribute(request, "me", u);
			model.addAttribute("info", "用户信息修改成功！");
			return "forward:/app";
		}
		try {
			userService.insert(u);
			WebUtils.setSessionAttribute(request, "me", u);
			model.addAttribute("info", "恭喜你注册成功！");
			return "forward:/app";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("info", "注册失败，请稍后重试。若多次尝试失败，请联系微信客服15751002197");
			return "forward:/app/profile";
		}
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf_day = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 快递后台
	 * 
	 * @param model
	 * @param us
	 * @param ps
	 * @param pn
	 * @param type
	 * @param freeNumber
	 *            免单数量
	 * @param freeMessage
	 *            免单消息 代寄send或者代取send
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping({ "/back" })
	public String expressBack(Model model, @ModelAttribute UserSign us, String repeat, Integer ps, Integer pn,
			String type, String from_sign_time, String to_sign_time, String send_time, Integer freeNumber,
			String freeMessage, String sys_remark) throws ParseException {
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || (!u.isManager() && !u.isBusiness() && !u.isPointManager()))
			return "redirect:/app/login?redirect=/app/back";

		if (Strings.isBlank(type))
			type = "sign";

		// 在application中维护一个刷新状态
		/*
		 * ServletContext application = request.getServletContext(); Long
		 * refresh_state = (Long) application.getAttribute(REFRESH_STATE); if
		 * (refresh_state == null) { refresh_state = new Date().getTime();
		 * application.setAttribute(REFRESH_STATE, refresh_state); }
		 * model.addAttribute("refresh_state", refresh_state);
		 */

		// 今日订单：10 未处理代取：5 未处理代寄：0
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 只给管理员看的数据
		if (u.isManager()) {
			int today_total = userSignService.count(Mapper.make("from_sign_time", sdf.format(new Date()))
					.put("to_sign_time", sdf.format(new Date(new Date().getTime() + 24l * 60l * 60l * 1000l)))
					.toHashMap())
					+ userSendService.count(Mapper.make("from_sign_time", sdf.format(new Date()))
							.put("to_sign_time", sdf.format(new Date(new Date().getTime() + 24l * 60l * 60l * 1000l)))
							.toHashMap());
			int processed_sign = userSignService.count(Mapper.make("state", "S").toHashMap());
			int processed_send = userSendService.count(Mapper.make("state", "S").toHashMap());
			model.addAttribute("today_total", today_total);
			model.addAttribute("processed_sign", processed_sign);
			model.addAttribute("processed_send", processed_send);
		}

		if (null == pn)
			pn = 1;
		if (null == ps)
			ps = 100;

		/*try {
			// 尝试吧name转换成id
			us.setName(Integer.parseInt(us.getName(), 36) + "");
		} catch (Exception e) {
			System.out.println("转换失败");
		}*/

		Mapper param = Mapper.pageTransfer(pn, ps).put("state", us.getState())
				.put("express", Strings.isBlank(us.getExpress()) ? null : us.getExpress())
				.put("name", Strings.isBlank(us.getName()) ? null : us.getName())
				.put("addr_region", Strings.isBlank(us.getAddr_region()) ? null : us.getAddr_region())
				.put("addr_building", Strings.isBlank(us.getAddr_building()) ? null : us.getAddr_building())
				.put("from_sign_time", Strings.isBlank(from_sign_time) ? null : from_sign_time)
				.put("to_sign_time", Strings.isBlank(to_sign_time) ? null : to_sign_time)
				.put("sys_remark", Strings.isBlank(sys_remark) ? null : sys_remark)
				.put("send_time", Strings.isBlank(send_time) ? null : send_time);
		if (us.getIs_free() != null) {
			param.put("is_free", us.getIs_free().equals('Y') ? 1 : null).put("is_not_free",
					us.getIs_free().equals('N') ? 1 : null);
		}

		List<?> list = null;
		int totalCount = 0;

		if (repeat == null) {
			if (u.isBusiness()) {
				param.put("express", u.getName());
			}

			if ("sign".equals(type)) {
				list = userSignService.list(param.toHashMap());
				totalCount = userSignService.count(param.toHashMap());
			}
			if ("send".equals(type)) {
				list = userSendService.list(param.toHashMap());
				totalCount = userSendService.count(param.toHashMap());
			}
		} else {
			list = userSignService.searchForDuplicate();
		}

		// 判断该单是不是首单，指定一个时间
		/*
		 * for(UserSign sign:list){ sign.setIsFirst(this.isFirstOrder(sign,
		 * FIRST_ORDER_DATE)); }
		 */

		// 加入今明后三天
		java.util.Date now = new java.util.Date();
		String today = sdf.format(now);
		calendar.setTime(now);

		String tomorrow = getNextDay();
		String afterTommorrow = getNextDay();

		model.addAttribute("today", today);
		model.addAttribute("tomorrow", tomorrow);
		model.addAttribute("afterTommorrow", afterTommorrow);
		model.addAttribute("send_time", send_time);

		model.addAttribute("list", list);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("pageCount", (int) Math.ceil((double) totalCount / ps));
		model.addAttribute("ps", ps);
		model.addAttribute("pn", pn);
		model.addAttribute("from_sign_time", from_sign_time);
		model.addAttribute("to_sign_time", to_sign_time);
		model.addAttribute("us", us);
		model.addAttribute("isManager", u.isManager());
		model.addAttribute("sys_remark", sys_remark);

		model.addAttribute("todayFreeOrders", userSignService.todayFreeOrders());
		model.addAttribute("todaySignNum", userSignService.todaySignNum());
		model.addAttribute("todayFinishedNum", userSignService.todayFinishedNum());

		model.addAttribute("order_pattern", Webs.ORDER_PATTERN);

		// model.addAttribute("cntDuplicate",
		// userSignService.countSearchForDuplicate());

		// model.addAttribute("sign_people_num", userService.count(null));
		// //注册用户数

		return "back/back";
	}

	/**
	 * ajax请求刷新状态，true表示需要刷新,false表示不需要刷新
	 * 
	 * @param currState
	 * @return
	 */
	@RequestMapping("/rb")
	@ResponseBody
	public Object refresh_back(@RequestParam Long currState) {
		ServletContext application = request.getServletContext();
		Long refresh_state = (Long) application.getAttribute(REFRESH_STATE);

		if (refresh_state != null && currState < refresh_state) {
			return true;
		}
		return false;
	}

	/**
	 * 商家后台页面
	 * 
	 * @param model
	 * @param state
	 * @return
	 */
	/*
	 * @RequestMapping("/bback") public String businessBack(Model
	 * model,Character state){ User u = SystemModule.getLoginUser(request);
	 * if(null == u || u.getType()!='B')return
	 * "redirect:/app/login?redirect=/app/bback";
	 * 
	 * model.addAttribute("me", u); model.addAttribute("state", state);
	 * Map<String,Object> par = Mapper.make("state", state). put("express",
	 * u.getName()) .toHashMap(); model.addAttribute("list",
	 * userSendService.list(par)); return "back/businessback"; }
	 */

	@RequestMapping("/myback")
	public String mybackForWechat() {
		return getWechatUrl("mybackinternal");
	}

	/**
	 * 我的订单页面
	 * 
	 * @param model
	 * @param state
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/mybackinternal")
	public String myback(Model model, String code) {
		System.out.println("code:"+code);
		if (Strings.isBlank(code)) {
			return "core/404";
		}
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u) {
			u = LoginUtil.login(request, code, userService);
			if (u == null) {
				return "core/bindPhone";
			}
		}
		Map<String, Object> par = Mapper.pageTransfer(1, 7).put("userid", u.getPhone()).toHashMap();
		model.addAttribute("list", userSignService.list(par));
		model.addAttribute("list2", userSendService.list(par));
		model.addAttribute("gateway", Config.GATEWAY);
		// 是否签到过
		boolean signed = u.getLast_sign() == null || u.getLast_sign().getDate() != new Date().getDate();
		model.addAttribute("signed", signed);

		return "back/myback";
	}

	@RequestMapping("/profile")
	public String profileForWechat() {
		return getWechatUrl("profileinternal");
	}

	/**
	 * 用户个人信息完善，修改
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/profileinternal")
	public String profile(Model model) {
		/*
		 * String openid = WeChatPay.getWechatOpenId(request, null); if (openid
		 * == null) { return "core/404"; }
		 */
		LoginUtil.getLoginUser(userService, request);

		model.addAttribute("profile", true);
		return "core/profile";
	}

	@RequestMapping("/comments")
	public String comments() {
		return "core/comments";
	}

	/**
	 * 每日签到加5分，从四点开始计算签到排名
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/reg")
	@ResponseBody
	public synchronized Object register() {
		final User u = LoginUtil.getLoginUser(userService, request);
		AjaxReturn ret = null;
		if (null == u)
			return "redirect:../login.jsp";

		final Date sign_time = new Date();

		if (u.getLast_sign() != null && u.getLast_sign().getDate() == new Date().getDate()) {
			if (u.getLast_sign().getHours() >= 0 && u.getLast_sign().getHours() < 6) {

				ret = new AjaxReturn(false, "今天已经签到过了，排名从每天6点开始统计");
			} else {
				ret = new AjaxReturn(false, "今天已经签到过了，您是第" + userService.todaySignOrder(u.getPhone()) + "位签到的用户");
			}
		} else {
			int result = userService
					.update(Mapper.make("last_sign", sign_time).put("phone", u.getPhone()).put("point", 5).toHashMap());
			int order = userService.todaySignOrder(u.getPhone());
			if (sign_time.getHours() < 6) {
				ret = AjaxReturn.ok(result, "签到成功，恭喜你获得5积分，排名从每天6点开始统计", "系统繁忙，稍后再试");
			} else {
				ret = AjaxReturn.ok(result, "签到成功，恭喜你获得5积分，您是今天第" + order + "位签到的用户", "系统繁忙，稍后再试");
/*				if (order <= 10) {
					new Thread() {
						public void run() {
							// userService.update(Mapper.make("phone",
							// u.getPhone()).put("point", 10).toHashMap());
							// 发送代金券
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
							WechatTemplateMessage.sendTemplateMsgReward(u.getOpenid(), "早起签到TOP10", u.getName(),
									"免单一次，仅限" + sdf.format(sign_time) + "给 " + u.getName() + " 同学使用");
						};
					}.start();
					ret = AjaxReturn.ok(result, "签到成功，恭喜你获得5积分，您是今天第" + order + "位签到的用户，获得一次免单机会，请在微信号查收", "系统繁忙，稍后再试");
				} else {
					ret = AjaxReturn.ok(result, "签到成功，恭喜你获得5积分，您是今天第" + order + "位签到的用户", "系统繁忙，稍后再试");
				}
*/			}
			u.setLast_sign(sign_time);
			u.setPoint(u.getPoint() + 5);
		}
		List<User> order = userService.showSignOrder();
		ret.setData(order);
		return ret;
	}

	/**
	 * 获取手机号的验证码
	 * 
	 * @param phoneNum
	 * @return
	 */
	@RequestMapping("/regi")
	@ResponseBody
	public Object regi(@RequestParam String phoneNum, HttpSession session) {
		Object msgexpire = session.getAttribute("msgexpire");
		if (msgexpire != null && new Date().getTime() < (Long) msgexpire) {
			return new AjaxReturn(false, "60s以后再重新发送验证码");
		}
		if (!phoneNum.matches("^\\d{11}$"))
			return new AjaxReturn(false, "请输入规范的手机号！");
		Random r = new Random(new Date().getTime());
		String rand = "";
		for (int i = 0; i < 5; i++) {
			char c = (char) ('0' + r.nextInt(10));
			rand += c;
		}
		System.out.println("短信验证码" + rand);
		// 解除此注释即可发送短信验证码
		RestTest.testTemplateSMS(true, phoneNum, rand);
		session.setAttribute("msgexpire", new Date().getTime() + 60 * 1000); // 设置不能发送验证码的期限
		session.setAttribute("msg", rand);
		return new AjaxReturn(true, null);
	}

	public static void main(String[] args) {
		RestTest.testTemplateSMS(true, "15618910573", "1234");
	}

	/*
	 * @RequestMapping("/resetpass")
	 * 
	 * @ResponseBody public Object resetpass(@RequestParam String
	 * phone, @RequestParam String msg, HttpSession session,
	 * 
	 * @RequestParam String newpass) { if (!Strings.isBlank(msg) &&
	 * !Strings.isBlank(phone) && !Strings.isBlank(newpass) &&
	 * msg.equals(session.getAttribute("msg"))) { User u = new User();
	 * u.setPhone(phone); u.setPassword(newpass); u.setPoint(null); return
	 * userService.update(u); } return 0; }
	 */

	/**
	 * 认证手机号
	 * 
	 * @param msg
	 * @param phone
	 * @return
	 */
	@RequestMapping("/authmobile")
	@ResponseBody
	@Transactional
	public Object authmobile(/* @RequestParam */ String msg, @RequestParam String phone) {

		try {
			String sesMsg = (String) WebUtils.getSessionAttribute(request, "msg");
			System.out.println(sesMsg);
			// if (sesMsg.equals(msg)) {

			String openid = WeChatPay.getWechatOpenId(request, null);
			if (Strings.isBlank(openid)) {

				return new AjaxReturn(false, "请从微信打开页面，谢谢配合。");
			}
			// int existsOID = userService.count(Mapper.make("openid",
			// openid).toHashMap());
			int existsPhone = userService.count(Mapper.make("phone", phone).toHashMap());

			// if (existsOID > 0)
			// return new AjaxReturn(false, "您已绑定手机号，请勿重复绑定");
			userService.unBindPhone(Mapper.make("openid", openid).toHashMap());

			if (existsPhone > 0) {
				// 如果该手机号存在，则修改
				return new AjaxReturn(false, "您已绑定手机号，请勿重复绑定，如需换绑请联系客服。");
				// userService.update(Mapper.make("phone", phone).put("openid",
				// openid).toHashMap());
			} else {
				// 否则插入这条记录
				User user = new User();
				user.setOpenid(openid);
				user.setPhone(phone);
				user.setPoint(0);
				user.setType("C");
				userService.insert(user);
			}
			return new AjaxReturn(true, null);
			// }

			// return new AjaxReturn(false, "验证码错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new AjaxReturn(false, "system error");

		}
	}

	/*
	 * @RequestMapping("/install")
	 * 
	 * @ResponseBody
	 */
	public Object install(@RequestParam String sql) {

		/*
		 * User u = SystemModule.invalidateUser(userService, request); if(null
		 * == u || u.getType()!='M') return "redirect:../login.jsp";
		 */

		String user = "root";// 用户名
		String password = "Cx3yMoa8Av4u";// 数据库密码
		// String password="804956748";//数据库密码
		String url = "jdbc:mysql://localhost:3306/express";// 数据库URL

		String current_sql = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, password);
			Statement state = con.createStatement();
			String[] sqls = sql.split(";");
			for (String each_sql : sqls) {
				current_sql = each_sql;
				state.executeUpdate(current_sql);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return new AjaxReturn(false, e.getMessage() + ",sql=" + current_sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return new AjaxReturn(false, e.getMessage() + ",sql=" + current_sql);
		}
		return new AjaxReturn(true, sql);
	}

	@RequestMapping("/std")
	@ResponseBody
	public Object retrieveStandard(@RequestParam String express) {
		String u = userService.loadStd(express);
		if (null != u) {
			return new AjaxReturn(true, u);
		}
		return new AjaxReturn(false, null);
	}

	/**
	 * 订单分析页面
	 * 
	 * @param queryDate
	 *            到月份就行，如2015-02
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/analysis")
	public String orderAnalysis(String year, String month, Model model) throws ParseException {

		Gson g = new Gson();
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || (!u.isManager() && !u.isSuperManager()))
			return "redirect:/app/login?redirect=/app/back";

		if (!Strings.isBlank(year) && !Strings.isBlank(month)) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			// 查询这个月有多少天
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(year + "-" + month + "-" + "01"));
			int cdays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			Date currday = null;

			int[] csignList = new int[cdays];
			int[] csendList = new int[cdays];
			// int[] orderPerHour = new int[24]; //24小时下单量统计图

			for (int i = 0; i < cdays; i++) {
				cal.set(Calendar.DAY_OF_MONTH, i + 1);
				currday = cal.getTime();

				// 两个查询拥有共同的参数
				Object param = Mapper.make("from_sign_time", sdf.format(currday))
						.put("to_sign_time", sdf.format(new Date(currday.getTime() + 24l * 60l * 60l * 1000l)))
						.toHashMap();
				int csign = userSignService.count(param);
				int csend = userSendService.count(param);

				csignList[i] = csign;
				csendList[i] = csend;
			}
			model.addAttribute("csignList", g.toJson(csignList));
			model.addAttribute("csendList", g.toJson(csendList));
			model.addAttribute("cdays", cdays);

		}

		// 统计24小时单量
		int[] numsOf24h = new int[24];
		for (int i = 0; i < 24; i++) {

			numsOf24h[i] = userSignService.countByHour(i);
		}

		model.addAttribute("year", year);
		model.addAttribute("numsOf24h", g.toJson(numsOf24h));
		model.addAttribute("month", month);
		return "back/orderAnalysis";
	}

	/**
	 * 下单次数用户统计
	 * 
	 * @param from_date
	 * @param to_date
	 * @param overTimes
	 * @param model
	 * @return
	 */
	@RequestMapping("/signTimes")
	public String signTimes(String from_date, String to_date, Integer overTimes, Model model) {
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || (!u.isManager() && !u.isSuperManager()))
			return "redirect:/app/login?redirect=/app/signTimes";

		if (Strings.isNotBlank(from_date) && Strings.isNotBlank(to_date) && overTimes != null) {
			List<SignTimes> list = userService.listSignTimes(Mapper.make("from_date", from_date).put("to_date", to_date)
					.put("overTimes", overTimes).toHashMap());

			model.addAttribute("list", list);
		}

		model.addAttribute("from_date", from_date);
		model.addAttribute("to_date", to_date);
		model.addAttribute("overTimes", overTimes);
		return "back/signTimes";
	}

	@RequestMapping("/sus")
	@ResponseBody
	public Object suspendService(@RequestParam Boolean suspend, String reason) {
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || !u.isManager())
			return new AjaxReturn(false, "no access");

		ServletContext application = request.getServletContext();
		application.setAttribute("suspend", suspend);
		application.setAttribute("suspendReason", reason);

		return new AjaxReturn(true, null);
	}

	/**
	 * 判断该订单是否是自fromDate以来的首单
	 * 
	 * @param sign
	 * @param fromDate
	 * @return
	 */
	public boolean isFirstOrder(UserSign sign, Date fromDate) {

		if (sign.getSign_time().getTime() < fromDate.getTime()) {
			return false;
		} else {
			int countAfter = userSignService.count(Mapper.make("from_sign_time", sdf.format(fromDate))
					.put("to_sign_time", sdf.format(sign.getSign_time())).put("userid", sign.getUserid())
					.put("inState", 1).toHashMap());
			if (countAfter > 0) {
				userSignService.update(Mapper.make("id", sign.getId()).put("isFirst", 'N').toHashMap());
				return false;
			} else {
				userSignService.update(Mapper.make("id", sign.getId()).put("isFirst", 'Y').toHashMap());
				return true;
			}
		}
	}

	@RequestMapping("/req")
	public String reqRecord(Model model, String key) {
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || !u.isManager())
			return "redirect:/app/back";

		model.addAttribute("list", reqRecordService.list(Mapper.make("key", key).toHashMap()));
		return "back/reqrecord";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/qr/{qrcode}")
	public String collectQrCode(@PathVariable() String qrcode, HttpSession session, boolean del, boolean clear,
			boolean setnull, Model model, @RequestParam(value = "bi", required = false) Integer beginIndex) {
		User u = LoginUtil.getLoginUser(userService, request);
		if (null == u || (!u.isManager() && !u.isEmployee()))
			return "redirect:/app/login?redirect=/app/qr/" + qrcode;

		if (null == beginIndex)
			beginIndex = (Integer) session.getAttribute("beginIndex");
		if (null == beginIndex)
			beginIndex = 0;

		List<UserSign> collected = (List<UserSign>) WebUtils.getSessionAttribute(request, "collected");
		if (null == collected) {
			collected = new LinkedList<UserSign>();
		}
		Mapper param = Mapper.make();
		UserSign signObj = new UserSign(qrcode);

		// 清空列表
		if (clear) {
			collected.clear();
		} else {

			// 删除编号
			if (del) {
				collected.remove(signObj);
			} else if (setnull) {
				collected.set(collected.indexOf(signObj), signObj);
			} else if (!"-1".equals(qrcode)) {
				// 添加编号

				if (!collected.contains(signObj)) {
					UserSign sign = userSignService.load(param.put("id", qrcode).toHashMap());
					if (null != sign) {
						String[] arrs = sign.getRemark().split("&nbsp;");
						if (arrs.length >= 2) {
							sign.setRemark(arrs[1]);
						} else {
							sign.setRemark(null);
						}
						collected.add(sign);
					} else {
						model.addAttribute("msg", "未找到订单！");
					}
				} else {
					model.addAttribute("msg", "订单已扫描！");
				}
			}
		}
		session.setAttribute("collected", collected);
		session.setAttribute("beginIndex", beginIndex);
		return "back/qrcode";
	}

	java.util.Calendar calendar = java.util.Calendar.getInstance();
	java.text.SimpleDateFormat sdfDay = new java.text.SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 返回calandar的下一天 字符串
	 * 
	 * @return
	 */
	private String getNextDay() {

		calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
		java.util.Date next = calendar.getTime();
		String nextDay = sdfDay.format(next);

		return nextDay;
	}

	/**
	 * 查询单号的状态
	 * 
	 * @param model
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("/express/query")
	public String queryExpressOrderState(Model model, String orderNo) {
		model.addAttribute("data", ExpressQuery.query(orderNo));
		model.addAttribute("orderNo", orderNo);
		return "core/expressQuery";
	}

	@RequestMapping("/bindPhone")
	public String bindPhonePageWechat() {
		return getWechatUrl("bindPhoneInternal");
	}

	@RequestMapping("/bindPhoneInternal")
	public String bindPhonePageInternal(String code) {
		String openid = WeChatPay.getWechatOpenId(request, code);
		if (Strings.isBlank(openid)) {
			return "core/404";
		}
		return "core/bindPhone";
	}

	/**
	 * 绑定手机号
	 * 
	 * @param phone
	 * @return
	 */
	/*
	 * @RequestMapping(value = "/bindPhone", method = RequestMethod.POST)
	 * 
	 * @Transactional
	 * 
	 * @ResponseBody public Object bindPhone(String phone) { String openid =
	 * WeChatPay.getWechatOpenId(request, null); if (Strings.isBlank(openid)) {
	 * return new AjaxReturn(false, "请使用微信打开"); } // update openid = NULL where
	 * openid = #{openid} // update openid = openid where phone = phone return
	 * new AjaxReturn(true, null); }
	 */

	/**
	 * 生成微信鏈接
	 * 
	 * @param module
	 * @return
	 */
	private String getWechatUrl(String module) {

		if (isSuspend()) {
			return "suspend";
		}

		String url =  "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Config.APP_ID
				+ "&redirect_uri=http://ujsdelivery.com/app/" + module + "&response_type=code&scope=snsapi_base&state="
				+ WebUtils.getSessionAttribute(request, "state") + "#wechat_redirect";
		System.out.println(url);
		return url;
	}
}
