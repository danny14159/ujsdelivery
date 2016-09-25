package com.express.travel.module;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.express.core.bean.User;
import com.express.core.extend.AjaxReturn;
import com.express.core.extend.Mapper;
import com.express.core.extend.Strings;
import com.express.core.util.LoginUtil;
import com.express.core.util.UploadHelper;
import com.express.travel.bean.Goods;
import com.express.travel.bean.Order;
import com.express.travel.service.GoodsCategoryService;
import com.express.travel.service.GoodsService;
import com.express.travel.service.OrderService;
import com.express.travel.service.RegionService;

/**江大旅游前端控制路由
 * @author Danny
 *
 */
@Controller
@RequestMapping("/app/tv")
public class FrontModule {

	@Autowired
	private GoodsCategoryService goodsCategoryService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private OrderService orderService;
	
	/**首页
	 * @return
	 */
	@RequestMapping(value={"","/","index"})
	public String index(Model model){
		model.addAttribute("list", goodsService.list(null));
		return "travel/index";
	}
	
	/**分类，搜索展示页
	 * @param model
	 * @param key 搜索关键字
	 * @param cid 分类id
	 * @param rid 区域id
	 * @return
	 */
	@RequestMapping("/search")
	public String search(
			Model model,String key,Integer cid,Integer rid){
		model.addAttribute("cates", goodsCategoryService.list(null));
		
		//搜索
		if(Strings.isNotBlank(key)){
			Map<String, Object> param = Mapper.make("key", key).toHashMap();
			model.addAttribute("list", goodsService.list(param));
			model.addAttribute("recordCount", goodsService.count(param));
		}
		
		//根据区域或者分类查找
		else {
			Map<String, Object> param = Mapper.make("category_id", cid).put("region_id", rid).toHashMap();
			model.addAttribute("list", goodsService.list(param));
			model.addAttribute("recordCount", goodsService.count(param));
		}
		
		return "travel/search";
	}
	
	/**查看产品详情
	 * @param gid 产品id
	 * @param model
	 * @return
	 */
	@RequestMapping("/view/{gid}")
	public String view(@PathVariable("gid") String gid,Model model){
		
		Goods goods = goodsService.load(Mapper.make("id", gid).toHashMap());
		model.addAttribute("region", regionService.load(Mapper.make("id", goods.getRegion_id())));
		model.addAttribute("goods", goods);
		
		Mapper param = Mapper.make("goods_id", gid).put("pid", 1);
		model.addAttribute("param1", goodsService.listParamValue(param.toHashMap()));
		model.addAttribute("param2", goodsService.listParamValue(param.put("pid", 2).toHashMap()));
		model.addAttribute("param3", goodsService.listParamValue(param.put("pid", 3).toHashMap()));
		return "travel/viewgoods";
	}
	
	/**文件上传操作
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	public void upload(HttpServletRequest request,HttpServletResponse response){
		
		//name:imgFile
		Map<String,String> resultMap = UploadHelper.processUploadRequest(request);
		
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("url", resultMap.get("imgFile"));
		retMap.put("error",0);
		
		//return:{"error":0,"url":"\/ke4\/attached\/11385343fbf2b211e9875a05ca8065380dd78ed4.jpg"}
		ObjectMapper mapper = new ObjectMapper();  
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(mapper.writeValueAsString(retMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**用户预定
	 * @param gid
	 * @param guide_level 导游费
	 * @param fare_level 车费
	 * @param ticket_level 门票费
	 * @return
	 */
	@RequestMapping("/order")
	@ResponseBody
	public Object order(
			Integer gid,
			int guide_level,
			int fare_level,
			int ticket_level,
			HttpServletRequest request
			){
		User u = LoginUtil.getLoginUser(null,request);
		if(null == u) return new AjaxReturn(false, "请先登录", -1);
		
		Order order = new Order();
		order.setConnection_methods(u.getPhone());
		order.setCreate_time(new Date());
		order.setGoods_id(gid);
		order.setStatus("S");
		
		order.setFare_level(fare_level);
		order.setGuide_level(guide_level);
		
		order.setTicket_level(ticket_level);
		order.setTotal_price(0.0f+guide_level + fare_level + ticket_level);
		order.setUserid(u.getPhone());
		
		return AjaxReturn.ok(orderService.insert(order), null, "下单失败，请稍后再试！");
	}
}
