/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.model

import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.entity.FlwTaskActor
import java.io.Serializable

/**
 * JSON BPM 分配到任务的人或角色
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class NodeAssignee public constructor() : Serializable {
    /**
     * 租户ID
     */
    public var tenantId: String? = null

    /**
     * 主键ID
     */
    public var id: String? = null

    /**
     * 名称
     */
    public var name: String? = null

    /**
     * 权重（ 用于票签，多个参与者合计权重 100% ）
     */
    public var weight: Int? = null

    /**
     * 扩展配置，用于存储头像、等其它信息
     */
    public var extendConfig: Map<String, Any>? = null

    public companion object {
        @JvmStatic
        public fun of(flwTaskActor: FlwTaskActor): NodeAssignee {
            val nodeAssignee = NodeAssignee()
            nodeAssignee.tenantId = flwTaskActor.tenantId
            nodeAssignee.id = flwTaskActor.actorId
            nodeAssignee.name = flwTaskActor.actorName
            nodeAssignee.weight = flwTaskActor.weight
            return nodeAssignee
        }

        @JvmStatic
        public fun ofFlowCreator(flowCreator: FlowCreator): NodeAssignee {
            val nodeAssignee = NodeAssignee()
            nodeAssignee.tenantId = flowCreator.tenantId
            nodeAssignee.id = flowCreator.createId
            nodeAssignee.name = flowCreator.createBy
            return nodeAssignee
        }
    }
}
