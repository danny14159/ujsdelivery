/**
 * @author Glan.duanyj
 * @date 2014-05-12
 * @project rest_demo
 */
package com;

import java.io.IOException;

import com.ucpaas.restDemo.client.AbsRestClient;
import com.ucpaas.restDemo.client.JsonReqClient;
import com.ucpaas.restDemo.client.XmlReqClient;

public class RestTest {
	
	public static AbsRestClient InstantiationRestAPI(boolean enable) {
		if(enable) {
			return new JsonReqClient();
		} else {
			return new XmlReqClient();
		}
	}
	public static void testFindAccount(boolean json,String ACCOUNT_SID,String AUTH_TOKEN){
		try {
			String result=InstantiationRestAPI(json).findAccoutInfo(ACCOUNT_SID, AUTH_TOKEN);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testCreateClient(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String appId,String clientName
			,String chargeType,String charge,String mobile){
		try {
			String result=InstantiationRestAPI(json).createClient(ACCOUNT_SID, AUTH_TOKEN, appId, clientName, chargeType, charge,mobile);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testfindClients(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String appId,String start
			,String limit){
		try {
			String result=InstantiationRestAPI(json).findClients(ACCOUNT_SID, AUTH_TOKEN, appId, start, limit);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testfindClientByNbr(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String clientNumber,String appId){
		try {
			String result=InstantiationRestAPI(json).findClientByNbr(ACCOUNT_SID, AUTH_TOKEN, clientNumber,appId);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testCloseClient(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String clientNumber,String appId){
		try {
			String result=InstantiationRestAPI(json).closeClient(ACCOUNT_SID, AUTH_TOKEN, clientNumber,appId);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testChargeClient(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String clientNumber,
			String chargeType,String charge,String appId){
		try {
			String result=InstantiationRestAPI(json).charegeClient(ACCOUNT_SID, AUTH_TOKEN, clientNumber, chargeType, charge,appId);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testBillList(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String appId,String date){
		try {
			String result=InstantiationRestAPI(json).billList(ACCOUNT_SID, AUTH_TOKEN, appId, date);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testClientBillList(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String appId,String clientNumber,String date){
		try {
			String result=InstantiationRestAPI(json).clientBillList(ACCOUNT_SID, AUTH_TOKEN, appId, clientNumber, date);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testCallback(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String appId,String fromClient,String to,String fromSerNum,String toSerNum){
		try {
			String result=InstantiationRestAPI(json).callback(ACCOUNT_SID, AUTH_TOKEN, appId, fromClient, to,fromSerNum,toSerNum);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testVoiceCode(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String appId,String to,String verifyCode){
		try {
			String result=InstantiationRestAPI(json).voiceCode(ACCOUNT_SID, AUTH_TOKEN, appId, to, verifyCode);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testTemplateSMS(boolean json,String to,String param){
		try {
			String result=InstantiationRestAPI(json).templateSMS(ACCOUNT_SID, AUTH_TOKEN, APP_ID_EXPRESS, MSG_ID_EXPRESS, to, param);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void testfindClientByMobile(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String mobile,String appId){
		try {
			String result=InstantiationRestAPI(json).findClientByMobile(ACCOUNT_SID, AUTH_TOKEN, mobile, appId);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void testDispalyNumber(boolean json,String ACCOUNT_SID,String AUTH_TOKEN,String appId,String clientNumber,String display){
		try {
			String result=InstantiationRestAPI(json).dispalyNumber(ACCOUNT_SID, AUTH_TOKEN, appId, clientNumber, display);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private static final String ACCOUNT_SID = "91d820f6c8079dbbbef90070aad72c65";
	private static final String AUTH_TOKEN = "db53bffb31248519865676fee23056e2";
	private static final String APP_ID_EXPRESS = "c69bc4385db74673a941d9e509b5a418";
	
	//private static final String APP_ID_EXPRESS= "e2fe3a17c82e4c8d89906388baecfca6";
	//private static final String APP_ID_DEMO = "f7c78c71abe7422a9334c0bd4fb2c158";
	/**
	 * 快递短信模板id
	 */
	private static final String MSG_ID_EXPRESS = "3579";
	//private static final String MSG_ID_EXPRESS = "25800";
	
	/**
	 * 金贝壳短信模板id
	 */
	//private static final String MSG_ID_JBK = "3579";
	
	
	/**
	 * 测试说明 参数顺序，请参照各具体方法参数名称
	 * 参数名称含义，请参考rest api 文档
	 * @author Glan.duanyj
	 * @date 2014-06-30
	 * @param params[]
	 * @return void
	 * @throws IOException 
	 * @method main
	 */
/*	public static void main(String[] args) throws IOException {
//		String jsonStr="{\"client\":\"1\"}";
//		JSONObject obj=JSONObject.fromObject(jsonStr);
//		System.out.println(obj.getInt("client"));
		System.out.println("请输入参数，以空格隔开...");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String param=br.readLine();
		String [] params=param.split(" ");
		String method = params[0];
		boolean json=true;
		if (params[1].equals("xml")) {
			json=false;
		}
		if (method.equals("1")) {
			testFindAccount(json,ACCOUNT_SID,AUTH_TOKEN);
		}else if (method.equals("2")) {
			testCreateClient(json, ACCOUNT_SID,AUTH_TOKEN, params[4], params[5], params[6], params[7], params[8]);
		}else if (method.equals("3")) {
			String appId="";
			testfindClients(json,ACCOUNT_SID,AUTH_TOKEN,appId,"0","5");
		}else if (method.equals("4")) {
			testfindClientByNbr(json,params[2],params[3], params[4], params[5]);
		}else if (method.equals("5")) {
			testCloseClient(json, params[2],params[3], params[4], params[5]);
		}else if (method.equals("6")) {
			testChargeClient(json, params[2],params[3], params[4], params[5], params[6], params[7]);
		}else if (method.equals("7")) {
			testBillList(json, params[2], params[3],params[4], params[5]);
		}else if (method.equals("8")) {
			testClientBillList(json, params[2], params[3],params[4],params[5], params[6]);
		}else if (method.equals("9")) {
			String fromClient="";
			String to="";
			String fromSerNum="";
			String toSerNum="";
			testCallback(json, ACCOUNT_SID, AUTH_TOKEN, APP_ID_EXPRESS, fromClient, to, fromSerNum, toSerNum);
		}else if (method.equals("10")) {
			String to="";
			String appId="";
			String para = "";
			testVoiceCode(json, ACCOUNT_SID, AUTH_TOKEN, appId, to, para);
		}else if (method.equals("11")) { //短信验证码 
			String to="18352862373";
			String para="123456";
			testTemplateSMS(json, to,para);
		}else if (method.equals("12")) {
			testfindClientByMobile(json, params[2],params[3], params[4], params[5]);
		}else if (method.equals("13")) {
			String clientNumber="";
			String appId="";
			String display="1";
			testDispalyNumber(json, ACCOUNT_SID, AUTH_TOKEN, appId, clientNumber, display);
		}
	}*/
	
	public static void main(String[] args) {
		String result=InstantiationRestAPI(true).templateSMS(ACCOUNT_SID, AUTH_TOKEN, APP_ID_EXPRESS, MSG_ID_EXPRESS, "18352862373", "123");
		System.out.println("Response content is: " + result);
	}
}
