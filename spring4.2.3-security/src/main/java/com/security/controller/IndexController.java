package com.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }


    @RequestMapping("/userlogin")
    public String login(){
        return "login";
    }

    @RequestMapping("/error")
    public String error(){
        return "error";
    }
}
