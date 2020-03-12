package com.shiro.service.impl;

import com.shiro.config.TokenConfig;
import com.shiro.constarts.Constant;
import com.shiro.dao.SysPermissionDao;
import com.shiro.enums.ResponseCode;
import com.shiro.exception.BusinessException;
import com.shiro.pojo.SysPermission;
import com.shiro.service.PermissionService;
import com.shiro.service.RedisService;
import com.shiro.service.RolePermissionService;
import com.shiro.service.UserRoleService;
import com.shiro.utils.IdWorker;
import com.shiro.utils.Response;
import com.shiro.vo.req.PermissionAddReqVo;
import com.shiro.vo.req.PermissionUpdateReqVo;
import com.shiro.vo.resp.PermissionRespNodeVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class PermissionServiceImpl implements PermissionService {

    private static Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private SysPermissionDao sysPermissionDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenConfig tokenConfig;

    /**
     * 用户id查询用户拥有的权限菜单列表
     * @param userId
     * @return
     */
    @Override
    public List<PermissionRespNodeVo> permissionTreeListByUserId(String userId) {
        // TODO: 这先查询所有菜单
        return getTree(selectAll(),false);
    }

    /**
     * 查询所有权限列表（树形表格结果数据展示）
     * 树形表格结果数据组装
     * @return
     */
    public List<SysPermission> selectAll() {
        List<SysPermission> sysPermissions = sysPermissionDao.selectAll();
        if (!sysPermissions.isEmpty()){
            for (SysPermission sysPermission : sysPermissions){
                for (SysPermission permission : sysPermissions){
                    if(StringUtils.isNotBlank(sysPermission.getPid()) && StringUtils.isNotBlank(permission.getId()) && StringUtils.equals(sysPermission.getPid(),permission.getId())){
                        sysPermission.setPidName(permission.getName());
                        break;
                    }
                }
            }
        }
        return sysPermissions;
    }

    /**
     * 添加权限的上级选择权限树结构展示（递归），不需要展示到按钮
     * @return
     */
    @Override
    public Response<List<PermissionRespNodeVo>> selectAllMenuByTree() {
        // 设置一个最顶级的吗，默认选项
        List<SysPermission> list = sysPermissionDao.selectAll();
        List<PermissionRespNodeVo> result = new ArrayList<>();
        PermissionRespNodeVo respNodeVo = new PermissionRespNodeVo();
        respNodeVo.setId("0");
        respNodeVo.setTitle("顶级菜单");
        respNodeVo.setChildren(getTree(list,false));
        result.add(respNodeVo);
        return Response.success(result);
    }
    //==================== start 递归筛选 目录与菜单权限 ，排除按钮 type = 3 ====================
    /**
     * type=true 递归遍历到菜单
     * type=false 递归遍历到按钮
     * @param permissionList
     * @param type
     * @return
     */
    private List<PermissionRespNodeVo> getTree(List<SysPermission> permissionList,boolean type){
        // 无父级 pid 默认 0
        return _getTree(permissionList,"0",type);
    }
    private List<PermissionRespNodeVo> _getTree(List<SysPermission> permissionList,String id,boolean type){
        List<PermissionRespNodeVo> list = new ArrayList<>();
        if (null == permissionList || permissionList.isEmpty()){
            return list;
        }
        for (SysPermission permission : permissionList){
            if (permission.getPid().equals(id)){
                // 遍历到按钮 与 不遍历到按钮 都会执行
                if (type || (!type && permission.getType().intValue() != 3)){
                    PermissionRespNodeVo respNodeVo = new PermissionRespNodeVo();
                    respNodeVo.setId(permission.getId());
                    respNodeVo.setTitle(permission.getName());
                    respNodeVo.setUrl(permission.getUrl());
                    respNodeVo.setChildren(_getTree(permissionList,permission.getId(),type));
                    list.add(respNodeVo);
                }/*else if (type){
                    PermissionRespNodeVo respNodeVo = new PermissionRespNodeVo();
                    respNodeVo.setId(permission.getId());
                    respNodeVo.setTitle(permission.getName());
                    respNodeVo.setUrl(permission.getUrl());
                    respNodeVo.setChildren(_getTree(permissionList,permission.getId(),type));
                    list.add(respNodeVo);
                }*/
            }
        }
        return list;
    }
    //==================== end 递归筛选 目录与菜单权限 ，排除按钮 type = 3 ====================



    /**
     * 新增权限
     * @param permissionAddReqVo
     * @return
     */
    @Override
    public Response addPermission(PermissionAddReqVo permissionAddReqVo) {
        // 验证表单数据
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(permissionAddReqVo,permission);
        verifyForm(permission);
        permission.setId(String.valueOf(idWorker.nextId()));
        permission.setCreateTime(new Date());
        int result = sysPermissionDao.insertSelective(permission);
        if (result != 1){
            return Response.error(ResponseCode.OPERATION_ERROR.getMessage());
        }
        return Response.success();
    }

    /**
     * 更新权限
     * @param permissionUpdateReqVo
     * @return
     */
    @Override
    public Response<String> updatePermission(PermissionUpdateReqVo permissionUpdateReqVo) {
        // 1.验证表单数据
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(permissionUpdateReqVo,permission);
        verifyForm(permission);
        SysPermission sysPermission = sysPermissionDao.selectByPrimaryKey(permission.getId());
        if (sysPermission == null){
            logger.info("传入的id在数据库中不存在,{}",permissionUpdateReqVo);
            throw new BusinessException(ResponseCode.DATA_ERROR);
        }
        // 2.验证 所属菜单是否发生了变化
        if (!sysPermission.getPid().equals(permissionUpdateReqVo.getPid())){
            // 目录 菜单 按钮，如 菜单有子级按钮，则不能吧当前菜单更新为按钮类型
            // 所属菜单发生了变化要校验该权限是否存在子集
            int resultCount = sysPermissionDao.selectChild(permissionUpdateReqVo.getId());
            if (resultCount > 0){
                throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_UPDATE);
            }
        }
        // 3.更新菜单信息
        permission.setUpdateTime(new Date());
        int updateResultCount = sysPermissionDao.updateByPrimaryKeySelective(permission);
        if (updateResultCount != 1){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        // 4.判断授权标识符是否发生了变化（***注意：如发生变化，需要更新与权限菜单相关的用户在 redis 中的状态）
        if (!sysPermission.getPerms().equals(permissionUpdateReqVo.getPerms())){
            // 通过权限 id 查询所有的角色 id
            List<String> roleIdsByPermissionId = rolePermissionService.getRoleIdsByPermissionId(permissionUpdateReqVo.getId());
            if (roleIdsByPermissionId != null && !roleIdsByPermissionId.isEmpty()){
                // 通过角色 id 查询所有的用户 id
                List<String> userIdsByRoleIds = userRoleService.getUserIdsByRoleIds(roleIdsByPermissionId);
                if (userIdsByRoleIds!=null && !userIdsByRoleIds.isEmpty()){
                    for (String userId :userIdsByRoleIds){
                        /**
                         * 标识用户主动刷新的标识
                         */
                        redisService.set(Constant.JWT_REFRESH_KEY + userId,userId,tokenConfig.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                        /**
                         * 清楚用户授权数据缓存
                         */
                        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
                    }
                }
            }
        }
        return Response.success();
    }

    // 验证参数
    private void verifyForm(SysPermission permissionReqVo){
        // 默认顶级 无父级 pid 默认 0
        //操作后的菜单类型是目录的时候 父级必须为目录
        //操作后的菜单类型是菜单的时候，父类必须为目录类型
        //操作后的菜单类型是按钮的时候 父类必须为菜单类型
        Integer type = permissionReqVo.getType();
        String pid = permissionReqVo.getPid();
        if (null != type && StringUtils.isNotBlank(pid)){
            SysPermission permission = sysPermissionDao.selectByPrimaryKey(pid);
            if (type == 1){
                if (permission != null && permission.getType() > 1){// 父级 只能为 无父级/目录类型  0 / 1
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                }
            }else if (type == 2) {// 父级 只能为目录类型 1
                if ("0".equals(pid) || (permission != null && permission.getType() != 1)){// 父级 只能为目录类型 1
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_MENU_ERROR);
                }
                if (StringUtils.isBlank(permissionReqVo.getUrl())){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
            }else if (type == 3) {// 父级 只能为菜单类型 2
                if ("0".equals(pid) || (permission != null && permission.getType() != 2)){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR);
                }
                if(StringUtils.isEmpty(permissionReqVo.getPerms())){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_URL_PERMS_NULL);
                }
                if(StringUtils.isEmpty(permissionReqVo.getUrl())){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                if(StringUtils.isEmpty(permissionReqVo.getMethod())){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_URL_METHOD_NULL);
                }
                if(StringUtils.isEmpty(permissionReqVo.getCode())){
                    throw new BusinessException(ResponseCode.OPERATION_MENU_PERMISSION_URL_CODE_NULL);
                }
            }
        }else{
            throw new BusinessException("参数错误");
        }
    }

    /**
     * 删除菜单权限
     * @param permissionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> deletedPermission(String permissionId) {
        //判断是否有子集关联，有则不能删除
        int resultCount = sysPermissionDao.selectChild(permissionId);
        if (resultCount > 0){
            throw new BusinessException(ResponseCode.ROLE_PERMISSION_RELATION);
        }
        //通过权限id删除相关角色和该菜单权限的关联表信息
        rolePermissionService.removeRoleByPermissionId(permissionId);
        // 删除权限菜单表
        SysPermission sysPermission = new SysPermission();
        sysPermission.setId(permissionId);
        sysPermission.setDeleted(0);
        sysPermission.setUpdateTime(new Date());
        int updateCount = sysPermissionDao.updateByPrimaryKeySelective(sysPermission);
        if (updateCount != 1){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        // 标记用户主动去刷新
        // 通过权限 id 查询所有的角色 id
        List<String> roleIdsByPermissionId = rolePermissionService.getRoleIdsByPermissionId(permissionId);
        if (roleIdsByPermissionId != null && !roleIdsByPermissionId.isEmpty()){
            // 通过角色 id 查询所有的用户 id
            List<String> userIdsByRoleIds = userRoleService.getUserIdsByRoleIds(roleIdsByPermissionId);
            if (userIdsByRoleIds!=null && !userIdsByRoleIds.isEmpty()){
                for (String userId :userIdsByRoleIds){
                    /**
                     * 标识用户主动刷新的标识
                     */
                    redisService.set(Constant.JWT_REFRESH_KEY + userId,userId,tokenConfig.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                    /**
                     * 清楚用户授权数据缓存
                     */
                    redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
                }
            }
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 获取所有权限（包括按钮），角色添加/编辑/分配权限时用到的结结构数据
     * @return
     */
    @Override
    public List<PermissionRespNodeVo> selectAllTree() {
        return getTree(selectAll(),true);
    }


    // ============================ 用于其他服务调用 ===============================
    /**
     * 通过用户id查询用户拥有的权限 (如：sys:user:add)
     * @param userId
     * @return
     */
    public Set<String> getPermissionByUserId(String userId){
        List<SysPermission> list = getPermission(userId);
        Set<String> permissions = new HashSet<>();
        for (SysPermission permission : list){
            if (StringUtils.isNotBlank(permission.getPerms())){
                permissions.add(permission.getPerms());
            }
        }
        return permissions;
    }
    // 通过用户id查询用户拥有的权限
    public List<SysPermission> getPermission(String userId){
        List<SysPermission> list = sysPermissionDao.getPermissionByUserId(userId);
        return list;
    }
}
