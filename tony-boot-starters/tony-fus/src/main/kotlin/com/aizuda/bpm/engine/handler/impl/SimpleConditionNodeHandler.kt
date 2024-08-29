/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.handler.impl

import com.aizuda.bpm.engine.FlowConstants
import com.aizuda.bpm.engine.FlowDataTransfer.get
import com.aizuda.bpm.engine.assist.Assert.illegal
import com.aizuda.bpm.engine.assist.Assert.isFalse
import com.aizuda.bpm.engine.assist.ObjectUtils.isEmpty
import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.handler.ConditionNodeHandler
import com.aizuda.bpm.engine.model.ConditionNode
import com.aizuda.bpm.engine.model.NodeModel

/**
 * 默认流程执行条件处理器
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class SimpleConditionNodeHandler : ConditionNodeHandler {
    override fun getConditionNode(
        flowLongContext: FlowLongContext,
        execution: Execution,
        nodeModel: NodeModel,
    ): ConditionNode {
        val conditionNodes = nodeModel.conditionNodes

        // 根据指定条件节点选择
        val conditionNodeKey = get<String>(FlowConstants.PROCESS_SPECIFY_CONDITION_NODE_KEY)
        if (null != conditionNodeKey) {
            val conditionNodeKeyOptional =
                conditionNodes?.firstOrNull {
                    it.nodeKey == conditionNodeKey
                }
            if (conditionNodeKeyOptional != null) {
                return conditionNodeKeyOptional
            }
        }

        // 根据正则条件节点选择
        val args = this.getArgs(flowLongContext, execution, nodeModel)
        val expression = flowLongContext.checkExpression()
        val conditionNodeOptional =
            conditionNodes
                ?.sortedBy { it.priorityLevel }
                ?.firstOrNull { expression.eval(it.conditionList!!, args!!) }
        if (conditionNodeOptional != null) {
            return conditionNodeOptional
        }

        // 未发现满足条件分支，使用无条件分支
        return defaultConditionNode(conditionNodes)
    }

    public fun getArgs(
        flowLongContext: FlowLongContext?,
        execution: Execution,
        nodeModel: NodeModel?,
    ): MutableMap<String?, Any?>? {
        val args = execution.args
        illegal(isEmpty(args), "Execution parameter cannot be empty")
        return args
    }

    public fun defaultConditionNode(conditionNodes: List<ConditionNode>?): ConditionNode {
        val cnOpt =
            conditionNodes?.firstOrNull {
                it.conditionList.isNullOrEmpty()
            }
        isFalse(cnOpt != null, "Not found executable ConditionNode")
        return cnOpt!!
    }

    override fun getInclusiveNodes(
        flowLongContext: FlowLongContext,
        execution: Execution,
        nodeModel: NodeModel,
    ): List<ConditionNode> {
        val inclusiveNodes = nodeModel.inclusiveNodes
        // 根据正则条件节点选择
        val expression = flowLongContext.checkExpression()
        val args = this.getArgs(flowLongContext, execution, nodeModel)
        return inclusiveNodes
            .orEmpty()
            .filter {
                expression.eval(it.conditionList!!, args!!)
            }.ifEmpty {
                listOf(defaultConditionNode(inclusiveNodes))
            }
    }

    public companion object {
        private var conditionNodeHandler: SimpleConditionNodeHandler? = null

        public val instance: SimpleConditionNodeHandler
            get() {
                if (null == conditionNodeHandler) {
                    synchronized(SimpleConditionNodeHandler::class.java) {
                        conditionNodeHandler = SimpleConditionNodeHandler()
                    }
                }
                return conditionNodeHandler!!
            }
    }
}
