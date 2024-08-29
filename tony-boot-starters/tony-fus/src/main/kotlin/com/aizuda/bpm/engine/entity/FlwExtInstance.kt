/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.aizuda.bpm.engine.FlowConstants
import com.aizuda.bpm.engine.ProcessModelCache
import java.io.Serializable

/**
 * 扩展流程实例实体类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlwExtInstance :
    ProcessModelCache,
    Serializable {
    /**
     * 主键ID
     */
    public var id: Long? = null

    /**
     * 租户ID
     */
    public var tenantId: String? = null

    /**
     * 流程定义ID
     */
    public var processId: Long? = null

    /**
     * 流程定义类型（冗余业务直接可用）
     */
    protected var processType: String? = null

    /**
     * 流程模型定义JSON内容
     *
     *
     * 在发起的时候拷贝自流程定义模型内容，用于记录当前实例节点的动态改变。
     *
     */
    override var modelContent: String? = null

    override fun modelCacheKey(): String =
        FlowConstants.PROCESS_INSTANCE_CACHE_KEY + this.id

    override fun toString(): String =
        "FlwExtInstance(id=" + this.id + ", tenantId=" + this.tenantId + ", processId=" +
            this.processId +
            ", processType=" +
            this.processType +
            ", modelContent=" +
            this.modelContent +
            ")"

    public companion object {
        public fun of(
            flwInstance: FlwInstance,
            flwProcess: FlwProcess,
        ): FlwExtInstance {
            val ext = FlwExtInstance()
            ext.id = flwInstance.id
            ext.tenantId = flwInstance.tenantId
            ext.processId = flwInstance.processId
            ext.processType = flwProcess.processType
            ext.modelContent = flwProcess.modelContent
            return ext
        }
    }
}
