#!/usr/bin/env bash
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER gitlab superuser password 'gitlab';
    CREATE DATABASE gitlabhq_production;
    GRANT ALL PRIVILEGES ON DATABASE gitlabhq_production TO gitlab;


create table sys_user
(
	id varchar(36) not null
		constraint sys_user_pkey
			primary key,
	state integer default 1 not null,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	name varchar(20),
	number varchar(20) not null,
	mobile varchar(11),
	password varchar(64),
	gender integer default 1 not null,
	address varchar(100),
	wechat_open_id varchar(50),
	referee_id varchar(36)
)
;

comment on table sys_user is '用户'
;

comment on column sys_user.state is '状态'
;

comment on column sys_user.create_time is '创建时间'
;

comment on column sys_user.name is '姓名'
;

comment on column sys_user.number is '编号'
;

comment on column sys_user.mobile is '手机号'
;

comment on column sys_user.password is '密码'
;

comment on column sys_user.wechat_open_id is '微信号'
;

comment on column sys_user.referee_id is '推荐人id'
;

comment on column sys_user.gender is '性别'
;

comment on column sys_user.address is '地址'
;

alter table sys_user owner to ${POSTGRES_USER}
;

create unique index sys_user_id_uindex
	on sys_user (id)
;

create unique index sys_user_number_uindex
	on sys_user (number)
;

create unique index sys_user_mobile_uindex
	on sys_user (mobile)
;

create table sys_employee
(
	id varchar(36) not null
		constraint sys_employee_pkey
			primary key,
	state integer default 1,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	mobile varchar(11) not null,
	account varchar(50) not null,
	name varchar(20) not null,
	password varchar(40) not null,
	number varchar(10),
	type integer,
	avatar varchar(200)
)
;

comment on table sys_employee is '雇员'
;

comment on column sys_employee.state is '状态'
;

alter table sys_employee owner to yijiabangtest
;

create unique index sys_employee_account_uindex
	on sys_employee (account)
;

create unique index sys_employee_mobile_index
	on sys_employee (mobile)
;

create unique index sys_employee_number_uindex
	on sys_employee (number)
;

create table maintain_user_address
(
	id varchar(36) not null
		constraint maintain_user_address_pkey
			primary key,
	state integer default 1,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	user_id varchar(36) not null,
	province varchar(50),
	city varchar(50),
	district varchar(50),
	address varchar(100),
	location point,
	name varchar(30) not null,
	gender integer default 1 not null,
	mobile varchar(11) not null,
	type integer not null,
	start_date date,
	end_date date
)
;

comment on column maintain_user_address.location is '地址坐标'
;

comment on column maintain_user_address.name is '联系人姓名'
;

comment on column maintain_user_address.mobile is '联系人电话'
;

comment on column maintain_user_address.type is '地址类型：（商户，个人）'
;

alter table maintain_user_address owner to ${POSTGRES_USER}
;

create table sys_order
(
	id varchar(36) not null
		constraint sys_order_pkey
			primary key,
	state integer default 1 not null,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	user_id varchar(36) not null,
	order_type integer not null,
	pay_type integer,
	trade_no varchar(60),
	total_amount bigint not null,
	order_body varchar(128),
	out_trade_no varchar(50),
	extra_info json,
	pay_state integer default 0 not null,
	start_time timestamp not null,
	expire_time timestamp not null,
	notify_time timestamp,
	detail varchar(100),
	title varchar(50)
)
;

comment on table sys_order is '订单'
;

comment on column sys_order.pay_type is '支付类型： 微信、支付宝等'
;

comment on column sys_order.total_amount is '订单总金额'
;

comment on column sys_order.out_trade_no is '商户订单号'
;

alter table sys_order owner to ${POSTGRES_USER}
;

create unique index sys_order_out_trade_number_index
	on sys_order (out_trade_no)
;

create unique index sys_order_trade_no_uindex
	on sys_order (trade_no)
;

create table maintain_service_package
(
	id varchar(36) not null
		constraint maintain_service_package_pkey
			primary key,
	state integer default 1 not null,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	name varchar(50),
	detail varchar(200),
	price bigint not null,
	year bigint default 0 not null,
	month bigint default 0 not null,
	week bigint default 0 not null,
	day bigint default 0 not null
)
;

alter table maintain_service_package owner to ${POSTGRES_USER}
;

create table maintain_service_item_category
(
	id varchar(36) not null
		constraint maintain_service_item_category_pkey
			primary key,
	state integer default 1 not null,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	parent_id varchar(36),
	name varchar(50) not null,
	icon varchar(200)
)
;

comment on table maintain_service_item_category is '服务项目分类'
;

comment on column maintain_service_item_category.icon is '图标'
;

alter table maintain_service_item_category owner to ${POSTGRES_USER}
;


create table maintain_service_item
(
	id varchar(36) not null
		constraint maintain_service_item_pkey
			primary key,
	state integer default 1 not null,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	category_id varchar(36) not null,
	name varchar(60) not null,
	price bigint not null,
	detail varchar(200),
	img varchar(200)
)
;

comment on column maintain_service_item.price is '价格'
;

comment on column maintain_service_item.detail is '服务项目描述'
;

comment on column maintain_service_item.img is '图片地址'
;

alter table maintain_service_item owner to ${POSTGRES_USER}
;

create table maintain_service_ticket
(
	id varchar(36),
	state integer default 1,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	user_id varchar(36) not null,
	detail varchar(200),
	img_list text[],
	record_list text[],
	phase integer default 0 not null,
	address varchar(100),
	item_list json,
	location point,
	name varchar(50),
	mobile varchar(20),
	address_type integer default 1,
	address_in_service boolean default false
)
;

comment on table maintain_service_ticket is '工单'
;

comment on column maintain_service_ticket.img_list is '图片列表'
;

