package com.shiro.dao;

import com.shiro.pojo.SysDept;
import com.shiro.vo.req.DeptUpdateReqVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SysDeptDao {
    int insert(SysDept record);

    int insertSelective(SysDept record);

    /**
     * 查询部门所有
     * @return
     */
    List<SysDept> selectAll(@Param("deptId") String deptId);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysDept selectByPrimaryKey(String id);

    /**
     * 编辑部门
     * @param sysDept
     * @return
     */
    int updateByPrimaryKeySelective(SysDept sysDept);

    /**
     * 更新层级关系编码
     * @param oleRelation
     * @param newRelation
     * @param relationCode
     * @return
     */
    int updateRelationCode(@Param("oleStr") String oleRelation,@Param("newStr") String newRelation,@Param("relationCode") String relationCode);

    /**
     * 通过部门编码relationCode查询部门id 与 部门子级id
     * @param relationCode
     * @return
     */
    List<String> selectChildIds(String relationCode);

    /**
     * 通过部门id集合删除部门
     * @param updateTime
     * @param list
     * @return
     */
    int deletedDepts(@Param("updateTime") Date updateTime,@Param("list") List<String> list);
}