/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.enums.AgentType
import com.aizuda.bpm.engine.model.NodeAssignee
import com.tony.utils.toJsonString
import java.io.Serializable

/**
 * 任务参与者实体类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public open class FlwTaskActor : Serializable {
    /**
     * 主键ID
     */
    public var id: Long? = null

    /**
     * 租户ID
     */
    public var tenantId: String? = null

    /**
     * 流程实例ID
     */
    public var instanceId: Long? = null

    /**
     * 关联的任务ID
     */
    public var taskId: Long? = null

    /**
     * 关联的参与者ID（参与者可以为用户、部门、角色）
     */
    public var actorId: String? = null

    /**
     * 关联的参与者名称
     */
    public var actorName: String? = null

    /**
     * 参与者类型 0，用户 1，角色 2，部门
     */
    public var actorType: Int? = null

    /**
     * 权重
     *
     *
     * 票签任务时，该值为不同处理人员的分量比例
     *
     */
    public var weight: Int? = null

    /**
     * 代理人ID
     */
    public var agentId: String? = null

    /**
     * 代理人类型 0，代理 1，被代理 2，认领角色 3，认领部门
     */
    public var agentType: Int? = null

    /**
     * 扩展json
     */
    public var extend: String? = null

    /**
     * 是否为代理人
     *
     * @return true 是 false 否
     */
    public fun agentActor(): Boolean =
        0 == this.actorType

    public fun eqActorId(actorId: String?): Boolean =
        this.actorId == actorId

    public fun setExtendOf(obj: Any?) {
        this.extend = obj.toJsonString()
    }

    public companion object {
        @JvmStatic
        public fun of(
            flowCreator: FlowCreator?,
            flwTask: FlwTask,
        ): FlwTaskActor {
            val flwTaskActor = ofUser(flowCreator?.tenantId, flowCreator?.createId, flowCreator?.createBy)
            flwTaskActor.instanceId = flwTask.instanceId
            flwTaskActor.taskId = flwTask.id
            return flwTaskActor
        }

        @JvmStatic
        public fun ofAgentIt(flowCreator: FlowCreator?): FlwTaskActor {
            val flwTaskActor = FlwTaskActor()
            flwTaskActor.agentId = flowCreator?.createId
            flwTaskActor.agentType = 1
            val map: MutableMap<String?, Any?> = HashMap()
            map["createBy"] = flowCreator?.createBy
            flwTaskActor.setExtendOf(map)
            return flwTaskActor
        }

        @JvmStatic
        public fun ofAgent(
            agentType: AgentType,
            flowCreator: FlowCreator?,
            flwTask: FlwTask,
            agentTaskActor: FlwTaskActor?,
        ): FlwTaskActor {
            val flwTaskActor = of(flowCreator, flwTask)
            flwTaskActor.agentId = agentTaskActor?.actorId
            flwTaskActor.agentType = agentType.value
            val map: MutableMap<String, Any?> = HashMap()
            map["actorType"] = agentTaskActor?.actorType
            map["actorName"] = agentTaskActor?.actorName
            flwTaskActor.setExtendOf(map)
            return flwTaskActor
        }

        @JvmStatic
        public fun ofFlwTask(flwTask: FlwTask): FlwTaskActor {
            val flwTaskActor =
                ofUser(
                    flwTask.tenantId,
                    flwTask.createId,
                    flwTask.createBy
                )
            flwTaskActor.instanceId = flwTask.instanceId
            flwTaskActor.taskId = flwTask.id
            return flwTaskActor
        }

        @JvmStatic
        public fun ofFlowCreator(flowCreator: FlowCreator): FlwTaskActor =
            ofUser(flowCreator.tenantId, flowCreator.createId, flowCreator.createBy)

        @JvmStatic
        public fun ofFlwInstance(
            flwInstance: FlwInstance,
            taskId: Long?,
        ): FlwTaskActor {
            val flwTaskActor = ofUser(flwInstance.tenantId, flwInstance.createId, flwInstance.createBy)
            flwTaskActor.instanceId = flwInstance.id
            flwTaskActor.taskId = taskId
            return flwTaskActor
        }

        @JvmStatic
        public fun ofNodeAssignee(nodeAssignee: NodeAssignee): FlwTaskActor =
            ofUser(nodeAssignee.tenantId, nodeAssignee.id, nodeAssignee.name)

        @JvmStatic
        public fun ofUser(
            tenantId: String?,
            actorId: String?,
            actorName: String?,
        ): FlwTaskActor =
            of(tenantId, actorId, actorName, 0, null)

        @JvmStatic
        public fun ofRole(
            tenantId: String?,
            actorId: String?,
            actorName: String?,
        ): FlwTaskActor =
            of(tenantId, actorId, actorName, 1, null)

        @JvmStatic
        public fun ofDepartment(
            tenantId: String?,
            actorId: String?,
            actorName: String?,
        ): FlwTaskActor =
            of(tenantId, actorId, actorName, 2, null)

        @JvmStatic
        public fun of(
            nodeAssignee: NodeAssignee,
            actorType: Int?,
        ): FlwTaskActor =
            of(nodeAssignee.tenantId, nodeAssignee.id, nodeAssignee.name, actorType, nodeAssignee.weight)

        @JvmStatic
        public fun of(
            taskId: Long?,
            t: FlwHisTaskActor,
        ): FlwTaskActor {
            val flwTaskActor = of(t.tenantId, t.actorId, t.actorName, t.actorType, t.weight)
            flwTaskActor.taskId = taskId
            flwTaskActor.instanceId = t.instanceId
            return flwTaskActor
        }

        @JvmStatic
        protected fun of(
            tenantId: String?,
            actorId: String?,
            actorName: String?,
            actorType: Int?,
            weight: Int?,
        ): FlwTaskActor {
            val taskActor = FlwTaskActor()
            taskActor.tenantId = tenantId
            taskActor.actorId = actorId
            taskActor.actorName = actorName
            taskActor.actorType = actorType
            taskActor.weight = weight
            return taskActor
        }

        @JvmStatic
        public fun ofFlwHisTaskActor(
            taskId: Long?,
            hta: FlwHisTaskActor,
        ): FlwTaskActor {
            val taskActor = FlwTaskActor()
            taskActor.tenantId = hta.tenantId
            taskActor.instanceId = hta.instanceId
            taskActor.taskId = taskId
            taskActor.actorId = hta.actorId
            taskActor.actorName = hta.actorName
            taskActor.actorType = hta.actorType
            taskActor.weight = hta.weight
            taskActor.agentId = hta.agentId
            taskActor.agentType = hta.agentType
            taskActor.extend = hta.extend
            return taskActor
        }
    }
}
