/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.model

import com.aizuda.bpm.engine.assist.ObjectUtils
import com.aizuda.bpm.engine.core.enums.NodeSetType
import com.aizuda.bpm.engine.core.enums.TaskType
import java.util.Collections

/**
 * 流程模型辅助类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public object ModelHelper {
    /**
     * 递归查找下一个执行节点
     *
     * @param nodeModel   当前节点
     * @param currentTask 当前任务列表
     * @return 流程节点模型
     */
    public fun findNextNode(
        nodeModel: NodeModel,
        currentTask: List<String?>?,
    ): NodeModel? {
        val parentNode = nodeModel.parentNode
        if (null == parentNode || 0 == parentNode.type) {
            // 递归至发起节点，流程结束
            return null
        }

        // 如果当前节点不是条件分支的子节点、而是条件审批的子节点
        if (parentNode.conditionNode()) {
            val childNode = parentNode.childNode
            if (null != childNode && childNode.nodeKey != nodeModel.nodeKey) {
                // 条件执行节点，返回子节点
                return childNode
            }
        }

        // 判断当前节点为并行分支或包容分支，需要判断当前并行是否走完
        if (parentNode.parallelNode() || parentNode.inclusiveNode()) {
            // 找到另外的分支，看是否列表有执行，有就不能返回 childNode
            if (null != currentTask && Collections.disjoint(currentTask, getAllNextConditionNodeKeys(parentNode))) {
                val childNode = parentNode.childNode
                if (null != childNode && childNode.nodeKey == nodeModel.nodeKey) {
                    // 父节点的子节点是当前节点，执行结束
                    return null
                }

                // 分支执行结束，执行子节点
                return childNode
            }

            // 分支未执行完
            return null
        }

        // 往上继续找下一个执行节点
        return findNextNode(parentNode, currentTask)
    }

    /**
     * 获取所有上一个节点key，不包含抄送节点
     *
     * @param nodeModel 当前节点
     * @return 所有节点key
     */
    @JvmStatic
    public fun getAllPreviousNodeKeys(nodeModel: NodeModel): List<String?> {
        val getNodeKeys = getAllParentNodeKeys(nodeModel.nodeKey, nodeModel.parentNode)
        // 往上递归需要去重
        return getNodeKeys.distinct()
    }

    private fun getAllParentNodeKeys(
        currentNodeKey: String?,
        nodeModel: NodeModel?,
    ): List<String?> {
        val nodeKeys: MutableList<String?> = ArrayList()
        if (null != nodeModel) {
            if (!nodeModel.ccNode()) {
                // 非抄送节点
                if (nodeModel.conditionNode()) {
                    // 条件节点找子节点
                    nodeKeys.addAll(getAllConditionNodeKeys(currentNodeKey, nodeModel))
                } else {
                    // 普通节点
                    nodeKeys.add(nodeModel.nodeKey)
                }
            }
            // 继续找上一个节点
            nodeKeys.addAll(getAllParentNodeKeys(currentNodeKey, nodeModel.parentNode))
        }
        return nodeKeys
    }

    private fun getAllConditionNodeKeys(
        currentNodeKey: String?,
        nodeModel: NodeModel?,
    ): List<String?> {
        val nodeKeys: MutableList<String?> = ArrayList()
        if (null != nodeModel) {
            val conditionNodes = nodeModel.conditionNodes
            if (ObjectUtils.isNotEmpty(conditionNodes)) {
                for (conditionNode in conditionNodes!!) {
                    val childNodeMode = conditionNode.childNode
                    if (null != childNodeMode) {
                        if (childNodeMode.conditionNode()) {
                            // 条件路由继续往下找
                            nodeKeys.addAll(getAllConditionNodeKeys(currentNodeKey, childNodeMode))
                        } else {
                            // 其它节点找子节点，必须包含当前节点的子节点分支
                            val allNextNodeKeys = getAllNextConditionNodeKeys(childNodeMode)
                            if (allNextNodeKeys.contains(currentNodeKey)) {
                                val legalNodeKeys: MutableList<String?> = ArrayList()
                                for (t in allNextNodeKeys) {
                                    if (currentNodeKey == t) {
                                        break
                                    }
                                    legalNodeKeys.add(t)
                                }
                                nodeKeys.addAll(legalNodeKeys)
                            }
                        }
                    }
                }
            }
        }
        return nodeKeys
    }

    private fun getAllNextConditionNodeKeys(nodeModel: NodeModel?): List<String?> {
        val nodeKeys: MutableList<String?> = ArrayList()
        if (null != nodeModel) {
            if (nodeModel.conditionNode()) {
                val conditionNodes = nodeModel.conditionNodes
                if (ObjectUtils.isNotEmpty(conditionNodes)) {
                    for (conditionNode in conditionNodes!!) {
                        // 条件节点分支子节点
                        nodeKeys.addAll(getAllNextConditionNodeKeys(conditionNode.childNode))
                    }
                }

                // 条件节点子节点
                nodeKeys.addAll(getAllNextConditionNodeKeys(nodeModel.childNode))
            } else if (nodeModel.parallelNode()) {
                // 并行节点
                for (node in nodeModel.parallelNodes!!) {
                    nodeKeys.addAll(getAllNextConditionNodeKeys(node))
                }
            } else if (nodeModel.inclusiveNode()) {
                // 包容节点
                for (conditionNode in nodeModel.inclusiveNodes!!) {
                    nodeKeys.addAll(getAllNextConditionNodeKeys(conditionNode.childNode))
                }
            } else {
                if (!nodeModel.ccNode()) {
                    // 非抄送节点
                    nodeKeys.add(nodeModel.nodeKey)
                }

                // 找子节点
                val childNodeModel = nodeModel.childNode
                if (null != childNodeModel) {
                    nodeKeys.addAll(getAllNextConditionNodeKeys(childNodeModel))
                }
            }
        }
        return nodeKeys
    }

    /**
     * 获取所有未设置处理人员节点【非发起人自己，只包含 1，审批 2，抄送 节点】
     *
     * @param rootNodeModel 根节点模型
     * @return 所有节点名称
     */
    @JvmStatic
    public fun getUnsetAssigneeNodes(rootNodeModel: NodeModel?): List<NodeModel> {
        val nodeModels = getRootNodeAllChildNodes(rootNodeModel)
        // 过滤发起和结束节点
        return nodeModels.filter {
            it.nodeAssigneeList.isNullOrEmpty() &&
                NodeSetType.INITIATOR_THEMSELVES.ne(it.setType) &&
                (TaskType.APPROVAL.eq(it.type) || TaskType.CC.eq(it.type))
        }
    }

    /**
     * 获取根节点下的所有节点类型【 注意，只对根节点查找有效！】
     *
     * @param rootNodeModel 根节点模型
     * @return 所有节点信息
     */
    @JvmStatic
    public fun getRootNodeAllChildNodes(rootNodeModel: NodeModel?): List<NodeModel> {
        val nodeModels: MutableList<NodeModel> = ArrayList()
        if (null != rootNodeModel) {
            if (rootNodeModel.conditionNode()) {
                val conditionNodes = rootNodeModel.conditionNodes
                if (ObjectUtils.isNotEmpty(conditionNodes)) {
                    for (conditionNode in conditionNodes!!) {
                        // 条件节点分支子节点
                        nodeModels.addAll(getRootNodeAllChildNodes(conditionNode.childNode))
                    }
                }

                // 条件节点子节点
                nodeModels.addAll(getRootNodeAllChildNodes(rootNodeModel.childNode))
            } else if (rootNodeModel.parallelNode()) {
                // 并行节点
                for (nodeModel in rootNodeModel.parallelNodes!!) {
                    nodeModels.addAll(getRootNodeAllChildNodes(nodeModel))
                }
            } else if (rootNodeModel.inclusiveNode()) {
                // 包容节点
                for (conditionNode in rootNodeModel.inclusiveNodes!!) {
                    nodeModels.addAll(getRootNodeAllChildNodes(conditionNode.childNode))
                }
            } else {
                // 普通节点
                nodeModels.add(rootNodeModel)

                // 找子节点
                val childNodeModel = rootNodeModel.childNode
                if (null != childNodeModel) {
                    nodeModels.addAll(getRootNodeAllChildNodes(childNodeModel))
                }
            }
        }
        return nodeModels
    }

    /**
     * 检查是否存在重复节点名称
     *
     * @param rootNodeModel 根节点模型
     * @return true 重复 false 不重复
     */
    @JvmStatic
    public fun checkDuplicateNodeKeys(rootNodeModel: NodeModel?): Boolean {
        val allNextNodes = getRootNodeAllChildNodes(rootNodeModel)
        val set: MutableSet<String?> = HashSet()
        for (nextNode in allNextNodes) {
            if (!set.add(nextNode.nodeKey)) {
                return true
            }
        }
        return false
    }

    /**
     * 检查条件节点
     *
     * @param nodeModel [NodeModel]
     * @return 0，合法情况 1，存在多个条件表达式为空 2，存在多个条件子节点为空 3，存在条件节点KEY重复
     */
    @JvmStatic
    public fun checkConditionNode(nodeModel: NodeModel?): Int {
        if (null != nodeModel) {
            val conditionNodes = nodeModel.conditionNodes
            if (ObjectUtils.isEmpty(conditionNodes)) {
                return checkConditionNode(nodeModel.childNode)
            }
            var i = 0
            var j = 0
            val nodeKeys: MutableSet<String?> = HashSet()
            for (conditionNode in conditionNodes!!) {
                if (!nodeKeys.add(conditionNode.nodeKey)) {
                    // 存在节点KEY重复
                    return 3
                }
                val conditionList = conditionNode.conditionList
                if (ObjectUtils.isEmpty(conditionList)) {
                    i++
                }
                if (null == conditionNode.childNode) {
                    j++
                }
                if (i > 1 || j > 1) {
                    break
                }
            }
            if (i > 1) {
                // 存在多个条件表达式为空
                return 1
            }
            if (j > 1) {
                // 存在多个子节点为空
                return 2
            }
            for (conditionNode in conditionNodes) {
                return checkConditionNode(conditionNode.childNode)
            }
        }
        // 合法情况
        return 0
    }

    /**
     * 检查是否存在审批节点
     *
     * @param rootNodeModel 根节点模型
     * @return true 存在 false 不存在
     */
    @JvmStatic
    public fun checkExistApprovalNode(rootNodeModel: NodeModel?): Boolean =
        getRootNodeAllChildNodes(rootNodeModel)
            .any { TaskType.APPROVAL.eq(it.type) }

    /**
     * 生成节点KEY规则（flk + 时间戳）
     *
     * @return 节点KEY
     */
    @JvmStatic
    public fun generateNodeKey(): String =
        "flk" + System.currentTimeMillis()

    /**
     * 获取动态分配处理人员
     *
     * @param rootNodeModel 根节点模型
     * @return 动态分配处理人员
     */
    @JvmStatic
    public fun getAssigneeMap(rootNodeModel: NodeModel?): Map<String?, DynamicAssignee> =
        getRootNodeAllChildNodes(rootNodeModel).associate {
            it.nodeKey to DynamicAssignee.ofNodeModel(it)
        }

    /**
     * 获取指定节点KEY模型信息
     *
     * @param nodeKey       节点 KEY
     * @param rootNodeModel 根节点模型
     * @return JSON BPM 节点
     */
    @JvmStatic
    public fun getNodeModel(
        nodeKey: String,
        rootNodeModel: NodeModel?,
    ): NodeModel? =
        getRootNodeAllChildNodes(rootNodeModel)
            .firstOrNull {
                it.nodeKey == nodeKey
            }
}
