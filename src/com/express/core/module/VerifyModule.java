package com.express.core.module;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by denglei on 2017/3/14.
 */
@Controller
public class VerifyModule {

    @RequestMapping(value = "/MP_verify_S6wFmMhSkAIlA4zf.txt",method = RequestMethod.GET)
    public String verify(){
        return "S6wFmMhSkAIlA4zf";
    }
}
