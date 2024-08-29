/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

/**
 * JSON BPM 常量类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public data object FlowConstants {
    /**
     * 流程定义缓存 KEY
     */
    public const val PROCESS_CACHE_KEY: String = "flwProcessModel#"

    /**
     * 流程实例缓存KEY
     */
    public const val PROCESS_INSTANCE_CACHE_KEY: String = "flwProcessInstanceModel#"

    /**
     * 流程节点动态分配节点处理人或角色
     */
    public const val PROCESS_DYNAMIC_ASSIGNEE: String = "flwProcessDynamicAssignee"

    /**
     * 流程指定条件节点 KEY
     */
    public const val PROCESS_SPECIFY_CONDITION_NODE_KEY: String = "flwProcessSpecifyConditionNodeKey"
}
