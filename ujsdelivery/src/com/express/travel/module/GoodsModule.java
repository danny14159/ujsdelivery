package com.express.travel.module;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.util.UploadHelper;
import com.express.travel.bean.Goods;
import com.express.travel.service.GoodsService;

@Controller
@RequestMapping("/goods")
public class GoodsModule{

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/ins")
    public String insert(HttpServletRequest request) {
    	
    	Goods goods = new Goods();
    	UploadHelper.processUploadRequest(request, goods);
    	goods.setState("S");
    	goodsService.insert(goods);
    	
    	return "redirect:/app/back/tv/listgoods";
    }
} 