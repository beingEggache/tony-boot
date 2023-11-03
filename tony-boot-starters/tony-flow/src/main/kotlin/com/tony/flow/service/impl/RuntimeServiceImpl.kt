package com.tony.flow.service.impl

import com.tony.flow.db.enums.InstanceState
import com.tony.flow.db.mapper.FlowHistoryInstanceMapper
import com.tony.flow.db.mapper.FlowInstanceMapper
import com.tony.flow.db.mapper.FlowTaskMapper
import com.tony.flow.db.po.FlowHistoryInstance
import com.tony.flow.db.po.FlowInstance
import com.tony.flow.db.po.FlowProcess
import com.tony.flow.db.po.FlowTask
import com.tony.flow.model.FlowOperator
import com.tony.flow.service.RuntimeService
import com.tony.flow.service.TaskService
import com.tony.utils.copyToNotNull
import com.tony.utils.toJsonString
import java.time.LocalDateTime

/**
 * RuntimServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:44
 * @since 1.0.0
 */
internal class RuntimeServiceImpl(
    private val flowInstanceMapper: FlowInstanceMapper,
    private val flowHistoryInstanceMapper: FlowHistoryInstanceMapper,
    private val flowTaskMapper: FlowTaskMapper,
    private val taskService: TaskService,
) : RuntimeService {
    override fun createInstance(
        flowProcess: FlowProcess,
        flowCreator: FlowOperator,
        variable: Map<String, Any?>?,
    ): FlowInstance =
        FlowInstance().apply {
            createTime = LocalDateTime.now()
            updateTime = createTime
            creatorId = flowCreator.operatorId
            creatorName = flowCreator.operatorName
            updatorId = flowCreator.operatorId
            updatorName = flowCreator.operatorName
            processId = flowProcess.processId
            this.variable = variable?.toJsonString() ?: "{}"
        }

    override fun complete(instanceId: String?) {
        val flowHistoryInstance =
            FlowHistoryInstance().apply {
                this.instanceId = instanceId
                this.instanceState = InstanceState.COMPLETE
                this.endTime = LocalDateTime.now()
            }
        flowInstanceMapper.deleteById(instanceId)
        flowHistoryInstanceMapper.updateById(flowHistoryInstance)
        // TODO Notify
    }

    override fun saveInstance(flowInstance: FlowInstance) {
        flowInstanceMapper.insert(flowInstance)
        val flowHistoryInstance =
            flowInstance.copyToNotNull(FlowHistoryInstance()).apply {
                this.instanceState = InstanceState.ACTIVE
            }
        flowHistoryInstanceMapper.insert(flowHistoryInstance)
        // TODO Notify
    }

    override fun terminate(
        instanceId: String,
        flowOperator: FlowOperator,
    ) {
        flowInstanceMapper.selectById(instanceId)?.apply {
            flowTaskMapper
                .ktQuery()
                .eq(FlowTask::instanceId, instanceId)
                .list()
                .forEach {
                    taskService.complete(it.taskId, flowOperator)
                }

            val flowHistoryInstance =
                this.copyToNotNull(FlowHistoryInstance()).apply {
                    this.instanceState = InstanceState.TERMINATED
                    this.endTime = LocalDateTime.now()
                }
            flowHistoryInstanceMapper.updateById(flowHistoryInstance)
            flowInstanceMapper.deleteById(instanceId)

            // TODO Notify
        }
    }

    override fun updateInstance(flowInstance: FlowInstance) {
        flowInstanceMapper.updateById(flowInstance)
    }

    override fun cascadeRemoveByProcessId(processId: String) {
        TODO("Not yet implemented")
    }
}
