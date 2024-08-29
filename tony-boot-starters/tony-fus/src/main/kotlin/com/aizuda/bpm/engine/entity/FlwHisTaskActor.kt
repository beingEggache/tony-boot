/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.aizuda.bpm.engine.core.enums.ActorType
import com.aizuda.bpm.engine.model.NodeAssignee

/**
 * 历史任务参与者实体类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlwHisTaskActor : FlwTaskActor() {
    public companion object {
        public fun ofNodeAssignee(
            nodeAssignee: NodeAssignee?,
            instanceId: Long?,
            taskId: Long?,
        ): FlwHisTaskActor {
            val his: FlwHisTaskActor = FlwHisTaskActor()
            his.tenantId = nodeAssignee?.tenantId
            his.instanceId = instanceId
            his.taskId = taskId
            his.actorId = nodeAssignee?.id
            his.actorName = nodeAssignee?.name
            his.weight = nodeAssignee?.weight
            his.actorType = ActorType.USER.value
            return his
        }

        public fun ofFlwHisTask(flwHisTask: FlwHisTask): FlwHisTaskActor {
            val his: FlwHisTaskActor = FlwHisTaskActor()
            his.tenantId = flwHisTask.tenantId
            his.instanceId = flwHisTask.instanceId
            his.taskId = flwHisTask.id
            his.actorId = flwHisTask.createId
            his.actorName = flwHisTask.createBy
            his.actorType = ActorType.USER.value
            return his
        }

        public fun of(taskActor: FlwTaskActor): FlwHisTaskActor {
            val his: FlwHisTaskActor = FlwHisTaskActor()
            his.tenantId = taskActor.tenantId
            his.instanceId = taskActor.instanceId
            his.taskId = taskActor.taskId
            his.actorId = taskActor.actorId
            his.actorName = taskActor.actorName
            his.weight = taskActor.weight
            his.actorType = taskActor.actorType
            his.agentId = taskActor.agentId
            his.agentType = taskActor.agentType
            his.extend = taskActor.extend
            return his
        }
    }
}
