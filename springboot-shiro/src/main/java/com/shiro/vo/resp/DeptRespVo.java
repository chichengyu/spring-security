package com.shiro.vo.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeptRespVo {
    @ApiModelProperty(value = "部门id")
    private String id;

    @ApiModelProperty(value = "部门编号")
    private String deptNo;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门父级id")
    private String pid;

    @ApiModelProperty(value = "部门父级名称")
    private String pidName;

    @ApiModelProperty(value = "部门状态:1:正常；0:弃用")
    private Integer status;

    @ApiModelProperty(value = "为了维护更深层级关系(规则：父级关系编码+自己的编码)")
    private String relationCode;

    @ApiModelProperty(value = "部门经理名称")
    private String managerName;

    @ApiModelProperty(value = "部门经理联系电话")
    private String phone;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
