package com.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 登陆失败后执行的方法，可返回json
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Map<String,Object> map = new HashMap<>();
        map.put("success",0);
        map.put("msg",e.getMessage());
        String json = objectMapper.writeValueAsString(map);
        // 或
        // httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.setHeader("Context-Type","application/json");
        httpServletResponse.getWriter().write(json);
    }
}
