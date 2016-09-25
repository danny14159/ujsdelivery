package com.express.travel.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.module.BasicModule;
import com.express.core.service.BaseService;
import com.express.travel.bean.Region;
import com.express.travel.service.RegionService;

@Controller
@RequestMapping("/region")
public class RegionModule extends BasicModule<Region>{

    @Autowired
    private RegionService regionService;

    @Override
    protected BaseService<Region> getService() {
        return regionService;
    }

} 