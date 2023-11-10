package com.tony.flow.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.flow.FlowContext
import com.tony.flow.db.enums.ProcessState
import com.tony.flow.extension.flowThrowIfNull
import com.tony.flow.handler.impl.EndProcessHandler
import com.tony.flow.model.FlowExecution
import com.tony.flow.model.FlowNode
import com.tony.flow.model.FlowProcessModel
import com.tony.flow.model.enums.NodeType
import java.time.LocalDateTime

/**
 * 流程定义表
 * @author Tang Li
 * @date 2023/09/29 16:13
 * @since 1.0.0
 */
@TableName(value = "flow_process")
public class FlowProcess {
    /**
     * 主键ID
     */
    @TableId(value = "process_id")
    public var processId: String? = null

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id")
    public var tenantId: String? = null

    /**
     * 创建人ID
     */
    @TableField(value = "creator_id")
    public var creatorId: String? = null

    /**
     * 创建人
     */
    @TableField(value = "creator_name")
    public var creatorName: String? = null

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    public var createTime: LocalDateTime? = null

    /**
     * 流程名称
     */
    @TableField(value = "process_name")
    public var processName: String? = null

    /**
     * 流程显示名称
     */
    @TableField(value = "display_name")
    public var displayName: String? = null

    /**
     * 流程图标地址
     */
    @TableField(value = "process_icon")
    public var processIcon: String? = null

    /**
     * 流程类型
     */
    @TableField(value = "process_type")
    public var processType: String? = null

    /**
     * 流程版本，默认 1
     */
    @TableField(value = "process_version")
    public var processVersion: Int? = null

    /**
     * 实例地址
     */
    @TableField(value = "instance_url")
    public var instanceUrl: String? = null

    /**
     * 使用范围 0，全员 1，指定人员（业务关联） 2，均不可提交
     */
    @TableField(value = "use_scope")
    public var useScope: Int? = null

    /**
     * 流程状态 0，不可用 1，可用
     */
    @TableField(value = "process_state")
    public var processState: ProcessState? = null

    /**
     * 流程模型定义JSON内容
     */
    @TableField(value = "model_content")
    public var modelContent: String? = null

    /**
     * 排序
     */
    @TableField(value = "sort")
    public var sort: Int? = null

    public val model: FlowProcessModel?
        get() {
            val content = modelContent ?: return null
            return FlowContext.parse(content, processId, false)
        }

    public tailrec fun nextNode(flowNode: FlowNode): FlowNode? {
        val parentNode = flowNode.parentNode
        if (parentNode == null || parentNode.nodeType == NodeType.INITIATOR) {
            return null
        }
        if (parentNode.isConditionNode && parentNode
                .childNode
                ?.nodeName != flowNode.nodeName
        ) {
            return parentNode.childNode
        }
        return nextNode(parentNode)
    }

    public fun execute(
        flowContext: FlowContext,
        flowExecution: FlowExecution?,
        nodeName: String?,
    ) {
        model?.also {
            val flowNode =
                it
                    .getNode(nodeName)
                    .flowThrowIfNull("流程模型中未发现，流程节点:$nodeName")

            val executeNode =
                flowNode.childNode
                    ?: nextNode(flowNode)
            if (executeNode == null) {
                EndProcessHandler.handle(flowContext, flowExecution)
                return
            }

            executeNode.execute(flowContext, flowExecution)
            if (executeNode.childNode == null &&
                executeNode
                    .conditionNodes
                    .isEmpty()
            ) {
                val nextNode = nextNode(executeNode)
                if (nextNode == null && executeNode.nodeType != NodeType.APPROVER) {
                    EndProcessHandler.handle(flowContext, flowExecution)
                }
            }
        }
    }

    public fun executeStart(
        flowContext: FlowContext,
        flowExecution: FlowExecution,
    ) {
        model?.also {
            it
                .flowNode
                .flowThrowIfNull("流程定义[processName=$processName, processVersion=$processVersion]没有开始节点")
                .createTask(flowContext, flowExecution)
        }
    }
}
