package com.shiro.controller;

import com.shiro.service.RoleService;
import com.shiro.utils.Response;
import com.shiro.vo.req.RolePageReqVo;
import com.shiro.vo.req.RoleReqVo;
import com.shiro.vo.req.RoleUpdateReqVo;
import com.shiro.vo.resp.PageVo;
import com.shiro.vo.resp.RoleDetailInfoRespVo;
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

@Api(value = "组织管理-角色管理",tags = "角色管理相关接口")
@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "分页获取角色数据接口",notes = "分页获取角色数据接口")
    @PostMapping("/roles")
    public Response<PageVo> pageInfo(@RequestBody @Valid RolePageReqVo rolePageReqVo){
        return roleService.pageInfoRoles(rolePageReqVo);
    }

    @ApiOperation(value = "新增角色接口",notes = "新增角色")
    @PostMapping("/role")
    public Response<String> createRole(@RequestBody @Valid RoleReqVo roleReqVo){
        return roleService.createRole(roleReqVo);
    }

    @ApiOperation(value = "获取角色详情接口",notes = "获取角色详情接口")
    @GetMapping("/role/{id}")
    public Response<RoleDetailInfoRespVo> detailInfo(@PathVariable("id") String roleId){
        return roleService.detailInfo(roleId);
    }

    @ApiOperation(value = "更新角色信息",notes = "更新角色信息接口")
    @PutMapping("/role")
    public Response<String> updateRole(@RequestBody @Valid RoleUpdateReqVo roleUpdateReqVo){
        return roleService.updateRole(roleUpdateReqVo);
    }

    @ApiOperation(value = "删除角色信息",notes = "删除角色接口")
    @DeleteMapping("/role/{id}")
    public Response<String> deletedRole(@PathVariable("id") String roleId){
        return roleService.deletedRole(roleId);
    }
}
