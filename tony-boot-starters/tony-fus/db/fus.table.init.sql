DROP TABLE if exists `fus_task_cc` cascade;
DROP TABLE if exists `fus_task_actor` cascade;
DROP TABLE if exists `fus_task` cascade;
DROP TABLE if exists `fus_instance` cascade;
DROP TABLE if exists `fus_history_task_actor` cascade;
DROP TABLE if exists `fus_history_task` cascade;
DROP TABLE if exists `fus_history_instance` cascade;
DROP TABLE if exists `fus_process` cascade;

CREATE TABLE `fus_task_cc`
(
    `task_cc_id`     varchar(32)  NOT NULL COMMENT '主键ID',
    `tenant_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '租户ID',
    `creator_id`     varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator_name`   varchar(50)  NOT NULL COMMENT '创建人',
    `create_time`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `instance_id`    varchar(32)  NOT NULL DEFAULT '' COMMENT '流程实例ID',
    `parent_task_id` varchar(32)  NOT NULL DEFAULT '' COMMENT '父任务ID',
    `task_name`      varchar(100) NOT NULL DEFAULT '' COMMENT '任务名称',
    `display_name`   varchar(200) NOT NULL DEFAULT '' COMMENT '任务显示名称',
    `actor_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '参与者ID',
    `actor_name`     varchar(300) NOT NULL DEFAULT '' COMMENT '参与者名称',
    `actor_type`     int          NOT NULL COMMENT '参与者类型 1，用户 2，角色 3，部门',
    `task_state`     smallint(1)  NOT NULL DEFAULT '1' COMMENT '任务状态 1，活动 2，结束',
    `finish_time`    timestamp    NULL     DEFAULT NULL COMMENT '完成时间',
    PRIMARY KEY (`task_cc_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='抄送任务表';

CREATE TABLE `fus_process`
(
    `process_id`      varchar(32)  NOT NULL COMMENT '主键ID',
    `tenant_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '租户ID',
    `creator_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator_name`    varchar(50)  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`     timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `process_name`    varchar(100) NOT NULL DEFAULT '' COMMENT '流程名称',
    `display_name`    varchar(200) NOT NULL DEFAULT '' COMMENT '流程显示名称',
    `process_icon`    varchar(255) NOT NULL DEFAULT '' COMMENT '流程图标地址',
    `process_type`    varchar(100) NOT NULL DEFAULT '' COMMENT '流程类型',
    `process_version` int          NOT NULL DEFAULT '1' COMMENT '流程版本，默认 1',
    `instance_url`    varchar(200) NOT NULL DEFAULT '' COMMENT '实例地址',
    `use_scope`       smallint(1)           DEFAULT '0' COMMENT '使用范围 0，全员 1，指定人员（业务关联） 2，均不可提交',
    `process_state`   tinyint(1)            DEFAULT '1' COMMENT '流程状态 0，不可用 1，可用',
    `model_content`   json                  DEFAULT NULL COMMENT '流程模型定义JSON内容',
    `sort`            smallint(1)           DEFAULT '0' COMMENT '排序',
    PRIMARY KEY (`process_id`) USING BTREE,
    KEY `idx_process_name` (`process_name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='流程定义表';

CREATE TABLE `fus_instance`
(
    `instance_id`      varchar(32)  NOT NULL COMMENT '主键ID',
    `tenant_id`        varchar(32)  NOT NULL DEFAULT '' COMMENT '租户ID',
    `creator_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator_name`     varchar(50)  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `process_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '流程定义ID',
    `priority`         smallint(1)           DEFAULT NULL COMMENT '优先级',
    `instance_no`      varchar(50)           DEFAULT NULL COMMENT '流程实例编号',
    `business_key`     varchar(100) NOT NULL DEFAULT '' COMMENT '业务KEY',
    `variable`         json                  DEFAULT NULL COMMENT '变量json',
    `instance_version` int                   DEFAULT NULL COMMENT '流程实例版本',
    `expire_time`      timestamp    NULL     DEFAULT NULL COMMENT '期望完成时间',
    `updator_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '更新人id',
    `updator_name`     varchar(50)  NOT NULL DEFAULT '' COMMENT '上次更新人',
    `update_time`      timestamp    NULL     DEFAULT NULL COMMENT '上次更新时间',
    PRIMARY KEY (`instance_id`) USING BTREE,
    KEY `idx_instance_process_id` (`process_id`) USING BTREE,
    CONSTRAINT `fk_instance_process_id` FOREIGN KEY (`process_id`) REFERENCES `fus_process` (`process_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='流程实例表';

CREATE TABLE `fus_task`
(
    `task_id`        varchar(32)  NOT NULL COMMENT '主键ID',
    `tenant_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '租户ID',
    `creator_id`     varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator_name`   varchar(50)  NOT NULL DEFAULT '' COMMENT '创建人',
    `create_time`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `instance_id`    varchar(32)  NOT NULL DEFAULT '' COMMENT '流程实例ID',
    `parent_task_id` varchar(32)  NOT NULL DEFAULT '' COMMENT '父任务ID',
    `task_name`      varchar(100) NOT NULL DEFAULT '' COMMENT '任务名称',
    `display_name`   varchar(200) NOT NULL DEFAULT '' COMMENT '任务显示名称',
    `task_type`      smallint(1)  NOT NULL COMMENT '任务类型 1.主办 2.转办 3.委派 4.会签',
    `perform_type`   smallint(1)  NOT NULL COMMENT '参与类型: 1-发起、其它, 2-按顺序依次审批, 3-会签, 4-或签, 5-票签, 10-抄送',
    `action_url`     varchar(200) NOT NULL DEFAULT '' COMMENT '任务处理的url',
    `variable`       json                  DEFAULT NULL COMMENT '变量json',
    `assignor_id`    varchar(32)  NOT NULL DEFAULT '' COMMENT '委托人ID',
    `assignor_name`  varchar(100) NOT NULL DEFAULT '' COMMENT '委托人',
    `expire_time`    timestamp    NULL     DEFAULT NULL COMMENT '任务期望完成时间',
    `remind_time`    timestamp    NULL     DEFAULT NULL COMMENT '提醒时间',
    `remind_repeat`  smallint(1)  NOT NULL DEFAULT '0' COMMENT '提醒次数',
    `viewed`         tinyint(1)   NOT NULL DEFAULT '0' COMMENT '已阅 0，否 1，是',
    `finish_time`    timestamp    NULL     DEFAULT NULL COMMENT '完成时间',
    PRIMARY KEY (`task_id`) USING BTREE,
    KEY `idx_task_instance_id` (`instance_id`) USING BTREE,
    CONSTRAINT `fk_task_instance_id` FOREIGN KEY (`instance_id`) REFERENCES `fus_instance` (`instance_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='任务表';

CREATE TABLE `fus_task_actor`
(
    `task_actor_id` varchar(32)  NOT NULL COMMENT '主键 ID',
    `tenant_id`     varchar(32)  NOT NULL DEFAULT '' COMMENT '租户ID',
    `instance_id`   varchar(32)  NOT NULL DEFAULT '' COMMENT '流程实例ID',
    `task_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '任务ID',
    `actor_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '参与者ID',
    `actor_name`    varchar(100) NOT NULL DEFAULT '' COMMENT '参与者名称',
    `actor_type`    int          NOT NULL COMMENT '参与者类型 1，用户 2，角色 3，部门',
    `weight`        int          NULL COMMENT '票签权重',
    PRIMARY KEY (`task_actor_id`) USING BTREE,
    KEY `idx_task_actor_task_id` (`task_id`) USING BTREE,
    CONSTRAINT `fk_task_actor_task_id` FOREIGN KEY (`task_id`) REFERENCES `fus_task` (`task_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='任务参与者表';

CREATE TABLE `fus_history_instance`
(
    `instance_id`      varchar(32)  NOT NULL COMMENT '主键ID',
    `tenant_id`        varchar(32)  NOT NULL DEFAULT '' COMMENT '租户ID',
    `creator_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator_name`     varchar(50)  NOT NULL COMMENT '创建人',
    `create_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `process_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '流程定义ID',
    `priority`         smallint(1)           DEFAULT NULL COMMENT '优先级',
    `instance_no`      varchar(50)  NOT NULL DEFAULT '' COMMENT '流程实例编号',
    `business_key`     varchar(100) NOT NULL DEFAULT '' COMMENT '业务KEY',
    `variable`         json                  DEFAULT NULL COMMENT '变量json',
    `instance_version` int                   DEFAULT NULL COMMENT '流程实例版本',
    `expire_time`      timestamp    NULL     DEFAULT NULL COMMENT '期望完成时间',
    `updator_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '更新人id',
    `updator_name`     varchar(50)  NOT NULL DEFAULT '' COMMENT '上次更新人',
    `update_time`      timestamp    NULL     DEFAULT NULL COMMENT '上次更新时间',
    `instance_state`   smallint(1)  NOT NULL DEFAULT '1' COMMENT '流程实例状态: 1-活动, 2-完成, 3-超时, 4-终止',
    `end_time`         timestamp    NULL     DEFAULT NULL COMMENT '结束时间',
    PRIMARY KEY (`instance_id`) USING BTREE,
    KEY `idx_his_instance_process_id` (`process_id`) USING BTREE,
    CONSTRAINT `fk_his_instance_process_id` FOREIGN KEY (`process_id`) REFERENCES `fus_process` (`process_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='历史流程实例表';

CREATE TABLE `fus_history_task`
(
    `task_id`        varchar(32)  NOT NULL COMMENT '主键ID',
    `tenant_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '租户ID',
    `creator_id`     varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人ID',
    `creator_name`   varchar(50)  NOT NULL COMMENT '创建人',
    `create_time`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `instance_id`    varchar(32)  NOT NULL DEFAULT '' COMMENT '流程实例ID',
    `parent_task_id` varchar(32)  NOT NULL DEFAULT '' COMMENT '父任务ID',
    `task_name`      varchar(100) NOT NULL COMMENT '任务名称',
    `display_name`   varchar(200) NOT NULL COMMENT '任务显示名称',
    `task_type`      smallint(1)  NOT NULL COMMENT '任务类型 1.主办 2.转办 3.委派 4.会签',
    `perform_type`   smallint(1)  NOT NULL COMMENT '参与类型: 1-发起、其它, 2-按顺序依次审批, 3-会签, 4-或签, 5-票签, 10-抄送',
    `action_url`     varchar(200) NOT NULL DEFAULT '' COMMENT '任务处理的url',
    `variable`       text COMMENT '变量json',
    `assignor_id`    varchar(100) NOT NULL DEFAULT '' COMMENT '委托人ID',
    `assignor_name`  varchar(100) NOT NULL DEFAULT '' COMMENT '委托人',
    `expire_time`    timestamp    NULL     DEFAULT NULL COMMENT '任务期望完成时间',
    `remind_time`    timestamp    NULL     DEFAULT NULL COMMENT '提醒时间',
    `remind_repeat`  smallint(1)  NOT NULL DEFAULT '0' COMMENT '提醒次数',
    `viewed`         tinyint(1)   NOT NULL DEFAULT '0' COMMENT '已阅 0，否 1，是',
    `finish_time`    timestamp    NULL     DEFAULT NULL COMMENT '任务完成时间',
    `task_state`     smallint(1)  NOT NULL DEFAULT '1' COMMENT '任务状态 1，活动 2，结束 3，超时 4，终止',
    PRIMARY KEY (`task_id`) USING BTREE,
    KEY `idx_his_task_instance_id` (`instance_id`) USING BTREE,
    KEY `idx_his_task_parent_task_id` (`parent_task_id`) USING BTREE,
    CONSTRAINT `fk_his_task_instance_id` FOREIGN KEY (`instance_id`) REFERENCES `fus_history_instance` (`instance_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='历史任务表';

CREATE TABLE `fus_history_task_actor`
(
    `task_actor_id` varchar(32)  NOT NULL COMMENT '主键 ID',
    `tenant_id`     varchar(32)  NOT NULL DEFAULT '' COMMENT '租户ID',
    `instance_id`   varchar(32)  NOT NULL DEFAULT '' COMMENT '流程实例ID',
    `task_id`       varchar(32)  NOT NULL DEFAULT '' COMMENT '任务ID',
    `actor_id`      varchar(32)  NOT NULL DEFAULT '' COMMENT '参与者ID',
    `actor_name`    varchar(100) NOT NULL COMMENT '参与者名称',
    `actor_type`    int          NOT NULL COMMENT '参与者类型 0，用户 1，角色 2，部门',
    `weight`        int          NULL COMMENT '票签权重',
    PRIMARY KEY (`task_actor_id`) USING BTREE,
    KEY `idx_his_task_actor_task_id` (`task_id`) USING BTREE,
    CONSTRAINT `fk_his_task_actor_task_id` FOREIGN KEY (`task_id`) REFERENCES `fus_history_task` (`task_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='历史任务参与者表';
