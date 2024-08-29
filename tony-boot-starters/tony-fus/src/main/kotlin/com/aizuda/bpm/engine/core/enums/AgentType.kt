/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

/**
 * 代理人类型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public enum class AgentType(
    public val value: Int,
) {
    /**
     * 代理人
     */
    AGENT(0),

    /**
     * 被代理人
     */
    PRINCIPAL(1),

    /**
     * 认领角色
     */
    CLAIM_ROLE(2),

    /**
     * 认领部门
     */
    CLAIM_DEPARTMENT(3),
    ;

    public fun ne(value: Int): Boolean =
        !eq(value)

    public fun eq(value: Int): Boolean =
        this.value == value
}
