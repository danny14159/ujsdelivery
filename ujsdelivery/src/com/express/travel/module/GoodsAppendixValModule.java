package com.express.travel.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.module.BasicModule;
import com.express.core.service.BaseService;
import com.express.travel.bean.GoodsAppendixVal;
import com.express.travel.service.GoodsAppendixValService;

@Controller
@RequestMapping("/gav")
public class GoodsAppendixValModule extends BasicModule<GoodsAppendixVal>{

    @Autowired
    private GoodsAppendixValService goodsAppendixValService;

    @Override
    protected BaseService<GoodsAppendixVal> getService() {
        return goodsAppendixValService;
    }

} 