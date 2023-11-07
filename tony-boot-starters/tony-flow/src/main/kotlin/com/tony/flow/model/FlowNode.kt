package com.tony.flow.model

import com.tony.flow.FlowContext
import com.tony.flow.extension.flowThrowIf
import com.tony.flow.extension.flowThrowIfNull
import com.tony.flow.handler.impl.CreateTaskHandler
import com.tony.flow.model.enums.ApproverType
import com.tony.flow.model.enums.InitiatorAssignMode
import com.tony.flow.model.enums.MultiApproveMode
import com.tony.flow.model.enums.MultiStageManagerMode
import com.tony.flow.model.enums.NodeType
import com.tony.utils.getLogger

/**
 * 节点.
 * @author tangli
 * @date 2023/10/19 10:08
 * @since 1.0.0
 */
public class FlowNode : FlowModel {
    private val logger = getLogger()

    /**
     * 节点名称
     */
    public var nodeName: String? = null

    /**
     * 节点类型
     */
    public var nodeType: NodeType? = null

    /**
     * 审核人类型
     */
    public var approverType: ApproverType? = null

    /**
     * 审核人成员
     */
    public var nodeUserList: MutableList<FlowNodeAssignee> = mutableListOf()

    /**
     * 审核角色
     */
    public var nodeRoleList: MutableList<FlowNodeAssignee> = mutableListOf()

    /**
     * 指定主管层级
     */
    public var managerLevel: Int? = null

    /**
     * 自定义连续主管审批层级
     */
    public var multistageManagerLevel: Int? = null

    /**
     * 发起人自选类型
     */
    public var initiatorAssignMode: InitiatorAssignMode? = null

    /**
     * 审批期限
     */
    public var expiresIn: Int? = null

    /**
     * 审批期限超时自动审批
     */
    public var autoWhenExpired: Boolean? = null

    /**
     * 过审批期限超时后执行类型
     */
    public var autoWhenExpiredExecuteMode: Int? = null

    /**
     * 多人审批时审批方式
     */
    public var multiApproveMode: MultiApproveMode? = null

    /**
     * 连续主管审批方式
     */
    public var multiStageManagerMode: MultiStageManagerMode? = null

    /**
     * 通过权重（ 所有分配任务权重之和大于该值即通过，默认 50 ）
     */
    public var passWeight: Int = 50

    /**
     * 条件节点
     */
    public var conditionNodes: MutableList<FlowConditionNode> = mutableListOf()

    /**
     * 允许发起人自选抄送人
     */
    public var allowInitiatorAssignCc: Boolean? = null

    /**
     * 子节点
     */
    public var childNode: FlowNode? = null

    /**
     * 父节点.
     * 模型 json 不存在该属性、属于逻辑节点.
     */
    public var parentNode: FlowNode? = null

    public val isConditionNode: Boolean
        get() = NodeType.CONDITIONAL_APPROVE == nodeType || NodeType.CONDITIONAL_BRANCH == nodeType

    override fun execute(
        flowContext: FlowContext,
        flowExecution: FlowExecution,
    ) {
        if (conditionNodes.isNotEmpty()) {
            val args = flowExecution.variable
            flowThrowIf(args.isEmpty(), "Execution parameter cannot be empty")
            val conditionNode =
                conditionNodes
                    .sortedBy { it.priority }
                    .firstOrNull {
                        val expr = it.expression
                        if (expr.isNullOrEmpty()) {
                            true
                        } else {
                            try {
                                flowContext
                                    .expression
                                    .eval(Boolean::class.java, expr, args) == true
                            } catch (e: Exception) {
                                logger.error(e.message, e)
                                false
                            }
                        }
                    }.flowThrowIfNull("Not found executable ConditionNode")
            createTask(conditionNode.childNode, flowContext, flowExecution)
        }
        if (nodeType == NodeType.CC || nodeType == NodeType.APPROVER) {
            createTask(flowContext, flowExecution)
        }
    }

    /**
     * 创建任务
     * @param [flowContext] 流上下文
     * @param [flowExecution] 流程执行
     * @author Tang Li
     * @date 2023/10/25 11:37
     * @since 1.0.0
     */
    public fun createTask(
        flowContext: FlowContext,
        flowExecution: FlowExecution,
    ) {
        createTask(this, flowContext, flowExecution)
    }

    /**
     * 创建任务
     * @param [flowNode] 流量节点
     * @param [flowContext] 流上下文
     * @param [flowExecution] 流程执行
     * @author Tang Li
     * @date 2023/10/25 11:37
     * @since 1.0.0
     */
    public fun createTask(
        flowNode: FlowNode?,
        flowContext: FlowContext,
        flowExecution: FlowExecution,
    ) {
        CreateTaskHandler(flowNode).handle(flowContext, flowExecution)
    }

    /**
     * 获取节点
     * @param [nodeName] 节点名称
     * @return [FlowNode?]
     * @author Tang Li
     * @date 2023/10/25 11:36
     * @since 1.0.0
     */
    public fun getNode(nodeName: String): FlowNode? {
        if (this.nodeName == nodeName) {
            return this
        }
        return getNodeFromConditionNode(nodeName) ?: childNode?.getNode(nodeName)
    }

    /**
     * 从条件节点获取节点
     * @param [nodeName] 节点名称
     * @return [FlowNode?]
     * @author Tang Li
     * @date 2023/10/25 11:36
     * @since 1.0.0
     */
    public fun getNodeFromConditionNode(nodeName: String): FlowNode? =
        conditionNodes.firstNotNullOfOrNull {
            it
                .childNode
                ?.getNode(nodeName)
        }
}
