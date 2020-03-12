package com.shiro.shiro;

import com.shiro.constarts.Constant;
import com.shiro.enums.ResponseCode;
import com.shiro.exception.BusinessException;
import com.shiro.service.RedisService;
import com.shiro.utils.JwtTokenUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/** 自定义的认证器
 *
 * Shiro 的 认证器 HashedCredentialsMatcher
 * Shiro主要是用过 HashedCredentialsMatcher 的 doCredentialsMatch方法对提交的用户名密码组装的 token 进行效验，我们这使用的是 jwt-token
 */
public class CustomHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    private RedisService redisService;

    /**
     * 真正的效验（主要是通过 HashedCredentialsMatcher 的 doCredentialsMatch 方法返回值进行效验）
     * 我们使用的是 jwt-token ，所以在此处效验
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        CustomUsernamePasswordToken customUsernamePasswordToken = (CustomUsernamePasswordToken) token;
        String accessToken = (String) customUsernamePasswordToken.getPrincipal();
        String userId = JwtTokenUtil.getInstance().getUserId(accessToken);
        /**
         * 判断用户是否被锁定
         */
        if (redisService.hasKey(Constant.ACCOUNT_LOCK_KEY + userId)){
            throw new BusinessException(ResponseCode.ACCOUNT_LOCK);
        }
        /**
         * 判断用户是否被删除
         */
        if (redisService.hasKey(Constant.DELETED_USER_KEY + userId)){
            throw new BusinessException(ResponseCode.ACCOUNT_HAS_DELETED_ERROR);
        }
        /**
         * 判断token 是否主动登出
         */
        if (redisService.hasKey(Constant.JWT_ACCESS_TOKEN_BLACKLIST + accessToken)){
            throw new BusinessException(ResponseCode.TOKEN_ERROR);
        }
        /**
         * 判断token是否通过校验（不通过，token 失效）
         */
        if (!JwtTokenUtil.getInstance().checkToken(accessToken)){
            throw new BusinessException(ResponseCode.TOKEN_PAST_DUE);
        }
        /**
         * 判断这个登录用户是否要主动去刷新
         * 如果 key=Constant.JWT_REFRESH_KEY+userId大于accessToken说明是在 accessToken不是重新生成的
         * 这样就要判断它是否刷新过了/或者是否是新生成的token
         */
        // 主动刷新 refresh_token 剩下的时间（设置场景 比如修改了用户的角色/权限去刷新token）
        //Long expire = redisService.getExpire(Constant.JWT_REFRESH_KEY + userId, TimeUnit.MILLISECONDS);
        // jwt token 的剩下时间
        long remainingTime = JwtTokenUtil.getInstance().getRemainingTime(accessToken);
        /*if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId) && expire > remainingTime){
            *//**
             * 是否存在刷新的标识
             *//*
            if (!redisService.hasKey(Constant.JWT_REFRESH_IDENTIFICATION + accessToken)){
                // expire > remainingTime 又新操作，且存在刷新的标识，token失效,请刷新token
                throw new BusinessException(ResponseCode.TOKEN_PAST_DUE);
            }
        }*/
        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)){
            /**
             * 是否存在刷新的标识
             * 通过剩余的过期时间比较如果token的剩余过期时间大与标记key的剩余过期时间，就说明这个tokne是在这个标记key之后生成的
             */
            if (redisService.getExpire(Constant.JWT_REFRESH_KEY + userId,TimeUnit.MILLISECONDS) > remainingTime){
                // expire > remainingTime 又新操作，且存在刷新的标识，token失效,请刷新token
                throw new BusinessException(ResponseCode.TOKEN_PAST_DUE);
            }
        }
        return true;
    }
}
