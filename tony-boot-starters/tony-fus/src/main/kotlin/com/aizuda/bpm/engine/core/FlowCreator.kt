/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.core

import java.io.Serializable

/**
 * 流程创建者
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlowCreator public constructor(
    /**
     * 创建人ID
     */
    public val createId: String,
    /**
     * 创建人
     */
    public val createBy: String,
) : Serializable {
    /**
     * 租户ID
     */
    public var tenantId: String? = null
        private set

    public fun tenantId(tenantId: String?): FlowCreator {
        this.tenantId = tenantId
        return this
    }

    public companion object {
        /**
         * 初始化管理员，用于操作权限忽略等场景
         */
        @JvmStatic
        public val ADMIN: FlowCreator = FlowCreator("0", "admin")

        @JvmStatic
        public fun of(
            createId: String,
            createBy: String,
        ): FlowCreator =
            of(null, createId, createBy)

        @JvmStatic
        public fun of(
            tenantId: String?,
            createId: String,
            createBy: String,
        ): FlowCreator =
            FlowCreator(createId, createBy).tenantId(tenantId)
    }
}
