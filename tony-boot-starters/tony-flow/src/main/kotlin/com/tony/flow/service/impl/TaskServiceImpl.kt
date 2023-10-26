package com.tony.flow.service.impl

import com.tony.flow.db.enums.PerformType
import com.tony.flow.db.enums.TaskType
import com.tony.flow.db.po.FlowHistoryTaskActor
import com.tony.flow.db.po.FlowTask
import com.tony.flow.db.po.FlowTaskActor
import com.tony.flow.model.FlowExecution
import com.tony.flow.model.FlowNode
import com.tony.flow.model.FlowOperator
import com.tony.flow.service.TaskService

/**
 * TaskServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:44
 * @since 1.0.0
 */
internal class TaskServiceImpl:TaskService {
    override fun complete(taskId: Long?, flowOperator: FlowOperator, variable: Map<String, Any?>?): FlowTask {
        TODO("Not yet implemented")
    }

    override fun updateTaskById(flowTask: FlowTask) {
        TODO("Not yet implemented")
    }

    override fun viewTask(taskId: Long, taskActor: FlowTaskActor) {
        TODO("Not yet implemented")
    }

    override fun taskTimeout(taskId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun claimTask(taskId: Long, flowHistoryTaskActor: FlowHistoryTaskActor): FlowTask {
        TODO("Not yet implemented")
    }

    override fun assignTask(
        taskId: Long,
        taskType: TaskType,
        flowTaskActor: FlowTaskActor,
        assignee: FlowTaskActor
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun reclaimTask(taskId: Long, flowOperator: FlowOperator): FlowTask? {
        TODO("Not yet implemented")
    }

    override fun withdrawTask(taskId: Long, flowOperator: FlowOperator): FlowTask? {
        TODO("Not yet implemented")
    }

    override fun rejectTask(flowTask: FlowTask, flowOperator: FlowOperator, variable: Map<String, Any?>?): FlowTask? {
        TODO("Not yet implemented")
    }

    override fun hasPermission(flowTask: FlowTask, userId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun createTask(flowNode: FlowNode?, flowExecution: FlowExecution): List<FlowTask> {
        TODO("Not yet implemented")
    }

    override fun createNewTask(
        taskId: Long,
        taskType: TaskType,
        taskActors: Collection<FlowTaskActor>
    ): List<FlowTask> {
        TODO("Not yet implemented")
    }

    override fun listExpiredOrRemindTasks(): List<FlowTask> {
        TODO("Not yet implemented")
    }

    override fun getTaskNode(taskId: Long): FlowNode {
        TODO("Not yet implemented")
    }

    override fun addTaskActor(taskId: Long, performType: PerformType, taskActors: List<FlowHistoryTaskActor>): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeTaskActor(taskId: Long, taskActorIds: Collection<Long>): Boolean {
        TODO("Not yet implemented")
    }

    override fun cascadeRemoveByInstanceId(instanceId: Long) {
        TODO("Not yet implemented")
    }
}
