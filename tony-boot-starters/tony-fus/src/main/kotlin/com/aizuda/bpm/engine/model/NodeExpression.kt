/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.model

/**
 * JSON BPM 节点表达式条件
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class NodeExpression public constructor() {
    /**
     * 名称
     */
    public var label: String? = null

    /**
     * 属性
     */
    public var field: String? = null

    /**
     * 操作
     */
    public var operator: String? = null

    /**
     * 内容
     */
    public var value: String? = null

    /**
     * 条件类型 [com.aizuda.bpm.engine.core.enums.ConditionType]
     */
    public var type: String? = null
}
