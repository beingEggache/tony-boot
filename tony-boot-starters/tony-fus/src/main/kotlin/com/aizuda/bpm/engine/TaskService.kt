/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.enums.EventType
import com.aizuda.bpm.engine.core.enums.InstanceState
import com.aizuda.bpm.engine.core.enums.PerformType
import com.aizuda.bpm.engine.core.enums.TaskState
import com.aizuda.bpm.engine.core.enums.TaskType
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.aizuda.bpm.engine.model.NodeModel
import java.util.Optional
import java.util.function.Function

/**
 * 任务业务类接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface TaskService {
    /**
     * 根据任务ID，创建人ID完成任务
     *
     *
     * 该方法仅仅结束活动任务，并不能驱动流程继续执行
     *
     *
     * @param taskId      任务ID
     * @param flowCreator 任务完成者
     * @param args        任务参数
     * @return Task 任务对象
     */
    public fun complete(
        taskId: Long?,
        flowCreator: FlowCreator?,
        args: Map<String?, Any?>?,
    ): FlwTask =
        this.executeTask(taskId, flowCreator, args, TaskState.COMPLETE, EventType.COMPLETE)

    public fun complete(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): FlwTask =
        this.complete(taskId, flowCreator, null)

    /**
     * 根据任务ID，创建人ID完成任务
     *
     * @param taskId      任务ID
     * @param flowCreator 任务完成者
     * @param args        任务参数
     * @param taskState   任务状态
     * @param eventType   任务执行事件类型
     * @return Task 任务对象
     */
    public fun executeTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        args: Map<String?, Any?>?,
        taskState: TaskState?,
        eventType: EventType?,
    ): FlwTask

    /**
     * 强制完成所有任务
     *
     * @param instanceId    流程实例ID
     * @param flowCreator   处理人员
     * @param instanceState 流程实例最终状态
     * @param eventType     监听事件类型
     * @return true 成功 false 失败
     */
    public fun forceCompleteAllTask(
        instanceId: Long?,
        flowCreator: FlowCreator?,
        instanceState: InstanceState?,
        eventType: EventType?,
    ): Boolean

    /**
     * 执行节点跳转任务
     *
     * @param taskId            任务ID
     * @param flowCreator       任务创建者
     * @param args              任务参数
     * @param nodeKey           跳转至目标节点key
     * @param executionFunction 执行函数
     * @return 当前 flowCreator 所在的任务
     */
    public fun executeJumpTask(
        taskId: Long?,
        nodeKey: String?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        executionFunction: Function<FlwTask, Execution?>?,
    ): Boolean

    public fun executeJumpTask(
        taskId: Long?,
        nodeKey: String?,
        flowCreator: FlowCreator?,
        executionFunction: Function<FlwTask, Execution?>?,
    ): Boolean =
        executeJumpTask(taskId, nodeKey, flowCreator, null, executionFunction)

    /**
     * 执行触发器任务
     *
     * @param execution [Execution]
     * @param flwTask   触发器任务
     * @return true 成功 false 失败
     */
    public fun executeTaskTrigger(
        execution: Execution?,
        flwTask: FlwTask?,
    ): Boolean

    /**
     * 完成指定实例ID活动任务
     *
     * @param instanceId  实例ID
     * @param flowCreator 处理人员
     * @return true 成功 false 失败
     */
    public fun completeActiveTasksByInstanceId(
        instanceId: Long?,
        flowCreator: FlowCreator?,
    ): Boolean

    /**
     * 更新任务对象
     *
     * @param flwTask     任务对象
     * @param flowCreator 处理人员
     */
    public fun updateTaskById(
        flwTask: FlwTask?,
        flowCreator: FlowCreator?,
    )

    /**
     * 查看任务设置为已阅状态
     *
     * @param taskId    任务ID
     * @param taskActor 任务参与者
     * @return true 成功 false 失败
     */
    public fun viewTask(
        taskId: Long?,
        taskActor: FlwTaskActor?,
    ): Boolean

    /**
     * 角色根据 任务ID 认领任务，删除其它任务参与者
     *
     * @param taskId      任务ID
     * @param flowCreator 任务认领者
     * @return Task 任务对象
     */
    public fun claimRole(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): FlwTask?

    /**
     * 部门根据 任务ID 认领任务，删除其它任务参与者
     *
     * @param taskId      任务ID
     * @param flowCreator 任务认领者
     * @return Task 任务对象
     */
    public fun claimDepartment(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): FlwTask?

    /**
     * 根据 任务ID 指定代理人
     *
     * @param taskId            任务ID
     * @param flowCreator       任务参与者
     * @param agentFlowCreators 指定代理人列表
     * @return true 成功 false 失败
     */
    public fun agentTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        agentFlowCreators: List<FlowCreator>,
    ): Boolean =
        this.assigneeTask(taskId, TaskType.AGENT, flowCreator, agentFlowCreators)

    /**
     * 根据 任务ID 转办任务
     *
     * @param taskId              任务ID
     * @param flowCreator         任务参与者
     * @param assigneeFlowCreator 指定办理人
     * @return true 成功 false 失败
     */
    public fun transferTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        assigneeFlowCreator: FlowCreator,
    ): Boolean =
        this.assigneeTask(taskId, TaskType.TRANSFER, flowCreator, listOf(assigneeFlowCreator))

    /**
     * 根据 任务ID 委派任务、代理人办理完任务该任务重新归还给原处理人
     *
     * @param taskId              任务ID
     * @param flowCreator         任务参与者
     * @param assigneeFlowCreator 指定办理人
     * @return true 成功 false 失败
     */
    public fun delegateTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        assigneeFlowCreator: FlowCreator,
    ): Boolean =
        this.assigneeTask(taskId, TaskType.DELEGATE, flowCreator, listOf(assigneeFlowCreator))

    /**
     * 根据 任务ID 分配任务给指定办理人、重置任务类型
     *
     * @param taskId               任务ID
     * @param taskType             任务类型
     * @param flowCreator          任务参与者
     * @param assigneeFlowCreators 指定办理人列表
     * @return true 成功 false 失败
     */
    public fun assigneeTask(
        taskId: Long?,
        taskType: TaskType?,
        flowCreator: FlowCreator?,
        assigneeFlowCreators: List<FlowCreator>,
    ): Boolean

    /**
     * 根据 任务ID 解决委派任务
     *
     * @param taskId      任务ID
     * @param flowCreator 任务参与者
     * @return true 成功 false 失败
     */
    public fun resolveTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): Boolean

    /**
     * 拿回任务、在当前办理人尚未处理文件前，允许上一节点提交人员执行拿回
     *
     * @param taskId      任务ID（当前节点的父任务ID属于历史任务）
     * @param flowCreator 任务创建者
     * @return 拿回任务
     */
    public fun reclaimTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): Optional<FlwTask>

    /**
     * 唤醒历史任务
     *
     *
     * 该方法会导致流程状态不可控，请慎用
     *
     *
     * @param taskId      历史任务ID
     * @param flowCreator 任务唤醒者
     * @return [FlwTask] 唤醒后的任务对象
     */
    public fun resume(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): FlwTask?

    /**
     * 根据任务ID、创建人撤回任务（该任务后续任务未执行前有效）
     *
     * @param taskId      待撤回历史任务ID
     * @param flowCreator 任务创建者
     * @return Task 任务对象
     */
    public fun withdrawTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): Optional<FlwTask>

    /**
     * 根据当前任务对象驳回至上一步处理
     *
     * @param currentFlwTask 当前任务对象
     * @param flowCreator    任务创建者
     * @param args           任务参数
     * @return Task 任务对象
     */
    public fun rejectTask(
        currentFlwTask: FlwTask?,
        flowCreator: FlowCreator?,
        args: Map<String?, Any?>?,
    ): Optional<FlwTask>

    public fun rejectTask(
        currentFlwTask: FlwTask?,
        flowCreator: FlowCreator?,
    ): Optional<FlwTask> =
        rejectTask(currentFlwTask, flowCreator, null)

    /**
     * 根据 taskId、createBy 判断创建人createBy是否允许执行任务
     *
     * @param flwTask 任务对象
     * @param userId  用户ID
     * @return 被允许参与者 [FlwTaskActor]
     */
    public fun isAllowed(
        flwTask: FlwTask,
        userId: String?,
    ): FlwTaskActor?

    /**
     * 根据任务模型、执行对象创建新的任务
     *
     * @param taskModel 任务模型
     * @param execution 执行对象
     * @return 创建任务集合
     */
    public fun createTask(
        taskModel: NodeModel?,
        execution: Execution?,
    ): List<FlwTask>

    /**
     * 根据已有任务、参与者创建新的任务
     *
     *
     * 适用于动态转派，动态协办等处理且流程图中不体现节点情况
     *
     *
     * @param taskId            主办任务ID
     * @param taskActors        参与者集合
     * @param taskType          任务类型
     * @param performType       参与类型
     * @param flowCreator       任务创建者
     * @param executionFunction 执行函数
     * @return 创建任务集合
     */
    public fun createNewTask(
        taskId: Long?,
        taskType: TaskType?,
        performType: PerformType?,
        taskActors: List<FlwTaskActor>,
        flowCreator: FlowCreator?,
        executionFunction: Function<FlwTask?, Execution?>?,
    ): List<FlwTask>

    /**
     * 获取超时或者提醒的任务
     *
     * @return 任务列表
     */
    public val timeoutOrRemindTasks: List<FlwTask>

    /**
     * 根据任务ID获取任务模型
     *
     * @param taskId 任务ID
     * @return 流程模型
     */
    public fun getTaskModel(taskId: Long?): NodeModel?

    /**
     * 向指定的任务ID添加参与者【加签】
     *
     * @param taskId        任务ID
     * @param performType   参与类型 [PerformType]
     * @param flwTaskActors 参与者列表
     * @param flowCreator   执行操作人员
     * @return true 成功 false 失败
     */
    public fun addTaskActor(
        taskId: Long?,
        performType: PerformType?,
        flwTaskActors: List<FlwTaskActor>,
        flowCreator: FlowCreator?,
    ): Boolean

    public fun addTaskActor(
        taskId: Long?,
        performType: PerformType?,
        flwTaskActor: FlwTaskActor,
        flowCreator: FlowCreator?,
    ): Boolean =
        this.addTaskActor(taskId, performType, listOf(flwTaskActor), flowCreator)

    /**
     * 对指定的任务ID删除参与者【减签】
     *
     * @param taskId      任务ID
     * @param actorIds    参与者ID列表
     * @param flowCreator 执行操作人员
     * @return true 成功 false 失败
     */
    public fun removeTaskActor(
        taskId: Long?,
        actorIds: List<String?>,
        flowCreator: FlowCreator?,
    ): Boolean

    public fun removeTaskActor(
        taskId: Long?,
        actorId: String,
        flowCreator: FlowCreator?,
    ): Boolean =
        removeTaskActor(taskId, listOf(actorId), flowCreator)

    /**
     * 结束调用外部流程任务
     *
     * @param callProcessId  调用外部流程定义ID
     * @param callInstanceId 调用外部流程实例ID
     */
    public fun endCallProcessTask(
        callProcessId: Long?,
        callInstanceId: Long?,
    )

    /**
     * 级联删除 flw_his_task, flw_his_task_actor, flw_task, flw_task_actor
     *
     * @param instanceIds 流程实例ID列表
     */
    public fun cascadeRemoveByInstanceIds(instanceIds: List<Long?>?)
}
