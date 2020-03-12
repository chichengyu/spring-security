package com.shiro.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiro.config.TokenConfig;
import com.shiro.constarts.Constant;
import com.shiro.dao.SysUserDao;
import com.shiro.enums.ResponseCode;
import com.shiro.exception.BusinessException;
import com.shiro.pojo.SysUser;
import com.shiro.service.PermissionService;
import com.shiro.service.RoleService;
import com.shiro.service.UserRoleService;
import com.shiro.service.UserService;
import com.shiro.utils.IdWorker;
import com.shiro.utils.JwtTokenUtil;
import com.shiro.service.RedisService;
import com.shiro.utils.PageUtil;
import com.shiro.utils.Response;
import com.shiro.vo.req.LoginReqVo;
import com.shiro.vo.req.UserAddReqVo;
import com.shiro.vo.req.UserOwnRoleReqVo;
import com.shiro.vo.req.UserPageReqVo;
import com.shiro.vo.req.UserUpdateReqVo;
import com.shiro.vo.resp.LoginRespVo;
import com.shiro.vo.resp.PageVo;
import com.shiro.vo.resp.UserOwnRoleRespVo;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TokenConfig tokenConfig;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 登陆
     * @param loginReqVo
     * @return
     */
    @Override
    public Response<LoginRespVo> login(LoginReqVo loginReqVo) {
        SysUser user = sysUserDao.findByUsername(loginReqVo.getUsername());
        if (null == user){// 账号不存在
            throw new BusinessException(ResponseCode.ACCOUNT_ERROR);
        }
        if (!BCrypt.checkpw(loginReqVo.getPassword(),user.getPassword())){// 密码不正确
            throw new BusinessException(ResponseCode.ACCOUNT_PASSWORD_ERROR);
        }
        if (user.getStatus() == 2){// 账号锁定
            return Response.error(ResponseCode.ACCOUNT_LOCK.getMessage());
        }
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        List<String> permissions = new ArrayList<>();
        permissions.add("sys:user");
        permissions.add("user:test");
        // 用户业务 token 令牌
        String accessToken = JwtTokenUtil.getInstance()
                .setIssuer(tokenConfig.getIssuer())
                .setSecret(tokenConfig.getSecretKey())
                .setExpired(tokenConfig.getAccessTokenExpireTime().toMillis())
                .setSubObject(user.getId())
                .setClaim(Constant.JWT_ROLES_KEY, JSON.toJSONString(roles))
                .setClaim(Constant.JWT_PERMISSIONS_KEY, JSON.toJSONString(permissions))
                .setClaim(Constant.JWT_USER_NAME, user.getUsername())
                .generateToken();
        // 刷新 refresh token
        Duration refreshTokenTime = loginReqVo.getType().equals("1") ? tokenConfig.getRefreshTokenExpireTime() : tokenConfig.getRefreshTokenExpireAppTime();
        String refreshToken = JwtTokenUtil.getInstance()
                .setIssuer(tokenConfig.getIssuer())
                .setSecret(tokenConfig.getSecretKey())
                .setExpired(refreshTokenTime.toMillis())
                .setSubObject(user.getId())
                .setClaim(Constant.JWT_ROLES_KEY, JSON.toJSONString(roles))
                .setClaim(Constant.JWT_PERMISSIONS_KEY, JSON.toJSONString(permissions))
                .setClaim(Constant.JWT_USER_NAME, user.getUsername())
                .generateToken();
        LoginRespVo loginRespVo = new LoginRespVo();
        loginRespVo.setAccessToken(accessToken);
        loginRespVo.setRefreshToken(refreshToken);
        loginRespVo.setId(user.getId());
        loginRespVo.setPhone(user.getPhone());
        loginRespVo.setUsername(user.getUsername());
        return Response.success(loginRespVo);
    }

    /**
     * 退出登陆
     * @param accessToken
     * @param refreshToken
     * @return
     */
    @Override
    public Response<String> logout(String accessToken, String refreshToken) {
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(refreshToken)){
            throw new BusinessException(ResponseCode.DATA_ERROR);
        }
        // 获取用户 id
        String userId = JwtTokenUtil.getInstance().getUserId(accessToken);
        long remainingTimeAccessToken = JwtTokenUtil.getInstance().getRemainingTime(accessToken);
        long remainingTimeRefreshToken = JwtTokenUtil.getInstance().getRemainingTime(refreshToken);
        /**
         * 把token 加入黑名单 禁止再访问我们的系统资源
         */
        redisService.set(Constant.JWT_ACCESS_TOKEN_BLACKLIST+accessToken,userId,remainingTimeAccessToken, TimeUnit.MILLISECONDS);
        /**
         * 把 refreshToken 加入黑名单 禁止再拿来刷新token
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,userId,remainingTimeRefreshToken,TimeUnit.MILLISECONDS);
        return Response.success();
    }

    /**
     * 分页查询用户（包括搜索条件）
     * @param userPageReqVo
     * @return
     */
    @Override
    public Response<PageVo<SysUser>> pageInfo(UserPageReqVo userPageReqVo) {
        PageHelper.startPage(userPageReqVo.getPageNum(),userPageReqVo.getPageSize());
        List<SysUser> sysUsers = sysUserDao.selectAll(userPageReqVo);
        return Response.success(PageUtil.getPageVo(new PageInfo<SysUser>(sysUsers)));
    }

    /**
     * 新增用户
     * @param userAddReqVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> addUser(UserAddReqVo userAddReqVo) {
        if (sysUserDao.findByUsername(userAddReqVo.getUsername()) != null){
            throw new BusinessException(ResponseCode.ACCOUNT_EXISTS_ERROR);
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userAddReqVo,sysUser);
        String salt = BCrypt.gensalt();
        sysUser.setId(String.valueOf(idWorker.nextId()));
        sysUser.setPassword(BCrypt.hashpw(userAddReqVo.getPassword(),salt));
        sysUser.setSalt(salt);
        sysUser.setCreateTime(new Date());
        if (sysUserDao.insertSelective(sysUser) != 1){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return Response.success();
    }

    /**
     * 查询用户拥有的角色数据接口
     * @param userId
     * @return
     */
    @Override
    public Response<UserOwnRoleRespVo> getUserOwnRole(String userId) {
        UserOwnRoleRespVo userOwnRoleRespVo = new UserOwnRoleRespVo();
        userOwnRoleRespVo.setOwnRoleIds(userRoleService.getRoleIdsByUserId(userId));
        userOwnRoleRespVo.setAllRole(roleService.selectAll());
        return Response.success(userOwnRoleRespVo);
    }

    /**
     * 更新用户角色
     * @param vo
     * @return
     */
    @Override
    public Response<String> setUserOwnRole(UserOwnRoleReqVo vo) {
        // 更新用户角色关联表信息
        userRoleService.addUserRoleInfo(vo);
        /**
         * 标记用户 要主动去刷新
         */
        redisService.set(Constant.JWT_REFRESH_KEY + vo.getUserId(),vo.getUserId(),tokenConfig.getAccessTokenExpireTime().toMillis(),TimeUnit.MILLISECONDS);
        /**
         * 清楚用户授权数据缓存
         */
        redisService.delete(Constant.IDENTIFY_CACHE_KEY+vo.getUserId());
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 刷新token接口  重新签发 token
     * @param refreshToken
     * @return
     */
    public Response refreshToken(String refreshToken){
        // 判断 token是否过期 或是否加入黑名单
        JwtTokenUtil instance = JwtTokenUtil.getInstance();
        if (!instance.checkToken(refreshToken) || redisService.hasKey(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken)){
            throw new BusinessException(ResponseCode.TOKEN_ERROR);
        }
        // 主动刷新（比如修改用户角色后，需要重新登陆）
        // 重新签发 token 与 标记刷新 token
        String userId = instance.getUserId(refreshToken);
        List<String> roleIds = new ArrayList<>();
        Set<String> permissions = new HashSet<>();
        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)){
            // 存在表示需要主动刷新
            roleIds = userRoleService.getRoleIdsByUserId(userId);
            permissions = getPermissionsByUserId(userId);
        }
        String newAccessToken = instance.setClaim(Constant.JWT_ROLES_KEY, JSON.toJSONString(roleIds))
                .setClaim(Constant.JWT_PERMISSIONS_KEY, JSON.toJSONString(permissions))
                .setExpired(tokenConfig.getAccessTokenExpireTime().toMillis())
                .generateToken();
        /*if(redisService.hasKey(Constant.JWT_REFRESH_KEY+userId)){
            redisService.set(Constant.JWT_REFRESH_IDENTIFICATION+newAccessToken,userId, redisService.getExpire(Constant.JWT_REFRESH_KEY+userId,TimeUnit.MILLISECONDS),TimeUnit.MILLISECONDS);
        }*/
        Map map = new HashMap<>();
        //map.put("authorization",newAccessToken);
        map.put("authorization",newAccessToken);
        return Response.success(map);
    }

    /**
     * 通过用户id获取该用户所拥有的角色
     * @param userId
     */
    public Set<String> getPermissionsByUserId(String userId){
        return permissionService.getPermissionByUserId(userId);
    }


    /**
     * 更新用户信息
     * @param userUpdateReqVo
     * @param operationId 操作人
     * @return
     */
    @Override
    public Response<String> updateUserInfo(UserUpdateReqVo userUpdateReqVo, String operationId) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userUpdateReqVo,sysUser);
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        if (StringUtils.isBlank(userUpdateReqVo.getPassword())){
            sysUser.setPassword(null);
        }else{
            String salt = BCrypt.gensalt();
            sysUser.setSalt(salt);
            sysUser.setPassword(BCrypt.hashpw(userUpdateReqVo.getPassword(),salt));
        }
        int result = sysUserDao.updateSelective(sysUser);
        if (result == 0){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        if(userUpdateReqVo.getStatus() == 2){
            // 账号锁定需要更新 redis 账号锁定状态
            redisService.set(Constant.ACCOUNT_LOCK_KEY+sysUser.getId(),sysUser.getId());
        }else {
            // 删除账号锁定状态
            redisService.delete(Constant.ACCOUNT_LOCK_KEY+sysUser.getId());
        }
        return Response.success();
    }

    /**
     * 批量/删除用户接口
     * @param list
     * @param operationId 操作人
     * @return
     */
    @Override
    public Response<String> deletedUsers(List<String> list, String operationId) {
        // 更新操作人
        SysUser sysUser = new SysUser();
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        int result = sysUserDao.deletedUsers(sysUser,list);
        if (result == 0){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        /**
         * 更新 redis 中 账号的删除状态
         */
        for (String userId : list){
            redisService.set(Constant.DELETED_USER_KEY + userId,userId,tokenConfig.getAccessTokenExpireTime().toMillis(),TimeUnit.MILLISECONDS);
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

}
