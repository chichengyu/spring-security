package com.security;

import com.security.dao.UserDao;
import com.security.pojo.Permission;
import com.security.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    /**
     * 登陆验证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据账号查询用户
        User user = userDao.findByUsername(username);

        // 根据账号查询当前用户拥有的权限 如：ROLE_ADD_PRODUCT / ROLE_EDIT_PRODUCT
        List<Permission> permissions = userDao.findPermissionByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Permission permission : permissions){
            authorities.add(new SimpleGrantedAuthority(permission.getTag()));// ROLE_ADD_PRODUCT 放进去
        }
        // 吧所有权限赋值给 user
        user.setAuthorities(authorities);
        return user;
    }
}
