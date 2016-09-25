package com.express.travel.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.module.BasicModule;
import com.express.core.service.BaseService;
import com.express.travel.bean.GoodsAppendixParam;
import com.express.travel.service.GoodsAppendixParamService;

@Controller
@RequestMapping("/gap")
public class GoodsAppendixParamModule extends BasicModule<GoodsAppendixParam>{

    @Autowired
    private GoodsAppendixParamService goodsAppendixParamService;

    @Override
    protected BaseService<GoodsAppendixParam> getService() {
        return goodsAppendixParamService;
    }

} 