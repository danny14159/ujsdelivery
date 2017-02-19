package com.express.travel.module;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.bean.User;
import com.express.core.extend.Mapper;
import com.express.core.service.UserService;
import com.express.core.util.LoginUtil;
import com.express.travel.bean.Order;
import com.express.travel.service.GoodsCategoryService;
import com.express.travel.service.GoodsService;
import com.express.travel.service.OrderService;
import com.express.travel.service.RegionService;

/**江大旅游-后台管理控制器
 * @author 邓雷
 *
 */
@RequestMapping("/app/back/tv")
//@Controller
public class AdministratorModule {
	@Autowired
	private GoodsCategoryService goodsCategoryService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
	public static final String FILE_PREFIX = "back/travel/";

	@RequestMapping("/addgoods")
	public String addgoods(Model model){
		model.addAttribute("cates", goodsCategoryService.list(null));
		model.addAttribute("rgns", regionService.list(null));
		return FILE_PREFIX + "addgoods";
	}
	@RequestMapping("/categories")
	public String categories(){
		
		return FILE_PREFIX + "categories";
	}
	@RequestMapping("/listgoods")
	public String listgoods(){
		
		return FILE_PREFIX + "listgoods";
	}
	@RequestMapping("/orders")
	public String orders(Model model){
		List<Order> orders = orderService.list(null);
		for(Order o:orders){
			o.setUser(userService.load(Mapper.make("phone", o.getUserid()).toHashMap()));
			o.setGoods(goodsService.load(Mapper.make("id", o.getGoods_id()).toHashMap()));
		}
		model.addAttribute("list", orders);
		return FILE_PREFIX + "orders";
	}
	@RequestMapping("/regions")
	public String regions(){
		
		return FILE_PREFIX + "regions";
	}
	
	/**江大旅游后台
	 * @return
	 */
	@RequestMapping({"","/"})
	public String index(HttpServletRequest request){
		User u = LoginUtil.getLoginUser(userService,request);
		if(null == u || !u.isManager()) return "redirect:/app/login?redirect=/app/back/tv";
		
		return "back/travel/back";
	}
}
