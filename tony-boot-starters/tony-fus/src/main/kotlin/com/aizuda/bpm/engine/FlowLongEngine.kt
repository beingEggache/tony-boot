/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.core.enums.PerformType
import com.aizuda.bpm.engine.core.enums.TaskType
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.entity.FlwProcess
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.aizuda.bpm.engine.model.NodeModel
import java.util.Optional
import java.util.function.Supplier

/**
 * FlowLong流程引擎接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlowLongEngine {
    /**
     * 根据Configuration对象配置实现类
     *
     * @param config 全局配置对象
     * @return FlowLongEngine 流程引擎
     */
    public fun configure(config: FlowLongContext?): FlowLongEngine?

    public val context: FlowLongContext?

    /**
     * 获取process服务
     *
     * @return ProcessService 流程定义服务
     */
    public fun processService(): ProcessService? =
        context!!.processService

    /**
     * 获取查询服务
     *
     * @return QueryService 常用查询服务
     */
    public fun queryService(): QueryService? =
        context!!.queryService

    /**
     * 获取实例服务
     *
     * @return RuntimeService 流程实例运行业务服务
     */
    public fun runtimeService(): RuntimeService? =
        context!!.runtimeService

    /**
     * 获取任务服务
     *
     * @return TaskService 任务服务
     */
    public fun taskService(): TaskService? =
        context!!.taskService

    /**
     * 根据流程定义ID，创建人ID，参数列表启动流程实例
     *
     * @param id          流程定义ID
     * @param flowCreator 流程实例任务创建者
     * @param args        参数列表
     * @param supplier    初始化流程实例提供者
     * @return [FlwInstance] 流程实例
     */
    public fun startInstanceById(
        id: Long?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        supplier: Supplier<FlwInstance>?,
    ): Optional<FlwInstance>

    public fun startInstanceById(
        id: Long?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
    ): Optional<FlwInstance> =
        this.startInstanceById(id, flowCreator, args, null)

    public fun startInstanceById(
        id: Long?,
        flowCreator: FlowCreator?,
        businessKey: String?,
    ): Optional<FlwInstance> =
        this.startInstanceById(id, flowCreator, null) {
            FlwInstance.of(businessKey)
        }

    public fun startInstanceById(
        id: Long?,
        flowCreator: FlowCreator?,
    ): Optional<FlwInstance> =
        this.startInstanceById(id, flowCreator, null, null)

    /**
     * 根据流程名称、版本号、创建人、参数列表启动流程实例
     *
     * @param processKey  流程定义key
     * @param version     版本号
     * @param flowCreator 流程实例任务创建者
     * @param args        参数列表
     * @param supplier    初始化流程实例提供者
     * @return [FlwInstance] 流程实例
     */
    public fun startInstanceByProcessKey(
        processKey: String?,
        version: Int?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        supplier: Supplier<FlwInstance>?,
    ): Optional<FlwInstance>

    public fun startInstanceByProcessKey(
        processKey: String?,
        version: Int?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
    ): Optional<FlwInstance> =
        this.startInstanceByProcessKey(processKey, version, flowCreator, args, null)

    public fun startInstanceByProcessKey(
        processKey: String?,
        version: Int?,
        flowCreator: FlowCreator?,
        businessKey: String?,
    ): Optional<FlwInstance> =
        this.startInstanceByProcessKey(processKey, version, flowCreator, null) {
            FlwInstance.of(businessKey)
        }

    public fun startInstanceByProcessKey(
        processKey: String?,
        version: Int?,
        flowCreator: FlowCreator?,
    ): Optional<FlwInstance> =
        this.startInstanceByProcessKey(processKey, version, flowCreator, null, null)

    public fun startInstanceByProcessKey(
        processKey: String?,
        flowCreator: FlowCreator?,
    ): Optional<FlwInstance> =
        this.startInstanceByProcessKey(processKey, null, flowCreator)

    /**
     * 根据流程对象启动流程实例
     *
     * @param process     [FlwProcess]
     * @param flowCreator 流程实例任务创建者
     * @param args        参数列表
     * @param supplier    初始化流程实例提供者
     * @return [FlwInstance] 流程实例
     */
    public fun startProcessInstance(
        process: FlwProcess?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        supplier: Supplier<FlwInstance>?,
    ): Optional<FlwInstance>

    /**
     * 重启流程实例（从当前所在节点currentNode位置开始）
     *
     * @param id          流程定义ID
     * @param currentNode 当前所在节点
     * @param execution   [Execution]
     */
    public fun restartProcessInstance(
        id: Long?,
        currentNode: String?,
        execution: Execution?,
    )

    /**
     * 根据任务ID，创建人ID，参数列表执行任务
     *
     * @param taskId      任务ID
     * @param flowCreator 任务创建者
     * @param args        参数列表
     * @return true 成功 false 失败
     */
    public fun executeTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
    ): Boolean

    public fun executeTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): Boolean =
        this.executeTask(taskId, flowCreator, null)

    /**
     * 自动跳转任务
     *
     * @param taskId      任务ID
     * @param args        任务参数
     * @param flowCreator 任务创建者
     * @return true 成功 false 失败
     */
    public fun autoJumpTask(
        taskId: Long?,
        args: MutableMap<String?, Any?>?,
        flowCreator: FlowCreator?,
    ): Boolean

    public fun autoJumpTask(
        taskId: Long?,
        flowCreator: FlowCreator?,
    ): Boolean =
        this.autoJumpTask(taskId, null, flowCreator)

    /**
     * 自动完成任务
     *
     * @param taskId 任务ID
     * @param args   任务参数
     * @return true 成功 false 失败
     */
    public fun autoCompleteTask(
        taskId: Long?,
        args: MutableMap<String?, Any?>?,
    ): Boolean

    public fun autoCompleteTask(taskId: Long?): Boolean =
        this.autoCompleteTask(taskId, null)

    /**
     * 自动拒绝任务
     *
     * @param taskId 任务ID
     * @param args   任务参数
     * @return true 成功 false 失败
     */
    public fun autoRejectTask(
        taskId: Long?,
        args: MutableMap<String?, Any?>?,
    ): Boolean

    public fun autoRejectTask(taskId: Long?): Boolean =
        this.autoRejectTask(taskId, null)

    /**
     * 根据任务ID，创建人ID，参数列表执行任务，并且根据nodeName跳转到任意节点
     *
     *
     * 1、nodeName为null时，则跳转至上一步处理
     * 2、nodeName不为null时，则任意跳转，即动态创建转移
     *
     *
     * @param taskId      任务ID
     * @param nodeKey     跳转的节点key
     * @param flowCreator 任务创建者
     * @param args        任务参数
     * @return true 成功 false 失败
     */
    public fun executeJumpTask(
        taskId: Long?,
        nodeKey: String?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>? = null,
    ): Boolean

    public fun executeJumpTask(
        taskId: Long?,
        nodeKey: String?,
        flowCreator: FlowCreator?,
    ): Boolean =
        executeJumpTask(taskId, nodeKey, flowCreator, null)

    /**
     * 根据已有任务、参与者创建新的任务
     *
     *
     * 适用于动态转派，动态协办等处理且流程图中不体现节点情况
     *
     *
     * @param taskId      主办任务ID
     * @param taskActors  参与者集合
     * @param taskType    任务类型
     * @param performType 参与类型
     * @param flowCreator 任务创建者
     * @param args        任务参数
     * @return 创建任务集合
     */
    public fun createNewTask(
        taskId: Long?,
        taskType: TaskType?,
        performType: PerformType?,
        taskActors: List<FlwTaskActor>,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
    ): List<FlwTask>

    /**
     * 执行追加节点模型
     *
     * @param taskId      当前任务ID
     * @param nodeModel   加签节点模型
     * @param flowCreator 任务创建者
     * @param args        任务参数
     * @param beforeAfter true 前置 false 后置
     * @return true 成功 false 失败
     */
    public fun executeAppendNodeModel(
        taskId: Long?,
        nodeModel: NodeModel?,
        flowCreator: FlowCreator?,
        args: MutableMap<String?, Any?>?,
        beforeAfter: Boolean,
    ): Boolean

    public fun executeAppendNodeModel(
        taskId: Long?,
        nodeModel: NodeModel?,
        flowCreator: FlowCreator?,
        beforeAfter: Boolean,
    ): Boolean =
        executeAppendNodeModel(taskId, nodeModel, flowCreator, null, beforeAfter)
}
