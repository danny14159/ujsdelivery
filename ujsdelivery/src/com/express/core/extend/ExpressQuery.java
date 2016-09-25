package com.express.core.extend;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class ExpressQuery {

	/**
	 * 从快递100上查询快递状态
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> query(String orderNo) {

		if(Strings.isBlank(orderNo)) return null;
		
		try {
			String typeJSON = HttpClientUtil.get("http://www.kuaidi100.com/autonumber/auto?num=" + orderNo, "http://www.kuaidi100.com/");

			List<Map<String, String>> autos = new Gson().fromJson(typeJSON, List.class);

			if (autos != null && !autos.isEmpty()) {
				String type = autos.get(0).get("comCode");
				String result = HttpClientUtil.get("http://www.kuaidi100.com/query?type=" + type + "&postid=" + orderNo
						+ "&id=1&valicode=&temp=0.8869508435018361", "http://www.kuaidi100.com/");
				Map<String, Object> retMap = new Gson().fromJson(result, Map.class);

				return (List<Map<String, String>>) retMap.get("data");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;

	}

	public static void main(String[] args) {
		System.out.println(query("70027103546757"));
		;
	}
}