comment on column maintain_service_ticket.record_list is '录音列表'
;

comment on column maintain_service_ticket.phase is '服务状态'
;

comment on column maintain_service_ticket.address is '具体地址'
;

comment on column maintain_service_ticket.item_list is '服务项目集合,id为键，数量为value'
;

comment on column maintain_service_ticket.location is '地址坐标'
;

comment on column maintain_service_ticket.name is '客户名字'
;

comment on column maintain_service_ticket.address_in_service is '地址是否已购买'
;

comment on column maintain_service_ticket.mobile is '电话号码'
;

alter table maintain_service_ticket owner to ${POSTGRES_USER}
;

create table sys_role
(
	id varchar(36) not null
		constraint sys_role_pkey
			primary key,
	state integer default 1 not null,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	app_id varchar(36) not null,
	name varchar(50),
	code varchar(50),
	detail varchar(200)
)
;

comment on table sys_role is '角色'
;

comment on column sys_role.app_id is '应用ID'
;

comment on column sys_role.name is '角色名'
;

comment on column sys_role.code is '角色编号'
;

comment on column sys_role.detail is '角色描述'
;

alter table sys_role owner to ${POSTGRES_USER}
;

create table sys_app
(
	id varchar(36) not null
		constraint sys_app_pkey
			primary key,
	state integer default 1 not null,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	name varchar(50) not null,
	code varchar(50),
	detail varchar(200)
)
;

comment on column sys_app.code is '应用代码'
;

comment on column sys_app.detail is '应用描述'
;

alter table sys_app owner to ${POSTGRES_USER}
;

create unique index sys_app_code_uindex
	on sys_app (code)
;

create unique index sys_app_name_uindex
	on sys_app (name)
;

create table sys_module
(
	id varchar(36) not null
		constraint sys_module_pkey
			primary key,
	state integer default 1 not null,
	create_time timestamp not null,
	create_user varchar(50),
	update_time timestamp,
	update_user varchar(50),
	parent_id varchar(36),
	app_id varchar(36) not null,
	name varchar(50) not null,
	code varchar(50),
	type integer not null,
    url varchar(200),
	detail varchar(200)
)
;

comment on table sys_module is '功能模块表'
;

comment on column sys_module.app_id is '应用ID'
;

comment on column sys_module.name is '功能模块名'
;

comment on column sys_module.code is '功能模块代码'
;

comment on column sys_module.type is '功能模块类型'
;

comment on column sys_module.url is 'url路径'
;

comment on column sys_module.detail is '模块描述'
;

alter table sys_module owner to ${POSTGRES_USER}
;

create table sys_employee_role
(
	employee_id varchar(36) not null,
	role_id varchar(36) not null,
	constraint sys_employee_role_pk
		primary key (employee_id, role_id)
)
;

alter table sys_employee_role owner to ${POSTGRES_USER}
;

create table sys_role_module
(
	role_id varchar(36) not null,
	module_id varchar(36) not null,
	constraint sys_role_module_pk
		primary key (role_id, module_id)
)
;

alter table sys_role_module owner to ${POSTGRES_USER}
;

create table maintain_service_attendance
(
	id varchar(36) not null
		constraint maintain_service_attendance_record_pkey
			primary key,
	state integer default 1,
	create_time timestamp not null,
	create_user varchar(36),
	update_time timestamp,
	update_user varchar(36),
	service_ticket_id varchar(36) not null,
	employee_id varchar(36),
	employee_name varchar(20),
	employee_mobile varchar(20),
	accepted_time timestamp,
	finished_time timestamp,
	arrival_time timestamp,
	estimated_arrival_time timestamp,
	estimated_finished_time timestamp,
	phase integer default 0 not null,
	remark varchar(100)
)
;

comment on table maintain_service_attendance is '师傅出勤记录'
;

comment on column maintain_service_attendance.accepted_time is '接单时间'
;

comment on column maintain_service_attendance.estimated_arrival_time is '预计到达时间'
;

comment on column maintain_service_attendance.arrival_time is '到达时间'
;

comment on column maintain_service_attendance.estimated_finished_time is '预计完成时间'
;

comment on column maintain_service_attendance.finished_time is '完成时间'
;

comment on column maintain_service_attendance.phase is '记录状态'
;

comment on column maintain_service_attendance.remark is '备注'
;

alter table maintain_service_attendance owner to ${POSTGRES_USER}
;

create table maintain_service_comment
(
	id varchar(36) not null
		constraint maintain_service_attendance_comment_pkey
			primary key,
	state integer default 1,
	create_time timestamp not null,
	create_user varchar(36),
	update_time timestamp,
	update_user varchar(36),
	user_id varchar(36),
	service_ticket_id varchar(36),
	employee_id varchar(36),
	employee_name varchar(20),
	score integer default 100 not null,
	content varchar(200)
)
;

alter table maintain_service_comment owner to ${POSTGRES_USER}
;

INSERT INTO sys_employee (id, state, create_time, account, password, type, mobile, name) VALUES ('BFAA9DEAD2E9478B8B905A23F78A3680', 1, '2018-10-14 18:06:30.307000',  '13984745475', 'E10ADC3949BA59ABBE56E057F20F883E', 3, '13984745475', '邓生德');
INSERT INTO sys_employee (id, state, create_time, account, password, type, mobile, name) VALUES ('8F879BCD12574827BCE2D28F5ADB6F9A', 1, '2018-10-14 18:06:30.307000',  '15885519010', 'E10ADC3949BA59ABBE56E057F20F883E', 3, '15885519010', '罗镇祥');
INSERT INTO sys_employee (id, state, create_time, account, password, type, mobile, name) VALUES ('E2CC8ACF370945EF9A455268257BD3CD', 1, '2018-10-14 18:06:30.307000',  '18212025614', 'E10ADC3949BA59ABBE56E057F20F883E', 3, '18212025614', '李俊');
INSERT INTO sys_employee (id, state, create_time, account, password, type, mobile, name) VALUES ('406EA0758F324097AEB0D119F8E18D0A', 1, '2018-10-14 18:06:30.307000',  '13765091638', 'E10ADC3949BA59ABBE56E057F20F883E', 3, '13765091638', '罗阳');
INSERT INTO sys_app (id, state, create_time, create_user, update_time, update_user, name, code, detail) VALUES ('YIJIABANG_MAINTAIN_ADMIN', 1, '2018-10-23 05:45:41.000000', 'admin', null, null, '易家帮后台', 'YIJIABANG_MAINTAIN_ADMIN', null);

