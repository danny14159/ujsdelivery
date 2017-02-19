package com.express.core.service;

import java.util.List;

import com.express.core.bean.CommentRecord;
import com.express.core.bean.UserSign;

public interface UserSignService extends BaseService<UserSign>{

	public UserSign getEarliestSubmitedOrder(Object param);
	
	/**查重
	 * @return
	 */
	public List<UserSign> searchForDuplicate();
	
	public List<CommentRecord> exportComment();
	
	public int countSearchForDuplicate();
	
	public int countByHour(int hour);
	
	/**今天免单数
	 * @return
	 */
	public int todayFreeOrders();
	
	/**今天签到数
	 * @return
	 */
	public int todaySignNum();
	
	/**今日已完成数量
	 * @return
	 */
	public int todayFinishedNum();
	
	/**昨日订单maxId
	 * @return
	 */
	public Integer selectYesterdayMaxId();
	
	public int countUserSign(String phone);
}