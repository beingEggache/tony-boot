/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.entity.FlwProcess
import com.aizuda.bpm.engine.model.NodeModel
import com.aizuda.bpm.engine.model.ProcessModel
import java.util.function.Supplier

/**
 * 流程实例运行业务类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface RuntimeService {
    /**
     * 根据流程、创建人员、父流程实例ID创建流程实例
     *
     * @param flwProcess  流程定义对象
     * @param flowCreator 流程实例任务创建者
     * @param args        参数列表
     * @param nodeModel   当前所在节点
     * @param supplier    初始化流程实例提供者
     * @return 活动流程实例对象
     */
    public fun createInstance(
        flwProcess: FlwProcess?,
        flowCreator: FlowCreator?,
        args: Map<String?, Any?>?,
        nodeModel: NodeModel?,
        supplier: Supplier<FlwInstance>?,
    ): FlwInstance

    /**
     * 根据流程实例ID获取流程实例模型
     *
     * @param instanceId 流程实例ID
     * @return [ProcessModel]
     */
    public fun getProcessModelByInstanceId(instanceId: Long?): ProcessModel?

    /**
     * 向指定实例id添加全局变量数据
     *
     * @param instanceId 实例id
     * @param args       变量数据
     */
    public fun addVariable(
        instanceId: Long?,
        args: Map<String?, Any?>,
    )

    /**
     * 结束流程实例（审批通过）
     *
     * @param execution  [Execution]
     * @param instanceId 流程实例ID
     * @param endNode    结束节点
     * @return true 成功 false 失败
     */
    public fun endInstance(
        execution: Execution,
        instanceId: Long?,
        endNode: NodeModel?,
    ): Boolean

    /**
     * 保存流程实例
     *
     * @param flwInstance 流程实例对象
     * @param flwProcess  流程定义对象
     * @param flowCreator 处理人员
     */
    public fun saveInstance(
        flwInstance: FlwInstance?,
        flwProcess: FlwProcess?,
        flowCreator: FlowCreator?,
    )

    /**
     * 流程实例拒绝审批强制终止（用于后续审核人员认为该审批不再需要继续，拒绝审批强行终止）
     *
     * @param instanceId  流程实例ID
     * @param flowCreator 处理人员
     */
    public fun reject(
        instanceId: Long?,
        flowCreator: FlowCreator,
    )

    /**
     * 流程实例撤销（用于错误发起审批申请，发起人主动撤销）
     *
     * @param instanceId  流程实例ID
     * @param flowCreator 处理人员
     */
    public fun revoke(
        instanceId: Long?,
        flowCreator: FlowCreator,
    )

    /**
     * 流程实例超时（设定审批时间超时，自动结束）
     *
     * @param instanceId  流程实例ID
     * @param flowCreator 处理人员
     */
    public fun timeout(
        instanceId: Long?,
        flowCreator: FlowCreator,
    )

    /**
     * 流程实例超时（忽略操作权限）
     *
     * @param instanceId 流程实例ID
     */
    public fun timeout(instanceId: Long?) {
        this.timeout(instanceId, FlowCreator.Companion.ADMIN)
    }

    /**
     * 流程实例强制终止
     *
     * @param instanceId  流程实例ID
     * @param flowCreator 处理人员
     */
    public fun terminate(
        instanceId: Long?,
        flowCreator: FlowCreator,
    )

    /**
     * 更新流程实例
     *
     * @param flwInstance 流程实例对象
     */
    public fun updateInstance(flwInstance: FlwInstance?)

    /**
     * 级联删除指定流程实例的所有数据
     *
     * @param processId 流程ID
     */
    public fun cascadeRemoveByProcessId(processId: Long?)

    /**
     * 追加节点模型（不执行任务跳转）
     *
     *
     * 执行追加节点模型调用 [FlowLongEngine.executeAppendNodeModel]
     *
     *
     * @param taskId      任务ID
     * @param nodeModel   节点模型
     * @param beforeAfter true 前置 false 后置
     */
    public fun appendNodeModel(
        taskId: Long?,
        nodeModel: NodeModel?,
        beforeAfter: Boolean,
    )
}
