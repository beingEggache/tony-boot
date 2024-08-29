/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

/**
 * 参与者类型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author insist
 * @since 1.0
 */
public enum class ActorType(
    public val value: Int,
) {
    /**
     * 用户
     */
    USER(0),

    /**
     * 角色
     */
    ROLE(1),

    /**
     * 部门
     */
    DEPARTMENT(2),
    ;

    public fun ne(value: Int): Boolean =
        !eq(value)

    public fun eq(value: Int): Boolean =
        this.value == value
}
