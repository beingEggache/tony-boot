/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.impl

import com.aizuda.bpm.engine.FlowConstants
import com.aizuda.bpm.engine.FlowDataTransfer.get
import com.aizuda.bpm.engine.QueryService
import com.aizuda.bpm.engine.RuntimeService
import com.aizuda.bpm.engine.TaskService
import com.aizuda.bpm.engine.assist.Assert.illegal
import com.aizuda.bpm.engine.assist.Assert.isFalse
import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.assist.DateUtils.currentDate
import com.aizuda.bpm.engine.assist.ObjectUtils.isNotEmpty
import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.core.enums.EventType
import com.aizuda.bpm.engine.core.enums.InstanceState
import com.aizuda.bpm.engine.dao.FlwExtInstanceDao
import com.aizuda.bpm.engine.dao.FlwHisInstanceDao
import com.aizuda.bpm.engine.dao.FlwInstanceDao
import com.aizuda.bpm.engine.entity.FlwExtInstance
import com.aizuda.bpm.engine.entity.FlwHisInstance
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.entity.FlwProcess
import com.aizuda.bpm.engine.listener.InstanceListener
import com.aizuda.bpm.engine.model.DynamicAssignee
import com.aizuda.bpm.engine.model.NodeModel
import com.aizuda.bpm.engine.model.ProcessModel
import com.tony.utils.toJsonString
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
public class RuntimeServiceImpl(
    private val instanceListener: InstanceListener?,
    private val queryService: QueryService,
    private val taskService: TaskService,
    private val instanceDao: FlwInstanceDao,
    private val hisInstanceDao: FlwHisInstanceDao,
    private val extInstanceDao: FlwExtInstanceDao,
) : RuntimeService {
    /**
     * 创建活动实例
     */
    override fun createInstance(
        flwProcess: FlwProcess?,
        flowCreator: FlowCreator?,
        args: Map<String?, Any?>?,
        nodeModel: NodeModel?,
        supplier: Supplier<FlwInstance>?,
    ): FlwInstance {
        var flwInstance: FlwInstance? = null
        if (null != supplier) {
            flwInstance = supplier.get()
        }
        if (null == flwInstance) {
            flwInstance = FlwInstance()
        }
        flwInstance.createTime = currentDate
        flwInstance.setFlowCreator(flowCreator)
        flwInstance.currentNodeName = nodeModel?.nodeName
        flwInstance.currentNodeKey = nodeModel?.nodeKey
        flwInstance.lastUpdateBy = flwInstance.createBy
        flwInstance.lastUpdateTime = flwInstance.createTime
        flwInstance.processId = flwProcess?.id
        flwInstance.setMapVariable(args)

        /*
         * 处理追加模型逻辑
         */
        val modelData = get<Map<String, Any>>(FlowConstants.PROCESS_DYNAMIC_ASSIGNEE)
        if (isNotEmpty(modelData)) {
            val processModel = flwProcess?.model()
            modelData!!.forEach { (key: String?, value: Any?) ->
                if (value is DynamicAssignee) {
                    val thisNodeModel = processModel!!.getNode(key)
                    if (null != thisNodeModel) {
                        thisNodeModel.nodeAssigneeList = value.assigneeList
                    }
                }
            }
            // 清理父节点
            processModel!!.cleanParentNode(processModel.nodeConfig!!)
            // 更新模型
            flwProcess.setModelContent2Json(processModel)
        }

        // 保存实例
        this.saveInstance(flwInstance, flwProcess, flowCreator)
        return flwInstance
    }

    /**
     * 根据流程实例ID获取流程实例模型
     *
     * @param instanceId 流程实例ID
     * @return [ProcessModel]
     */
    override fun getProcessModelByInstanceId(instanceId: Long?): ProcessModel? {
        val flwExtInstance = extInstanceDao.selectById(instanceId)
        isNull(flwExtInstance, "The process instance model does not exist.")
        return flwExtInstance!!.model()
    }

    /**
     * 向活动实例临时添加全局变量数据
     *
     * @param instanceId 实例id
     * @param args       变量数据
     */
    public override fun addVariable(
        instanceId: Long?,
        args: Map<String?, Any?>,
    ) {
        val flwInstance = instanceDao.selectById(instanceId)
        val data = flwInstance!!.variableToMap()
        data.putAll(args)
        val temp = FlwInstance()
        temp.id = instanceId
        temp.setMapVariable(data)
        instanceDao.updateById(temp)
    }

    /**
     * 删除活动流程实例数据，更新历史流程实例的状态、结束时间
     */
    override fun endInstance(
        execution: Execution,
        instanceId: Long?,
        endNode: NodeModel?,
    ): Boolean {
        val flwInstance = instanceDao.selectById(instanceId)
        if (null != flwInstance) {
            val his = FlwHisInstance()
            his.id = instanceId
            val instanceState = InstanceState.COMPLETE
            his.setInstanceState(instanceState)
            if (null != endNode) {
                his.currentNodeName = endNode.nodeName
                his.currentNodeKey = endNode.nodeKey
            } else {
                his.currentNodeName = instanceState.name
                his.currentNodeKey = instanceState.name
            }
            his.createTime = flwInstance.createTime
            his.lastUpdateBy = flwInstance.lastUpdateBy
            his.lastUpdateTime = flwInstance.lastUpdateTime
            his.calculateDuration()
            instanceDao.deleteById(instanceId)
            hisInstanceDao.updateById(his)
            // 流程实例监听器通知
            this.instanceNotify(EventType.END, { hisInstanceDao.selectById(instanceId) }, execution.flowCreator)

            /*
             * 实例为子流程，重启动父流程任务
             */
            if (null != flwInstance.parentInstanceId) {
                // 重启父流程实例
                val parentFlwInstance = instanceDao.selectById(flwInstance.parentInstanceId)
                execution.flwInstance = parentFlwInstance
                execution.restartProcessInstance(parentFlwInstance!!.processId, parentFlwInstance.currentNodeKey)

                // 结束调用外部流程任务
                taskService.endCallProcessTask(flwInstance.processId, flwInstance.id)
            }
        }
        return true
    }

    protected fun instanceNotify(
        eventType: EventType?,
        supplier: Supplier<FlwHisInstance?>,
        flowCreator: FlowCreator?,
    ) {
        instanceListener?.notify(eventType, supplier, null, flowCreator)
    }

    /**
     * 流程实例数据会保存至活动实例表、历史实例表
     *
     * @param flwInstance 流程实例对象
     * @param flwProcess  流程定义对象
     * @param flowCreator 处理人员
     */
    override fun saveInstance(
        flwInstance: FlwInstance?,
        flwProcess: FlwProcess?,
        flowCreator: FlowCreator?,
    ) {
        // 保存流程实例
        instanceDao.insert(flwInstance)

        // 保存历史实例设置为活的状态
        val flwHisInstance = FlwHisInstance.of(flwInstance!!, InstanceState.ACTIVE)
        hisInstanceDao.insert(flwHisInstance)

        // 保存扩展流程实例
        extInstanceDao.insert(FlwExtInstance.of(flwInstance, flwProcess!!))

        // 流程实例监听器通知
        this.instanceNotify(EventType.START, { flwHisInstance }, flowCreator)
    }

    override fun reject(
        instanceId: Long?,
        flowCreator: FlowCreator,
    ) {
        this.forceComplete(instanceId, flowCreator, InstanceState.REJECT, EventType.REJECT)
    }

    override fun revoke(
        instanceId: Long?,
        flowCreator: FlowCreator,
    ) {
        this.forceComplete(instanceId, flowCreator, InstanceState.REVOKE, EventType.REVOKE)
    }

    override fun timeout(
        instanceId: Long?,
        flowCreator: FlowCreator,
    ) {
        this.forceComplete(instanceId, flowCreator, InstanceState.TIMEOUT, EventType.TIMEOUT)
    }

    /**
     * 强制终止活动实例,并强制完成活动任务
     *
     * @param instanceId  流程实例ID
     * @param flowCreator 处理人员
     */
    override fun terminate(
        instanceId: Long?,
        flowCreator: FlowCreator,
    ) {
        this.forceComplete(instanceId, flowCreator, InstanceState.TERMINATE, EventType.TERMINATE)
    }

    /**
     * 强制完成流程实例
     *
     * @param instanceId    流程实例ID
     * @param flowCreator   处理人员
     * @param instanceState 流程实例最终状态
     * @param eventType     监听事件类型
     */
    protected fun forceComplete(
        instanceId: Long?,
        flowCreator: FlowCreator?,
        instanceState: InstanceState?,
        eventType: EventType?,
    ) {
        val flwInstance = instanceDao.selectById(instanceId) ?: return

        val parentInstanceId = flwInstance.parentInstanceId
        if (null != parentInstanceId) {
            // 找到主流程去执行完成逻辑
            this.forceComplete(parentInstanceId, flowCreator, instanceState, eventType)
        } else {
            // 结束所有子流程实例
            instanceDao
                .selectListByParentInstanceId(flwInstance.id)
                .forEach { t ->
                    this.forceCompleteAll(t, flowCreator, instanceState, eventType)
                }
        }

        // 结束当前流程实例
        this.forceCompleteAll(flwInstance, flowCreator, instanceState, eventType)
    }

    /**
     * 强制完成流程所有实例
     */
    protected fun forceCompleteAll(
        flwInstance: FlwInstance,
        flowCreator: FlowCreator?,
        instanceState: InstanceState?,
        eventType: EventType?,
    ) {
        // 实例相关任务强制完成

        taskService.forceCompleteAllTask(flwInstance.id, flowCreator, instanceState, eventType)

        // 更新历史实例设置状态为终止
        val flwHisInstance = FlwHisInstance.of(flwInstance, instanceState!!)
        hisInstanceDao.updateById(flwHisInstance)

        // 删除实例
        instanceDao.deleteById(flwInstance.id)

        // 流程实例监听器通知
        this.instanceNotify(eventType, { flwHisInstance }, flowCreator)
    }

    /**
     * 更新活动实例
     */
    override fun updateInstance(flwInstance: FlwInstance?) {
        illegal(
            flwInstance?.id == null,
            "instance id cannot be empty"
        )
        instanceDao.updateById(flwInstance!!)
    }

    /**
     * 级联删除指定流程实例的所有数据
     *
     *
     * 删除表 flw_his_task_actor, flw_his_task, flw_task_actor, flw_task, flw_his_instance, flw_ext_instance, flw_instance
     *
     *
     * @param processId 流程ID
     */
    override fun cascadeRemoveByProcessId(processId: Long?) {
        val flwHisInstances = hisInstanceDao.selectListByProcessId(processId)
        if (isNotEmpty(flwHisInstances)) {
            // 删除活动任务相关信息
            taskService.cascadeRemoveByInstanceIds(flwHisInstances.map { it.id })

            // 删除扩展实例
            extInstanceDao.deleteByProcessId(processId)

            // 删除历史实例
            hisInstanceDao.deleteByProcessId(processId)

            // 删除实例
            instanceDao.deleteByProcessId(processId)
        }
    }

    override fun appendNodeModel(
        taskId: Long?,
        nodeModel: NodeModel?,
        beforeAfter: Boolean,
    ) {
        val flwTask = queryService.getTask(taskId)
        val flwExtInstance = extInstanceDao.selectById(flwTask!!.instanceId)
        val appendTaskKey = flwTask.taskKey

        val processModel = flwExtInstance!!.model()
        var selectNode = processModel!!.getNode(appendTaskKey)
        if (beforeAfter) {
            // 前置追溯父节点
            selectNode = selectNode!!.parentNode
        }
        if (null != selectNode!!.conditionNodes) {
            // 如果直接跟着条件节点，找到分支作为父节点
            for (conditionNode in selectNode.conditionNodes!!) {
                val conditionChildNode = conditionNode.childNode
                if (conditionChildNode!!.nodeKey == appendTaskKey) {
                    nodeModel?.childNode = conditionChildNode
                    conditionNode.childNode = nodeModel
                    break
                }
            }
        } else {
            // 当前节点即为真实父节点
            nodeModel?.childNode = selectNode.childNode
            selectNode.childNode = nodeModel
        }

        // 清理父节点关系
        processModel.cleanParentNode(processModel.nodeConfig!!)

        // 更新最新模型
        val temp = FlwExtInstance()
        temp.id = (flwExtInstance.id)
        temp.modelContent = processModel.toJsonString()
        isFalse(extInstanceDao.updateById(temp) > 0, "Update FlwExtInstance Failed")

        // 使缓存失效
        FlowLongContext.invalidateProcessModel(flwExtInstance.modelCacheKey())
    }
}
