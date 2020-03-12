package com.shiro.controller;


import com.shiro.service.PermissionService;
import com.shiro.utils.Response;
import com.shiro.vo.req.PermissionAddReqVo;
import com.shiro.vo.req.PermissionUpdateReqVo;
import com.shiro.vo.resp.PermissionRespNodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(value = "组织管理-菜单权限管理",tags = "菜单权限管理相关接口")
@RestController
@RequestMapping("/api")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取所有权限（包括按钮）",notes = "获取所有权限（包括按钮），角色添加/编辑/分配权限时用到的结果数据")
    @GetMapping("/permissions/tree/all")
    public Response getAllPermissionTree(){
        return Response.success(permissionService.selectAllTree());
    }

    @ApiOperation(value = "获取所有的菜单权限数据接口",notes = "菜单权限接口")
    @GetMapping("/permissions")
    public Response getAllPermission(){
        return Response.success(permissionService.selectAll());
    }

    @ApiOperation(value = "菜单权限树",notes = "只递归查询目录与菜单，不要按钮，添加权限的上级选择权限树结构展示")
    @GetMapping("/permissions/tree")
    public Response<List<PermissionRespNodeVo>> getAllPermissionTreeExBtn(){
        return permissionService.selectAllMenuByTree();
    }

    @ApiOperation(value = "新增权限",notes = "新增菜单权限接口")
    @PostMapping("/permission")
    public Response<String> createPermission(@RequestBody @Valid PermissionAddReqVo permissionAddReqVO){
        return permissionService.addPermission(permissionAddReqVO);
    }

    @ApiOperation(value = "更新权限",notes = "新更新菜单权限接口")
    @PutMapping("/permission")
    public Response<String> updatePermission(@RequestBody @Valid PermissionUpdateReqVo permissionUpdateReqVo){
        return permissionService.updatePermission(permissionUpdateReqVo);
    }

    @ApiOperation(value = "删除权限",notes = "删除菜单权限接口")
    @DeleteMapping("/permission/{permissionId}")
    public Response<String> deletedPermission(@PathVariable("permissionId") String permissionId){
        return permissionService.deletedPermission(permissionId);
    }
}
