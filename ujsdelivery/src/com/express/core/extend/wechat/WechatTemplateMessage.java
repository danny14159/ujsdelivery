package com.express.core.extend.wechat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import com.express.core.extend.HttpClientUtil;
import com.express.core.util.Commons;

/**微信模板消息类
 * @author Danny
 *
 */
public class WechatTemplateMessage {
	
	/**
	 * 快递下单成功通知
	 */
	private static final String MSG_TPL_ORDER_SUCCESS="6dmdY-W_ukGF1G09rS3mixLPWqK2GSOSfXBPANYo84o";
	
	/**
	 * 中奖结果通知
	 */
	private static final String MSG_TPL_REWARD="DuuCqd2vGsbCdoaYWwx3f7DACncXesLZizgAHBcVCXA";
	
	/**下单成功通知
	 * @param toUser
	 * @param topcolor
	 * @param sign_time
	 * @param sign_address
	 */
	public static void sendTemplateMessageOrderSuccess(
			String toUser,
			String topcolor,
			String sign_no,
			String sign_address,
			String express
			){
		TemplateMessage msg = new TemplateMessage();
		msg.setTemplate_id(MSG_TPL_ORDER_SUCCESS);
		msg.setTopcolor(topcolor);
		msg.setTouser(toUser);
		msg.setUrl("http://ujsdelivery.com/app/myback");
		msg.getData().put("first", new TplMessageData("【"+Commons.PROJECT_NAME+"】您的订单已成功提交，如有变动请重新下单", "#173177"));
		msg.getData().put("keyword1", new TplMessageData(sign_address, "#D43D3D"));
		msg.getData().put("keyword2", new TplMessageData(sign_no, "#D43D3D"));
		msg.getData().put("keyword4", new TplMessageData(express, "#D43D3D"));
		msg.getData().put("remark", new TplMessageData("打开“我的订单”查询订单状态。", "#173177"));
		
		//String param = "{\"touser\":\"oHUl-uNrbIfd4jq9Cwdccbh9PzWE\",\"template_id\":\"7C-oR44qsuRGHxpY-XWXo7OYaMFLshSnDUBdzl7IFZo\",\"url\":\"http://weixin.qq.com/download\",\"topcolor\":\"#FF0000\",\"data\":{\"first\":{\"value\":\"尊敬的用户您好，您的快递已到达\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"编号20\",\"color\":\"#173177\"},\"CardNumber\":{\"value\":\"0426\",\"color\":\"#173177\"},\"Type\":{\"value\":\"消费\",\"color\":\"#173177\"},\"Money\":{\"value\":\"人民币260.00元\",\"color\":\"#173177\"},\"DeadTime\":{\"value\":\"06月07日19时24分\",\"color\":\"#173177\"},\"remark\":{\"value\":\"请及时下楼领取！\",\"color\":\"#173177\"}}}";
		ObjectMapper objectMapper = new ObjectMapper();
		String param = "";
		try {
			param = objectMapper.writeValueAsString(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String accessToken = WeChatPay.getAccessToken();
		HttpClientUtil.postJson("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken, param);
		//WeChatPay.getOpenIdByCode(code);
	}
	/**
	 * 
	 */
	public static void sendTemplateMsgReward(
			String toUser,
			String activity,
			String username,
			String reward
			){
		TemplateMessage msg = new TemplateMessage();
		msg.setTemplate_id(MSG_TPL_REWARD);
		msg.setTopcolor("#ffffff");
		msg.setTouser(toUser);
		msg.setUrl("http://ujsdelivery.com");
		msg.getData().put("first", new TplMessageData("【"+Commons.PROJECT_NAME+"】恭喜你，奖品已发放", "#173177"));
		msg.getData().put("keyword1", new TplMessageData(activity, "#D43D3D"));
		msg.getData().put("keyword2", new TplMessageData(reward, "#D43D3D"));
		msg.getData().put("remark", new TplMessageData("请在取件时出示本通知即可免单，感谢您对"+Commons.PROJECT_NAME+"的支持！", "#173177"));
		
		//String param = "{\"touser\":\"oHUl-uNrbIfd4jq9Cwdccbh9PzWE\",\"template_id\":\"7C-oR44qsuRGHxpY-XWXo7OYaMFLshSnDUBdzl7IFZo\",\"url\":\"http://weixin.qq.com/download\",\"topcolor\":\"#FF0000\",\"data\":{\"first\":{\"value\":\"尊敬的用户您好，您的快递已到达\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"编号20\",\"color\":\"#173177\"},\"CardNumber\":{\"value\":\"0426\",\"color\":\"#173177\"},\"Type\":{\"value\":\"消费\",\"color\":\"#173177\"},\"Money\":{\"value\":\"人民币260.00元\",\"color\":\"#173177\"},\"DeadTime\":{\"value\":\"06月07日19时24分\",\"color\":\"#173177\"},\"remark\":{\"value\":\"请及时下楼领取！\",\"color\":\"#173177\"}}}";
		ObjectMapper objectMapper = new ObjectMapper();
		String param = "";
		try {
			param = objectMapper.writeValueAsString(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String accessToken = WeChatPay.getAccessToken();
		HttpClientUtil.postJson("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken, param);

	}
	public static void main(String[] args) {
		//sendTemplateMessage(null, null, null, null, null, null, null);
		/*WechatTemplateMessage.sendTemplateMessageOrderSuccess("oHUl-uNrbIfd4jq9Cwdccbh9PzWE", 
				"#ffffff","2015-01-01","D区4栋AB座"
				);*/
		//sendTemplateMsgReward("oHUl-uNrbIfd4jq9Cwdccbh9PzWE", "早起啦前十签到", "20积分");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		WechatTemplateMessage.sendTemplateMsgReward("oHUl-uNrbIfd4jq9Cwdccbh9PzWE", "早起签到TOP10", "邓雷","免单一次，仅限"+sdf.format(new Date())+"给 邓雷 同学使用");
	}
}
