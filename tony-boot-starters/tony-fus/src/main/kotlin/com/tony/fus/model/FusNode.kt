package com.tony.fus.model

import com.tony.fus.FusContext
import com.tony.fus.extension.fusThrowIfEmpty
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.handler.impl.CreateTaskHandler
import com.tony.fus.model.enums.ApproverType
import com.tony.fus.model.enums.InitiatorAssignMode
import com.tony.fus.model.enums.MultiApproveMode
import com.tony.fus.model.enums.MultiStageManagerMode
import com.tony.fus.model.enums.NodeType
import com.tony.utils.applyIf
import com.tony.utils.ifNull

/**
 * 节点.
 * @author tangli
 * @date 2023/10/19 10:08
 * @since 1.0.0
 */
public class FusNode : FusModel {
    /**
     * 节点名称
     */
    public val nodeName: String = ""

    /**
     * 节点类型
     */
    public val nodeType: NodeType? = null

    /**
     * 审核人类型
     */
    public val approverType: ApproverType? = null

    /**
     * 审核人成员
     */
    public val nodeUserList: List<FusNodeAssignee> = emptyList()

    /**
     * 审核角色
     */
    public val nodeRoleList: List<FusNodeAssignee> = emptyList()

    /**
     * 指定主管层级
     */
    public val managerLevel: Int? = null

    /**
     * 自定义连续主管审批层级
     */
    public val multistageManagerLevel: Int? = null

    /**
     * 发起人自选类型
     */
    public val initiatorAssignMode: InitiatorAssignMode? = null

    /**
     * 审批期限
     */
    public val expiresIn: Int? = null

    /**
     * 审批期限超时自动审批
     */
    public val autoWhenExpired: Boolean? = null

    /**
     * 过审批期限超时后执行类型
     */
    public val autoWhenExpiredExecuteMode: Int? = null

    /**
     * 多人审批时审批方式
     */
    public val multiApproveMode: MultiApproveMode? = null

    /**
     * 连续主管审批方式
     */
    public val multiStageManagerMode: MultiStageManagerMode? = null

    /**
     * 通过权重（ 所有分配任务权重之和大于该值即通过，默认 50 ）
     */
    public val passWeight: Int = 50

    /**
     * 条件节点
     */
    public val conditionNodes: List<FusConditionNode> = emptyList()

    /**
     * 允许发起人自选抄送人
     */
    public val allowInitiatorAssignCc: Boolean? = null

    /**
     * 子节点
     */
    public val childNode: FusNode? = null

    /**
     * 父节点.
     * 模型 json 不存在该属性、属于逻辑节点.
     */
    public var parentNode: FusNode? = null

    public val isConditionNode: Boolean
        get() = NodeType.CONDITIONAL_APPROVE == nodeType || NodeType.CONDITIONAL_BRANCH == nodeType

    override fun execute(
        context: FusContext,
        execution: FusExecution,
    ) {
        conditionNodes
            .applyIf(conditionNodes.isNotEmpty()) {
                val conditionNode =
                    conditionNodes
                        .sortedBy { it.priority }
                        .firstOrNull {
                            context
                                .expressionEvaluator
                                .eval(
                                    it.expressionList,
                                    execution
                                        .variable
                                        .fusThrowIfEmpty("Execution parameter cannot be empty")
                                )
                        }.ifNull {
                            conditionNodes.firstOrNull {
                                it.expressionList.isEmpty()
                            }
                        }.fusThrowIfNull("Not found executable ConditionNode")
                createTask(conditionNode.childNode, context, execution)
            }
        if (nodeType == NodeType.CC || nodeType == NodeType.APPROVER) {
            createTask(context, execution)
        }
    }

    /**
     * 创建任务
     * @param [context] 流上下文
     * @param [execution] 流程执行
     * @author Tang Li
     * @date 2023/10/25 11:37
     * @since 1.0.0
     */
    public fun createTask(
        context: FusContext,
        execution: FusExecution,
    ) {
        createTask(this, context, execution)
    }

    /**
     * 创建任务
     * @param [node] 流量节点
     * @param [context] 流上下文
     * @param [execution] 流程执行
     * @author Tang Li
     * @date 2023/10/25 11:37
     * @since 1.0.0
     */
    public fun createTask(
        node: FusNode?,
        context: FusContext,
        execution: FusExecution,
    ) {
        CreateTaskHandler(node).handle(context, execution)
    }

    /**
     * 获取节点
     * @param [nodeName] 节点名称
     * @return [FusNode?]
     * @author Tang Li
     * @date 2023/10/25 11:36
     * @since 1.0.0
     */
    public fun getNode(nodeName: String?): FusNode? {
        if (this.nodeName == nodeName) {
            return this
        }
        return getNodeFromConditionNode(nodeName) ?: childNode?.getNode(nodeName)
    }

    /**
     * 从条件节点获取节点
     * @param [nodeName] 节点名称
     * @return [FusNode?]
     * @author Tang Li
     * @date 2023/10/25 11:36
     * @since 1.0.0
     */
    public fun getNodeFromConditionNode(nodeName: String?): FusNode? =
        conditionNodes.firstNotNullOfOrNull {
            it
                .childNode
                ?.getNode(nodeName)
        }
}
