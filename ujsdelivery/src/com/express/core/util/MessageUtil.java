package com.express.core.util;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.RestTest;
import com.express.core.bean.MessageItem;
import com.express.core.bean.UserSign;
import com.express.core.extend.Mapper;
import com.express.core.service.UserSignService;

/**短信模板工具
 * @author Danny
 *
 */
public class MessageUtil {
	
	private static final String ACCOUNT_SID = "91d820f6c8079dbbbef90070aad72c65";
	private static final String AUTH_TOKEN = "db53bffb31248519865676fee23056e2";
	private static final String APP_ID_EXPRESS = "c69bc4385db74673a941d9e509b5a418";
	
	/**
	 * 派件消息模板id
	 */
	private static final String MSG_ID_SEND = "14928";

	
	/**根据地址查找指定的短信模板
	 * @param configService
	 * @param buildingRegion
	 * @return
	 */
	public static String getDstLocation(String buildingRegion,WebConfig webConfig){
		String rst = webConfig.get("msg", buildingRegion);
		return rst == null?"楼下":rst;
	}
	/**发送短信
	 * @param dstPhoneNumber 电话号码
	 * @param cantactPhone 联系号码
	 * @param addr_region 订单所属的区域
	 * @param orderno 订单编号
	 */
	public static boolean sendMessage(String dstPhoneNumber,String addr_region,String addr_building,Integer orderno,WebConfig webConfig){
		if(dstPhoneNumber == null) return false;
		System.out.println("短信已向"+dstPhoneNumber+"发送"+addr_building+","+addr_region+","+orderno);
		String dstLocation = getDstLocation(addr_region+"-"+addr_building, webConfig);
		System.out.println(addr_region+addr_building+":"+dstLocation);
		try{
			String result=RestTest.InstantiationRestAPI(true).templateSMS(ACCOUNT_SID, AUTH_TOKEN, APP_ID_EXPRESS, MSG_ID_SEND, dstPhoneNumber, 
					orderno+","+dstLocation);
			System.out.println(result);
			return result.indexOf("000000")>=0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 短信队列
	 */
	static ArrayBlockingQueue<MessageItem<UserSign>> MessageQueue = new ArrayBlockingQueue<MessageItem<UserSign>>(100);
	
	/**生产者线程
	 * @author Danny
	 *
	 */
	public static class ThPut extends Thread{
		List<MessageItem<UserSign>> signs = null;
		public ThPut(List<MessageItem<UserSign>> signs) {
			this.signs = signs;
		}
		
		@Override
		public void run() {
			for(MessageItem<UserSign> sign:signs){
				try {
					MessageQueue.put(sign);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**消费者线程
	 * @author Danny
	 *
	 */
	public static class ThTake extends Thread{
		UserSignService userSignService;
		WebConfig config;
		
		public ThTake(UserSignService userSignService,WebConfig config) {
			this.userSignService = userSignService;
			this.config = config;
		}
		
		@Override
		public void run() {
			while(!MessageQueue.isEmpty()){
				try {
					MessageItem<UserSign> item = MessageQueue.take();
					UserSign sign = item.getObj();
					if(sign!=null && 'P'==sign.getState() && MessageUtil.sendMessage(sign.getPhone(), sign.getAddr_region(),
							sign.getAddr_building(),item.getIndex(),config)){
						userSignService.update(Mapper.make("id", sign.getId()).put("state", 'E').toHashMap());
						sign.setState('E');
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String result=RestTest.InstantiationRestAPI(true).templateSMS(ACCOUNT_SID, AUTH_TOKEN, APP_ID_EXPRESS, MSG_ID_SEND, "18352862373", 
				1+","+112);
		System.out.println(result);
	}
}
