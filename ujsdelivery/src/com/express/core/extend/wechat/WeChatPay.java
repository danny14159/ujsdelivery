package com.express.core.extend.wechat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.util.WebUtils;

import com.express.core.bean.AccessToken;
import com.express.core.extend.HttpClientUtil;
import com.express.core.extend.Strings;
import com.express.core.extend.sign.MD5;
import com.google.gson.Gson;

public class WeChatPay {
	
	/**根据微信的5分钟code获取用户的openID
	 * @param code
	 * @return
	 */
	private static AccessToken getOpenIdByCode(String code){
		if(Strings.isBlank(code)) return null;
		AccessToken token = null;
		
		try{
			String tokenStr = HttpClientUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Config.APP_ID+"&secret="+Config.APP_SECRET+"&code="+code+"&grant_type=authorization_code", null);
			System.out.println(tokenStr);
			token = new Gson().fromJson(tokenStr, AccessToken.class);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return token;
	}
	public static String getAccessToken(){
		AccessToken token = null;
		try{
			String tokenStr = HttpClientUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+Config.APP_ID+"&secret="+Config.APP_SECRET, null);
			token = new Gson().fromJson(tokenStr, AccessToken.class);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		if(null == token) return null;
		return token.getAccess_token();
	}
/*	public static String getOpenIdStringByCode(String code){
		if(Strings.isBlank(code)) return null;
		
		String tokenStr = HttpClientUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APP_ID+"&secret="+APP_SECRET+"&code="+code+"&grant_type=authorization_code", null);
		
		return tokenStr;
	}*/
	
	/**生成指定len长度的随机串
	 * @param len
	 * @return
	 */
	public static String randomString(int len){
		
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for(int i = 0;i<len;i++){
			int randInt = r.nextInt();
			if(randInt%3 == 0){
				sb.append(""+(char)('a'+r.nextInt(26)));
			}
			else if(randInt%3 == 1){
				sb.append(""+(char)('A'+r.nextInt(26)));
			}
			else{
				sb.append(""+r.nextInt(10));
			}
		}
		
		return sb.toString();
	}
	/**测试用的主函数
	 * @param args
	 */
	public static void main(String[] args) {
		
		getOpenIdByCode("");
	}
	
	   /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	
    	//拼接API秘钥
    	prestr += "&key="+Config.ACCOUNT_KEY;
    	System.out.println("stringSignTemp:"+prestr);
        String mysign = "";
        if(Config.SIGN_TYPE.equals("MD5") ) {
        	mysign = MD5.sign(prestr, "", "UTF-8");
        }
        return mysign.toUpperCase();
    }
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String createLinkString(Map<String, String> params) {
    	params = paraFilter(params);
    	
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    private static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }
    

    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    public static List<NameValuePair> generatNameValuePair(Map<String, String> properties) {
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
        	nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nameValuePair;
    }
    
    /**将map转换成xml字符串，按照字典序排列，但是sign还是要写在最后
     * @param param
     * @return
     */
    public static String transformXml(Map<String,String> param){
    	List<String> keys = new ArrayList<String>(param.keySet());
    	boolean containsSign = keys.remove("sign");
        Collections.sort(keys);

        String strXml = "<xml>";

        for (int i = 0; i < keys.size(); i++) {
        	String key = keys.get(i),val = param.get(key);
        	strXml += "<"+key+"><![CDATA["+val+"]]></"+key+">";
        }
        if(containsSign) strXml += "<sign><![CDATA["+param.get("sign")+"]]></sign>";
    	
    	strXml +="</xml>";
    	return strXml;
    }
    
	/**登录微信openid用户
	 * @param code
	 * @param request
	 * @return 返回openid
	 */
/*	private static String loginWeChatUser(
			@RequestParam String code,
			HttpServletRequest request
			){
		HttpSession session = request.getSession();
		if(null!=session.getAttribute(Config.ME_WECHAT)) return (String) session.getAttribute(Config.ME_WECHAT);
		
		AccessToken token = WeChatPay.getOpenIdByCode(code);
		if(null != token) {
			session.setAttribute(Config.ME_WECHAT, token.getOpenid());
			return token.getOpenid();
		}
		
		return null;
	}*/
	
	/**获取根据code用户的openid，并把openid存入session，优先查session
	 * @param request
	 * @return
	 */
	public static String getWechatOpenId(HttpServletRequest request,String code){
		
		String openid = (String) WebUtils.getSessionAttribute(request, Config.ME_WECHAT);
		
		if(null == openid && Strings.isNotBlank(code)){
			AccessToken token = WeChatPay.getOpenIdByCode(code);
			if(token == null) return null;
			WebUtils.setSessionAttribute(request, Config.ME_WECHAT, token.getOpenid());
			return token.getOpenid();
		}
		return openid;
		
	}
	
}
