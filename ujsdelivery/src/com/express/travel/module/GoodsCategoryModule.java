package com.express.travel.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.module.BasicModule;
import com.express.core.service.BaseService;
import com.express.travel.bean.GoodsCategory;
import com.express.travel.service.GoodsCategoryService;

@Controller
@RequestMapping("/gc")
public class GoodsCategoryModule extends BasicModule<GoodsCategory>{

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @Override
    protected BaseService<GoodsCategory> getService() {
        return goodsCategoryService;
    }

} 