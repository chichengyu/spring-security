##############################################################
############################ RBAC ############################
##############################################################
### 用户表
drop table if exists sys_user;
create table sys_user(
	id int unsigned auto_increment primary key,
	username varchar(50) not null comment '账号',
	realname varchar(50) not null comment '真实姓名',
	password varchar(60) not null comment '密码',
	create_time datetime not null comment'创建时间',
	lastlogin_time datetime not null comment '最后登陆时间',
	enabled int(1) not null default 1 comment'是否启用',
	expired int(1) not null default 1 comment '是否过期',
	locked int(1) not null default 1 comment '是否锁定',
	cert_expried int(1) not null default 1 comment '证书是否过期', 
	key (username),
	key (realname)
)engine=innodb default charset=utf8;
insert into sys_user values(null,'admin','小二','123456','2018-05-06 17:17:17','2018-05-06 17:17:17',1,1,1,1),
	(null,'admin123','小三','123456','2020-05-06 17:17:17','2018-05-06 17:17:17',1,1,1,1);


### 角色表
drop table if exists sys_role;
create table sys_role(
	id int unsigned auto_increment primary key,
	role_name varchar(50) not null comment '角色名称',
	role_desc varchar(50) not null comment '角色说明',
	key (role_name)
)engine=innodb default charset=utf8;
insert into sys_role values(null,'普通用户','普通用户'),
	(null,'管理员','管理员');


### 用户角色表
drop table if exists sys_user_role;
create table sys_user_role(
	user_id int not null comment '用户id',
	role_id int not null comment '角色id',
	key (user_id),
	key (role_id)
)engine=innodb default charset=utf8;
insert into sys_user_role values(1,1),(2,2);


### 权限表
drop table if exists sys_permission;
create table sys_permission(
	id int unsigned auto_increment primary key,
	name varchar(50) not null comment '权限名称',
	url varchar(50) not null comment '路径',
	tag varchar(50) not null comment '权限标识符',
	key (name)
)engine=innodb default charset=utf8;
insert into sys_permission values(null,'产品查询','/product/list','ROLE_LIST_PRODUCT'),
	(null,'产品添加','/product/add','ROLE_ADD_PRODUCT'),
	(null,'产品修改','/product/update','ROLE_UPDATE_PRODUCT'),
	(null,'产品删除','/product/delete','ROLE_DELETE_PRODUCT');


### 角色权限表
drop table if exists sys_role_permission;
create table sys_role_permission(
	role_id int not null comment '角色id',
	permission_id int not null comment '权限id',
	key (role_id),
	key (permission_id)
)engine=innodb default charset=utf8;
insert into sys_role_permission values(1,1),(1,2),(2,3),(2,4);