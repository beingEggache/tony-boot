/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.tony.utils.getLogger
import com.tony.utils.ifNullOrBlank
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import java.util.Date

/**
 * 流程实例实体类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public open class FlwInstance public constructor() : FlowEntity() {
    /**
     * 流程定义ID
     */
    public var processId: Long? = null

    /**
     * 父流程实例ID
     */
    public var parentInstanceId: Long? = null

    /**
     * 流程实例优先级
     */
    public var priority: Int? = null

    /**
     * 流程实例编号
     */
    public var instanceNo: String? = null

    /**
     * 业务KEY（用于关联业务逻辑实现预留）
     */
    public var businessKey: String? = null

    /**
     * 变量json
     */
    public var variable: String? = null

    /**
     * 当前所在节点名称
     */
    public var currentNodeName: String? = null

    /**
     * 当前所在节点key
     */
    public var currentNodeKey: String? = null

    /**
     * 流程实例期望完成时间
     */
    public var expireTime: Date? = null

    /**
     * 流程实例上一次更新人
     */
    public var lastUpdateBy: String? = null

    /**
     * 流程实例上一次更新时间
     */
    public var lastUpdateTime: Date? = null

    public fun variableToMap(): MutableMap<String?, Any?> {
        getLogger().info(variable)
        return variable.ifNullOrBlank("{}").jsonToObj()
    }

    public fun setMapVariable(args: Map<String?, Any?>?) {
        this.variable = args.toJsonString()
    }

    override fun toString(): String =
        "FlwInstance(processId=" + this.processId + ", parentInstanceId=" + this.parentInstanceId + ", priority=" +
            this.priority +
            ", instanceNo=" +
            this.instanceNo +
            ", businessKey=" +
            this.businessKey +
            ", variable=" +
            this.variable +
            ", currentNodeName=" +
            this.currentNodeName +
            ", currentNodeKey=" +
            this.currentNodeKey +
            ", expireTime=" +
            this.expireTime +
            ", lastUpdateBy=" +
            this.lastUpdateBy +
            ", lastUpdateTime=" +
            this.lastUpdateTime +
            ")"

    public companion object {
        public fun of(businessKey: String?): FlwInstance {
            val flwInstance = FlwInstance()
            flwInstance.businessKey = businessKey
            return flwInstance
        }
    }
}
