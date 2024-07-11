-- mysql 8.0.13+ json default value supported
DROP TABLE if exists `sys_app` cascade;
DROP TABLE if exists `sys_module` cascade;
DROP TABLE if exists `sys_role` cascade;
DROP TABLE if exists `sys_role_module` cascade;
DROP TABLE if exists `sys_employee` cascade;
DROP TABLE if exists `sys_employee_role` cascade;
DROP TABLE if exists `sys_dept` cascade;
DROP TABLE if exists `sys_employee_dept` cascade;


CREATE TABLE `sys_app`
(
    `app_id`       varchar(50)  NOT NULL,
    `app_name`     varchar(50)  NOT NULL,
    `create_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`   varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name` varchar(30)  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`   varchar(32)  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name` varchar(30)  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`      tinyint      NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `remark`       varchar(200) NOT NULL DEFAULT '',
    `deleted`      tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    PRIMARY KEY (`app_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `sys_employee`
(
    `employee_id`     varchar(50)  NOT NULL COMMENT '员工id',
    `account`         varchar(30)  NOT NULL COMMENT '员工登录名',
    `employee_no`     varchar(30)  NOT NULL COMMENT '员工登录名',
    `real_name`       varchar(30)  NOT NULL COMMENT '员工真实姓名',
    `employee_mobile` varchar(14)  NOT NULL COMMENT '手机号',
    `pwd`             varchar(100) NOT NULL COMMENT '密码',
    `salt`            varchar(100) NOT NULL COMMENT '盐',
    `account_state`   smallint     NOT NULL DEFAULT '0' COMMENT '用户状态: 0-需要修改密码, 1-正常',
    `create_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`    varchar(30)  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name`    varchar(30)  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`         tinyint      NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `remark`          varchar(200) NOT NULL DEFAULT '',
    `deleted`         tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    PRIMARY KEY (`employee_id`),
    KEY `idx_account` (`account`),
    KEY `idx_employee_mobile` (`employee_mobile`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='员工';

CREATE TABLE IF NOT EXISTS `sys_role`
(
    `role_id`      varchar(50)  NOT NULL,
    `role_name`    varchar(50)  NOT NULL COMMENT '角色名',
    `role_code`    varchar(50)  NOT NULL COMMENT '角色编码',
    `sort`         int          NOT NULL DEFAULT '0' COMMENT '排序',
    `remark`       varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
    `create_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`   varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name` varchar(30)  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`   varchar(32)  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name` varchar(30)  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`      tinyint      NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `deleted`      tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    `tenant_id`    varchar(32)  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`role_id`),
    KEY `idx_role_name` (`role_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色';

CREATE TABLE IF NOT EXISTS `sys_dept`
(
    `dept_id`        varchar(50)  NOT NULL,
    `parent_dept_id` varchar(50)  NOT NULL DEFAULT '' comment '上级id',
    `dept_name`      varchar(50)  NOT NULL COMMENT '部门名',
    `dept_code`      varchar(50)  NOT NULL COMMENT '部门编码',
    `dept_code_seq`  varchar(300) NOT NULL COMMENT '部门编码序列',
    `sort`           int          NOT NULL DEFAULT '0' COMMENT '部门排序',
    `remark`         varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
    `create_time`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`     varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`   varchar(30)  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`     varchar(32)  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name`   varchar(30)  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`        tinyint      NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `deleted`        tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    `tenant_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`dept_id`),
    KEY `idx_dept_code_seq` (`dept_code_seq`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='部门';

CREATE TABLE IF NOT EXISTS `sys_module`
(
    `module_id`        varchar(50)  NOT NULL,
    `app_id`           varchar(50)  NOT NULL DEFAULT '' COMMENT '应用ID',
    `parent_module_id` varchar(50)  NOT NULL DEFAULT '' NOT NULL COMMENT '上级id',
    `module_name`      varchar(50)  NOT NULL COMMENT '模块名',
    `module_code`      varchar(50)  NOT NULL COMMENT '编码',
    `module_code_seq`  varchar(300) NOT NULL COMMENT '编码序列',
    `module_value`     varchar(200) NOT NULL COMMENT '模块值（接口URL，前端路由，前端组件名）',
    `module_type`      smallint     NOT NULL COMMENT '模块类型:0-节点(纯做树形结构的节点), 1-接口，2-前端路由，3-前端组件）',
    `module_group`     varchar(100) NOT NULL DEFAULT '' COMMENT '模块分组',
    `module_meta`      json         NOT NULL DEFAULT ('{}') COMMENT '模块meta',
    `create_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`     varchar(30)  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '更新人',
    `updator_name`     varchar(30)  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`          tinyint      NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `remark`           varchar(200) NOT NULL DEFAULT '',
    `deleted`          tinyint      NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    PRIMARY KEY (`module_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='模块(菜单,按钮,接口)';

CREATE TABLE IF NOT EXISTS `sys_employee_role`
(
    `employee_id` varchar(50) NOT NULL COMMENT '员工id',
    `role_id`     varchar(50) NOT NULL,
    `tenant_id`   varchar(32) NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`employee_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `sys_employee_dept`
(
    `employee_id` varchar(50) NOT NULL COMMENT '员工id',
    `dept_id`     varchar(50) NOT NULL,
    `tenant_id`   varchar(50) NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`employee_id`, `dept_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `sys_role_module`
(
    `role_id`   varchar(50) NOT NULL,
    `module_id` varchar(50) NOT NULL,
    `tenant_id` varchar(50) NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`role_id`, `module_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;








