package com.shiro.service.impl;

import com.shiro.constarts.Constant;
import com.shiro.dao.SysDeptDao;
import com.shiro.dao.SysUserDao;
import com.shiro.enums.ResponseCode;
import com.shiro.exception.BusinessException;
import com.shiro.pojo.SysDept;
import com.shiro.service.DeptService;
import com.shiro.service.RedisService;
import com.shiro.utils.CodeUtil;
import com.shiro.utils.IdWorker;
import com.shiro.utils.Response;
import com.shiro.vo.req.DeptAddReqVo;
import com.shiro.vo.req.DeptUpdateReqVo;
import com.shiro.vo.resp.DeptRespNodeVo;
import com.shiro.vo.resp.DeptRespVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class DeptServiceImpl implements DeptService {

    private static Logger logger = LoggerFactory.getLogger(DeptServiceImpl.class);

    @Autowired
    private SysDeptDao sysDeptDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserDao sysUserDao;

    /**
     * 部门列表结构树
     * @return
     */
    @Override
    public Response<List<DeptRespVo>> selectAllTee() {
        return Response.success(selectAll(sysDeptDao.selectAll(null)));
    }

    private List<DeptRespVo> selectAll(List<SysDept> list){
        List<DeptRespVo> deptRespVos = new ArrayList<>();
        for (SysDept sysDept : list){
            DeptRespVo deptRespVo = new DeptRespVo();
            deptRespVo.setId(sysDept.getId());
            deptRespVo.setDeptNo(sysDept.getDeptNo());
            deptRespVo.setName(sysDept.getName());
            deptRespVo.setRelationCode(sysDept.getRelationCode());
            deptRespVo.setManagerName(sysDept.getManagerName());
            deptRespVo.setPhone(sysDept.getPhone());
            deptRespVo.setStatus(sysDept.getStatus());
            deptRespVo.setCreateTime(sysDept.getCreateTime());
            deptRespVo.setUpdateTime(sysDept.getUpdateTime());
            deptRespVo.setPid(sysDept.getPid());
            if (StringUtils.isNotBlank(sysDept.getPid())){
                for (SysDept dept : list){
                    if (StringUtils.equals(dept.getId(),sysDept.getPid())){
                        deptRespVo.setPidName(dept.getName());
                        break;
                    }
                }
            }
            deptRespVos.add(deptRespVo);
        }
        return deptRespVos;
    }

    /**
     * 添加部门时选择框树形结构数据展示
     * @return
     */
    @Override
    public Response<List<DeptRespNodeVo>> getDeptTree(String deptId) {
        List<SysDept> sysDepts = sysDeptDao.selectAll(deptId);
        DeptRespNodeVo deptRespNodeVo = new DeptRespNodeVo();
        deptRespNodeVo.setId("0");
        deptRespNodeVo.setTitle("顶级部门");
        deptRespNodeVo.setChildren(getTree(sysDepts));
        List<DeptRespNodeVo> list = new ArrayList<>();
        list.add(deptRespNodeVo);
        return Response.success(list);
    }

    // =======  递归遍历组装 添加部门时选择框树形结构数据
    private List<DeptRespNodeVo> getTree(List<SysDept> sysDepts){
        // 顶级无父级 pid 为 0
        return _getTree(sysDepts,"0");
    }
    private List<DeptRespNodeVo> _getTree(List<SysDept> sysDepts,String pid){
        List<DeptRespNodeVo> list = new ArrayList<>();
        for (SysDept sysDept : sysDepts){
            if (StringUtils.equals(sysDept.getPid(),pid)){
                DeptRespNodeVo deptRespNodeVo = new DeptRespNodeVo();
                deptRespNodeVo.setId(sysDept.getId());
                deptRespNodeVo.setTitle(sysDept.getName());
                deptRespNodeVo.setChildren(_getTree(sysDepts,sysDept.getId()));
                list.add(deptRespNodeVo);
            }
        }
        return list;
    }

    /**
     * 新增部门
     * @param deptAddReqVo
     * @return
     */
    @Override
    public Response<String> addDept(DeptAddReqVo deptAddReqVo) {
        String elationCode = null;
        // 获取机构编码 YXD0001
        long deptCount = redisService.incrby(Constant.DEPT_CODE_KEY, 1);
        // 补位
        String deptCode = CodeUtil.deptCode(String.valueOf(deptCount), 7, "0");
        //
        if (deptAddReqVo.getPid().equals("0")){
            elationCode = deptCode;
        }else if (StringUtils.isNotBlank(deptAddReqVo.getPid())){
            SysDept parentDept = sysDeptDao.selectByPrimaryKey(deptAddReqVo.getPid());
            if (parentDept != null && parentDept.getPid().equals("0")){
                elationCode = parentDept.getRelationCode() + deptCode;
            }else {
                logger.info("父级数据不存在{}",deptAddReqVo.getPid());
                throw new BusinessException(ResponseCode.DATA_ERROR);
            }
        }
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(deptAddReqVo,sysDept);
        sysDept.setId(String.valueOf(idWorker.nextId()));
        sysDept.setDeptNo(deptCode);
        sysDept.setRelationCode(elationCode);
        sysDept.setCreateTime(new Date());
        int addCount = sysDeptDao.insertSelective(sysDept);
        if (addCount != 1){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return Response.success();
    }

    /**
     * 编辑部门
     * @param deptUpdateReqVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> updateDept(DeptUpdateReqVo deptUpdateReqVo) {
        SysDept sysDept = sysDeptDao.selectByPrimaryKey(deptUpdateReqVo.getId());
        if (sysDept == null){
            logger.error("传入 的 id:{} 不合法",deptUpdateReqVo.getId());
            throw new BusinessException(ResponseCode.DATA_ERROR);
        }
        SysDept updateSysDept = new SysDept();
        BeanUtils.copyProperties(deptUpdateReqVo,updateSysDept);
        updateSysDept.setUpdateTime(new Date());
        int updateCount = sysDeptDao.updateByPrimaryKeySelective(updateSysDept);
        if (updateCount != 1){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        // 处理层级编码关系
        if (!sysDept.getPid().equals(deptUpdateReqVo.getPid())){
            // 子集的部门层级关系编码 =父级部门层级关系编码 +它本身部门编码
            SysDept newParent = sysDeptDao.selectByPrimaryKey(deptUpdateReqVo.getPid());
            if (null == newParent && !"0".equals(deptUpdateReqVo.getPid())){
                logger.error("修改后的部门在数据库查找不到{}",deptUpdateReqVo.getPid());
                throw new BusinessException(ResponseCode.DATA_ERROR);
            }
            String oleRelation;// 原来的关系编码
            String newRelation;// 新的关系编码

            if ("0".equals(sysDept.getPid())){// 根目录挂靠到其它目录 ，如果原来的父id 为 0 顶级
                oleRelation = sysDept.getRelationCode();
                // 新的关系编码 = 新的父级关系编码 + 原来的部门编码
                newRelation = newParent.getRelationCode() + sysDept.getDeptNo();
            }else if ("0".equals(deptUpdateReqVo.getPid())){// 把原来部门挂载到到根目录
                oleRelation = sysDept.getRelationCode();
                newRelation = sysDept.getDeptNo();
            }else {
                SysDept oldParent = sysDeptDao.selectByPrimaryKey(sysDept.getPid());
                oleRelation = oldParent.getRelationCode();
                newRelation = newParent.getRelationCode();
            }
            // 更新层级关系编码
            sysDeptDao.updateRelationCode(oleRelation,newRelation,sysDept.getRelationCode());
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 删除部门
     * @param deptId
     * @return
     */
    @Override
    public Response<String> deletedDept(String deptId) {
        SysDept sysDept = sysDeptDao.selectByPrimaryKey(deptId);
        if (null == sysDept){
            logger.error("传入的部门id在数据库不存在{}",deptId);
            throw new BusinessException(ResponseCode.DATA_ERROR);
        }
        // 判断它和它子集的叶子节点是否关联有用户
        List<String> list = sysDeptDao.selectChildIds(sysDept.getRelationCode());
        int resultCount = sysUserDao.selectUserInfoByDeptIds(list);
        if (resultCount > 0){
            throw new BusinessException(ResponseCode.NOT_PERMISSION_DELETED_DEPT);
        }
        int deletedCount = sysDeptDao.deletedDepts(new Date(), list);
        if (deletedCount == 0){
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return Response.success(ResponseCode.SUCCESS.getMessage());
    }
}
