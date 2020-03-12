package com.shiro.service;

import com.shiro.utils.Response;
import com.shiro.vo.req.DeptAddReqVo;
import com.shiro.vo.req.DeptUpdateReqVo;
import com.shiro.vo.resp.DeptRespNodeVo;
import com.shiro.vo.resp.DeptRespVo;

import java.util.List;

public interface DeptService {

    /**
     * 部门列表展示结构树
     * @return
     */
    Response<List<DeptRespVo>> selectAllTee();

    /**
     * 添加部门时选择框树形结构数据展示
     * @return
     */
    Response<List<DeptRespNodeVo>> getDeptTree(String deptId);

    /**
     * 新增部门
     * @param deptAddReqVo
     * @return
     */
    Response<String> addDept(DeptAddReqVo deptAddReqVo);

    /**
     * 编辑部门
     * @param deptUpdateReqVo
     * @return
     */
    Response<String> updateDept(DeptUpdateReqVo deptUpdateReqVo);

    /**
     * 删除部门
     * @param deptId
     * @return
     */
    Response<String> deletedDept(String deptId);
}
