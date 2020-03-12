package com.shiro.controller;

import com.shiro.constarts.Constant;
import com.shiro.pojo.SysUser;
import com.shiro.service.UserService;
import com.shiro.utils.JwtTokenUtil;
import com.shiro.utils.Response;
import com.shiro.vo.req.LoginReqVo;
import com.shiro.vo.req.UserAddReqVo;
import com.shiro.vo.req.UserOwnRoleReqVo;
import com.shiro.vo.req.UserPageReqVo;
import com.shiro.vo.req.UserUpdateReqVo;
import com.shiro.vo.resp.PageVo;
import com.shiro.vo.resp.UserOwnRoleRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(value = "用户登陆/登出",tags = "用户登陆/登出相关的接口")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登陆
     * @param loginReqVo
     * @return
     */
    @ApiOperation(value = "用户登陆",notes = "用户登陆的接口")
    @PostMapping("/login")
    public Response login(@RequestBody @Valid LoginReqVo loginReqVo){
        return userService.login(loginReqVo);
    }

    /**
     * 登出
     * @param request
     * @return
     */
    @ApiOperation(value = "用户登出",notes = "用户登出的接口")
    @GetMapping("/logout")
    public Response logout(HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
        return userService.logout(accessToken,refreshToken);
    }

    @ApiOperation(value = "分页查询用户",notes = "分页查询用户接口")
    @PostMapping("/users")
    public Response<PageVo<SysUser>> test(@RequestBody @Valid UserPageReqVo userPageReqVo){
        return userService.pageInfo(userPageReqVo);
    }

    @ApiOperation(value = "新增用户",notes = "新增用户接口")
    @PostMapping("/user")
    public Response<String> addUser(@RequestBody @Valid UserAddReqVo userAddReqVo){
        return userService.addUser(userAddReqVo);
    }

    @ApiOperation(value = "查询用户拥有的角色数据接口",notes = "查询用户拥有的角色数据接口")
    @GetMapping("/user/roles/{userId}")
    public Response<UserOwnRoleRespVo> getUserOwnRole(@PathVariable("userId")String userId){
        return userService.getUserOwnRole(userId);
    }

    @ApiOperation(value = "跟新用户角色",notes = "保存用户拥有的角色信息接口")
    @PutMapping("/user/roles")
    public Response<String> saveUserOwnRole(@RequestBody @Valid UserOwnRoleReqVo vo){
        return userService.setUserOwnRole(vo);
    }

    @ApiOperation(value = "jwt token 刷新接口",notes = "jwt token 刷新接口")
    @GetMapping("/user/token")
    public Response<String> refreshToken(HttpServletRequest request){
        String refreshToken=request.getHeader(Constant.REFRESH_TOKEN);
        return userService.refreshToken(refreshToken);
    }

    @ApiOperation(value = "更新用户接口",notes = "更新用户接口")
    @PutMapping("/user")
    public Response<String> updateUserInfo(@RequestBody @Valid UserUpdateReqVo userUpdateReqVo,HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String operationId = JwtTokenUtil.getInstance().getUserId(accessToken);// 操作人
        return userService.updateUserInfo(userUpdateReqVo,operationId);
    }

    @ApiOperation(value = "批量/删除用户接口",notes = "批量/删除用户接口")
    @DeleteMapping("/user")
    public Response<String> deletedUsers(@RequestBody @ApiParam(value = "用户id集合")List<String> list,HttpServletRequest request){
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String operationId = JwtTokenUtil.getInstance().getUserId(accessToken);// 操作人
        return userService.deletedUsers(list,operationId);
    }
}