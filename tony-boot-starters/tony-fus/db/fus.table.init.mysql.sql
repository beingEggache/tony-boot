DROP TABLE IF EXISTS `fus_process`;
CREATE TABLE `fus_process`
(
    `process_id`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程定义id',
    `process_key`     varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程定义Key, 唯一标识',
    `process_name`    varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程定义名称',
    `process_version` int                                     NOT NULL DEFAULT 1 COMMENT '流程版本，默认 1',
    `model_content`   json                                    NOT NULL DEFAULT ('{}') COMMENT '流程模型定义JSON内容',
    `remark`          varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
    `create_time`     timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`      varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`    varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `enabled`         tinyint                                 NOT NULL DEFAULT '1' COMMENT '状态: 1-启用，0-禁用',
    `tenant_id`       varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`process_id`) USING BTREE,
    INDEX `idx_process_name` (`process_name` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic COMMENT = '流程定义';

DROP TABLE IF EXISTS `fus_history_instance`;
CREATE TABLE `fus_history_instance`
(
    `instance_id`        varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程实例id',
    `process_id`         varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程定义id',
    `parent_instance_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '父流程实例id',
    `business_key`       varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务KEY',
    `priority`           tinyint(1)                              NOT NULL DEFAULT 1 COMMENT '优先级',
    `variable`           json                                    NOT NULL DEFAULT ('{}') COMMENT '变量json',
    `node_name`          varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前节点名称',
    `node_key`           varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前节点key',
    `expired_time`       timestamp                               NULL     DEFAULT NULL COMMENT '期望完成时间, 过期时间',
    `end_time`           timestamp                               NULL     DEFAULT NULL COMMENT '结束时间',
    `duration`           bigint                                  NULL     DEFAULT NULL COMMENT '处理耗时',
    `instance_state`     tinyint(1)                              NOT NULL DEFAULT 1 COMMENT '状态: 1-审批中 2-审批通过 3-审批拒绝 4-撤销审批 5-超时结束 6-强制终止',
    `create_time`        timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`         varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`       varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `updator_id`         varchar(32)                             NOT NULL DEFAULT '' COMMENT '更新人id',
    `updator_name`       varchar(50)                             NOT NULL DEFAULT '' COMMENT '上次更新人',
    `update_time`        timestamp                               NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `tenant_id`          varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`instance_id`) USING BTREE,
    INDEX `idx_history_instance_process_id` (`process_id` ASC) USING BTREE,
    CONSTRAINT `fk_history_instance_process_id` FOREIGN KEY (`process_id`) REFERENCES `fus_process` (`process_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic COMMENT = '历史流程实例';

DROP TABLE IF EXISTS `fus_history_task`;
CREATE TABLE `fus_history_task`
(
    `task_id`           varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '任务ID',
    `instance_id`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程实例id',
    `parent_task_id`    varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '父任务ID',
    `outer_process_id`  varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '外部流程定义ID',
    `outer_instance_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '外部流程实例ID',
    `task_name`         varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
    `task_key`          varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务key唯一标识',
    `task_type`         tinyint(1)                              NOT NULL COMMENT '任务类型: -1-结束, 1-主办, 2-审批, 3-抄送, 4-条件审批, 5-条件分支, 6-子流程, 7-定时器, 8-触发器, 9-并行分支, 10-包容分支, 11-转办, 12-委派, 13-委派归还, 14-代理, 15-代理归还, 16-代理协办, 17-代理完成',
    `perform_type`      tinyint(1)                              NOT NULL COMMENT '参与类型: 1-发起, 2-顺序依次审批, 3-会签, 4-或签, 5-票签, 6-定时器, 7-触发器, 8-抄送',
    `variable`          json                                    NOT NULL DEFAULT ('{}') COMMENT '变量json',
    `assignor_id`       json COMMENT '委托人ID',
    `assignor_name`     json COMMENT '委托人姓名',
    `expired_time`      timestamp                               NULL     DEFAULT NULL COMMENT '期望完成时间, 过期时间',
    `remind_time`       timestamp                               NULL     DEFAULT NULL COMMENT '提醒时间',
    `remind_repeat`     tinyint(1)                              NOT NULL DEFAULT 0 COMMENT '提醒次数',
    `viewed`            tinyint(1)                              NOT NULL DEFAULT 0 COMMENT '已阅 0，否 1，是',
    `finished_time`     timestamp                               NULL     DEFAULT NULL COMMENT '任务完成时间',
    `duration`          bigint                                  NULL     DEFAULT NULL COMMENT '处理耗时',
    `task_state`        tinyint(1)                              NOT NULL DEFAULT 1 COMMENT '任务状态: 1-活动, 2-跳转, 3-完成, 4-拒绝, 5-撤销, 6-超时, 7-终止, 8-驳回终止.',
    `create_time`       timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`        varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`      varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `tenant_id`         varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`task_id`) USING BTREE,
    INDEX `idx_history_task_instance_id` (`instance_id` ASC) USING BTREE,
    INDEX `idx_history_task_parent_task_id` (`parent_task_id` ASC) USING BTREE,
    CONSTRAINT `fk_history_task_instance_id` FOREIGN KEY (`instance_id`) REFERENCES `fus_history_instance` (`instance_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic COMMENT = '历史任务';

DROP TABLE IF EXISTS `fus_history_task_actor`;
CREATE TABLE `fus_history_task_actor`
(
    `task_actor_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `instance_id`   varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程实例id',
    `task_id`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '任务ID',
    `actor_id`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '参与者ID',
    `actor_name`    varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '参与者名称',
    `actor_type`    tinyint(1)                              NOT NULL COMMENT '参与者类型: 0-用户, 1-角色, 2-部门',
    `weight`        int                                     NULL     DEFAULT NULL COMMENT '权重，票签任务时，该值为不同处理人员的分量比例，代理任务时，该值为 1 时为代理人',
    `agent_id`      varchar(50)                             NULL     DEFAULT NULL COMMENT '代理人ID',
    `agent_type`    int                                     NULL     DEFAULT NULL COMMENT '代理人类型: 0-代理, 1-被代理, 2-认领角色, 3-认领部门',
    `extend`        json                                    NOT NULL DEFAULT ('{}') COMMENT '扩展json',
    `tenant_id`     varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`task_actor_id`) USING BTREE,
    INDEX `idx_history_task_actor_task_id` (`task_id` ASC) USING BTREE,
    CONSTRAINT `fk_history_task_actor_task_id` FOREIGN KEY (`task_id`) REFERENCES `fus_history_task` (`task_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic COMMENT = '历史任务参与者';

DROP TABLE IF EXISTS `fus_instance`;
CREATE TABLE `fus_instance`
(
    `instance_id`        varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程实例id',
    `process_id`         varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程定义id',
    `parent_instance_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '父流程实例id',
    `business_key`       varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务KEY',
    `priority`           tinyint(1)                              NOT NULL DEFAULT 1 COMMENT '优先级',
    `variable`           json                                    NOT NULL DEFAULT ('{}') COMMENT '变量json',
    `node_name`          varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前节点名称',
    `node_key`           varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前节点key',
    `expired_time`       timestamp                               NULL     DEFAULT NULL COMMENT '期望完成时间, 过期时间',
    `create_time`        timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`         varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`       varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `updator_id`         varchar(32)                             NOT NULL DEFAULT '' COMMENT '更新人id',
    `updator_name`       varchar(50)                             NOT NULL DEFAULT '' COMMENT '上次更新人',
    `update_time`        timestamp                               NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `tenant_id`          varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`instance_id`) USING BTREE,
    INDEX `idx_instance_process_id` (`process_id` ASC) USING BTREE,
    CONSTRAINT `fk_instance_process_id` FOREIGN KEY (`process_id`) REFERENCES `fus_process` (`process_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic COMMENT = '流程实例';

DROP TABLE IF EXISTS `fus_task`;
CREATE TABLE `fus_task`
(
    `task_id`        varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '任务ID',
    `instance_id`    varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程实例id',
    `parent_task_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '父任务ID',
    `task_name`      varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
    `task_key`       varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务key唯一标识',
    `task_type`      tinyint(1)                              NOT NULL COMMENT '任务类型: -1-结束, 1-主办, 2-审批, 3-抄送, 4-条件审批, 5-条件分支, 6-调用外部流程任务, 7-定时器任务, 8-触发器任务, 9-并行分支, 10-包容分支, 11-转办, 12-委派, 13-委派归还, 14-代理, 15-代理归还, 16-代理协办, 17-代理完成',
    `perform_type`   tinyint(1)                              NOT NULL COMMENT '参与类型: 1-发起, 2-顺序依次审批, 3-会签, 4-或签, 5-票签, 6-定时器, 7-触发器, 8-抄送',
    `variable`       json                                    NOT NULL DEFAULT ('{}') COMMENT '变量json',
    `assignor_id`    json COMMENT '委托人ID',
    `assignor_name`  json COMMENT '委托人姓名',
    `expired_time`   timestamp                               NULL     DEFAULT NULL COMMENT '期望完成时间, 过期时间',
    `remind_time`    timestamp                               NULL     DEFAULT NULL COMMENT '提醒时间',
    `remind_repeat`  tinyint(1)                              NOT NULL DEFAULT 0 COMMENT '提醒次数',
    `viewed`         tinyint(1)                              NOT NULL DEFAULT 0 COMMENT '已阅 0，否 1，是',
    `create_time`    timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator_id`     varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人',
    `creator_name`   varchar(30) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建人名称',
    `tenant_id`      varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`task_id`) USING BTREE,
    INDEX `idx_task_instance_id` (`instance_id` ASC) USING BTREE,
    CONSTRAINT `fk_task_instance_id` FOREIGN KEY (`instance_id`) REFERENCES `fus_instance` (`instance_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic COMMENT = '任务';

DROP TABLE IF EXISTS `fus_task_actor`;
CREATE TABLE `fus_task_actor`
(
    `task_actor_id` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `instance_id`   varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '流程实例id',
    `task_id`       varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '任务ID',
    `actor_id`      varchar(50) COLLATE utf8mb4_general_ci  NOT NULL COMMENT '参与者ID',
    `actor_name`    varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '参与者名称',
    `actor_type`    tinyint(1)                              NOT NULL COMMENT '参与者类型: 1-用户, 2-角色, 3-部门',
    `weight`        int                                     NULL     DEFAULT NULL COMMENT '权重，票签任务时，该值为不同处理人员的分量比例，代理任务时，该值为 1 时为代理人',
    `agent_id`      varchar(50)                             NULL     DEFAULT NULL COMMENT '代理人ID',
    `agent_type`    int                                     NULL     DEFAULT NULL COMMENT '代理人类型: 1-代理人, 2-被代理人, 3-认领角色, 4-认领部门',
    `extend`        json                                    NOT NULL DEFAULT ('{}') COMMENT '扩展json',
    `tenant_id`     varchar(32) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '租户id',
    PRIMARY KEY (`task_actor_id`) USING BTREE,
    INDEX `idx_task_actor_task_id` (`task_id` ASC) USING BTREE,
    CONSTRAINT `fk_task_actor_task_id` FOREIGN KEY (`task_id`) REFERENCES `fus_task` (`task_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic COMMENT = '任务参与者';

DROP TABLE IF EXISTS `fus_ext_instance`;
CREATE TABLE `fus_ext_instance`
(
    `instance_id`   varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
    `process_id`    varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程定义id',
    `model_content` json                                   NOT NULL DEFAULT ('{}') COMMENT '流程模型定义JSON内容',
    PRIMARY KEY (`instance_id`) USING BTREE,
    CONSTRAINT `fk_ext_instance_id` FOREIGN KEY (`instance_id`) REFERENCES `fus_history_instance` (`instance_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic COMMENT = '任务';
