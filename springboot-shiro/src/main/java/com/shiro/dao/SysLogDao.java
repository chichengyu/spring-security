package com.shiro.dao;

import com.shiro.pojo.SysLog;

public interface SysLogDao {
    int insert(SysLog record);

    int insertSelective(SysLog record);
}