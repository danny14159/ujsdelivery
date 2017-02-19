package com.express.core.module;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.express.core.extend.HttpClientUtil;
import com.express.core.extend.wechat.Config;
import com.express.core.extend.wechat.WeChatPay;


@Controller
@RequestMapping("/app/wechatp")
public class WeChatPayController {
	

	/**支付结果通知URL）,注意要验证sign是否与服务器计算的一致，以此判断是否是微信发来的支付结果通知
	 * @param request
	 * @return 返回xml格式的数据给微信
	 */
	@RequestMapping("/notify/url/")
	public void notifyUrl(
			HttpServletRequest request,
			@RequestParam String sign,
			@RequestParam String result_code,
			@RequestParam String out_trade_no,
			HttpServletResponse resp
			){
		Map<String,String> param = new HashMap<String, String>();
		Enumeration<String> keys = request.getParameterNames();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			param.put(key, request.getParameter(key));
		}
		
		Map<String,String> retParam = new HashMap<String, String>(); //返回给微信的参数，
		String return_code=null,return_msg=null;
		String localSign = WeChatPay.buildRequestMysign(param);
		if(localSign.equals(sign)){
			//签名验证成功
			
			if("SUCCESS".equals(return_code)){
				
				//支付成功
				if("SUCCESS".equals(result_code)){
					
					//在此检查业务数据的状态，将订单标记为已支付
					/*if(orderModule.payOrder(out_trade_no) > 0){
						return_code = "SUCCESS";
						return_msg = "OK";					
					}
					else{
						return_code = "FAIL";
						return_msg = "逻辑处理失败";	
					}*/
				}
				else{
					return_code = "FAIL";
					return_msg = "微信支付失败";
				}
			}
			else{
				return_code = "FAIL";
				return_msg = "微信通信失败";
			}
		}
		else{
			return_code = "FAIL";
			return_msg = "签名失败";
		}
		
		retParam.put("return_code", return_code);
		retParam.put("return_msg", return_msg);
		String retXml = WeChatPay.transformXml(retParam);
		System.out.println(retXml);

		resp.setContentType("text/xml;charset=utf-8"); //要返回xml数据格式
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.print(retXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**调用统一下单API（测试使用）
	 * @param orderNo 订单号
	 * @param body 商品描述
	 * @param total_fee 商品总金额，单位为分，只能是整数
	 * @param request
	 * @param code 用于拿到用户的openid
	 * @return 返回H5 JSAPI调用的参数
	 */
	@RequestMapping("/unifiedorder/url/")
	public String unifiedorder(
			@RequestParam("state") String orderNo,
			//@RequestParam String body,
			//@RequestParam String total_fee,
			@RequestParam String code,
			HttpServletRequest request,
			Model model
			){
		String openid = WeChatPay.getWechatOpenId(request, code);
		
		if(null == openid) {
			model.addAttribute("msg", "页面过期，请刷新重试！");
			return "core/fail";
		}
		System.out.println(openid);
		
		String body = "测试商品";
		//加载订单描述
		/*Order order = orderService.load(Mapper.make("order_no", orderNo).toHashMap());
		List<OrderItem> items = orderItemService.list(Mapper.make("order_no", orderNo).toHashMap());
		for(OrderItem item:items){
			body += item.getGoods_name()+"|";
		}*/
		
		Map<String,String> param = new HashMap<String, String>();
		param.put("appid", Config.APP_ID);
		param.put("mch_id", Config.MCH_ID);
		param.put("nonce_str", WeChatPay.randomString(32));
		param.put("body", body);
		param.put("total_fee", "1"); //注意！！！支付金额只能为整数，而且单位为：分
		param.put("spbill_create_ip", request.getRemoteAddr()/*"211.103.108.33"*/);
		param.put("notify_url", Config.NOTIFY_URL);
		param.put("openid", /*"oGBNluAKh4Wev9zmGto9iebmk4Cs"*/openid); //注意这里要拿到openid
		param.put("trade_type", "JSAPI");
		param.put("out_trade_no", orderNo);
		
		param.put("sign", WeChatPay.buildRequestMysign(param));
		
		String ret = HttpClientUtil.postXml("https://api.mch.weixin.qq.com/pay/unifiedorder", WeChatPay.transformXml(param));
		
		//解析ret字符串
		
		Document document ;
		String prepay_id = null;
		try {
			document = DocumentHelper.parseText(ret);
			Element root = document.getRootElement();  
			
			prepay_id = root.element("prepay_id").getText();
		} catch (DocumentException e) {
			
			e.printStackTrace();
			model.addAttribute("msg", "支付失败，请稍后再试！");
			return "/frontend/wechatpay/fail";
		} 
		
		//构建jsapi参数
		Map<String,String> jsapiParam = new HashMap<String, String>();
		jsapiParam.put("appId", Config.APP_ID);
		jsapiParam.put("timeStamp", new Date().getTime()/1000+"");
		jsapiParam.put("nonceStr",WeChatPay.randomString(32) );
		jsapiParam.put("package","prepay_id="+prepay_id );
		jsapiParam.put("signType","MD5" );
		
		jsapiParam.put("paySign", WeChatPay.buildRequestMysign(jsapiParam));
		jsapiParam.put("orderNo",orderNo );
		
		model.addAttribute("jsapiParam", jsapiParam);
		return "core/wechatpay";
	}
	
	/**测试输出xml文本，支持中文
	 * @param resp
	 */
	@RequestMapping("/xmltest")
	public void testXml(HttpServletResponse resp){
		resp.setContentType("text/xml;charset=utf-8");
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.print("<xml><a>测试中文</a></xml>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**订单状态查询接口，返回true表示已付款，false且code==-1表示订单未支付
	 * @param order_no
	 * @return
	 */
	/*@RequestMapping("/pay/orderquery/{order_no}")
	@ResponseBody
	public Object orderQuery(
			@PathVariable ("order_no")String order_no){
		Map<String,String> param = new HashMap<String, String>();
		param.put("appid", Config.APP_ID);
		param.put("mch_id", Config.MCH_ID);
		param.put("out_trade_no",order_no );
		param.put("nonce_str",WeChatPay.randomString(32) );
		
		param.put("sign", WeChatPay.buildRequestMysign(param));
		
		String retStr = HttpClientUtil.postXml("https://api.mch.weixin.qq.com/pay/orderquery", WeChatPay.transformXml(param));
		Document document ;
		try {
			document = DocumentHelper.parseText(retStr);
			Element root = document.getRootElement();  
			String return_code = root.element("return_code").getText();
			String return_msg = root.element("return_msg").getText();
			if("SUCCESS".equals(return_code)){
				String result_code = root.element("result_code").getText();
				if("SUCCESS".equals(result_code)){
					String trade_state = root.element("trade_state").getText();
					
					if("SUCCESS".equals(trade_state)){
						
						return new AjaxReturn(true, null);
						//调用支付订单逻辑
						if(orderModule.payOrder(order_no) > 0){
						}
						else{
							return new AjaxReturn(false, "系统错误！");
						}
					}
					else{
						
						return new AjaxReturn(false, "订单未支付",-1);
					}
				}
				else{
					return new AjaxReturn(false, "订单查询失败！");
				}
			}
			else{
				return new AjaxReturn(false, "订单查询失败！"+return_msg);
			}
			
		} catch (DocumentException e) {
			
			e.printStackTrace();
			return new AjaxReturn(false, "订单查询失败！系统异常");
		} 
		
	}*/
}
