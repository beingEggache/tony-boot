CREATE TABLE `sys_app`
(
    `app_id`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `app_name`     varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `create_time`  timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`   bigint                                  NOT NULL DEFAULT '0' COMMENT '创建人',
    `creator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`  timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`   bigint                                  NOT NULL DEFAULT '0' COMMENT '更新人',
    `updator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`      tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `remark`       varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
    `deleted`      tinyint                                 NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    PRIMARY KEY (`app_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `sys_module`
(
    `module_id`    varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
    `app_id`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '应用ID',
    `module_name`  varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '模块/权限名',
    `module_value` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块/权限值（接口URL，前端路由，前端组件名）',
    `module_type`  smallint                                NOT NULL COMMENT '模块/权限类型（1：接口，2：前端路由，3：前端组件）',
    `module_group` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '模块/权限分组',
    `create_time`  timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`   bigint                                  NOT NULL DEFAULT '0' COMMENT '创建人',
    `creator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`  timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`   bigint                                  NOT NULL DEFAULT '0' COMMENT '更新人',
    `updator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`      tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `remark`       varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
    `deleted`      tinyint                                 NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    PRIMARY KEY (`module_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='模块/权限';

CREATE TABLE `sys_role`
(
    `role_id`      varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `app_id`       varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用ID',
    `role_name`    varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
    `remark`       varchar(200) COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '备注',
    `create_time`  timestamp                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`   bigint                                 NOT NULL DEFAULT '0' COMMENT '创建人',
    `creator_name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`  timestamp                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`   bigint                                 NOT NULL DEFAULT '0' COMMENT '更新人',
    `updator_name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`      tinyint                                NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `deleted`      tinyint                                NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    `tenant_id`    bigint                                 NOT NULL DEFAULT '0' COMMENT '租户id',
    PRIMARY KEY (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色';

CREATE TABLE `sys_role_module`
(
    `role_id`   varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `module_id` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `tenant_id` bigint                                 NOT NULL DEFAULT '0' COMMENT '租户id',
    PRIMARY KEY (`role_id`, `module_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `sys_user`
(
    `user_id`      bigint                                  NOT NULL COMMENT '用户id',
    `user_name`    varchar(30) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户登录名',
    `real_name`    varchar(30) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户真实姓名',
    `mobile`       varchar(14) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '手机号',
    `pwd`          varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `create_time`  timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `creator_id`   bigint                                  NOT NULL DEFAULT '0' COMMENT '创建人',
    `creator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `update_time`  timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `updator_id`   bigint                                  NOT NULL DEFAULT '0' COMMENT '更新人',
    `updator_name` varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人名称',
    `enabled`      tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    `remark`       varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
    `deleted`      tinyint                                 NOT NULL DEFAULT '0' COMMENT '删除标记：1-已删除，0-未删除',
    `tenant_id`    bigint                                  NOT NULL DEFAULT '0' COMMENT '租户id',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `t_sys_user_phone_uindex` (`mobile`),
    UNIQUE KEY `t_sys_user_user_name_uindex` (`user_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='角色';

CREATE TABLE `sys_user_role`
(
    `user_id`   varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
    `role_id`   varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `tenant_id` bigint                                 NOT NULL DEFAULT '0' COMMENT '租户id',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;





