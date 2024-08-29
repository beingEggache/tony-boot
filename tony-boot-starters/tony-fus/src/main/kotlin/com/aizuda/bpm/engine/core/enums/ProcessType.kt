/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core.enums

/**
 * 流程类型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public enum class ProcessType {
    /**
     * 业务流程
     */
    BUSINESS,

    /**
     * 子流程
     */
    CHILD,

    /**
     * 主流程
     */
    MAIN,

    ;

    public fun ne(type: String): Boolean =
        !eq(type)

    public fun eq(type: String): Boolean =
        this.name == type
}
