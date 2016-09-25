package com.express.core.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.express.core.bean.ReqRecord;
import com.express.core.service.BaseService;
import com.express.core.service.ReqRecordService;

@Controller
@RequestMapping("/req")
public class ReqRecordModule{

    @Autowired
    private ReqRecordService reqRecordService;

    //@Override
    protected BaseService<ReqRecord> getService() {
        return reqRecordService;
    }

} 