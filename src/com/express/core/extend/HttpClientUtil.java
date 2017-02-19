package com.express.core.extend;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	
	static CloseableHttpClient httpclient = HttpClients.createDefault();

	/**
	 * 发送 get请求
	 */
	public static String get(String url,String referer) {
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		String body = "No data response from:"+url+"!";
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Connection", "keep-alive");
			httpget.setHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.setHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");
			httpget.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");
			httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
			httpget.setHeader("Referer",referer);

			System.out.println("executing request " + httpget.getURI());
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				//System.out.println(response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度
					//System.out.println("Response content length: "
							//+ entity.getContentLength());
					// 打印响应内容
					body = EntityUtils.toString(entity);
					System.out.println("Response content: "
							+ body);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			/*try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		return body;
	}
	public static String post(String url,List<NameValuePair> formparams) {
		String body = "No data response from:"+url+"!";
		try {
			// 创建httpget.
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Connection", "keep-alive");
			httpPost.setHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpPost.setHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
			httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
			
			System.out.println("executing request " + httpPost.getURI());
			UrlEncodedFormEntity uefEntity=null;
			CloseableHttpResponse response=null;
			try {
				uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
				httpPost.setEntity(uefEntity);
				response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				if (entity != null) {
					// 打印响应内容
					body = EntityUtils.toString(entity);
					System.out.println("Response content: "
							+ body);
				}
			} finally {
				if(response!=null) response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return body;
	}
	
	public static void main(String[] args) {
		//System.out.println(HttpClientUtil.get("http://api.map.baidu.com/geocoder?ak=E4805d16520de693a3fe707cdc962045&location=32.198186,119.508704&output=json&pois=1", 
				//"http://developer.baidu.com/map/index.php?title=webapi/guide/webservice-geocoding"));
		//System.out.println(post("https://www.baidu.com/s?ie=UTF-8&wd=123",new ArrayList<NameValuePair>()));
		//postString("http://www.baidu.com/","");
	}
	
	public static String postXml(String url,String str){
		return _postString(url,str,"xml");
	}
	
	public static String postJson(String url,String str){
		return _postString(url,str,"json");
	}
	
	private static String _postString(String url,String str,String format){
		System.out.println(str);
		String body = "no response";
		CloseableHttpResponse response=null;
		try{
			HttpPost httpPost = new HttpPost(url);
			HttpEntity reqEntity = new StringEntity(str,Charset.forName("UTF-8"));
			httpPost.setHeader("Content-Type", "application/"+format+";charset=utf-8");
			httpPost.setHeader("Accept", "application/"+format);
			httpPost.setEntity(reqEntity);
			response = httpclient.execute(httpPost);
			HttpEntity Respentity = response.getEntity();
			// 打印响应状态
			if (Respentity != null) {
				// 打印响应内容
				body =new String( EntityUtils.toString(Respentity).getBytes("ISO-8859-1"),"UTF-8");
				System.out.println("Response content: "
						+ body);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(response!=null)
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
		
		return body;
	}
}
