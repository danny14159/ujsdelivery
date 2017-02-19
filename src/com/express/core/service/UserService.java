package com.express.core.service;

import java.util.List;

import com.express.core.bean.SignTimes;
import com.express.core.bean.User;

public interface UserService extends BaseService<User>{
	public String loadStd(String name);
	
	public List<String> listBusinessAccount();

	/**解绑手机,删除openid
	 * @param param
	 * @return
	 */
	public void unBindPhone(Object param);
	
	/**
	 * @param param int overTimes
	 * long|string:from_date(milliseconds)
	 * long|string:to_date(milliseconds)
	 * @return
	 */
	public List<SignTimes> listSignTimes(Object param);
	
	/**显示每日签到顺序和人数，limit 10
	 * @return
	 */
	public List<User> showSignOrder();
	
	/**计算我今天是第几个签到的
	 * @param phone
	 * @return
	 */
	public int todaySignOrder(String phone);
}