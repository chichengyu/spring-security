package com.shiro.controller;

import com.shiro.aop.annotation.MyLog;
import com.shiro.service.DeptService;
import com.shiro.utils.Response;
import com.shiro.vo.req.DeptAddReqVo;
import com.shiro.vo.req.DeptUpdateReqVo;
import com.shiro.vo.resp.DeptRespNodeVo;
import com.shiro.vo.resp.DeptRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(value = "组织管理-部门管理",tags = "部门管理相关接口")
@RestController
@RequestMapping("/api")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @ApiOperation(value = "查询所有部门数据接口",notes = "部门管理列表")
    @RequiresPermissions("sys:dept:list")
    @MyLog(title = "组织管理-部门管理",action = "查询所有部门数据接口")
    @GetMapping("/depts")
    public Response<List<DeptRespVo>> getAllDeptTree(){
        return deptService.selectAllTee();
    }

    @ApiOperation(value = "添加/编辑部门时选择框树形结构数据展示",notes = "部门树形结构列表接口")
    @RequiresPermissions(value = {"sys:user:update","sys:user:add","sys:dept:add","sys:dept:update"},logical = Logical.OR)
    @MyLog(title = "组织管理-部门管理",action = "部门树形结构数据接口")
    @GetMapping("/dept/tree")
    public Response<List<DeptRespNodeVo>> getDeptTree(@RequestParam(required = false) String deptId){
        return deptService.getDeptTree(deptId);
    }

    @ApiOperation(value = "新增部门",notes = "新增部门接口")
    @RequiresPermissions("sys:dept:add")
    @MyLog(title = "组织管理-部门管理",action = "新增部门数据接口")
    @PostMapping("/dept")
    public Response<String> addDept(@RequestBody @Valid DeptAddReqVo deptAddReqVo){
        return deptService.addDept(deptAddReqVo);
    }

    @ApiOperation(value = "编辑部门",notes = "编辑部门接口")
    @RequiresPermissions("sys:dept:update")
    @MyLog(title = "组织管理-部门管理",action = "更新部门数据接口")
    @PutMapping("/dept")
    public Response<String> updateDept(@RequestBody @Valid DeptUpdateReqVo deptUpdateReqVo){
        return deptService.updateDept(deptUpdateReqVo);
    }

    @ApiOperation(value = "删除部门",notes = "删除部门接口")
    @RequiresPermissions("sys:dept:delete")
    @MyLog(title = "组织管理-部门管理",action = "删除部门接口")
    @DeleteMapping("/dept/{id}")
    public Response<String> deletedDepts(@PathVariable("id")String deptId){
        return deptService.deletedDept(deptId);
    }
}