INSERT INTO sys_module (id,state,create_time,create_user,update_time,update_user,parent_id,app_id,name,code,type,url,detail)
VALUES ('4302ed77cc074b549b91cfab65809965',0,'2018-10-24 15:30:26.11',null,null,null,'f056bec2560b4c9b9cd2812a4a5f9e6b','YIJIABANG_MAINTAIN_ADMIN','模块管理','ModuleList',1,'/system/module/list',null),
       ('fbaf3b6293c8472f9f9293859301e480',1,'2018-10-25 21:00:10.626',null,null,null,'a7b97b81b64b47c99e8e8efb430c80db','YIJIABANG_MAINTAIN_ADMIN','新增员工','UserCreate',2,null,null),
       ('55c2f0f505b343379bce074fa4084772',1,'2018-10-26 10:39:01.273',null,null,null,'a7b97b81b64b47c99e8e8efb430c80db','YIJIABANG_MAINTAIN_ADMIN','修改员工','UserUpdate',2,null,null),
       ('93260381901b4b06ac062ad9e8c5bd38',1,'2018-10-26 10:41:14.103',null,null,null,'a1f779fa57654cc697b3521da2deff57','YIJIABANG_MAINTAIN_ADMIN','修改角色','RoleUpdate',2,null,null),
       ('25951dd728e44d6f9669546a9672ce97',1,'2018-10-26 10:41:34.176',null,null,null,'a1f779fa57654cc697b3521da2deff57','YIJIABANG_MAINTAIN_ADMIN','删除角色','RoleRemove',2,null,null),
       ('b92da98d4ded4f40a4ad8b9c67154d7b',0,'2018-10-24 15:28:41.502',null,null,null,null,'YIJIABANG_MAINTAIN_ADMIN','员工管理','EmployeeList',1,'/system/employee/list',null),
       ('97faa24acf724655b72d7ec36be53738',1,'2018-11-11 10:25:33.42',null,null,null,null,'YIJIABANG_MAINTAIN_ADMIN','服务项管理','Service',1,'/service',null),
       ('1715f2f9604b4687b854383e82125288',1,'2018-11-11 10:26:00.366',null,null,null,'97faa24acf724655b72d7ec36be53738','YIJIABANG_MAINTAIN_ADMIN','服务项','ServiceItemList',1,'/service/service-item/list',null),
       ('6741234a58764b8aa8ed2873a16074eb',1,'2018-11-11 10:59:15.446',null,null,null,'1715f2f9604b4687b854383e82125288','YIJIABANG_MAINTAIN_ADMIN','服务项更新','ServiceItemUpdate',2,null,null),
       ('1e5e1de6a9c44a99b22ff11f2983eff9',1,'2018-11-12 12:00:08.422',null,null,null,'c324eb6586eb426c843bafc2e4b5ed2a','YIJIABANG_MAINTAIN_ADMIN','新增会员包','ServicePackageCreate',2,null,null),
       ('f056bec2560b4c9b9cd2812a4a5f9e6b',1,'2018-10-24 15:29:23.513',null,null,null,null,'YIJIABANG_MAINTAIN_ADMIN','系统管理','System',1,'/system',null),
       ('a7b97b81b64b47c99e8e8efb430c80db',1,'2018-10-24 15:29:44.72',null,null,null,'f056bec2560b4c9b9cd2812a4a5f9e6b','YIJIABANG_MAINTAIN_ADMIN','员工管理','EmployeeList',1,'/system/employee/list',null),
       ('127eade8913f4f59acc9a4490f832f00',1,'2018-11-12 11:53:06.903',null,'2018-11-12 15:44:42.534',null,'19c3de8a421a486eba8a40f63f85fefd','YIJIABANG_MAINTAIN_ADMIN','用户详情','UserDetail',2,'/user/:id/detail',null),
       ('fd0952f862e24dabb84d79994bda1a7b',1,'2018-11-13 10:52:44.099',null,null,null,null,'YIJIABANG_MAINTAIN_ADMIN','工程师管理','Engineer',1,'/engineer',null),
       ('864ce83e88c54cd1ac363c8d0374d20f',1,'2018-11-13 11:21:25.636',null,null,null,null,'YIJIABANG_MAINTAIN_ADMIN','报修管理','ServiceTicket',1,'/service-ticket',null),
       ('a2c32550cb744a78bc791eac4f4cd987',1,'2018-11-13 11:24:46.101',null,null,null,'864ce83e88c54cd1ac363c8d0374d20f','YIJIABANG_MAINTAIN_ADMIN','报修单列表','ServiceTicketList',1,'/service-ticket/list',null),
       ('c324eb6586eb426c843bafc2e4b5ed2a',1,'2018-11-12 11:59:32.379',null,'2018-11-13 11:27:54.049',null,'003b943b21d04f5d897b3ba1e76423c9','YIJIABANG_MAINTAIN_ADMIN','会员包','ServicePackageList',1,'/service-package/list',null),
       ('003b943b21d04f5d897b3ba1e76423c9',1,'2018-11-12 11:59:16.873',null,'2018-11-13 11:28:01.79',null,null,'YIJIABANG_MAINTAIN_ADMIN','会员包管理','ServicePackage',1,'/service-package',null),
       ('477ece3fcc8842c897f096ff1c3e6f52',1,'2018-10-24 19:40:22.28',null,null,null,null,'YIJIABANG_MAINTAIN_ADMIN','用户管理','User',1,'/user',null),
       ('19c3de8a421a486eba8a40f63f85fefd',1,'2018-10-24 19:40:40.45',null,null,null,'477ece3fcc8842c897f096ff1c3e6f52','YIJIABANG_MAINTAIN_ADMIN','用户列表','UserList',1,'/user/list',null),
       ('9a3c4ca8f23947bea64a071db6af55e1',1,'2018-10-26 10:39:46.963',null,null,null,'a7b97b81b64b47c99e8e8efb430c80db','YIJIABANG_MAINTAIN_ADMIN','删除员工','UserRemove',2,null,null),
       ('a1f779fa57654cc697b3521da2deff57',1,'2018-10-24 15:30:01.864',null,null,null,'f056bec2560b4c9b9cd2812a4a5f9e6b','YIJIABANG_MAINTAIN_ADMIN','角色管理','RoleList',1,'/system/role/list',null),
       ('7839592afea14d669978e89fd543f914',1,'2018-10-26 10:40:20.866',null,null,null,'a1f779fa57654cc697b3521da2deff57','YIJIABANG_MAINTAIN_ADMIN','新增角色','RoleCreate',2,null,null),
       ('c78d17f6dce84144807e96043979fd00',1,'2018-11-11 10:55:40.75',null,null,null,'1715f2f9604b4687b854383e82125288','YIJIABANG_MAINTAIN_ADMIN','服务项新增','ServiceItemCreate',2,null,null),
       ('93ea5f05d4c94545be91954ce7c54d42',1,'2018-11-11 11:55:58.097',null,null,null,'97faa24acf724655b72d7ec36be53738','YIJIABANG_MAINTAIN_ADMIN','服务项分类','ServiceCategoryList',1,'/service/service-category/list',null),
       ('4c61382447214b0088dea6f16626a297',1,'2018-11-12 11:54:05.286',null,null,null,'127eade8913f4f59acc9a4490f832f00','YIJIABANG_MAINTAIN_ADMIN','新增地址','UserAddressCreate',2,null,null),
       ('4b1e703ab04e46ff820bcc1dcc2d7434',1,'2018-11-12 11:54:58.149',null,null,null,'127eade8913f4f59acc9a4490f832f00','YIJIABANG_MAINTAIN_ADMIN','修改地址','UserAddressUpdate',2,null,null),
       ('59f1d3325428466f91f8c3cf80ea5f0f',1,'2018-11-12 11:55:15.964',null,null,null,'127eade8913f4f59acc9a4490f832f00','YIJIABANG_MAINTAIN_ADMIN','派单','ServiceTicketDispatch',2,null,null),
       ('480d35922ca84593a564be501d348cff',1,'2018-11-12 12:00:34.748',null,null,null,'c324eb6586eb426c843bafc2e4b5ed2a','YIJIABANG_MAINTAIN_ADMIN','修改会员包','ServicePackageUpdate',2,null,null),
       ('ca777d46693e432abd156520046b1e65',1,'2018-11-13 10:57:12.644',null,null,null,'fd0952f862e24dabb84d79994bda1a7b','YIJIABANG_MAINTAIN_ADMIN','工程师状态','EngineerState',1,'/engineer/state',null),
       ('54143f908eaf4aab95712d4e5e087c79',1,'2018-11-13 21:40:09.811',null,null,null,'1715f2f9604b4687b854383e82125288','YIJIABANG_MAINTAIN_ADMIN','服务项删除','ServiceItemRemove',2,null,null),
       ('72f0402df4004f2dab044edcee00a6f2',1,'2018-11-13 22:02:42.901',null,null,null,'93ea5f05d4c94545be91954ce7c54d42','YIJIABANG_MAINTAIN_ADMIN','服务项分类新增','ServiceItemCategoryCreate',2,null,null),
       ('53d934945e484335939a161e936e562f',1,'2018-11-13 22:02:58.51',null,null,null,'93ea5f05d4c94545be91954ce7c54d42','YIJIABANG_MAINTAIN_ADMIN','服务项分类修改','ServiceItemCategoryUpdate',2,null,null),
       ('69819be88c084cdaab6d4a4820b4023d',1,'2018-11-17 18:25:09.074',null,null,null,'dc4f0a1c74ce4b85963d75bfe4dc8b89','YIJIABANG_MAINTAIN_ADMIN','App版本新增','AppVersionCreate',2,null,null),
       ('dc4f0a1c74ce4b85963d75bfe4dc8b89',1,'2018-11-17 18:24:17.933',null,'2018-11-17 18:28:04.659',null,'f056bec2560b4c9b9cd2812a4a5f9e6b','YIJIABANG_MAINTAIN_ADMIN','App版本管理','AppVersionList',1,'/system/app-version/list',null),
       ('12a0fde6197344f498851e967bd6f766',1,'2018-11-17 18:28:26.223',null,null,null,'dc4f0a1c74ce4b85963d75bfe4dc8b89','YIJIABANG_MAINTAIN_ADMIN','App版本修改','AppVersionUpdate',2,null,null),
       ('83e9bf2fb1eb47649117d0b9acfb8ffd',1,'2018-11-19 13:05:08.739',null,null,null,'a2c32550cb744a78bc791eac4f4cd987','YIJIABANG_MAINTAIN_ADMIN','手动派单','ServiceTicketDispatch',2,null,null);


