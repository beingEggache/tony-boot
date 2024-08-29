/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.aizuda.bpm.engine.assist.Assert.isEmpty
import com.aizuda.bpm.engine.assist.DateUtils.currentDate
import com.aizuda.bpm.engine.assist.DateUtils.parseTimerTaskTime
import com.aizuda.bpm.engine.core.enums.PerformType
import com.aizuda.bpm.engine.core.enums.TaskType
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import java.util.Date

/**
 * 任务实体类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public open class FlwTask : FlowEntity() {
    /**
     * 流程实例ID
     */
    public var instanceId: Long? = null

    /**
     * 父任务ID
     */
    public var parentTaskId: Long? = null

    /**
     * 任务名称
     */
    public var taskName: String? = null

    /**
     * 任务 key 唯一标识
     */
    public var taskKey: String? = null

    /**
     * 任务类型 [TaskType]
     */
    public var taskType: Int? = null

    /**
     * 参与方式 [PerformType]
     */
    public var performType: Int? = null

    /**
     * 任务关联的表单url
     */
    public var actionUrl: String? = null

    /**
     * 变量json
     */
    public var variable: String? = null

    /**
     * 委托人ID
     */
    public var assignorId: String? = null

    /**
     * 委托人
     */
    public var assignor: String? = null

    /**
     * 期望任务完成时间
     */
    public var expireTime: Date? = null

    /**
     * 提醒时间
     */
    public var remindTime: Date? = null

    /**
     * 提醒次数
     */
    public var remindRepeat: Int? = null

    /**
     * 已阅 0，否 1，是
     */
    public var viewed: Int? = null

    public fun major(): Boolean =
        this.taskType == TaskType.MAJOR.value

    public fun variableMap(): MutableMap<String?, Any?> =
        variable?.jsonToObj() ?: mutableMapOf()

    public fun setTaskType(taskType: TaskType?) {
        this.taskType = taskType?.value
    }

    public fun setPerformType(performType: PerformType?) {
        this.performType = performType?.value
    }

    public fun setVariable(args: Map<String?, Any?>?) {
        if (null != args && !args.isEmpty()) {
            this.variable = args.toJsonString()
        }
    }

    /**
     * 从扩展配置中加载期望任务完成时间
     *
     * @param extendConfig 扩展配置
     * @param checkEmpty   检查是否为空
     */
    public fun loadExpireTime(
        extendConfig: Map<String, Any>?,
        checkEmpty: Boolean,
    ) {
        var expireTime: Date? = null
        if (null != extendConfig) {
            val time = extendConfig["time"] as String?
            if (null != time) {
                expireTime = parseTimerTaskTime(time)
            }
        }
        if (checkEmpty) {
            isEmpty(expireTime, "Timer task config error")
        }
        this.expireTime = expireTime
    }

    /**
     * 开始节点判断
     *
     * @return true 是 false 非
     */
    public fun startNode(): Boolean =
        0L == this.parentTaskId

    public fun cloneTask(flwHisTaskActor: FlwHisTaskActor?): FlwTask {
        if (null != flwHisTaskActor) {
            this.createId = flwHisTaskActor.actorId
            this.createBy = flwHisTaskActor.actorName
        }
        return cloneTask(createId, createBy)
    }

    public fun cloneTask(
        createId: String?,
        createBy: String?,
    ): FlwTask {
        val newFlwTask = FlwTask()
        newFlwTask.tenantId = tenantId
        newFlwTask.instanceId = instanceId
        newFlwTask.parentTaskId = parentTaskId
        newFlwTask.taskName = taskName
        newFlwTask.taskKey = taskKey
        newFlwTask.taskType = (taskType)
        newFlwTask.performType = (performType)
        newFlwTask.actionUrl = actionUrl
        newFlwTask.variable = variable
        newFlwTask.assignorId = assignorId
        newFlwTask.assignor = assignor
        newFlwTask.expireTime = expireTime
        newFlwTask.remindTime = remindTime
        newFlwTask.remindRepeat = remindRepeat
        newFlwTask.viewed = viewed
        newFlwTask.createId = createId
        newFlwTask.createBy = createBy
        newFlwTask.createTime = currentDate
        return newFlwTask
    }
}
