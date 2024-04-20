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

import com.tony.fus.model.enums.ApproveSelfMode
import com.tony.fus.model.enums.ApproverType
import com.tony.fus.model.enums.InitiatorAssignMode
import com.tony.fus.model.enums.MultiApproveMode
import com.tony.fus.model.enums.MultiStageManagerMode
import com.tony.fus.model.enums.NodeType

/**
 * 节点.
 * @author tangli
 * @date 2023/10/19 19:08
 * @since 1.0.0
 */
public class FusNode {
    /**
     * 节点名称
     */
    public var nodeName: String = ""

    /**
     * 外部流程定义 key
     */
    public val outProcessKey: String = ""

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
    public val nodeUserList: MutableList<FusNodeAssignee> = mutableListOf()

    /**
     * 审核角色
     */
    public val nodeRoleList: MutableList<FusNodeAssignee> = mutableListOf()

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
     * 允许转交
     */
    public val allowTransfer: Boolean? = null

    /**
     * 允许加签/减签
     */
    public val allowInsertNode: Boolean? = null

    /**
     * 允许回退
     */
    public val allowRollback: Boolean? = null

    /**
     * 审批人与提交人为同一人时, 审批模式
     */
    public val approveSelfMode: ApproveSelfMode? = null

    /**
     * 子节点
     */
    public var childNode: FusNode? = null

    /**
     * 父节点.
     * 模型 json 不存在该属性、属于逻辑节点.
     */
    public var parentNode: FusNode? = null

    /**
     * 条件节点
     */
    public val isConditionNode: Boolean
        get() = NodeType.CONDITIONAL_APPROVE == nodeType || NodeType.CONDITIONAL_BRANCH == nodeType

    /**
     * 抄送节点
     */
    public val isCcNode: Boolean
        get() = NodeType.CC == nodeType

    /**
     * 获取节点
     * @param [nodeName] 节点名称
     * @return [FusNode]?
     * @author tangli
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
     * @author tangli
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
     * @author tangli
     * @date 2023/11/29 19:14
     * @since 1.0.0
     */
    public fun nextNode(): FusNode? =
        childNode ?: nextNode(this)

    public companion object {
        @JvmStatic
        public tailrec fun FusNode.nextNode(node: FusNode): FusNode? {
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

        @JvmStatic
        public fun FusNode?.parentNodeNames(nodeName: String): List<String> =
            mutableListOf<String>()
                .let { nodeNames ->
                    if (this == null) {
                        nodeNames
                    } else {
                        if (!isCcNode) {
                            if (isConditionNode) {
                                nodeNames.addAll(conditionNodeNames(nodeName))
                            } else {
                                nodeNames.add(nodeName)
                            }
                        }
                        nodeNames.addAll(parentNode.parentNodeNames(nodeName))
                        nodeNames
                    }
                }

        @JvmStatic
        private fun FusNode?.conditionNodeNames(nodeName: String): List<String> =
            mutableListOf<String>()
                .let { nodeNames ->
                    if (this == null) {
                        nodeNames
                    } else {
                        conditionNodes.fold(nodeNames) { list, conditionNode ->
                            val conditionNodeChildNode = conditionNode.childNode
                            if (conditionNodeChildNode == null) {
                                list
                            } else {
                                if (conditionNodeChildNode.isConditionNode) {
                                    list.addAll(conditionNodeChildNode.conditionNodeNames(nodeName))
                                    list
                                } else {
                                    // TODO 迷惑的写法, 后续需要优化
                                    val nextConditionNodeNames = conditionNodeChildNode.nextConditionNodeNames()
                                    if (nextConditionNodeNames.contains(nodeName)) {
                                        val legalNodeNames = mutableListOf<String>()
                                        nextConditionNodeNames.forEach { nextConditionNodeName ->
                                            if (nodeName == nextConditionNodeName) {
                                                return@forEach
                                            }
                                            legalNodeNames.add(nextConditionNodeName)
                                        }
                                        nodeNames.addAll(legalNodeNames)
                                    }
                                    list
                                }
                            }
                        }
                    }
                }

        @JvmStatic
        private fun FusNode?.nextConditionNodeNames(): List<String> =
            mutableListOf<String>()
                .let { nodeNames ->
                    if (this == null) {
                        nodeNames
                    } else {
                        if (isConditionNode) {
                            conditionNodes.fold(nodeNames) { list, conditionNode ->
                                list.addAll(conditionNode.childNode.nextConditionNodeNames())
                                list
                            }
                            nodeNames.addAll(childNode.nextConditionNodeNames())
                        } else {
                            if (!isCcNode) {
                                nodeNames.add(nodeName)
                            }
                            childNode?.also { nodeNames.addAll(it.nextConditionNodeNames()) }
                        }
                        nodeNames
                    }
                }

        /**
         * 递归获取所有上一个节点名称
         * @return [List<String>]
         * @author tangli
         * @date 2024/04/19 23:17
         * @since 1.0.0
         */
        @JvmStatic
        public fun FusNode.previousNodeNames(): List<String> =
            parentNode
                .parentNodeNames(nodeName)
                .distinct()

        /**
         * 获取根节点下的所有节点名称【 注意，只对根节点查找有效！】
         * @return [List<String>]
         * @author tangli
         * @date 2024/04/19 23:17
         * @since 1.0.0
         */
        @JvmStatic
        private fun FusNode.rootNodeChildNodeNames(): List<String> =
            mutableListOf<String>()
                .also { list ->
                    if (isConditionNode) {
                        conditionNodes.fold(list) { nodeNameList, conditionNode ->
                            conditionNode
                                .childNode
                                ?.takeIf { childNode != null }
                                ?.also {
                                    nodeNameList.addAll(it.rootNodeChildNodeNames())
                                }
                            nodeNameList
                        }
                        childNode?.also {
                            list.addAll(it.rootNodeChildNodeNames())
                        }
                    } else {
                        list.add(nodeName)
                        childNode?.also { childNode ->
                            list.addAll(childNode.rootNodeChildNodeNames())
                        }
                    }
                }

        /**
         * 是否存在重复节点名称
         * @return [Boolean]
         * @author tangli
         * @date 2024/04/20 10:37
         * @since 1.0.0
         */
        @JvmStatic
        public fun FusNode.hasDuplicateNodeNames(): Boolean {
            rootNodeChildNodeNames().fold(mutableSetOf<String>()) { set, nodeName ->
                if (!set.add(nodeName)) {
                    return true
                }
                set
            }
            return false
        }

        /**
         * 检查条件节点
         * @return [Int] 0，合法情况 1，存在多个条件表达式为空 2，存在多个子节点为空
         * @author tangli
         * @date 2024/04/20 10:48
         * @since 1.0.0
         */
        @JvmStatic
        public fun FusNode?.checkConditionNode(): Int {
            if (this == null) {
                return 0
            }
            if (conditionNodes.isEmpty()) {
                return childNode.checkConditionNode()
            }

            for (conditionNode in conditionNodes) {
                if (conditionNode.expressionList.isEmpty()) {
                    return 1
                }
                if (conditionNode.childNode == null) {
                    return 2
                }

                return conditionNode.childNode.checkConditionNode()
            }

            return 0
        }
    }
}
