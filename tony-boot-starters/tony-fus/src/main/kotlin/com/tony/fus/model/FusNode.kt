/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.fus.model

import com.tony.fus.FusContext
import com.tony.fus.extension.fusThrowIfEmpty
import com.tony.fus.extension.fusThrowIfNull
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
 * @date 2023/10/19 19:08
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

                (conditionNode.childNode ?: childNode)?.execute(context, execution)
            }
        if (nodeType == NodeType.CC || nodeType == NodeType.APPROVER) {
            context.createTask(execution, this)
        }

        if (childNode == null &&
            conditionNodes.isEmpty() &&
            nextNode() == null &&
            nodeType != NodeType.APPROVER
        ) {
            execution.endInstance()
        }
    }

    /**
     * 获取节点
     * @param [nodeName] 节点名称
     * @return [FusNode]?
     * @author Tang Li
     * @date 2023/10/25 19:36
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
     * @return [FusNode]?
     * @author Tang Li
     * @date 2023/10/25 19:36
     * @since 1.0.0
     */
    public fun getNodeFromConditionNode(nodeName: String?): FusNode? =
        conditionNodes.firstNotNullOfOrNull {
            it
                .childNode
                ?.getNode(nodeName)
        }

    /**
     * 下一个节点
     * @return [FusNode]?
     * @author Tang Li
     * @date 2023/11/29 19:14
     * @since 1.0.0
     */
    public fun nextNode(): FusNode? =
        childNode ?: nextNode(this)

    public companion object {
        public tailrec fun nextNode(node: FusNode): FusNode? {
            val parentNode = node.parentNode
            if (parentNode == null || parentNode.nodeType == NodeType.INITIATOR) {
                return null
            }
            if (parentNode.isConditionNode &&
                parentNode
                    .childNode
                    ?.nodeName != node.nodeName
            ) {
                return parentNode.childNode
            }
            return nextNode(parentNode)
        }
    }
}
