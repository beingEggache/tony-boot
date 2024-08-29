/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.handler

import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.model.ConditionNode
import com.aizuda.bpm.engine.model.NodeModel

/**
 * 流程执行条件节点处理器
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface ConditionNodeHandler {
    /**
     *
     *
     * 获取满足条件的条件节点
     *
     *
     *
     * 子类需要实现的方法，来处理具体的操作
     *
     *
     * @param flowLongContext 流程引擎上下文
     * @param execution       执行对象
     * @param nodeModel       节点模型
     * @return true 成功 false 失败
     */
    public fun getConditionNode(
        flowLongContext: FlowLongContext,
        execution: Execution,
        nodeModel: NodeModel,
    ): ConditionNode

    /**
     *
     *
     * 获取满足条件的所有包容分支节点
     *
     *
     *
     * 子类需要实现的方法，来处理具体的操作
     *
     *
     * @param flowLongContext 流程引擎上下文
     * @param execution       执行对象
     * @param nodeModel       节点模型
     * @return true 成功 false 失败
     */
    public fun getInclusiveNodes(
        flowLongContext: FlowLongContext,
        execution: Execution,
        nodeModel: NodeModel,
    ): List<ConditionNode?>
}
