package com.shiro.shiro;

import com.alibaba.fastjson.JSON;
import com.shiro.constarts.Constant;
import com.shiro.enums.ResponseCode;
import com.shiro.exception.BusinessException;
import com.shiro.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 效验请求是否携带 token ,此时还未进入 controller 所以抛出的异常捕获不到，只能直接输出 json
 */
@Slf4j
public class CustomAccessControllerFilter extends AccessControlFilter {

    /**
     * 返回 true 则进入过滤器链中的下一个过滤器
     * 返回 false 则进入 onAccessDenied 方法，进行效验
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 效验是否携带 token ，此时还未进入 controller 所以抛出的异常捕获不到，只能直接输出 json
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
            if (StringUtils.isBlank(accessToken)) {
                throw new BusinessException(ResponseCode.TOKEN_NOT_NULL);
            }
            // 吧 accessToken 设置进 Shiro
            CustomUsernamePasswordToken customUsernamePasswordToken = new CustomUsernamePasswordToken(accessToken);
            /************************* Shiro 认证 *************************
                Subject，主体即用户，
                在Shiro中subject是一个接口,面向程序员的接口，程序员使用Subject接口执行认证操作。
                调用subject.login(token)执行认证。
            */
            getSubject(servletRequest, servletResponse).login(customUsernamePasswordToken);
        }catch (BusinessException e){
            responseJson(response,e.getCode(),e.getMessage());
            return false;
        }catch (AuthenticationException e){// 认证异常
            log.info("【打印 e.getCause 】，{}",e.getCause());
            // 判断该异常是我们手动抛出，还是 Shiro 抛出，为了区分提示信息
            if (e.getCause() instanceof BusinessException){ // 手动抛出
                BusinessException exception = (BusinessException) e.getCause();
                responseJson(response,exception.getCode(),exception.getMessage());
            }else if (e.getCause() instanceof ShiroException){ // Shiro 抛出
                // 用户认证异常（登陆）
                responseJson(response,ResponseCode.SHIRO_AUTHENTICATION_ERROR.getCode(),ResponseCode.SHIRO_AUTHENTICATION_ERROR.getMessage());
            }else { // 系统异常
                responseJson(response,ResponseCode.SYSTEM_ERROR.getCode(),ResponseCode.SYSTEM_ERROR.getMessage());
            }
            return false;
        }
        return true;
    }

    /**
     * 返回 json
     * @param response
     * @param message
     */
    private void responseJson(HttpServletResponse response,int code,String message){
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(Response.error(code,message)));
            writer.close();
            response.flushBuffer();
        } catch (IOException e) {
            log.error("【输出 JSON 异常】，{}",e);
        }
    }
}
