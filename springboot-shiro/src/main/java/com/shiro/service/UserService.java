package com.shiro.service;

import com.shiro.pojo.SysUser;
import com.shiro.utils.Response;
import com.shiro.vo.req.LoginReqVo;
import com.shiro.vo.req.UserAddReqVo;
import com.shiro.vo.req.UserOwnRoleReqVo;
import com.shiro.vo.req.UserPageReqVo;
import com.shiro.vo.req.UserUpdateReqVo;
import com.shiro.vo.resp.LoginRespVo;
import com.shiro.vo.resp.PageVo;
import com.shiro.vo.resp.UserOwnRoleRespVo;

import java.util.List;

public interface UserService {

    /**
     * 登陆
     * @param loginReqVo
     * @return
     */
    Response<LoginRespVo> login(LoginReqVo loginReqVo);

    /**
     * 退出登陆
     * @param accessToken
     * @param refreshToken
     * @return
     */
    Response<String> logout(String accessToken, String refreshToken);

    /**
     * 分页查询用户（包括搜索条件）
     * @param userPageReqVo
     * @return
     */
    Response<PageVo<SysUser>> pageInfo(UserPageReqVo userPageReqVo);

    /**
     * 新增用户
     * @param userAddReqVo
     * @return
     */
    Response<String> addUser(UserAddReqVo userAddReqVo);

    /**
     * 查询用户拥有的角色数据接口
     * @param userId
     * @return
     */
    Response<UserOwnRoleRespVo> getUserOwnRole(String userId);

    /**
     * 更新用户角色
     * @param vo
     * @return
     */
    Response<String> setUserOwnRole(UserOwnRoleReqVo vo);

    /**
     * 刷新token接口
     * @param refreshToken
     * @return
     */
    public Response refreshToken(String refreshToken);

    /**
     * 更新用户信息
     * @param userUpdateReqVo
     * @param operationId 操作人
     * @return
     */
    Response<String> updateUserInfo(UserUpdateReqVo userUpdateReqVo, String operationId);

    /**
     * 批量/删除用户接口
     * @param list
     * @param operationId 操作人
     * @return
     */
    Response<String> deletedUsers(List<String> list, String operationId);


}
