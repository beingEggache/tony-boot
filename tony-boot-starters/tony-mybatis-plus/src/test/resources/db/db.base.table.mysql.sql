-- 用户表
create table t_sys_user
(
    user_id     varchar(36)                         not null primary key comment '用户id',
    user_name   varchar(30)                         not null comment '用户登录名',
    real_name   varchar(30)                         not null comment '用户真实姓名',
    mobile      varchar(14)                         not null comment '手机号',
    pwd         varchar(100)                        not null comment '密码',
    create_time timestamp default current_timestamp not null,
    states      int       default 1                 not null,
    remark      varchar(200)
) comment '角色';

create unique index t_sys_user_phone_uindex
    on sys_user (mobile);

create unique index t_sys_user_user_name_uindex
    on sys_user (user_name);

--
-- 角色表
create table t_sys_role
(
    role_id   varchar(50) not null
        primary key,
    app_id    varchar(50) not null comment '应用ID',
    role_name varchar(50) not null comment '角色名',
    remark    varchar(200) comment '备注'
) comment '角色';



--
-- 用户角色中间表
create table t_sys_user_role
(
    user_id varchar(36) not null,
    role_id varchar(50) not null,
    primary key (user_id, role_id)
);

--
-- 模块/权限表
create table t_sys_module
(
    module_id          varchar(100)            not null primary key,
    app_id             varchar(50)  default '' not null comment '应用ID',
    module_name        varchar(50)             not null comment '模块/权限名',
    module_value       varchar(200)            not null comment '模块/权限值（接口URL，前端路由，前端组件名）',
    module_type        smallint                not null comment '模块/权限类型（1：接口，2：前端路由，3：前端组件）',
    module_group       varchar(100) default '' not null comment '模块/权限分组',
    module_description varchar(200) default '' not null comment '模块/权限说明'
) comment '模块/权限';


--
create table t_sys_app
(
    app_id          varchar(50)        not null
        primary key,
    app_name        varchar(50)        not null,
    app_description varchar(200),
    states          smallint default 1 not null
);

--
-- 角色模块/权限表
create table t_sys_role_module
(
    role_id   varchar(50) not null,
    module_id varchar(50) not null,
    constraint t_sys_role_module_pkey
        primary key (role_id, module_id)
);

--





