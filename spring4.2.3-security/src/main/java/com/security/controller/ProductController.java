package com.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/product")
public class ProductController {

    @RequestMapping("/add")
    public String add(){
        return "product/add";
    }


    @RequestMapping("/list")
    /*@RolesAllowed("ADMIN")*/
    /*@Secured("ROLE_ADMIN")*/
    //@PreAuthorize("authentication.principal.username=='tom'")// 表示当前登陆用户，只有tom才能访问
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String list(){
        // 获取登陆用户信息
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && principal instanceof UserDetails){
          UserDetails user =  (UserDetails)principal;
            String name = user.getUsername();
        }

        return "product/list";
    }


    @RequestMapping("/edit")
    public String update(){
        return "product/edit";
    }

    @RequestMapping("/delete")
    public String delete(){
        return "product/delete";
    }
}
