package com.express.core.extend.wechat;

public class Config {
	public static final String ME_WECHAT = "mewechat";
	
	//public static final String APP_ID = "wx041974fbeae57af2";讲快 
	public static final String APP_ID = "wx226ca6d4688495b8"; 
	//public static final String APP_SECRET = "93397162002e2d65340f54aad6ab68ef";江快
	//public static final String APP_SECRET = "f06ae4afd0d92cb965a9872883312fb3";
	public static final String APP_SECRET = "b7a85ab3dbeb3d7f1739a83fbfb7c09e";
	
	public static final String SIGN_TYPE = "MD5";
	/**
	 * 商户号
	 */
	public static final String MCH_ID = "1265308501";
	
	/**
	 * 商户API秘钥
	 */
	public static final String ACCOUNT_KEY = "1hsh9o0b74478z5AQ65x50v7U5z50uY3";
	
	
	/**
	 * 网关
	 */
	public static final String GATEWAY = "http://804956748a.imwork.net";
	//public static final String GATEWAY = "http://ujsdelivery.com";
	
	/**
	 * 异步回调通知地址
	 */
	public static final String NOTIFY_URL = GATEWAY + "/app/wechatp/notify/url/";
}
