package com.express.core.extend.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.express.core.extend.HttpClientUtil;

/**短信平台工具类
 * @author Danny
 *
 */
public class IMessageUtil {
	
	static final String ACCOUNT_SID = "91d820f6c8079dbbbef90070aad72c65";
	static final String APP_ID = "c69bc4385db74673a941d9e509b5a418";
	
	private static final String MSG_ID_EXPRESS = "3579";

	public static void main(String[] args) {
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("appId", APP_ID));
		params.add(new BasicNameValuePair("templateId", MSG_ID_EXPRESS));
		params.add(new BasicNameValuePair("to", "18352862367"));
		params.add(new BasicNameValuePair("param", "1234"));
		HttpClientUtil.post("https://api.ucpaas.com/2014-06-30/Accounts/"+ACCOUNT_SID+"/Messages/templateSMS",params );
	}
}
