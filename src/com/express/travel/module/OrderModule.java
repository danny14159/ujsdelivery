package com.express.travel.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.module.BasicModule;
import com.express.core.service.BaseService;
import com.express.travel.bean.Order;
import com.express.travel.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderModule extends BasicModule<Order>{

    @Autowired
    private OrderService orderService;

    @Override
    protected BaseService<Order> getService() {
        return orderService;
    }

} 