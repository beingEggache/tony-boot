package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.FusContext
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.handler.impl.EndProcessHandler
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.FusProcessModel
import com.tony.fus.model.enums.NodeType
import java.time.LocalDateTime

/**
 * 流程定义表
 * @author Tang Li
 * @date 2023/09/29 16:13
 * @since 1.0.0
 */
@TableName
public class FusProcess {
    /**
     * 主键ID
     */
    @TableId
    public var processId: String = ""

    /**
     * 租户ID
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var tenantId: String = ""

    /**
     * 创建人ID
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var creatorId: String = ""

    /**
     * 创建人
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var creatorName: String = ""

    /**
     * 创建时间
     */
    @OrderBy
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    public val createTime: LocalDateTime = LocalDateTime.now()

    /**
     * 流程名称
     */
    public var processName: String = ""

    /**
     * 流程定义 key 唯一标识
     */
    public var processKey: String = ""

    /**
     * 流程类型
     */
    public var processType: String = ""

    /**
     * 流程版本，默认 1
     */
    public var processVersion: Int = 0

    /**
     * 流程状态: 0.不可用, 1.可用
     */
    public var enabled: Boolean = true

    /**
     * 流程模型定义JSON内容
     */
    public var modelContent: String = "{}"

    /**
     * 排序
     */
    public var sort: Int = 0

    public fun model(): FusProcessModel =
        FusContext.parse(modelContent, processId, false)

    private tailrec fun nextNode(node: FusNode): FusNode? {
        val parentNode = node.parentNode
        if (parentNode == null || parentNode.nodeType == NodeType.INITIATOR) {
            return null
        }
        if (parentNode.isConditionNode && parentNode
                .childNode
                ?.nodeName != node.nodeName
        ) {
            return parentNode.childNode
        }
        return nextNode(parentNode)
    }

    public fun execute(
        context: FusContext,
        execution: FusExecution,
        nodeName: String?,
    ) {
        model().also {
            val node =
                it
                    .getNode(nodeName)
                    .fusThrowIfNull("流程模型中未发现，流程节点:$nodeName")

            val executeNode =
                node.childNode
                    ?: nextNode(node)
            if (executeNode == null) {
                EndProcessHandler.handle(context, execution)
                return
            }

            executeNode.execute(context, execution)
            if (executeNode.childNode == null &&
                executeNode
                    .conditionNodes
                    .isEmpty()
            ) {
                val nextNode = nextNode(executeNode)
                if (nextNode == null && executeNode.nodeType != NodeType.APPROVER) {
                    EndProcessHandler.handle(context, execution)
                }
            }
        }
    }

    public fun executeStart(
        context: FusContext,
        execution: FusExecution,
    ) {
        model().also {
            it
                .node
                .fusThrowIfNull("流程定义[processName=$processName, processVersion=$processVersion]没有开始节点")
                .createTask(context, execution)
        }
    }
}
