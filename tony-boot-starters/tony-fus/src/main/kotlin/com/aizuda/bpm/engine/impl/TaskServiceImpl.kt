/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.impl

import com.aizuda.bpm.engine.TaskAccessStrategy
import com.aizuda.bpm.engine.TaskService
import com.aizuda.bpm.engine.TaskTrigger
import com.aizuda.bpm.engine.assist.Assert.illegal
import com.aizuda.bpm.engine.assist.Assert.isEmpty
import com.aizuda.bpm.engine.assist.Assert.isFalse
import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.assist.Assert.isTrue
import com.aizuda.bpm.engine.assist.DateUtils.currentDate
import com.aizuda.bpm.engine.assist.DateUtils.now
import com.aizuda.bpm.engine.assist.DateUtils.toDate
import com.aizuda.bpm.engine.assist.ObjectUtils
import com.aizuda.bpm.engine.assist.ObjectUtils.isNotEmpty
import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.enums.AgentType
import com.aizuda.bpm.engine.core.enums.EventType
import com.aizuda.bpm.engine.core.enums.InstanceState
import com.aizuda.bpm.engine.core.enums.PerformType
import com.aizuda.bpm.engine.core.enums.TaskState
import com.aizuda.bpm.engine.core.enums.TaskType
import com.aizuda.bpm.engine.dao.FlwExtInstanceDao
import com.aizuda.bpm.engine.dao.FlwHisInstanceDao
import com.aizuda.bpm.engine.dao.FlwHisTaskActorDao
import com.aizuda.bpm.engine.dao.FlwHisTaskDao
import com.aizuda.bpm.engine.dao.FlwInstanceDao
import com.aizuda.bpm.engine.dao.FlwTaskActorDao
import com.aizuda.bpm.engine.dao.FlwTaskDao
import com.aizuda.bpm.engine.entity.FlwHisInstance
import com.aizuda.bpm.engine.entity.FlwHisTask
import com.aizuda.bpm.engine.entity.FlwHisTaskActor
import com.aizuda.bpm.engine.entity.FlwHisTaskActor.Companion.ofFlwHisTask
import com.aizuda.bpm.engine.entity.FlwHisTaskActor.Companion.ofNodeAssignee
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.aizuda.bpm.engine.listener.TaskListener
import com.aizuda.bpm.engine.model.NodeModel
import java.util.Optional
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * 任务执行业务类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class TaskServiceImpl(
    private val taskAccessStrategy: TaskAccessStrategy?,
    private val taskListener: TaskListener?,
    private val taskTrigger: TaskTrigger?,
    private val instanceDao: FlwInstanceDao,
    private val extInstanceDao: FlwExtInstanceDao,
    private val hisInstanceDao: FlwHisInstanceDao,
    private val taskDao: FlwTaskDao,
    private val taskActorDao: FlwTaskActorDao,
    private val hisTaskDao: FlwHisTaskDao,
    private val hisTaskActorDao: FlwHisTaskActorDao,
) : TaskService {
    /**
     * 更新当前执行节点信息
     *
     * @param flwTask 当前所在执行任务
     */
    protected fun updateCurrentNode(flwTask: FlwTask) {
        val flwInstance = FlwInstance()
        flwInstance.id = flwTask.instanceId
        flwInstance.currentNodeName = flwTask.taskName
        flwInstance.currentNodeKey = flwTask.taskKey
        flwInstance.lastUpdateBy = flwTask.createBy
        flwInstance.lastUpdateTime = currentDate
        instanceDao.updateById(flwInstance)
        val flwHisInstance = FlwHisInstance()
        flwHisInstance.id = flwInstance.id
        flwHisInstance.currentNodeName = flwInstance.currentNodeName
        flwHisInstance.currentNodeKey = flwInstance.currentNodeKey
        flwHisInstance.lastUpdateBy = flwInstance.lastUpdateBy
        flwHisInstance.lastUpdateTime = flwInstance.lastUpdateTime
        hisInstanceDao.updateById(flwHisInstance)
    }

    /**
     * 执行任务
     *
     * @param taskId      任务ID
     * @param flowCreator 任务创建者
     * @param args        执行参数
     * @param taskState   任务状态
     * @param eventType   执行事件
     * @return [FlwTask]
     */
    public override fun executeTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        args: Map<String?, Any?>?,
        taskState: TaskState?,
        eventType: EventType?,
    ): FlwTask {
        val flwTask = this.getAllowedFlwTask(taskId, flowCreator, args, taskState)

        // 触发器情况直接移除任务
        if (PerformType.TRIGGER.eq(flwTask.performType)) {
            taskDao.deleteById(flwTask.id)
            return flwTask
        }

        // 迁移任务至历史表
        this.moveToHisTask(flwTask, taskState, flowCreator)

        // 任务监听器通知
        this.taskNotify(eventType, { flwTask }, null, flowCreator)
        return flwTask
    }

    /**
     * 强制完成所有任务
     */
    override fun forceCompleteAllTask(
        instanceId: Long?,
        flowCreator: FlowCreator?,
        instanceState: InstanceState?,
        eventType: EventType?,
    ): Boolean {
        val flwTasks = taskDao.selectListByInstanceId(instanceId)
        if (null != flwTasks) {
            val taskState = TaskState.of(instanceState!!)
            flwTasks.forEach(
                Consumer<FlwTask?> { t ->
                    this.moveToHisTask(t, taskState, flowCreator)
                    // 任务监听器通知
                    this.taskNotify(eventType, { t }, null, flowCreator)
                }
            )
        }
        return true
    }

    /**
     * 执行节点跳转任务
     */
    override fun executeJumpTask(
        taskId: Long?,
        nodeKey: String?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        executionFunction: Function<FlwTask, Execution?>?,
    ): Boolean {
        val flwTask = this.getAllowedFlwTask(taskId, flowCreator, null, null)

        // 执行跳转到目标节点
        val execution = executionFunction?.apply(flwTask)
        val processModel = execution?.processModel
        isNull(processModel, "当前任务未找到流程定义模型")
        execution?.args = args

        // 查找模型节点
        val nodeModel =
            if (null == nodeKey) {
                // 1，找到当前节点的父节点
                processModel?.getNode(flwTask.taskKey)!!.parentNode
            } else {
                // 2，找到指定 nodeName 节点
                processModel?.getNode(nodeKey)
            }
        isNull(nodeModel!!, "根据节点key[$nodeKey]无法找到节点模型")

        // 获取当前执行实例的所有正在执行的任务，强制终止执行并跳到指定节点
        taskDao.selectListByInstanceId(flwTask.instanceId).forEach { t ->
            this.moveToHisTask(t, TaskState.JUMP, flowCreator)
        }

        if (0 == nodeModel.type) {
            // 发起节点，创建发起任务，分配发起人
            val initiationTask = this.createTaskBase(nodeModel, execution!!)
            initiationTask.setPerformType(PerformType.START)
            isFalse(taskDao.insert(initiationTask) > 0, "Failed to create initiation task")
            taskActorDao.insert(FlwTaskActor.ofFlwInstance(execution.flwInstance!!, initiationTask.id))
        } else {
            // 其它节点创建
            this.createTask(nodeModel, execution)
        }

        // 任务监听器通知
        this.taskNotify(EventType.JUMP, { flwTask }, nodeModel, flowCreator)
        return true
    }

    /**
     * 获取执行任务并验证合法性
     *
     * @param taskId      任务ID
     * @param flowCreator 任务创建者
     * @param args        执行参数
     * @param taskState   [TaskState]
     * @return 流程任务
     */
    protected fun getAllowedFlwTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        args: Map<String?, Any?>?,
        taskState: TaskState?,
    ): FlwTask {
        val flwTask = taskDao.selectCheckById(taskId)
        if (null != args) {
            flwTask.setVariable(args)
        }
        if (null == taskState || TaskState.allowedCheck(taskState)) {
            isNull(
                isAllowed(flwTask, flowCreator?.createId),
                "当前参与者 [" + flowCreator?.createBy + "]不允许执行任务[taskId=" + taskId + "]"
            )
        }
        return flwTask
    }

    /**
     * 迁移任务至历史表
     *
     * @param flwTask     执行任务
     * @param taskState   任务状态
     * @param flowCreator 任务创建者
     * @return true 成功 false 失败
     */
    protected fun moveToHisTask(
        flwTask: FlwTask?,
        taskState: TaskState?,
        flowCreator: FlowCreator?,
    ): Boolean {
        // 获取当前所有处理人员
        var taskActors = taskActorDao.selectListByTaskId(flwTask?.id)
        if (taskState != TaskState.AUTO_COMPLETE &&
            taskState != TaskState.AUTO_REJECT &&
            taskState != TaskState.AUTO_JUMP &&
            ObjectUtils.isEmpty(taskActors)
        ) {
            // 非自动处理，不存在处理人，不再继续执行
            return true
        }

        // 迁移 task 信息到 flw_his_task
        val hisTask = FlwHisTask.of(flwTask!!)
        hisTask.setTaskState(taskState)
        hisTask.setFlowCreator(flowCreator)
        hisTask.calculateDuration()

        // 代理人审批
        if (TaskType.AGENT.eq(flwTask.taskType)) {
            // 当前处理人为代理人员

            val agentFlwTaskActor =
                taskActors
                    .stream()
                    .filter { t: FlwTaskActor? ->
                        t!!.agentActor() &&
                            t.eqActorId(flowCreator?.createId)
                    }.findFirst()
                    .orElse(null)
            if (null != agentFlwTaskActor) {
                // 设置历史代理任务状态为【代理人协办完成的任务】设置被代理人信息

                hisTask.setTaskType(TaskType.AGENT_ASSIST)
                taskActors
                    .stream()
                    .filter { t: FlwTaskActor? -> agentFlwTaskActor.agentId == t!!.actorId }
                    .findFirst()
                    .ifPresent { t: FlwTaskActor? ->
                        hisTask.assignorId = t!!.actorId
                        hisTask.assignor = t.actorName

                        // 更新被代理人信息
                        val flwTaskActor = FlwTaskActor.ofAgentIt(flowCreator)
                        flwTaskActor.id = t.id
                        taskActorDao.updateById(flwTaskActor)
                    }
                hisTaskDao.insert(hisTask)

                // 迁移任务当前代理人员
                hisTaskActorDao.insert(FlwHisTaskActor.of(agentFlwTaskActor))

                // 清理其它代理人
                taskActorDao.deleteByTaskIdAndAgentType(flwTask.id, 0)

                // 代理人完成任务，当前任务设置为代理人归还任务，代理人信息变更
                val newFlwTask = FlwTask()
                newFlwTask.id = flwTask.id
                newFlwTask.setTaskType(TaskType.AGENT_RETURN)
                newFlwTask.assignorId = flowCreator?.createId
                newFlwTask.assignor = flowCreator?.createBy
                return taskDao.updateById(newFlwTask) > 0
            } else {
                // 当前处理人员为被代理人，删除代理人员

                val newFlwTaskActor: MutableList<FlwTaskActor> = ArrayList()
                for (taskActor in taskActors) {
                    if (taskActor.agentActor() == true) {
                        taskActorDao.deleteById(taskActor.id)
                    } else {
                        newFlwTaskActor.add(taskActor)
                    }
                }
                taskActors = newFlwTaskActor
                // 设置被代理人自己完成任务
                flwTask.setTaskType(TaskType.AGENT_OWN)
            }
        } else if (TaskType.AGENT_RETURN.eq(flwTask.taskType)) {
            hisTaskActorDao.deleteByTaskId(flwTask.id)
            hisTaskDao.deleteById(flwTask.id)

            // 代理人协办完成的任务
            hisTask.setTaskType(TaskType.AGENT_ASSIST)
        }

        // 会签情况处理其它任务 排除完成情况
        if (PerformType.COUNTERSIGN.eq(flwTask.performType) && TaskState.COMPLETE.ne(taskState?.value)) {
            val flwTaskList = taskDao.selectListByParentTaskId(flwTask.parentTaskId)
            flwTaskList.forEach { t ->
                val ht = FlwHisTask.of(t)
                ht.setTaskState(taskState)
                ht.setFlowCreator(flowCreator)
                ht.calculateDuration()
                ht.taskType = hisTask.taskType
                hisTaskDao.insert(ht)
            }

            val taskIds = flwTaskList.map { it.id }

            // 迁移任务参与者
            this.moveToHisTaskActor(taskActorDao.selectListByTaskIds(taskIds))

            // 删除会签任务
            return taskDao.deleteByIds(taskIds) > 0
        }

        // 迁移任务至历史表
        isFalse(hisTaskDao.insert(hisTask) > 0, "Migration to FlwHisTask table failed")

        // 迁移任务参与者
        this.moveToHisTaskActor(taskActors)

        // 删除 flw_task 中指定 task 信息
        return taskDao.deleteById(flwTask.id) > 0
    }

    /**
     * 迁移任务参与者至历史表
     *
     * @param taskActors 任务参与者列表
     */
    protected fun moveToHisTaskActor(taskActors: List<FlwTaskActor>) {
        taskActors.forEach { t ->
            // 将 task 参与者信息迁移到 flw_his_task_actor
            hisTaskActorDao.insert(FlwHisTaskActor.of(t))
            // 移除 flw_task_actor 中 task 参与者信息
            taskActorDao.deleteById(t.id)
        }
    }

    protected fun taskNotify(
        eventType: EventType?,
        supplier: Supplier<FlwTask?>,
        nodeModel: NodeModel?,
        flowCreator: FlowCreator?,
    ) {
        taskListener?.notify(eventType, supplier, nodeModel, flowCreator)
    }

    override fun executeTaskTrigger(
        execution: Execution?,
        flwTask: FlwTask?,
    ): Boolean {
        val nodeModel = execution?.processModel?.getNode(flwTask?.taskKey)
        nodeModel!!.executeTrigger(execution) { e: Exception? ->
            // 使用默认触发器
            val taskTrigger = execution.engine.context!!.taskTrigger ?: return@executeTrigger false
            taskTrigger.execute(nodeModel, execution)
        }

        // 任务监听器通知
        this.taskNotify(EventType.TRIGGER, { flwTask }, nodeModel, execution.flowCreator)

        /*
         * 可能存在子节点
         */
        nodeModel.nextNode().ifPresent { nextNode: NodeModel ->
            nextNode.execute(execution.engine.context!!, execution)
        }
        return true
    }

    /**
     * 完成指定实例ID活动任务
     *
     * @param instanceId 实例ID
     * @return true 成功 false 失败
     */
    override fun completeActiveTasksByInstanceId(
        instanceId: Long?,
        flowCreator: FlowCreator?,
    ): Boolean {
        val flwTasks = taskDao.selectListByInstanceId(instanceId)
        if (isNotEmpty(flwTasks)) {
            for (flwTask in flwTasks) {
                // 迁移任务至历史表，设置任务状态为终止
                if (!this.moveToHisTask(flwTask, TaskState.TERMINATE, flowCreator)) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 更新任务对象的 finishTime、createBy、expireTime、version、variable
     *
     * @param flwTask 任务对象
     */
    override fun updateTaskById(
        flwTask: FlwTask?,
        flowCreator: FlowCreator?,
    ) {
        taskDao.updateById(flwTask)
        // 任务监听器通知
        this.taskNotify(EventType.UPDATE, { flwTask }, null, flowCreator)
    }

    /**
     * 查看任务设置为已阅状态
     *
     * @param taskId    任务ID
     * @param taskActor 任务参与者
     */
    override fun viewTask(
        taskId: Long?,
        taskActor: FlwTaskActor?,
    ): Boolean {
        if (taskActorDao.selectCountByTaskIdAndActorId(taskId, taskActor?.actorId) > 0) {
            /*
             * 设置任务为已阅状态
             */
            val flwTask = FlwTask()
            flwTask.id = taskId
            flwTask.viewed = 1
            return taskDao.updateById(flwTask) > 0
        }
        return false
    }

    override fun claimRole(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): FlwTask =
        claim(taskId, AgentType.CLAIM_ROLE, EventType.CLAIM_ROLE, flowCreator)

    override fun claimDepartment(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): FlwTask =
        claim(taskId, AgentType.CLAIM_DEPARTMENT, EventType.CLAIM_DEPARTMENT, flowCreator)

    /**
     * 根据 任务ID 认领任务，删除其它任务参与者
     *
     * @param taskId      任务ID
     * @param agentType   代理人类型
     * @param eventType   流程引擎监听类型
     * @param flowCreator 任务认领者
     * @return Task 任务对象
     */
    protected fun claim(
        taskId: Long?,
        agentType: AgentType?,
        eventType: EventType?,
        flowCreator: FlowCreator?,
    ): FlwTask {
        val flwTask = taskDao.selectCheckById(taskId)
        val taskActor = this.isAllowed(flwTask, flowCreator?.createId)
        if (null == taskActor) {
            illegal("当前执行用户ID [" + flowCreator?.createBy + "] 不允许认领任务 [taskId=" + taskId + "]")
        }

        // 删除任务参与者
        taskActorDao.deleteById(taskActor!!.id)

        // 插入当前用户ID作为唯一参与者
        taskActorDao.insert(FlwTaskActor.ofAgent(agentType!!, flowCreator, flwTask, taskActor))

        // 任务监听器通知
        this.taskNotify(eventType, { flwTask }, null, flowCreator)
        return flwTask
    }

    /**
     * 根据 任务ID 分配任务给指定办理人、重置任务类型
     *
     * @param taskId               任务ID
     * @param taskType             任务类型
     * @param flowCreator          任务参与者
     * @param assigneeFlowCreators 指定办理人列表
     * @return true 成功 false 失败
     */
    override fun assigneeTask(
        taskId: Long?,
        taskType: TaskType?,
        flowCreator: FlowCreator?,
        assigneeFlowCreators: List<FlowCreator>,
    ): Boolean {
        // 受理任务权限验证
        val flwTaskActor = this.getAllowedFlwTaskActor(taskId, flowCreator)

        // 不允许重复分配
        val dbFlwTask = taskDao.selectById(taskId)
        if (isNotEmpty(dbFlwTask!!.assignorId)) {
            illegal("Do not allow duplicate assign , taskId = $taskId")
        }

        // 设置任务为委派任务或者为转办任务
        val flwTask = FlwTask()
        flwTask.id = taskId
        flwTask.setTaskType(taskType)

        if (taskType == TaskType.AGENT) {
            // 设置代理人员信息，第一个人为主办 assignorId 其他人为协办 assignor 多个英文逗号分隔
            val afc = assigneeFlowCreators[0]
            flwTask.assignorId = afc.createId
            flwTask.assignor =
                assigneeFlowCreators.stream().map<String>(FlowCreator::createBy).collect(Collectors.joining(", "))
            // 分配代理人可见代理任务
            assigneeFlowCreators.forEach(
                Consumer { t: FlowCreator? ->
                    taskActorDao.insert(FlwTaskActor.ofAgent(AgentType.AGENT, t!!, dbFlwTask, flwTaskActor))
                }
            )
        } else {
            // 设置委托人信息
            flwTask.assignorId = flowCreator?.createId
            flwTask.assignor = flowCreator?.createBy

            // 删除任务历史参与者
            taskActorDao.deleteById(flwTaskActor?.id)

            // 分配任务给办理人
            val afc = assigneeFlowCreators[0]
            this.assignTask(flwTaskActor?.instanceId, taskId, flwTaskActor?.actorType, FlwTaskActor.ofFlowCreator(afc))
        }

        // 更新任务
        taskDao.updateById(flwTask)

        // 任务监听器通知
        this.taskNotify(EventType.ASSIGNMENT, {
            dbFlwTask.setTaskType(taskType)
            dbFlwTask.assignorId = flwTask.assignorId
            dbFlwTask.assignor = flwTask.assignor
            dbFlwTask
        }, null, flowCreator)
        return true
    }

    /**
     * 获取指定 任务ID 合法参与者对象
     *
     * @param taskId      任务ID
     * @param flowCreator 任务参与者
     * @return 任务参与者
     */
    protected fun getAllowedFlwTaskActor(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): FlwTaskActor? {
        val taskActors = taskActorDao.selectListByTaskIdAndActorId(taskId, flowCreator?.createId)
        return taskAccessStrategy?.getAllowedFlwTaskActor(taskId, flowCreator, taskActors)
    }

    /**
     * 根据 任务ID 解决委派任务
     *
     * @param taskId      任务ID
     * @param flowCreator 任务参与者
     * @return true 成功 false 失败
     */
    override fun resolveTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): Boolean {
        // 解决任务权限验证
        val flwTaskActor = this.getAllowedFlwTaskActor(taskId, flowCreator)

        // 当前委托任务
        val flwTask = taskDao.selectCheckById(taskId)

        // 任务归还至委托人
        val taskActor = FlwHisTaskActor()
        taskActor.id = flwTaskActor?.id
        taskActor.actorId = flwTask.assignorId
        taskActor.actorName = flwTask.assignor
        if (taskActorDao.updateById(taskActor) > 0) {
            // 设置任务状态为委托归还，委托人设置为归还人
            val temp = FlwTask()
            temp.id = taskId
            temp.setTaskType(TaskType.DELEGATE_RETURN)
            temp.assignorId = flowCreator?.createId
            temp.assignor = flowCreator?.createBy
            isFalse(taskDao.updateById(temp) > 0, "resolveTask failed")

            // 任务监听器通知
            this.taskNotify(EventType.DELEGATE_RESOLVE, {
                flwTask.taskType = temp.taskType
                flwTask.assignorId = temp.createId
                flwTask.assignor = temp.createBy
                flwTask
            }, null, flowCreator)
        }
        return true
    }

    /**
     * 拿回任务、根据历史任务ID撤回下一个节点的任务、恢复历史任务
     */
    override fun reclaimTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): Optional<FlwTask> {
        // 下面执行撤回逻辑

        val flwTaskOptional =
            this.undoHisTask(
                taskId,
                flowCreator
            ) { hisTask ->
                var checkReclaim = true
                // 顺序签或会签情况，判断存在未执行并行任务不检查允许拿回
                if (PerformType.SORT.eq(hisTask.performType) || PerformType.COUNTERSIGN.eq(hisTask.performType)) {
                    checkReclaim = taskDao.selectCountByParentTaskId(hisTask.parentTaskId) < 1
                }
                if (checkReclaim) {
                    // 当前任务子任务已经执行完成不允许撤回
                    isTrue(taskDao.selectCountByParentTaskId(taskId) == 0L, "Do not allow reclaim task")
                }

                val flwTaskList = taskDao.selectListByInstanceId(hisTask.instanceId)
                isEmpty(flwTaskList, "No approval tasks found")
                val existFlwTask = flwTaskList[0]
                if (!PerformType.COUNTERSIGN.eq(existFlwTask.performType)) {
                    // 非会签情况
                    isFalse(existFlwTask.parentTaskId == taskId, "Do not allow cross level reclaim task")
                }
                flwTaskList.forEach(
                    Consumer { flwTask ->
                        this.moveToHisTask(flwTask, TaskState.REVOKE, flowCreator)
                    }
                )
            }

        // 任务监听器通知
        flwTaskOptional.ifPresent { flwTask: FlwTask? ->
            this.taskNotify(EventType.RECLAIM, { flwTask }, null, flowCreator)
        }
        return flwTaskOptional
    }

    /**
     * 唤醒指定的历史任务
     */
    override fun resume(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): FlwTask {
        val histTask = hisTaskDao.selectCheckById(taskId)
        isTrue(
            ObjectUtils.isEmpty(histTask.createBy) || histTask.createBy != flowCreator?.createBy,
            "当前参与者[" + flowCreator?.createBy + "]不允许唤醒历史任务[taskId=" + taskId + "]"
        )

        // 流程实例结束情况恢复流程实例
        val flwInstance = instanceDao.selectById(histTask.instanceId)
        isNull(flwInstance!!, "已结束流程任务不支持唤醒")

        // 历史任务恢复
        val flwTask = histTask.cloneTask(null)
        taskDao.insert(flwTask)

        // 历史任务参与者恢复
        val hisTaskActors = hisTaskActorDao.selectListByTaskId(taskId)
        hisTaskActors.forEach { t ->
            taskActorDao.insert(FlwTaskActor.ofFlwHisTaskActor(flwTask.id, t))
        }

        // 更新当前执行节点信息
        this.updateCurrentNode(flwTask)

        // 任务监听器通知
        this.taskNotify(EventType.RESUME, { flwTask }, null, flowCreator)
        return flwTask
    }

    /**
     * 撤回指定的任务
     */
    override fun withdrawTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): Optional<FlwTask> =
        this.undoHisTask(taskId, flowCreator) { hisTask: FlwHisTask ->
            var flwTasks: List<FlwTask?>? = null
            val performType = PerformType.get(hisTask.performType)
            if (performType == PerformType.COUNTERSIGN) {
                // 根据父任务ID查询所有子任务
                flwTasks = taskDao.selectListByParentTaskId(hisTask.id)
            } else {
                val hisTaskIds =
                    hisTaskDao
                        .selectListByInstanceIdAndTaskNameAndParentTaskId(
                            hisTask.instanceId,
                            hisTask.taskName,
                            hisTask.parentTaskId
                        ).map { it.id }
                if (isNotEmpty(hisTaskIds)) {
                    flwTasks = taskDao.selectListByParentTaskIds(hisTaskIds)
                }
            }
            isEmpty(flwTasks, "后续活动任务已完成或不存在，无法撤回.")
            val taskIds = flwTasks?.map { it?.id }
            // 查询任务参与者
            val taskActorIds =
                taskActorDao
                    .selectListByTaskIds(taskIds)
                    .map { it.id }
            if (isNotEmpty(taskActorIds)) {
                taskActorDao.deleteByIds(taskActorIds)
            }
            taskDao.deleteByIds(flwTasks?.map { it?.id })

            // 任务监听器通知
            this.taskNotify(EventType.WITHDRAW, { hisTask }, null, flowCreator)
        }

    public override fun rejectTask(
        currentFlwTask: FlwTask?,
        flowCreator: FlowCreator?,
        args: Map<String?, Any?>?,
    ): Optional<FlwTask> {
        isTrue(currentFlwTask?.startNode() == true, "上一步任务ID为空，无法驳回至上一步处理")

        // 执行任务驳回
        this.executeTask(currentFlwTask?.id, flowCreator, args, TaskState.REJECT, EventType.REJECT)

        // 撤回至上一级任务
        val parentTaskId = currentFlwTask?.parentTaskId
        val flwTaskOptional = this.undoHisTask(parentTaskId, flowCreator, null)

        // 任务监听器通知
        flwTaskOptional.ifPresent { flwTask: FlwTask? ->
            this.taskNotify(EventType.RECREATE, { flwTask }, null, flowCreator)
        }
        return flwTaskOptional
    }

    /**
     * 撤回历史任务
     *
     * @param hisTaskId       历史任务ID
     * @param flowCreator     任务创建者
     * @param hisTaskConsumer 历史任务业务处理
     * @return 任务参与者
     */
    protected fun undoHisTask(
        hisTaskId: Long?,
        flowCreator: FlowCreator?,
        hisTaskConsumer: Consumer<FlwHisTask>?,
    ): Optional<FlwTask> {
        val hisTask = hisTaskDao.selectCheckById(hisTaskId)
        hisTaskConsumer?.accept(hisTask)

        // 撤回历史任务
        if (hisTask.startNode()) {
            // 如果直接撤回到发起人，构建发起人关联信息
            val flwTask = hisTask.undoTask()
            taskDao.insert(flwTask)
            taskActorDao.insert(FlwTaskActor.ofFlwTask(flwTask))
        } else {
            if (PerformType.COUNTERSIGN.eq(hisTask.performType)) {
                // 会签任务需要撤回所有子任务
                val hisTasks = hisTaskDao.selectListByParentTaskId(hisTask.parentTaskId)

                // 撤回任务参与者
                val hisTaskActors = hisTaskActorDao.selectListByTaskIds(hisTasks.map { it.id })
                if (null != hisTaskActors) {
                    val taskActorMap: MutableMap<String?, FlwHisTaskActor> = HashMap()
                    for (t in hisTaskActors) {
                        val t1 = taskActorMap[t.actorId]
                        if (null == t1 || t.taskId!! > t1.taskId!!) {
                            // 同一个任务参与者，获取最新的任务
                            taskActorMap[t.actorId] = t
                        }
                    }

                    // 恢复最新历史任务
                    taskActorMap.forEach { (k: String?, v: FlwHisTaskActor) ->
                        hisTasks
                            .stream()
                            .filter { t -> t!!.id == v.taskId }
                            .findFirst()
                            .ifPresent { t ->
                                val flwTask = t.undoTask()
                                taskDao.insert(flwTask)
                                taskActorDao.insert(FlwTaskActor.of(flwTask.id, v))
                            }
                    }
                }
            } else {
                // 恢复历史任务
                val flwTask = hisTask.undoTask()
                taskDao.insert(flwTask)

                // 撤回任务参与者
                val hisTaskActors = hisTaskActorDao.selectListByTaskId(hisTask.id)
                hisTaskActors.forEach { t ->
                    taskActorDao.insert(FlwTaskActor.of(flwTask.id, t))
                }
            }
        }

        // 更新当前执行节点信息
        this.updateCurrentNode(hisTask)
        return Optional.of(hisTask)
    }

    /**
     * 根据已有任务、参与者创建新的任务
     *
     *
     * 适用于动态转派，动态协办等处理且流程图中不体现节点情况
     *
     */
    override fun createNewTask(
        taskId: Long?,
        taskType: TaskType?,
        performType: PerformType?,
        taskActors: List<FlwTaskActor>,
        flowCreator: FlowCreator?,
        executionFunction: Function<FlwTask?, Execution?>?,
    ): List<FlwTask> {
        val flwTask = taskDao.selectCheckById(taskId)
        val newFlwTask = flwTask.cloneTask(flowCreator?.createId, flowCreator?.createBy)
        newFlwTask.setTaskType(taskType)
        newFlwTask.setPerformType(performType)
        newFlwTask.parentTaskId = taskId
        val execution = executionFunction?.apply(newFlwTask)
        execution?.flowCreator = flowCreator
        return this.saveTask(newFlwTask, performType, taskActors, execution, null)
    }

    /**
     * 对指定的任务分配参与者。参与者可以为用户、部门、角色
     *
     * @param instanceId 实例ID
     * @param taskId     任务ID
     * @param actorType  参与者类型 0，用户 1，角色 2，部门
     * @param taskActor  任务参与者
     */
    protected fun assignTask(
        instanceId: Long?,
        taskId: Long?,
        actorType: Int?,
        taskActor: FlwTaskActor?,
    ) {
        taskActor?.id = null
        taskActor?.instanceId = instanceId
        taskActor?.taskId = taskId
        taskActor?.actorType = actorType
        taskActorDao.insert(taskActor)
    }

    override val timeoutOrRemindTasks: List<FlwTask>
        /**
         * 获取超时或者提醒的任务
         *
         * @return 任务列表
         */
        get() = taskDao.selectListTimeoutOrRemindTasks(currentDate)

    /**
     * 获取任务模型
     *
     * @param taskId 任务ID
     * @return 节点模型
     */
    override fun getTaskModel(taskId: Long?): NodeModel? {
        var flwTask: FlwTask? = hisTaskDao.selectById(taskId)
        if (null == flwTask) {
            flwTask = taskDao.selectCheckById(taskId)
        }
        val extInstance = extInstanceDao.selectById(flwTask.instanceId)
        val model = extInstance!!.model()
        val nodeModel = model!!.getNode(flwTask.taskKey)
        if (null == nodeModel) {
            illegal("Cannot find NodeModel. taskId = $taskId")
        }
        return nodeModel
    }

    /**
     * 创建 task 根据 model 决定是否分配参与者
     *
     * @param nodeModel 节点模型
     * @param execution 执行对象
     * @return 任务列表
     */
    override fun createTask(
        nodeModel: NodeModel?,
        execution: Execution?,
    ): List<FlwTask> {
        // 构建任务
        val flwTask = this.createTaskBase(nodeModel!!, execution!!)

        // 模型中获取参与者信息
        val taskActors = execution.taskActorProvider!!.getTaskActors(nodeModel, execution)
        val flwTasks: MutableList<FlwTask> = ArrayList()

        // 处理流程任务
        val nodeType = nodeModel.type

        // 更新当前执行节点信息，抄送节点除外
        if (!TaskType.CC.eq(nodeType)) {
            this.updateCurrentNode(flwTask)
        }

        if (TaskType.MAJOR.eq(nodeType)) {
            /*
             * 0，发起人 （ 直接保存历史任务、执行进入下一个节点逻辑 ）
             */
            flwTasks.addAll(this.saveTask(flwTask, PerformType.START, taskActors, execution, nodeModel))

            /*
             * 执行进入下一个节点
             */
            nodeModel.nextNode().ifPresent { nextNode: NodeModel ->
                nextNode.execute(execution.engine.context!!, execution)
            }
        } else if (TaskType.APPROVAL.eq(nodeType)) {
            /*
             * 1，审批人
             */
            val performType = PerformType.get(nodeModel.examineMode)
            flwTasks.addAll(this.saveTask(flwTask, performType, taskActors, execution, nodeModel))
            // 审批提醒
            val taskReminder = execution.engine.context!!.taskReminder
            if (null != taskReminder) {
                flwTasks.forEach(
                    Consumer { s: FlwTask? ->
                        taskReminder.remind(execution.engine.context, s!!.instanceId, s)
                    }
                )
            }
        } else if (TaskType.CC.eq(nodeType)) {
            /*
             * 2，抄送任务
             */
            this.saveTaskCc(nodeModel, flwTask, execution.flowCreator)

            /*
             * 可能存在子节点
             */
            nodeModel.nextNode().ifPresent { nextNode: NodeModel ->
                nextNode.execute(execution.engine.context!!, execution)
            }
        } else if (TaskType.CONDITION_NODE.eq(nodeType)) {
            /*
             * 3，条件审批
             */
            val singleFlwTask = flwTask.cloneTask(null)
            val performType = PerformType.get(nodeModel.examineMode)
            flwTasks.addAll(this.saveTask(singleFlwTask, performType, taskActors, execution, nodeModel))
        } else if (TaskType.CALL_PROCESS.eq(nodeType)) {
            /*
             * 5，办理子流程
             */
            val flowCreator = execution.flowCreator
            val callProcess = nodeModel.callProcess
            isEmpty(callProcess, "The execution parameter callProcess does not exist")
            val callProcessArr = callProcess!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val processService = execution.engine.processService()
            val flwProcess =
                if (2 == callProcessArr.size) {
                    processService!!.getProcessById(callProcessArr[0].toLong())
                } else {
                    processService!!.getProcessByKey(flowCreator?.tenantId, callProcessArr[0])
                }
            if (null == flwProcess) {
                illegal("No found flwProcess, callProcess=$callProcess")
            }
            // 启动子流程，任务归档历史
            execution.engine
                .startProcessInstance(flwProcess, flowCreator, null) {
                    val flwInstance = FlwInstance()
                    flwInstance.parentInstanceId = flwTask.instanceId
                    flwInstance
                }.ifPresent { instance: FlwInstance ->
                    // 归档历史
                    val flwHisTask = FlwHisTask.ofCallInstance(nodeModel, instance)
                    if (hisTaskDao.insert(flwHisTask) > 0) {
                        // 追加子流程实例ID
                        nodeModel.callProcess = nodeModel.callProcess + ":" + instance.id
                        // 主流程监听
                        this.taskNotify(EventType.CALL_PROCESS, { flwHisTask }, nodeModel, flowCreator)
                    }
                }
        } else if (TaskType.TIMER.eq(nodeType)) {
            /*
             * 6，定时器任务
             */
            flwTask.loadExpireTime(nodeModel.extendConfig, true)
            flwTasks.addAll(this.saveTask(flwTask, PerformType.TIMER, taskActors, execution, nodeModel))
        } else if (TaskType.TRIGGER.eq(nodeType)) {
            /*
             * 7、触发器任务
             */
            flwTask.loadExpireTime(nodeModel.extendConfig, false)
            if (null == flwTask.expireTime) {
                // 立即触发器，直接执行
                execution.flwTask = flwTask
                // 使用默认触发器
                nodeModel.executeTrigger(execution) { taskTrigger?.execute(nodeModel, execution) ?: false }
                // 执行成功，任务归档
                val hisTask = FlwHisTask.of(flwTask)
                hisTask.setTaskState(TaskState.COMPLETE)
                hisTask.setFlowCreator(execution.flowCreator)
                hisTask.calculateDuration()
                hisTaskDao.insert(hisTask)

                // 任务监听器通知
                this.taskNotify(EventType.TRIGGER, { hisTask }, nodeModel, execution.flowCreator)

                /*
                 * 可能存在子节点
                 */
                nodeModel.nextNode().ifPresent { nextNode: NodeModel ->
                    nextNode.execute(execution.engine.context!!, execution)
                }
            } else {
                // 定时触发器，等待执行
                flwTasks.addAll(this.saveTask(flwTask, PerformType.TRIGGER, taskActors, execution, nodeModel))
            }
        }

        return flwTasks
    }

    /**
     * 保存抄送任务
     *
     * @param nodeModel   节点模型
     * @param flwTask     流程任务对象
     * @param flowCreator 处理人
     */
    public fun saveTaskCc(
        nodeModel: NodeModel,
        flwTask: FlwTask?,
        flowCreator: FlowCreator?,
    ) {
        val nodeUserList = nodeModel.nodeAssigneeList
        if (isNotEmpty(nodeUserList)) {
            // 抄送任务
            val flwHisTask = FlwHisTask.of(flwTask!!, TaskState.COMPLETE)
            flwHisTask.setTaskType(TaskType.CC)
            flwHisTask.setPerformType(PerformType.COPY)
            flwHisTask.calculateDuration()
            hisTaskDao.insert(flwHisTask)

            // 历史任务参与者数据入库
            for (nodeUser in nodeUserList!!) {
                hisTaskActorDao.insert(ofNodeAssignee(nodeUser, flwHisTask.instanceId, flwHisTask.id))
            }

            // 任务监听器通知
            this.taskNotify(EventType.CC, { flwHisTask }, nodeModel, flowCreator)
        }
    }

    /**
     * 根据模型、执行对象、任务类型构建基本的task对象
     *
     * @param nodeModel 节点模型
     * @param execution 执行对象
     * @return Task任务对象
     */
    private fun createTaskBase(
        nodeModel: NodeModel,
        execution: Execution,
    ): FlwTask {
        val flwTask = FlwTask()
        flwTask.setFlowCreator(execution.flowCreator)
        flwTask.createTime = currentDate
        flwTask.instanceId = execution.flwInstance!!.id
        flwTask.taskName = nodeModel.nodeName
        flwTask.taskKey = nodeModel.nodeKey
        flwTask.taskType = nodeModel.type
        // 触发器 父任务ID flwTask 不为 null 但 getFlwTask().getId() == null
        val executionTask = execution.flwTask
        if (executionTask?.id == null) {
            flwTask.parentTaskId = 0L
        } else {
            flwTask.parentTaskId = executionTask.id
        }
        var args = execution.args
        // 审批期限非空，设置期望任务完成时间
        val term = nodeModel.term
        if (null != term && term > 0) {
            flwTask.expireTime = toDate(now().plusHours(term.toLong()))
            if (null == args) {
                args = HashMap()
            }
            args.put("termMode", nodeModel.termMode)
        }
        flwTask.setVariable(args)
        flwTask.remindRepeat = 0
        flwTask.viewed = 0
        return flwTask
    }

    /**
     * 保存任务及参与者信息
     *
     * @param flwTask     流程任务对象
     * @param performType 参与类型 [PerformType]
     * @param taskActors  参与者ID集合
     * @param execution   流程执行处理类 [Execution]
     * @param nodeModel   流程节点模型对象 [NodeModel]
     * @return 流程任务列表
     */
    protected fun saveTask(
        flwTask: FlwTask,
        performType: PerformType?,
        taskActors: List<FlwTaskActor>?,
        execution: Execution?,
        nodeModel: NodeModel?,
    ): List<FlwTask> {
        val flwTasks: MutableList<FlwTask> = ArrayList()
        flwTask.setPerformType(performType)
        val flowCreator = execution?.flowCreator

        if (performType == PerformType.TIMER || performType == PerformType.TRIGGER) {
            // 定时器任务，触发器任务
            taskDao.insert(flwTask)
            flwTasks.add(flwTask)
            return flwTasks
        }

        if (performType == PerformType.START) {
            // 发起任务
            val flwHisTask = FlwHisTask.of(flwTask, TaskState.COMPLETE)
            flwHisTask.calculateDuration()
            if (hisTaskDao.insert(flwHisTask) > 0) {
                // 设置为执行任务
                execution?.flwTask = flwHisTask
                // 记录发起人
                hisTaskActorDao.insert(ofFlwHisTask(flwHisTask))
                flwTask.id = flwHisTask.id
                flwTasks.add(flwTask)

                // 创建任务监听
                this.taskNotify(EventType.START, { flwTask }, nodeModel, flowCreator)
            }
            return flwTasks
        }

        if (ObjectUtils.isEmpty(taskActors)) {
            illegal(
                "taskActors cannot be empty. taskName = " + flwTask.taskName + ", taskKey = " +
                    flwTask.taskKey + ", performType = " + performType?.value
            )
        }

        // 参与者类型
        val actorType = nodeModel?.actorType()

        if (performType == PerformType.OR_SIGN) {
            /*
             * 或签一条任务多个参与者
             */
            taskDao.insert(flwTask)
            taskActors?.forEach(
                Consumer { t: FlwTaskActor ->
                    this.assignTask(flwTask.instanceId, flwTask.id, actorType, t)
                }
            )
            flwTasks.add(flwTask)

            // 创建任务监听
            this.taskNotify(EventType.CREATE, { flwTask }, nodeModel, flowCreator)
            return flwTasks
        }

        if (performType == PerformType.SORT) {
            /*
             * 按顺序依次审批，一个任务执行完，按顺序多个参与者依次执行
             */
            taskDao.insert(flwTask)
            flwTasks.add(flwTask)

            // 分配下一个参与者
            val nextFlwTaskActor = execution?.nextFlwTaskActor
            this.assignTask(flwTask.instanceId, flwTask.id, actorType, nextFlwTaskActor ?: taskActors?.get(0))

            // 创建任务监听
            this.taskNotify(EventType.CREATE, { flwTask }, nodeModel, flowCreator)
            return flwTasks
        }

        /*
         * 会签（票签）每个参与者生成一条任务
         */
        taskActors?.forEach(
            Consumer<FlwTaskActor> { t: FlwTaskActor ->
                val newFlwTask = flwTask.cloneTask(null)
                taskDao.insert(newFlwTask)
                flwTasks.add(newFlwTask)

                // 分配参与者
                this.assignTask(newFlwTask.instanceId, newFlwTask.id, actorType, t)

                // 创建任务监听
                this.taskNotify(EventType.CREATE, { newFlwTask }, nodeModel, flowCreator)
            }
        )
        return flwTasks
    }

    /**
     * 根据 taskId、createId 判断创建人是否允许执行任务
     *
     * @param flwTask 流程任务
     * @param userId  用户ID
     * @return true 允许 false 不允许
     */
    override fun isAllowed(
        flwTask: FlwTask,
        userId: String?,
    ): FlwTaskActor? {
        // 未指定创建人情况，默认为不验证执行权限
        if (null == flwTask.createId) {
            return null
        }

        // 任务执行创建人不存在
        if (ObjectUtils.isEmpty(userId)) {
            return null
        }

        // 任务参与者列表
        val actors = taskActorDao.selectListByTaskId(flwTask.id)
        return taskAccessStrategy?.isAllowed(userId, actors)
    }

    /**
     * 向指定的任务ID添加参与者
     *
     * @param taskId        任务ID
     * @param flwTaskActors 参与者列表
     */
    override fun addTaskActor(
        taskId: Long?,
        performType: PerformType?,
        flwTaskActors: List<FlwTaskActor>,
        flowCreator: FlowCreator?,
    ): Boolean {
        val flwTask = taskDao.selectCheckById(taskId)
        isTrue(ObjectUtils.isEmpty(flwTaskActors), "actorIds cannot be empty")

        val taskActorList = this.getTaskActorsByTaskId(taskId)

        val taskActorMap =
            taskActorList.stream().collect(
                Collectors.toMap(FlwTaskActor::actorId) { t: FlwTaskActor? -> t }
            )
        for (flwTaskActor in flwTaskActors) {
            // 不存在的参与者
            if (null != taskActorMap[flwTaskActor.actorId]) {
                continue
            }
            if (PerformType.COUNTERSIGN.eq(flwTask.performType)) {
                /*
                 * 会签多任务情况
                 */
                val newFlwTask = flwTask.cloneTask(flowCreator?.createId, flowCreator?.createBy)
                taskDao.insert(newFlwTask)
                this.assignTask(flwTask.instanceId, newFlwTask.id, 0, flwTaskActor)
            } else {
                /*
                 * 单一任务多处理人员情况
                 */
                this.assignTask(flwTask.instanceId, taskId, 0, flwTaskActor)
            }
        }

        // 更新任务参与类型
        val temp = FlwTask()
        temp.id = taskId
        temp.setPerformType(performType)
        if (taskDao.updateById(temp) > 0) {
            // 创建任务监听
            this.taskNotify(EventType.ADD_TASK_ACTOR, { flwTask }, null, flowCreator)
            return true
        }
        return false
    }

    protected fun getTaskActorsByTaskId(taskId: Long?): List<FlwTaskActor> {
        val taskActorList = taskActorDao.selectListByTaskId(taskId)
        isTrue(ObjectUtils.isEmpty(taskActorList), "not found task actor")
        return taskActorList
    }

    override fun removeTaskActor(
        taskId: Long?,
        actorIds: List<String?>,
        flowCreator: FlowCreator?,
    ): Boolean {
        val flwTask = taskDao.selectCheckById(taskId)
        isTrue(ObjectUtils.isEmpty(actorIds), "actorIds cannot be empty")

        if (PerformType.COUNTERSIGN.eq(flwTask.performType)) {
            /*
             * 会签多任务情况
             */
            val taskActorList = taskActorDao.selectListByInstanceId(flwTask.instanceId)
            if (ObjectUtils.isEmpty(taskActorList)) {
                return false
            }
            taskActorList.forEach(
                Consumer { t: FlwTaskActor? ->
                    if (actorIds.contains(t!!.actorId)) {
                        // 删除参与者表
                        taskActorDao.deleteById(t.id)
                        // 删除任务表
                        taskDao.deleteById(t.taskId)
                    }
                }
            )
        } else {
            /*
             * 单一任务多处理人员情况，删除参与者表，任务关联关系
             */
            if (!taskActorDao.deleteByTaskIdAndActorIds(taskId, actorIds)) {
                return false
            }
        }

        // 创建任务监听
        this.taskNotify(EventType.REMOVE_TASK_ACTOR, { flwTask }, null, flowCreator)
        return true
    }

    override fun endCallProcessTask(
        callProcessId: Long?,
        callInstanceId: Long?,
    ) {
        val flwHisTasks = hisTaskDao.selectListByCallProcessIdAndCallInstanceId(callProcessId, callInstanceId)
        if (isNotEmpty(flwHisTasks)) {
            val dbHis = flwHisTasks[0]
            val his = FlwHisTask()
            his.id = dbHis.id
            his.createTime = dbHis.createTime
            his.setTaskState(TaskState.COMPLETE)
            his.calculateDuration()
            his.createTime = null
            hisTaskDao.updateById(his)
        }
    }

    /**
     * 级联删除表 flw_his_task_actor, flw_his_task, flw_task_actor, flw_task
     *
     * @param instanceIds 流程实例ID列表
     */
    override fun cascadeRemoveByInstanceIds(instanceIds: List<Long?>?) {
        // 删除历史任务及参与者
        hisTaskActorDao.deleteByInstanceIds(instanceIds)
        hisTaskDao.deleteByInstanceIds(instanceIds)

        // 删除任务及参与者
        taskActorDao.deleteByInstanceIds(instanceIds)
        taskDao.deleteByInstanceIds(instanceIds)
    }
}
