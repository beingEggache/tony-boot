package com.tony.flow.service.impl

import com.tony.flow.FlowContext
import com.tony.flow.db.enums.ProcessState
import com.tony.flow.db.mapper.FlowProcessMapper
import com.tony.flow.db.po.FlowProcess
import com.tony.flow.extension.flowThrowIf
import com.tony.flow.extension.flowThrowIfNull
import com.tony.flow.model.FlowOperator
import com.tony.flow.service.ProcessService
import java.time.LocalDateTime

/**
 * ProcessServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:43
 * @since 1.0.0
 */
internal class ProcessServiceImpl(
    private val flowProcessMapper: FlowProcessMapper,
) : ProcessService {
    override fun getById(processId: Long): FlowProcess =
        flowProcessMapper.selectById(processId)

    override fun getByVersion(
        processName: String,
        processVersion: Int?,
    ): FlowProcess {
        flowThrowIf(processName.isEmpty(), "processName can not be empty")
        return flowProcessMapper
            .ktQuery()
            .eq(FlowProcess::processName, processName)
            .eq(processVersion != null, FlowProcess::processVersion, processVersion)
            .orderByDesc(FlowProcess::processVersion)
            .last("limit 1")
            .list()
            .firstOrNull()
            .flowThrowIfNull()
    }

    override fun deploy(
        modelContent: String,
        flowCreator: FlowOperator,
        repeat: Boolean,
    ): Long {
        flowThrowIf(modelContent.isEmpty(), "modelContent can not be empty")

        val processModel =
            FlowContext
                .parse(modelContent, null, false)
                .flowThrowIfNull()

        val processVersion =
            flowProcessMapper
                .ktQuery()
                .select(FlowProcess::processId, FlowProcess::processVersion)
                .eq(FlowProcess::processName, processModel.name)
                .orderByDesc(FlowProcess::processVersion)
                .last("limit 1")
                .list()
                .firstOrNull()
                .flowThrowIfNull()
                .let {
                    if (!repeat) {
                        return it.processId!!
                    }
                    it.processVersion!!
                }

        val flowProcess =
            FlowProcess().apply {
                this.processVersion = processVersion + 1
                this.processState = ProcessState.ACTIVE
                this.processName = processModel.name
                this.displayName = processModel.name
                this.instanceUrl = processModel.instanceUrl
                this.useScope = 0
                this.modelContent = modelContent
                this.creatorId = flowCreator.operatorId
                this.creatorName = flowCreator.operatorName
                this.createTime = LocalDateTime.now()
            }
        flowProcessMapper.insert(flowProcess)
        return flowProcess.processId!!
    }

    override fun redeploy(
        processId: Long,
        modelContent: String,
    ): Boolean {
        val flowProcess =
            flowProcessMapper
                .selectById(processId)
                .flowThrowIfNull()

        val flowProcessModel = FlowContext.parse(modelContent, processId, true)
        flowProcess.processName = flowProcessModel?.name
        flowProcess.displayName = flowProcessModel?.name
        flowProcess.instanceUrl = flowProcessModel?.instanceUrl
        flowProcess.modelContent = modelContent

        return flowProcessMapper.updateById(flowProcess) > 0
    }

    override fun cascadeRemove(processId: Long) {
        TODO("Not yet implemented")
    }
}
