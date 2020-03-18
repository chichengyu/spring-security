/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : sys_shiro

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2020-03-18 15:11:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `dept_no` varchar(18) DEFAULT NULL COMMENT '部门编号',
  `name` varchar(300) DEFAULT NULL COMMENT '部门名称',
  `pid` varchar(64) NOT NULL COMMENT '父级id',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态(1:正常；0:弃用)',
  `relation_code` varchar(3000) DEFAULT NULL COMMENT '为了维护更深层级关系(规则：父级关系编码+自己的编码)',
  `dept_manager_id` varchar(64) DEFAULT NULL COMMENT '部门经理user_id',
  `manager_name` varchar(255) DEFAULT NULL COMMENT '部门经理名称',
  `phone` varchar(20) DEFAULT NULL COMMENT '部门经理联系电话',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1237322421447561210', 'YXD0000006', '迎学教育总公司', '0', '1', 'YXD0000006', null, '小霍', '13866666666', '2020-01-07 19:33:24', null, '1');
INSERT INTO `sys_dept` VALUES ('1237322421447561211', 'YXD0000008', '迎学教育广州分部', '1237322421447561210', '1', 'YXD0000006YXD0000008', null, '小刘', '13866666666', '2020-01-07 19:36:19', '2020-01-08 12:20:41', '1');
INSERT INTO `sys_dept` VALUES ('1237322421447561212', 'YXD0000007', '迎学教育深圳分部', '1237322421447561210', '1', 'YXD0000006YXD0000007', null, '小庄', '13866666667', '2020-01-07 19:34:49', '2020-01-08 12:04:42', '1');
INSERT INTO `sys_dept` VALUES ('1237322421447561213', 'YXD0000009', '广州分部白云区', '1237322421447561211', '1', 'YXD0000006YXD0000008YXD0000009', null, '小黄', '13899999999', '2020-01-08 11:28:17', '2020-03-12 12:59:11', '1');
INSERT INTO `sys_dept` VALUES ('1237322421447561216', 'YXD0000002', '测试添加部门', '0', '1', 'YXD0000002', null, '小池', '18888888888', '2020-03-10 10:20:28', null, '1');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `time` int(11) DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `code` varchar(64) DEFAULT NULL COMMENT '菜单权限编码',
  `name` varchar(300) DEFAULT NULL COMMENT '菜单权限名称',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(如：sys:user:add)',
  `url` varchar(100) DEFAULT NULL COMMENT '访问地址URL',
  `method` varchar(10) DEFAULT NULL COMMENT '资源请求类型',
  `pid` varchar(64) DEFAULT '0' COMMENT '父级菜单权限id',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `type` tinyint(4) DEFAULT '1' COMMENT '菜单权限类型(1:目录;2:菜单;3:按钮)',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态1:正常 0：禁用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表（菜单）';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1236916745927790556', 'btn-user-delete', '删除用户权限', 'sys:user:delete', '/api/user', 'DELETE', '1236916745927790575', '100', '3', '1', '2020-01-08 15:42:50', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790557', 'btn-log-delete', '删除日志权限', 'sys:log:delete', '/api/log', 'DELETE', '1236916745927790589', '100', '3', '1', '2020-01-08 16:12:53', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790558', '', '接口管理', '', '/swagger-ui.html', 'GET', '1236916745927790569', '97', '2', '1', '2020-01-08 14:28:56', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790559', 'btn-dept-delete', '删除部门权限', 'sys:dept:delete', '/api/dept/*', 'DELETE', '1236916745927790573', '100', '3', '1', '2020-01-08 15:45:52', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790560', '', '菜单权限管理', '', '/index/menus', 'GET', '1236916745927790564', '99', '2', '1', '2020-01-06 21:55:59', '2020-01-08 09:10:59', '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790561', 'btn-user-add', '新增用户权限', 'sys:user:add', '/api/user', 'POST', '1236916745927790575', '100', '3', '1', '2020-01-08 15:40:36', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790562', 'btn-role-update', '更新角色权限', 'sys:role:update', '/api/role', 'PUT', '1236916745927790578', '100', '3', '1', '2020-01-08 16:09:55', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790563', 'btn-permission-delete', '删除菜单权限', 'sys:permission:delete', '/api/permission', 'DELETE', '1236916745927790560', '100', '3', '1', '2020-01-08 15:48:37', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790564', '', '组织管理', '', '', '', '0', '100', '1', '1', '2020-01-06 21:53:55', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790565', 'btn-permission-list', '查询菜单权限列表权限', 'sys:permission:list', '/api/permissions', 'POST', '1236916745927790560', '100', '3', '1', '2020-01-08 15:46:36', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790566', 'btn-dept-list', '查询部门信息列表权限', 'sys:dept:list', '/api/depts', 'POST', '1236916745927790573', '100', '3', '1', '2020-01-08 15:43:36', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790567', '', '测试删除', '', '', '', '1236917038023315456', '100', '1', '1', '2020-01-08 09:30:53', '2020-01-08 09:31:01', '0');
INSERT INTO `sys_permission` VALUES ('1236916745927790568', 'btn-user-list', '查询用户信息列表权限', 'sys:user:list', '/api/users', 'POST', '1236916745927790575', '100', '3', '1', '2020-01-08 15:39:55', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790569', '', '系统管理', '', '', '', '0', '98', '1', '1', '2020-01-08 13:55:56', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790570', 'btn-role-delete', '删除角色权限', 'sys:role:delete', '/api/role/*', 'DELETE', '1236916745927790578', '100', '3', '1', '2020-01-08 16:11:22', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790571', '', 'SQL监控', '', '/druid/sql.html', 'GET', '1236916745927790569', '96', '2', '1', '2020-01-08 14:30:01', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790572', 'btn-role-add', '新增角色权限', 'sys:role:add', '/api/role', 'POST', '1236916745927790578', '100', '3', '1', '2020-01-08 15:50:00', '2020-03-12 05:15:46', '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790573', '', '部门管理', '', '/index/depts', '', '1236916745927790564', '97', '2', '1', '2020-01-07 18:28:31', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790574', 'btn-role-detail', '角色详情权限', 'sys:role:detail', '/api/role/*', 'GET', '1236916745927790578', '100', '3', '1', '2020-01-08 16:10:32', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790575', '', '用户管理', '', '/index/users', '', '1236916745927790564', '99', '2', '1', '2020-01-07 19:49:37', '2020-03-12 04:13:46', '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790576', 'btn-dept-update', '更新部门信息权限', 'sys:dept:update', '/api/dept', 'PUT', '1236916745927790573', '100', '3', '1', '2020-01-08 15:44:59', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790577', 'btn-permission-update', '更新菜单权限', 'sys:permission:update', '/api/permission', 'PUT', '1236916745927790560', '100', '3', '1', '2020-01-08 15:47:56', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790578', '', '角色管理', '', '/index/roles', '', '1236916745927790564', '100', '2', '1', '2020-01-06 22:33:55', '2020-01-08 09:13:30', '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790579', 'btn-user-update-role', '赋予用户角色权限', 'sys:user:role:update', '/api/user/roles', 'PUT', '1236916745927790575', '100', '3', '1', '2020-01-08 15:41:20', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790580', 'btn-user-update', '列表更新用户信息权限', 'sys:user:update', '/api/user', 'PUT', '1236916745927790575', '100', '3', '1', '2020-01-08 15:42:06', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790581', 'btn-role-add', '角色管理-新增角色', 'sys:role:add', '/api/role', 'POST', '1236916745927790578', '100', '3', '1', '2020-01-08 15:28:09', '2020-01-08 15:29:31', '0');
INSERT INTO `sys_permission` VALUES ('1236916745927790582', 'btn-permission-add', '新增菜单权限', 'sys:permission:add', '/api/permission', 'POST', '1236916745927790560', '100', '3', '1', '2020-01-08 15:47:16', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790583', 'btn-role-list', '查询角色列表权限', 'sys:role:list', '/api/roles', 'POST', '1236916745927790578', '100', '3', '1', '2020-01-08 15:49:20', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790589', '', '日志管理', '', '/index/logs', '', '1236916745927790569', '100', '2', '1', '2020-01-08 13:57:12', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790590', 'btn-dept-add', '新增部门权限', 'sys:dept:add', '/api/dept', 'POST', '1236916745927790573', '100', '3', '1', '2020-01-08 15:44:18', null, '1');
INSERT INTO `sys_permission` VALUES ('1236916745927790591', 'btn-log-list', '查询日志列表权限', 'sys:log:list', '/api/logs', 'POST', '1236916745927790589', '100', '3', '1', '2020-01-08 16:12:14', null, '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(300) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '状态(1:正常0:弃用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1237258113002901504', '小池测试新增角色', '', '1', '2020-03-10 06:04:56', '2020-03-17 18:39:01', '1');
INSERT INTO `sys_role` VALUES ('1237258113002901505', '测试删除角色', '测试删除角色', '1', '2020-01-08 10:49:49', '2020-01-08 10:53:00', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901506', '测试角色删除', '测试角色删除', '1', '2020-01-08 10:46:10', '2020-01-08 10:46:15', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901507', '标记用户角色测试', '标记用户角色测试', '1', '2020-01-08 10:48:13', '2020-01-08 10:49:46', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901508', '测试角色', '测试角色', '1', '2020-01-08 10:45:31', '2020-01-08 10:47:28', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901509', '角色回显测试5', '角色回显测试5', '1', '2020-01-08 10:03:23', '2020-01-08 12:00:46', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901510', '测试是否会自定刷新token角色', '测试是否会自定刷新token角色', '1', '2020-01-08 10:46:55', '2020-01-08 10:52:13', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901511', '测试角色删除功能', '测试角色删除功能', '1', '2020-01-08 10:53:38', '2020-01-08 11:02:40', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901512', '超级管理员', '我是超级管理员', '1', '2020-01-06 23:37:45', '2020-03-18 11:07:12', '1');
INSERT INTO `sys_role` VALUES ('1237258113002901513', '标记用户角色测试', '标记用户角色测试', '1', '2020-01-08 10:53:35', '2020-01-08 11:06:36', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901514', '测试删除角色', '432432', '1', '2020-01-08 10:54:24', '2020-01-08 10:45:12', '0');
INSERT INTO `sys_role` VALUES ('1237258113002901515', 'test', '我是test', '1', '2020-01-07 21:19:04', '2020-03-18 10:27:00', '1');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(64) DEFAULT NULL COMMENT '菜单权限id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1239863802590269440', '1237258113002901504', '1236916745927790564', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269441', '1237258113002901504', '1236916745927790573', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269442', '1237258113002901504', '1236916745927790559', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269443', '1237258113002901504', '1236916745927790566', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269444', '1237258113002901504', '1236916745927790576', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269445', '1237258113002901504', '1236916745927790590', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269446', '1237258113002901504', '1236916745927790569', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269447', '1237258113002901504', '1236916745927790589', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269448', '1237258113002901504', '1236916745927790557', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1239863802590269449', '1237258113002901504', '1236916745927790591', '2020-03-17 18:39:01');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858368', '1237258113002901515', '1236916745927790564', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858369', '1237258113002901515', '1236916745927790560', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858370', '1237258113002901515', '1236916745927790563', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858371', '1237258113002901515', '1236916745927790565', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858372', '1237258113002901515', '1236916745927790577', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858373', '1237258113002901515', '1236916745927790582', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858374', '1237258113002901515', '1236916745927790575', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858375', '1237258113002901515', '1236916745927790561', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858376', '1237258113002901515', '1236916745927790568', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858377', '1237258113002901515', '1236916745927790580', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858378', '1237258113002901515', '1236916745927790573', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858379', '1237258113002901515', '1236916745927790566', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858380', '1237258113002901515', '1236916745927790576', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858381', '1237258113002901515', '1236916745927790590', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858382', '1237258113002901515', '1236916745927790569', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858383', '1237258113002901515', '1236916745927790589', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858384', '1237258113002901515', '1236916745927790557', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240102373372858385', '1237258113002901515', '1236916745927790591', '2020-03-18 10:27:00');
INSERT INTO `sys_role_permission` VALUES ('1240112488297664512', '1237258113002901512', '1236916745927790564', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858816', '1237258113002901512', '1236916745927790578', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858817', '1237258113002901512', '1236916745927790562', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858818', '1237258113002901512', '1236916745927790570', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858819', '1237258113002901512', '1236916745927790572', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858820', '1237258113002901512', '1236916745927790574', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858821', '1237258113002901512', '1236916745927790583', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858822', '1237258113002901512', '1236916745927790560', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858823', '1237258113002901512', '1236916745927790563', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858824', '1237258113002901512', '1236916745927790565', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858825', '1237258113002901512', '1236916745927790577', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858826', '1237258113002901512', '1236916745927790582', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858827', '1237258113002901512', '1236916745927790575', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858828', '1237258113002901512', '1236916745927790556', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858829', '1237258113002901512', '1236916745927790561', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858830', '1237258113002901512', '1236916745927790568', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858831', '1237258113002901512', '1236916745927790579', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858832', '1237258113002901512', '1236916745927790580', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858833', '1237258113002901512', '1236916745927790573', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858834', '1237258113002901512', '1236916745927790559', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858835', '1237258113002901512', '1236916745927790566', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858836', '1237258113002901512', '1236916745927790576', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858837', '1237258113002901512', '1236916745927790590', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858838', '1237258113002901512', '1236916745927790569', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858839', '1237258113002901512', '1236916745927790589', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858840', '1237258113002901512', '1236916745927790557', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858841', '1237258113002901512', '1236916745927790591', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858842', '1237258113002901512', '1236916745927790558', '2020-03-18 11:07:12');
INSERT INTO `sys_role_permission` VALUES ('1240112488301858843', '1237258113002901512', '1236916745927790571', '2020-03-18 11:07:12');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '用户id',
  `username` varchar(50) NOT NULL COMMENT '账户名称',
  `salt` varchar(32) DEFAULT NULL COMMENT '加密盐值',
  `password` varchar(200) NOT NULL COMMENT '用户密码密文',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `dept_id` varchar(64) DEFAULT NULL COMMENT '部门id',
  `real_name` varchar(60) DEFAULT NULL COMMENT '真实名称',
  `nick_name` varchar(60) DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱(唯一)',
  `status` tinyint(4) DEFAULT '1' COMMENT '账户状态(1.正常 2.锁定 )',
  `sex` tinyint(4) DEFAULT '1' COMMENT '性别(1.男 2.女)',
  `deleted` tinyint(4) DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  `create_id` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_id` varchar(64) DEFAULT NULL COMMENT '更新人',
  `create_where` tinyint(4) DEFAULT '1' COMMENT '创建来源(1.web 2.android 3.ios )',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1237361915165020161', 'admin', 'c1f4a8a78e7d4186ac0b', '$2a$10$0hOixfSTyxFS1F/P/R1HsOCrBt51GQhC.rB/qKb9fa0Sy3/BMEQcy', '13888888888', '1237322421447561216', '小池', '小霍', '875267425@qq.com', '1', '1', '1', null, '1237361915165020161', '3', '2019-09-22 19:38:05', '2020-03-17 13:40:35');
INSERT INTO `sys_user` VALUES ('1237365636208922624', 'test', '$2a$10$sgbCP0oSJV2DVRgdAWJQe.', '$2a$10$sgbCP0oSJV2DVRgdAWJQe.q7Lz6IGZ9VS0u0ULNq3nM7jC.xPwaN6', '16666666666', '0', null, null, null, '1', '1', '1', null, '1237361915165020161', '1', '2020-03-10 13:12:12', '2020-03-12 14:44:41');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1239912670904324096', '1237361915165020161', '1237258113002901512', '2020-03-17 21:53:12');
INSERT INTO `sys_user_role` VALUES ('1240096546897399808', '1237365636208922624', '1237258113002901515', '2020-03-18 10:03:51');