INSERT INTO maintain_service_item_category (id, state, create_time, create_user, update_time, update_user, parent_id, name, icon)
VALUES
        ('9582588fe5ec42ffb0c968691c6326ef', 1, '2018-10-22 11:49:05.290000', null, null, null, null, '厨卫', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/869CE3F2-4D47-4EAC-B848-505B37E07817.png'),
        ('a81ccfe89c0042fb815f730ef02cd1c9', 1, '2018-10-22 11:49:18.247000', null, null, null, null, '打孔疏通', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/3D4FDFD7-7702-4124-BB5D-2F9359958042.png'),
        ('f4009c594e414a0898ada71b6c1aa016', 1, '2018-10-22 11:49:31.648000', null, null, null, null, '灯具', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/86E105F5-CDDA-4300-B3EA-3E57D4B42DB1.png'),
        ('958706d8feeb4661ab6b77c17347e041', 1, '2018-10-22 11:49:55.729000', null, null, null, null, '阀门龙头', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/6A7DFBD1-0C29-4CC3-BDF9-77F333C9D969.png'),
        ('e4c4658e46f34d71aad508cf356a476d', 1, '2018-10-22 11:50:06.020000', null, null, null, null, '挂件安装', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/31131572-AA01-46A4-9D53-6DFAA32683DE.png'),
        ('fe58cd92f4bc4ba589b624e2d0e6d5ed', 1, '2018-10-22 11:50:23.246000', null, null, null, null, '开关电路', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/8A4B916E-91AC-48E7-9C61-2DE8927E5971.png'),
        ('c7fe1ad961c14672a5f20238ba8b1b45', 1, '2018-10-22 11:50:37.266000', null, null, null, null, '开锁换锁', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/A31C83FF-E21A-49AB-8784-B3D807E875B2.png'),
        ('cc9d4b415b714c649e0b9350a6796d8c', 1, '2018-10-22 11:50:50.565000', null, null, null, null, '门窗', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/FAF0C7EA-8A88-4684-A37A-A397A3A6C824.png'),
        ('41f2abfb147843a68ac77bd49195c558', 1, '2018-10-22 11:51:01.617000', null, null, null, null, '墙面地面', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/87E5ADD5-56E6-4770-BE94-8ABB6A795B09.png'),
        ('47653322ea664934b61adacb7f73f8d7', 1, '2018-10-22 11:51:14.000000', null, '2018-11-11 15:16:16.707000', null, null, '家具', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-category-icon/2693C3D1-E011-42CE-A50C-19C565690625.png');

INSERT INTO maintain_service_item (id, state, create_time, create_user, update_time, update_user, category_id, name, price, detail, img)
VALUES
 ('0bde9d943146452d8e57b91d74c1e5a3', 1, '2018-11-11 13:53:32.454000', null, '2018-11-13 12:26:01.224000', null, 'e4c4658e46f34d71aad508cf356a476d', '安装挂钟', 1, '将时钟固定在合理位置。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/881090B1-94A7-4224-AFF9-DC7A385655AB.jpg'),
 ('c986dcd1de8548a2a5232bfec473485d', 1, '2018-11-11 13:55:26.560000', null, '2018-11-13 13:33:15.618000', null, 'f4009c594e414a0898ada71b6c1aa016', '维修轨道灯', 1, '对轨道灯出现的问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/697CD774-386C-45D1-BE98-325EE9E779A9.jpg'),
 ('6e9bdeddaab04771a0d1cff93fe0e2b9', 1, '2018-11-11 13:52:18.824000', null, '2018-11-13 12:27:34.948000', null, 'cc9d4b415b714c649e0b9350a6796d8c', '维修窗子', 1, '对窗户各部位出现的问题进行维修。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/16FA9024-3784-41D3-8C47-7E779B1AB298.jpg'),
 ('44a84be99e8b4ccf88ec24249c4f64c1', 1, '2018-10-22 13:12:44.677000', null, '2018-11-13 13:34:11.866000', null, '9582588fe5ec42ffb0c968691c6326ef', '马桶安装', 1, '安装马桶或更换马桶。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/D3A66531-D86B-4166-B756-931E72BCB02C.jpg'),
 ('85a2c0447ed047319e4adfe548f6f21b', 1, '2018-11-11 13:52:35.670000', null, '2018-11-13 12:33:19.267000', null, 'c7fe1ad961c14672a5f20238ba8b1b45', '维修门锁', 1, '对室内门锁出现的各种问题进行维修，不包含开锁。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/243C174F-853C-4B93-8DBC-6BFABD23B12D.jpg'),
 ('ae2f162ad5164310bbf9abc458dc5f36', 1, '2018-11-11 13:51:43.755000', null, '2018-11-13 12:32:26.545000', null, 'cc9d4b415b714c649e0b9350a6796d8c', '维修房内门', 1, '对房屋内的门出现的各种问题进行维修，不包含漆面修复。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/64DC7313-BE91-48EE-8596-E917E42BA998.jpg'),
 ('4071eac972e64569b3b52e434e12c3a9', 1, '2018-11-11 13:57:15.060000', null, '2018-11-13 12:34:29.807000', null, 'e4c4658e46f34d71aad508cf356a476d', '安装晾衣杆', 1, '将晾衣杆各部件连接后固定于合位置并保证正常工作。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/F65B0836-2FF9-40CD-A385-7A766617662E.jpg'),
 ('df3301cabe8541ea8e8bf633999fa66c', 1, '2018-10-22 13:13:21.446000', null, '2018-11-13 11:52:32.444000', null, 'f4009c594e414a0898ada71b6c1aa016', '射灯维修', 1, null, 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/25DFC70D-CAE9-4001-92DB-E7E8CF631C5A.jpg'),
 ('5d39e48ef5ef47cd81a0fc01d354f51f', 1, '2018-11-11 13:58:12.246000', null, '2018-11-13 12:36:16.476000', null, 'f4009c594e414a0898ada71b6c1aa016', '吊灯维修', 1, '对吊灯出现的问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/15925922-A17E-40EA-9396-E18D2597EEAD.jpg'),
 ('ab67092c50a04a7190742074b964dd74', 1, '2018-11-11 13:58:27.929000', null, '2018-11-13 12:37:12.675000', null, 'f4009c594e414a0898ada71b6c1aa016', '壁灯维修', 1, '对壁灯/床头灯出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/E0E3CAB6-221D-4DE8-96DC-2301B7B77501.jpg'),
 ('5f910233ec834f1aa45420e7ad84a3f1', 1, '2018-11-11 13:59:13.171000', null, '2018-11-13 12:37:55.550000', null, '9582588fe5ec42ffb0c968691c6326ef', '维修换气扇', 1, '对换气扇出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/EC4FC801-F8CE-449E-B6DD-ACA9CC6D4F76.jpg'),
 ('e7aaa44057904c94894e2803e5076af7', 1, '2018-11-11 13:59:34.270000', null, '2018-11-13 12:38:23.972000', null, 'f4009c594e414a0898ada71b6c1aa016', '灯带维修', 1, '对灯带出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/321D5283-A613-468B-896A-769D05A6E881.jpg'),
 ('f9a67489053140238aba2d5ca6aed72a', 1, '2018-11-11 13:51:16.976000', null, '2018-11-13 12:39:25.946000', null, '958706d8feeb4661ab6b77c17347e041', '维修角阀', 1, '对角阀出现的各类问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/BCBF03F2-CFAE-4A39-BDD9-E88F28B438B7.jpg'),
 ('6758b15c330d420d8b801a11f688870c', 1, '2018-11-11 13:50:55.528000', null, '2018-11-13 12:40:04.882000', null, '958706d8feeb4661ab6b77c17347e041', '维修水龙头', 1, '对水龙头出现的各类问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/8BD3C29C-AD02-4D76-BC91-7006728DD498.jpg'),
 ('9aa9528d5ec940b1bbaa931f7db316e4', 1, '2018-11-11 13:54:24.523000', null, '2018-11-13 12:40:45.926000', null, '41f2abfb147843a68ac77bd49195c558', '维修墙纸', 1, '对墙纸出现的各类问题进行维修或更换。每平方米的价格。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/410C7279-F3AB-4FC1-BD13-805A4EB7EE00.jpg'),
 ('4908407914ac43f3bc166ccd229d7621', 1, '2018-11-11 15:02:09.887000', null, '2018-11-13 12:41:23.050000', null, '9582588fe5ec42ffb0c968691c6326ef', '维修淋浴房', 1, '对淋浴房出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/646EA640-635F-4882-86F9-C84E70271BD9.jpg'),
 ('cd8e3183adbe4baf9934326252c19d05', 1, '2018-11-11 15:02:30.421000', null, '2018-11-13 12:42:19.516000', null, 'a81ccfe89c0042fb815f730ef02cd1c9', '打孔', 1, '对房屋内进行打孔操作，2毫米以内的单个孔价格。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/7E230F0B-DEE2-4690-8324-C09C9E2C44B6.jpg'),
 ('dd6fa2cfc21145689f7d66742ce59e7a', 1, '2018-11-11 15:03:13.124000', null, '2018-11-13 12:43:54.323000', null, 'a81ccfe89c0042fb815f730ef02cd1c9', '疏通马桶', 1, '对马桶出现的堵塞进行疏通。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/76C8525D-06CF-465A-A5C0-97433A2FA771.jpg'),
 ('c3459c7c22b64c99b1508d08e3048720', 1, '2018-11-11 13:53:11.303000', null, '2018-11-13 12:44:28.840000', null, 'e4c4658e46f34d71aad508cf356a476d', '安装相册', 1, '把相册固定在合理位置。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/F38C14A9-F829-4091-A6FB-AAD7FFB43F65.jpg'),
 ('5c3280f90e3e47f9853e834dce82d859', 1, '2018-11-11 15:03:32.773000', null, '2018-11-13 12:44:57.391000', null, 'a81ccfe89c0042fb815f730ef02cd1c9', '疏通小便池', 1, '对小便池出现的堵塞进行疏通。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/611AD528-DEBD-4EE0-846C-367019CFC318.jpg'),
 ('b9af4055573a4a0681e945a9d71d2622', 1, '2018-11-11 13:55:00.431000', null, '2018-11-13 12:45:42.692000', null, '47653322ea664934b61adacb7f73f8d7', '维修沙发脚', 0, '对沙发脚出现的问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/F667419A-347F-4385-9C20-64D0CA1DDBA4.jpg'),
 ('ac4932fffc5f4f2486313da88b116ddc', 1, '2018-11-11 15:04:38.945000', null, '2018-11-13 12:46:20.565000', null, 'a81ccfe89c0042fb815f730ef02cd1c9', '疏通管道', 1, '对房内管道堵塞进行疏通。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/28DC742B-D87D-4B9B-A815-61280EC7F216.jpg'),
 ('6c2d16b134b3494599d2cd27779c7527', 1, '2018-11-11 13:54:04.684000', null, '2018-11-13 12:47:41.413000', null, '41f2abfb147843a68ac77bd49195c558', '维修木地板', 1, '按要求更换或维修损坏的木地板。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/3F0474DB-27B7-4C82-85DC-BCC253427AD3.jpg'),
 ('4d4ae26d18d2415298e97c06695b4b57', 1, '2018-11-11 13:49:42.659000', null, '2018-11-13 13:18:47.046000', null, 'fe58cd92f4bc4ba589b624e2d0e6d5ed', '维修电路空开', 1, '对电路空开出现的类类问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/E310D880-5C61-4132-AD1B-8B8DE00C1985.jpg'),
 ('03784d27a2a44a8f9fddd4aa188c636f', 1, '2018-11-11 18:28:29.186000', null, '2018-11-13 11:38:59.511000', null, 'fe58cd92f4bc4ba589b624e2d0e6d5ed', '综合布线', 1, null, 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/256BCDAC-E174-486A-980C-7DAB90A17DD7.jpg'),
 ('1f6bdd999c434d01bf2c8a5f30b0c94e', 1, '2018-11-11 15:18:59.521000', null, '2018-11-13 13:22:05.207000', null, '47653322ea664934b61adacb7f73f8d7', '浴室柜维修', 1, '对浴室柜出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/6B6E34AB-D04C-48BF-B50C-8261DDE138E4.jpg'),
 ('40e68e10a22f4d7eae83898e424086d5', 1, '2018-11-11 15:06:39.072000', null, '2018-11-13 13:23:49.305000', null, 'f4009c594e414a0898ada71b6c1aa016', '平板灯安装', 1, '安装平板灯具。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/9E06A2C8-63AC-4D2F-B572-D826475602D7.jpg'),
 ('cba1e5c6fbb841a7b18c16e0d612155d', 1, '2018-11-11 15:08:21.935000', null, '2018-11-13 13:20:33.791000', null, '9582588fe5ec42ffb0c968691c6326ef', '干手器维修', 0, '对干手器出现的各类问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/C27A8FC7-A559-42D6-A067-8039F86F7918.jpg'),
 ('68b2394344f541978812b15ccae8c998', 1, '2018-11-11 15:16:53.201000', null, '2018-11-13 13:20:57.607000', null, '47653322ea664934b61adacb7f73f8d7', '床头柜维修', 0, '对床头柜出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/CF757BC2-E500-493A-8963-A13200895A37.jpg'),
 ('f963a0b4edd44a7295ea674154a39c60', 1, '2018-11-11 15:20:26.687000', null, '2018-11-13 13:21:27.226000', null, '9582588fe5ec42ffb0c968691c6326ef', '面盆疏通', 1, '对面盆出现的堵塞问题进行疏通。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/F437A9AB-A2FA-4E7E-956A-8A51F8811053.jpg'),
 ('b8e6f79a6f7b492db23eecedaa163575', 1, '2018-10-22 13:12:59.393000', null, '2018-11-13 13:25:44.206000', null, '9582588fe5ec42ffb0c968691c6326ef', '马桶维修', 1, '对马桶出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/6795E521-50B9-4710-8E5A-1E040E14BF6C.jpg'),
 ('ba9891eef8d34601b400972430a4ca63', 1, '2018-11-13 12:01:05.930000', null, '2018-11-13 13:24:49.556000', null, 'c7fe1ad961c14672a5f20238ba8b1b45', '锁芯更换', 1, '更换门锁锁芯，不含开锁', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/271C53C5-9877-490A-B4A8-D0159BEEC62A.jpg'),
 ('6137d821ed8742af9eb6a9a1aaf8a734', 1, '2018-11-11 13:49:12.259000', null, '2018-11-13 13:26:10.907000', null, 'fe58cd92f4bc4ba589b624e2d0e6d5ed', '维修插座面板', 1, '对插座面板出现的各类问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/C94D11B7-28B7-456D-B61A-55638FE57008.jpg'),
 ('5f65503d1e96412fa043f392abc11f07', 1, '2018-11-13 11:55:38.643000', null, '2018-11-13 13:28:55.675000', null, '958706d8feeb4661ab6b77c17347e041', '混水阀维修', 1, '对混水阀出现的各类问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/C0581A3B-E2C4-4B56-A42C-B3A51F3620F7.jpg'),
 ('40d276d9e8d445fa99776c3a02d0534a', 1, '2018-11-11 15:17:18.957000', null, '2018-11-13 11:44:39.287000', null, '47653322ea664934b61adacb7f73f8d7', '电视柜维修', 1, null, 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/AD908446-F509-4E81-BDA8-100F395A18AC.jpg'),
 ('5abc44bf3735469e924c560e6ae8ff9b', 1, '2018-11-11 15:05:03.454000', null, '2018-11-13 13:29:00.566000', null, 'a81ccfe89c0042fb815f730ef02cd1c9', '疏通拖把池', 1, '对拖把池堵塞进行疏通处理。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/C90B52A0-8043-43D8-ACEC-7E9CAA67A7CE.jpg'),
 ('7d5a48c9142e41d0824910d9eb9f2f59', 1, '2018-11-13 11:54:31.768000', null, '2018-11-13 13:26:49.487000', null, '958706d8feeb4661ab6b77c17347e041', '洗菜盆水龙头维修', 1, '对洗菜盆水龙头出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/1A76955E-033A-4744-87C0-0BDC69AA3B16.jpg'),
 ('f6d0e7ff2dbb46908b343981b741502d', 1, '2018-11-11 15:07:12.992000', null, '2018-11-13 13:28:06.254000', null, '9582588fe5ec42ffb0c968691c6326ef', '浴霸安装', 1, '把浴霸安装固定在合理位置，并接上已经布好的线路。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/1D740B6D-496A-48A7-A5D1-894DF70B50F6.jpg'),
 ('c2652784037147b78d434448d0c1ad6c', 1, '2018-11-11 13:59:50.604000', null, '2018-11-13 12:38:55.783000', null, 'f4009c594e414a0898ada71b6c1aa016', '灯管维修', 1, '对灯管出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/D0A06D74-C862-4CFA-88E9-9B981E1D9C16.jpg'),
 ('c3a84a08e45c4826a629caac12903753', 1, '2018-11-11 15:04:21.817000', null, '2018-11-13 12:43:23.028000', null, 'a81ccfe89c0042fb815f730ef02cd1c9', '疏通地漏', 1, '对地漏出现的堵塞进行疏通，单个地漏的疏通价格。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/F1740427-3DA7-413E-ADB3-F354794232F0.jpg'),
 ('9453c29670dd497287d6e5a3c57db9af', 1, '2018-11-11 13:55:54.408000', null, '2018-11-13 12:47:47.810000', null, 'fe58cd92f4bc4ba589b624e2d0e6d5ed', '维修射灯', 1, '对射灯出现的问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/6760E22C-C226-4B45-B757-7228B92D6F12.jpg'),
 ('202c63a3330b46f3bdd8a7298d2fa3fb', 1, '2018-11-11 15:12:37.504000', null, '2018-11-13 12:49:52.118000', null, '958706d8feeb4661ab6b77c17347e041', '软管维修', 1, '对软管出现的各类问题进行维修或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/D47EEF92-64FD-4BD9-9A5E-1769DACB88AB.jpg'),
 ('987217b76a1442298f051c1879259cb1', 1, '2018-11-11 15:18:24.927000', null, '2018-11-13 15:16:27.438000', null, '47653322ea664934b61adacb7f73f8d7', '鞋柜维修', 1, null, 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/A6827CA9-A34D-4A22-8865-36D93B335EE1.jpg'),
 ('e5b8f4d2dac04e0bbd96a94e5aba0efe', 1, '2018-11-11 15:17:53.935000', null, '2018-11-13 13:12:19.144000', null, '47653322ea664934b61adacb7f73f8d7', '儿童床维修', 1, null, 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/9588B658-9D6B-42C4-9FAA-0C0781B9F1B0.jpg'),
 ('34ccb8e51a49431fa302404aa55bdbef', 1, '2018-11-11 15:19:40.301000', null, '2018-11-13 13:15:11.838000', null, '47653322ea664934b61adacb7f73f8d7', '书柜维修', 1, null, 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/B9667D1E-0CD3-4FF0-8F45-E2009B7CC8F3.jpg'),
 ('508b6a1295ce4cbf8c93bcb5e8914594', 1, '2018-11-11 15:19:17.534000', null, '2018-11-13 13:15:45.923000', null, '47653322ea664934b61adacb7f73f8d7', '桌椅维修', 1, '对家庭桌椅出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/26A334CF-7967-4EBD-B05E-CBCC8795B236.jpg'),
 ('44d76158a6cd461da868ee28a511da67', 1, '2018-11-11 13:47:56.072000', null, '2018-11-13 13:16:14.386000', null, 'fe58cd92f4bc4ba589b624e2d0e6d5ed', '维修开关面板', 1, '对开关面板出现的各类问题进行进行或更换。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/246E1D0F-3CBF-40E1-B410-1480B8CED631.jpg'),
 ('0371a30b39d2464dba93d5d84be29bc9', 1, '2018-11-11 13:57:37.992000', null, '2018-11-13 13:29:03.839000', null, '9582588fe5ec42ffb0c968691c6326ef', '维修花洒', 1, '对花洒出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/23B659E1-3D5D-4220-8EE7-732C0095DA0A.jpg'),
 ('b51c662802a840a392f617edddc0144c', 1, '2018-11-11 15:18:40.781000', null, '2018-11-13 13:29:38.580000', null, '47653322ea664934b61adacb7f73f8d7', '衣柜维修', 1, '对衣柜出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/51BE7956-E00C-4A68-BEFF-6377FA773C79.jpg'),
 ('ad03a29e53af4c63ab3401e2feb8f760', 1, '2018-11-11 15:05:56.818000', null, '2018-11-13 12:48:36.802000', null, 'e4c4658e46f34d71aad508cf356a476d', '五金挂件', 1, '根据客户要求把相应的五金挂件安装在合理位置。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/B4C0E216-67C4-4750-96F3-E3A4615E61CD.jpg'),
 ('d215ce2733ff4546860e1edf2252d1a7', 1, '2018-11-11 15:10:06.881000', null, '2018-11-13 12:49:19.977000', null, 'a81ccfe89c0042fb815f730ef02cd1c9', '小便池疏通', 1, '对小便池出现的堵塞问题进行疏通。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/9189B312-7AD1-4F78-97CE-82A6DBE11CF3.jpg'),
 ('af2377d7b3a242e88a9b336816406408', 1, '2018-11-11 15:10:36.898000', null, '2018-11-13 13:19:50.379000', null, 'e4c4658e46f34d71aad508cf356a476d', '家居挂件安装', 1, '对房内需要安装的小挂件固定在合理位置。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/82335700-9179-4611-BA69-2690BFFEC3CC.jpg'),
 ('6942f57473364e1f8ea067460187cea4', 1, '2018-11-11 15:07:52.966000', null, '2018-11-13 13:23:27.659000', null, '9582588fe5ec42ffb0c968691c6326ef', '洗菜盆维修', 1, '对洗菜盆出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/9428685E-7278-4CF1-9A68-8C26DCD8FF79.jpg'),
 ('d858a5410af84f6f8fc30bb0e249ac38', 1, '2018-11-11 15:09:29.672000', null, '2018-11-13 13:28:33.465000', null, '41f2abfb147843a68ac77bd49195c558', '墙面修复', 1, '对于墙面进行修复，每平方单价。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/79541E30-B052-4145-9199-F89B7E1DB310.jpg'),
 ('12f8b412d7ea42a89ab7f81e1a945dea', 1, '2018-11-13 15:17:30.997000', null, null, null, '47653322ea664934b61adacb7f73f8d7', '酒柜维修', 1, '对酒柜出现的各类问题进行维修或更换配件。', 'https://yijiabang.oss-cn-shenzhen.aliyuncs.com/service-item-img/FF1CF2CB-F146-46AA-A926-DFCA03A57C05.jpg');
EOSQL
