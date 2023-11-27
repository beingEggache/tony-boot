SET search_path TO tony_api, public;

-- 用户表
create table tony_api.public.sys_user
(
    user_id     varchar(36)             not null
        constraint sys_user_pk
            primary key,
    user_name   varchar(30)             not null,
    real_name   varchar(30)             not null,
    mobile      varchar(14)             not null,
    pwd         varchar(100)            not null,
    create_time timestamp default now() not null,
    states      int2      default 1     not null,
    remark      varchar(200)
);

comment on table tony_api.public.sys_user is '用户';

comment on column tony_api.public.sys_user.user_id is '用户id';

comment on column tony_api.public.sys_user.user_name is '用户登录名';

comment on column tony_api.public.sys_user.real_name is '用户真实姓名';

comment on column tony_api.public.sys_user.mobile is '手机号';

comment on column tony_api.public.sys_user.pwd is '密码';

comment on column tony_api.public.sys_user.create_time is '创建时间';

comment on column tony_api.public.sys_user.states is '用户状态：1:启用，0:禁用。 可根据需求扩展';

comment on column tony_api.public.sys_user.remark is '备注';

create unique index sys_user_phone_uindex
    on tony_api.public.sys_user (mobile);

create unique index sys_user_user_name_uindex
    on tony_api.public.sys_user (user_name);

alter table tony_api.public.sys_user
    owner to tony_api;
--
-- 角色表
create table tony_api.public.sys_role
(
    role_id   varchar(50) not null
        constraint sys_role_pk
            primary key,
    app_id    varchar(50) not null,
    role_name varchar(50) not null,
    remark    varchar(200)
);

comment on table tony_api.public.sys_role is '角色';

comment on column tony_api.public.sys_role.role_id is '角色ID';

comment on column tony_api.public.sys_role.app_id is '应用ID';

comment on column tony_api.public.sys_role.role_name is '角色名';

comment on column tony_api.public.sys_role.remark is '备注';

alter table tony_api.public.sys_role
    owner to tony_api;
--
-- 用户角色中间表
create table tony_api.public.sys_user_role
(
    user_id varchar(36) not null,
    role_id varchar(50) not null,
    primary key (user_id, role_id)
);

comment on table tony_api.public.sys_user_role is '用户角色中间表';
--
-- 模块/权限表
create table tony_api.public.sys_module
(
    module_id          varchar(100)                               not null
        constraint sys_module_pk
            primary key,
    app_id             varchar(50),
    module_name        varchar(50)                                not null,
    module_value       varchar(200) default ''::character varying not null,
    module_type        smallint                                   not null,
    module_group       varchar(100) default ''::character varying not null,
    module_description varchar(200) default ''::character varying not null
);

comment on table tony_api.public.sys_module is '模块/权限表';

comment on column tony_api.public.sys_module.module_id is '模块/权限ID';

comment on column tony_api.public.sys_module.app_id is '应用ID';

comment on column tony_api.public.sys_module.module_name is '模块/权限名';

comment on column tony_api.public.sys_module.module_value is '模块/权限值（接口URL，前端路由，前端组件名）';

comment on column tony_api.public.sys_module.module_type is '模块/权限类型（1：接口，2：前端路由，3：前端组件）';

comment on column tony_api.public.sys_module.module_group is '模块/权限分组';

comment on column tony_api.public.sys_module.module_description is '模块/权限说明';

alter table tony_api.public.sys_module
    owner to tony_api;
--
--应用表
create table tony_api.public.sys_app
(
    app_id          varchar(50)        not null
        constraint sys_app_pk
            primary key,
    app_name        varchar(50)        not null,
    app_description varchar(200),
    states          smallint default 1 not null
);

comment on table tony_api.public.sys_app is '应用表';

comment on column tony_api.public.sys_app.app_id is '应用ID';

comment on column tony_api.public.sys_app.app_name is '应用名';

comment on column tony_api.public.sys_app.app_description is '应用说明';

comment on column tony_api.public.sys_app.states is '状态：1:启用，0:禁用。 可根据需求扩展';

alter table tony_api.public.sys_app
    owner to tony_api;
--
--角色模块/权限表
create table tony_api.public.sys_role_module
(
    role_id   varchar(50) not null,
    module_id varchar(50) not null,
    constraint sys_role_module_pkey
        primary key (role_id, module_id)
);

comment on table tony_api.public.sys_role_module is '角色模块/权限表';

alter table tony_api.public.sys_role_module
    owner to tony_api;
--





