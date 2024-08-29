/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.model

/**
 * JSON BPM 条件节点
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class ConditionNode {
    /**
     * 节点名称
     */
    public var nodeName: String? = null

    /**
     * 节点 key
     */
    public var nodeKey: String? = null

    /**
     * 节点类型
     */
    public var type: Int? = null

    /**
     * 优先级
     */
    public var priorityLevel: Int? = null

    /**
     * 节点条件表达式列表
     *
     *
     * 外层 List 为条件组或关系、内层 List 为具体条件且关系
     *
     */
    public var conditionList: List<List<NodeExpression>>? = null

    /**
     * 子节点
     */
    public var childNode: NodeModel? = null
}
