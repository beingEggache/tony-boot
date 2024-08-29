/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

/**
 * 模型节点设置类型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public enum class NodeSetType(
    private val value: Int,
) {
    /**
     * 指定成员
     */
    SPECIFY_MEMBERS(1),

    /**
     * 主管
     */
    SUPERVISOR(2),

    /**
     * 角色
     */
    ROLE(3),

    /**
     * 发起人自选
     */
    INITIATOR_SELECTED(4),

    /**
     * 发起人自己
     */
    INITIATOR_THEMSELVES(5),

    /**
     * 连续多级主管
     */
    MULTI_LEVEL_SUPERVISORS(6),

    /**
     * 部门
     */
    DEPARTMENT(7),
    ;

    public fun ne(value: Int?): Boolean =
        !eq(value)

    public fun eq(value: Int?): Boolean =
        this.value == value
}
