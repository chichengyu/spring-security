package com.security.dao;

import com.security.pojo.Permission;
import com.security.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    /**
     * 查询当前用户
     * @param username
     * @return
     */
    User findByUsername(@Param("username") String username);

    /**
     * 查询当前用户拥有的权限
     * @param username
     * @return
     */
    List<Permission> findPermissionByUsername(@Param("username")String username);
}
