/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.model

import java.io.Serializable

/**
 * JSON BPM 模型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class ProcessModel public constructor() : Serializable {
    /**
     * 节点名称
     */
    public var name: String? = null

    /**
     * 流程 key
     */
    public var key: String? = null

    /**
     * 实例地址
     */
    public var instanceUrl: String? = null

    /**
     * 节点信息
     */
    public var nodeConfig: NodeModel? = null

    /**
     * 获取process定义的指定节点key的节点模型
     *
     * @param nodeKey 节点key
     * @return [NodeModel]
     */
    public fun getNode(nodeKey: String?): NodeModel? =
        if (null == nodeConfig) null else nodeConfig!!.getNode(nodeKey)

    /**
     * 构建父节点
     *
     * @param rootNode 根节点
     */
    public fun buildParentNode(rootNode: NodeModel?) {
        // 条件分支
        this.buildParentConditionNodes(rootNode, rootNode?.conditionNodes)

        // 并行分支
        val parallelNodes = rootNode?.parallelNodes
        if (null != parallelNodes) {
            for (nodeModel in parallelNodes) {
                nodeModel.parentNode = rootNode
                this.buildParentNode(nodeModel)
            }
        }

        // 包容分支
        this.buildParentConditionNodes(rootNode, rootNode?.inclusiveNodes)

        // 子节点
        val childNode = rootNode?.childNode
        if (null != childNode) {
            childNode.parentNode = rootNode
            this.buildParentNode(childNode)
        }
    }

    /**
     * 构建条件节点的父节点
     *
     * @param rootNode       根节点
     * @param conditionNodes 条件节点
     */
    private fun buildParentConditionNodes(
        rootNode: NodeModel?,
        conditionNodes: List<ConditionNode>?,
    ) {
        if (null != conditionNodes) {
            for (conditionNode in conditionNodes) {
                val conditionChildNode = conditionNode.childNode
                if (null != conditionChildNode) {
                    conditionChildNode.parentNode = rootNode
                    this.buildParentNode(conditionChildNode)
                }
            }
        }
    }

    /**
     * 清理父节点关系
     *
     * @param rootNode 根节点
     */
    public fun cleanParentNode(rootNode: NodeModel) {
        rootNode.parentNode = null
        // 清理条件节点
        val conditionNodes = rootNode.conditionNodes
        if (null != conditionNodes) {
            for (conditionNode in conditionNodes) {
                val conditionChildNode = conditionNode.childNode
                if (null != conditionChildNode) {
                    this.cleanParentNode(conditionChildNode)
                }
            }
        }
        // 清理子节点
        val childNode = rootNode.childNode
        if (null != childNode) {
            childNode.parentNode = null
            this.cleanParentNode(childNode)
        }
    }
}
