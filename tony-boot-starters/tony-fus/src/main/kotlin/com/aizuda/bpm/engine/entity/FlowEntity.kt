/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.aizuda.bpm.engine.core.FlowCreator
import java.io.Serializable
import java.util.Date

/**
 * 流程表实体基类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public open class FlowEntity public constructor() : Serializable {
    /**
     * 主键ID
     */
    public var id: Long? = null

    /**
     * 租户ID
     */
    public var tenantId: String? = null

    /**
     * 创建人ID
     */
    public var createId: String? = null

    /**
     * 创建人名称
     */
    public var createBy: String? = null

    /**
     * 创建时间
     */
    public var createTime: Date? = null

    public fun setFlowCreator(flowCreator: FlowCreator?) {
        this.tenantId = flowCreator?.tenantId
        this.createId = flowCreator?.createId
        this.createBy = flowCreator?.createBy
    }
}
