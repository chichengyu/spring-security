package com.shiro.shiro;

import com.shiro.constarts.Constant;
import com.shiro.service.RedisService;
import com.shiro.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/** 自定义域 CustomRealm
 *
 * Realm即领域，相当于datasource数据源，securityManager进行安全认证需要通过Realm获取用户身份信息及用户权限数据，
 * 比如：如果用户身份数据在数据库那么realm就需要从数据库获取用户身份信息
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private RedisService redisService;

    /**
     * 此方法必须有，不然我们自定义的 CustomUsernamePasswordToken 不生效
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomUsernamePasswordToken;
    }

    /**
     * 用户授权，设置用户所拥有的 角色/权限
     * @param principals
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String accessToken = (String) principals.getPrimaryPrincipal();
        // 授权器
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String userId = JwtTokenUtil.getInstance().getUserId(accessToken);
        // 从 token 中解析角色与权限信息，不用在去数据库查
        /**
         * 通过剩余的过期时间比较如果token的剩余过期时间大与标记key的剩余过期时间
         * 就说明这个tokne是在这个标记key之后生成的
         */
        Claims claims = JwtTokenUtil.getInstance().parseToken(accessToken);
        if (claims.get(Constant.JWT_ROLES_KEY) != null){
            authorizationInfo.addRoles((Collection<String>) claims.get(Constant.JWT_ROLES_KEY));
        }
        /*Long expire = redisService.getExpire(Constant.JWT_ROLES_KEY + userId, TimeUnit.MILLISECONDS);
        long remainingTime = JwtTokenUtil.getInstance().getRemainingTime(accessToken);
        if (redisService.hasKey(Constant.JWT_ROLES_KEY + userId) && expire > remainingTime){
            *//**
             * 返回该用户的角色信息给授权器
             *//*
            // expire > remainingTime 说明有更新角色信息
            Claims claims = JwtTokenUtil.getInstance().parseToken(accessToken);
            Object o = claims.get(Constant.JWT_ROLES_KEY + userId);

            authorizationInfo.addRole();
        }*/
        /**
         * 返回该用户的权限信息给授权器
         */
        if (claims.get(Constant.JWT_PERMISSIONS_KEY) != null){
            authorizationInfo.addStringPermissions((Collection<String>) claims.get(Constant.JWT_PERMISSIONS_KEY));
        }
        return authorizationInfo;
    }

    /**
     * 用户认证，以前是验证用户名/密码。现在我们验证 token，吧我们的 token 交还给 认证器
     * @param token
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 吧我们的 token 交还给 认证器
        CustomUsernamePasswordToken customUsernamePasswordToken = (CustomUsernamePasswordToken) token;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(customUsernamePasswordToken.getPrincipal(), customUsernamePasswordToken.getCredentials(), CustomRealm.class.getName());
        return info;
    }
}
