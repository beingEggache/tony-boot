-- mysql 8.0.13+ json default value supported
DROP TABLE if exists `sys_app` cascade;
DROP TABLE if exists `sys_module` cascade;
DROP TABLE if exists `sys_role` cascade;
DROP TABLE if exists `sys_role_module` cascade;
DROP TABLE if exists `sys_employee` cascade;
DROP TABLE if exists `sys_employee_role` cascade;
DROP TABLE if exists `sys_dept` cascade;
DROP TABLE if exists `sys_employee_dept` cascade;
DROP TABLE if exists `sys_dict` cascade;
DROP TABLE if exists `sys_dict_type` cascade;


CREATE TABLE `sys_dept`
(
    `dept_id`        varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `parent_dept_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '上级部门id',
    `dept_name`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '部门名',
    `dept_code`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '部门编码',
    `dept_code_seq`  varchar(300) COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门编码序列',
    `sort`           int                                     NOT NULL DEFAULT '0' COMMENT '部门排序',
    `remark`         varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `created_at`     timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`     varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`   varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `updated_at`     timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`     varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name`   varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`        tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `deleted_at`     timestamp                               NULL     DEFAULT NULL COMMENT '删除标记：!null-已删除，null-未删除',
    `tenant_id`      varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`dept_id`),
    KEY `idx_dept_code_seq` (`dept_code_seq`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='部门';

CREATE TABLE `sys_dict`
(
    `dict_id`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `dict_type_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '字典类型id',
    `dict_name`    varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '字典标签',
    `dict_code`    varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '字典code',
    `dict_value`   varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '字典值',
    `dict_meta`    json                                    NOT NULL DEFAULT (_utf8mb4'{}') COMMENT '字典meta',
    `build_in`     tinyint                                 NOT NULL DEFAULT '0' COMMENT '系统内建,不可删除',
    `sort`         int                                     NOT NULL DEFAULT '0' COMMENT '排序',
    `remark`       varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
    `created_at`   timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`   varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `updated_at`   timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`   varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `deleted_at`   timestamp                               NULL     DEFAULT NULL COMMENT '删除标记：!null-已删除，null-未删除',
    PRIMARY KEY (`dict_id`),
    KEY `idx_sys_dict_type_id` (`dict_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='字典';

CREATE TABLE `sys_dict_type`
(
    `dict_type_id`        varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `parent_dict_type_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '上级id',
    `app_id`              varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '应用ID',
    `dict_type_code`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '字典类型编码',
    `dict_type_code_seq`  varchar(300) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型编码序列',
    `dict_type_name`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '字典类型名',
    `build_in`            tinyint                                 NOT NULL DEFAULT '0' COMMENT '系统内建,不可删除',
    `sort`                int                                     NOT NULL DEFAULT '0' COMMENT '排序',
    `remark`              varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
    `created_at`          timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`          varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`        varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `updated_at`          timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`          varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name`        varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `deleted_at`          timestamp                               NULL     DEFAULT NULL COMMENT '删除标记：!null-已删除，null-未删除',
    PRIMARY KEY (`dict_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='字典类型';

CREATE TABLE `sys_employee`
(
    `employee_id`     varchar(32) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '员工id',
    `account`         varchar(30) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '员工登录名',
    `real_name`       varchar(30) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '员工真实姓名',
    `employee_mobile` varchar(14) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '手机号',
    `pwd`             varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `salt`            varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '盐',
    `account_state`   int                                     NOT NULL DEFAULT '0' COMMENT '用户状态: 0-需要修改密码, 1-正常',
    `created_at`      timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`      varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`    varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `updated_at`      timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`      varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name`    varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`         tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `remark`          varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
    `deleted_at`      timestamp                               NULL     DEFAULT NULL COMMENT '删除标记：!null-已删除，null-未删除',
    `tenant_id`       varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`employee_id`),
    KEY `idx_employee_name` (`account`),
    KEY `idx_employee_mobile` (`employee_mobile`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='员工';

CREATE TABLE `sys_employee_dept`
(
    `employee_id` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工id',
    `dept_id`     varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `tenant_id`   varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`employee_id`, `dept_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `sys_employee_role`
(
    `employee_id` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工id',
    `role_id`     varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `tenant_id`   varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`employee_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `sys_module`
(
    `module_id`        varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `app_id`           varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '应用ID',
    `parent_module_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '上级id',
    `module_name`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '模块/权限名',
    `module_code`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '编码',
    `module_code_seq`  varchar(300) COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码序列',
    `module_value`     varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块值（接口URL，前端路由，前端组件名）',
    `module_type`      smallint                                NOT NULL COMMENT '模块类型（0:节点(纯做树形结构的节点), 1：接口，2：前端路由，3：前端组件）',
    `module_group`     varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '模块分组',
    `module_meta`      json                                    NOT NULL DEFAULT (_utf8mb4'{}') COMMENT '模块meta',
    `created_at`       timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`       varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`     varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `updated_at`       timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`       varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name`     varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`          tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `remark`           varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
    `deleted_at`       timestamp                               NULL     DEFAULT NULL COMMENT '删除标记：!null-已删除，null-未删除',
    PRIMARY KEY (`module_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='模块(菜单,按钮,接口)';

CREATE TABLE `sys_role`
(
    `role_id`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `role_name`    varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '角色名',
    `role_code`    varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '角色编码',
    `build_in`     tinyint                                 NOT NULL DEFAULT '0' COMMENT '系统内建, 不可删除',
    `sort`         int                                     NOT NULL DEFAULT '-1' COMMENT '排序',
    `remark`       varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `created_at`   timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`   varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `updated_at`   timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`   varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`      tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `deleted_at`   timestamp                               NULL     DEFAULT NULL COMMENT '删除标记：!null-已删除，null-未删除',
    `tenant_id`    varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`role_id`),
    KEY `idx_role_name` (`role_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色';

CREATE TABLE `sys_role_module`
(
    `role_id`   varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `module_id` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `tenant_id` varchar(50) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`role_id`, `module_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
