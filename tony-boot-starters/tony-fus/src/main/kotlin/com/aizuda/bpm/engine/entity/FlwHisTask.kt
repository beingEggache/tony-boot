/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.assist.DateUtils.calculateDateDifference
import com.aizuda.bpm.engine.assist.DateUtils.currentDate
import com.aizuda.bpm.engine.core.enums.TaskState
import com.aizuda.bpm.engine.model.NodeModel
import java.util.Date

/**
 * 历史任务实体类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlwHisTask : FlwTask() {
    /**
     * 调用外部流程定义ID
     */
    public var callProcessId: Long? = null

    /**
     * 调用外部流程实例ID
     */
    public var callInstanceId: Long? = null

    /**
     * 完成时间
     */
    public var finishTime: Date? = null

    /**
     * 任务状态 0，活动 1，跳转 2，完成 3，拒绝 4，撤销审批  5，超时 6，终止 7，驳回终止
     */
    public var taskState: Int? = null
        protected set

    /**
     * 处理耗时
     */
    public var duration: Long? = null

    public fun setTaskState(taskState: TaskState?): FlwHisTask {
        this.taskState = taskState?.value
        return this
    }

    public fun setTaskState(taskState: Int): FlwHisTask {
        isNull(TaskState.get(taskState), "插入的实例状态异常 [taskState=$taskState]")
        this.taskState = taskState
        return this
    }

    /**
     * 根据历史任务产生撤回的任务对象
     *
     *
     * 创建人信息保留
     *
     *
     * @return 任务对象
     */
    public fun undoTask(): FlwTask =
        cloneTask(this.createId, this.createBy)

    /**
     * 计算流程实例处理耗时
     */
    public fun calculateDuration() {
        this.finishTime = currentDate
        this.duration = calculateDateDifference(this.createTime, this.finishTime)
    }

    override fun toString(): String =
        "FlwHisTask(callProcessId=" + this.callProcessId + ", callInstanceId=" + this.callInstanceId +
            ", finishTime=" +
            this.finishTime +
            ", taskState=" +
            this.taskState +
            ", duration=" +
            this.duration +
            ")"

    public companion object {
        public fun of(
            flwTask: FlwTask,
            taskState: TaskState,
        ): FlwHisTask =
            of(flwTask).setTaskState(taskState)

        public fun of(flwTask: FlwTask): FlwHisTask {
            val hisTask = FlwHisTask()
            hisTask.id = flwTask.id
            hisTask.tenantId = flwTask.tenantId
            hisTask.createId = flwTask.createId
            hisTask.createBy = flwTask.createBy
            hisTask.createTime = flwTask.createTime
            hisTask.instanceId = flwTask.instanceId
            hisTask.parentTaskId = flwTask.parentTaskId
            hisTask.taskName = flwTask.taskName
            hisTask.taskKey = flwTask.taskKey
            hisTask.taskType = flwTask.taskType
            hisTask.performType = flwTask.performType
            hisTask.actionUrl = flwTask.actionUrl
            hisTask.variable = flwTask.variable
            hisTask.assignorId = flwTask.assignorId
            hisTask.assignor = flwTask.assignor
            hisTask.expireTime = flwTask.expireTime
            hisTask.remindTime = flwTask.remindTime
            hisTask.remindRepeat = flwTask.remindRepeat
            hisTask.viewed = flwTask.viewed
            return hisTask
        }

        public fun ofCallInstance(
            nodeModel: NodeModel,
            instance: FlwInstance,
        ): FlwHisTask {
            val flwHisTask = FlwHisTask()
            flwHisTask.tenantId = instance.tenantId
            flwHisTask.createId = instance.createId
            flwHisTask.createBy = instance.createBy
            flwHisTask.createTime = instance.createTime
            flwHisTask.instanceId = instance.parentInstanceId
            flwHisTask.taskName = nodeModel.nodeName
            flwHisTask.taskKey = nodeModel.nodeKey
            flwHisTask.callProcessId = instance.processId
            flwHisTask.callInstanceId = instance.id
            flwHisTask.taskType = nodeModel.type
            return flwHisTask
        }
    }
}
