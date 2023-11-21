package com.tony.fus.service.impl

import com.tony.fus.FusContext
import com.tony.fus.db.mapper.FusProcessMapper
import com.tony.fus.db.po.FusProcess
import com.tony.fus.extension.fusOneNotNull
import com.tony.fus.extension.fusSelectByIdNotNull
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.model.FusOperator
import com.tony.fus.service.ProcessService

/**
 * ProcessServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:43
 * @since 1.0.0
 */
internal class ProcessServiceImpl(
    private val processMapper: FusProcessMapper,
) : ProcessService {
    override fun getById(processId: String): FusProcess =
        processMapper.fusSelectByIdNotNull(processId, "流程[id=$processId]不存在")

    override fun getByNameAndVersion(
        processName: String,
        processVersion: Int,
    ): FusProcess =
        processMapper
            .ktQuery()
            .eq(FusProcess::processName, processName)
            .eq(FusProcess::processVersion, processVersion)
            .fusOneNotNull("流程[processName=$processName, processVersion=$processVersion] 不存在.")

    override fun getByName(processName: String): FusProcess =
        processMapper
            .ktQuery()
            .eq(FusProcess::processName, processName)
            .fusOneNotNull("流程[processName=$processName] 不存在.")

    override fun deploy(
        modelContent: String,
        creator: FusOperator,
        repeat: Boolean,
    ): String {
        fusThrowIf(modelContent.isEmpty(), "modelContent can not be empty")

        val processModel =
            FusContext
                .parse(modelContent, null, false)

        val process =
            processMapper
                .ktQuery()
                .select(FusProcess::processId, FusProcess::processVersion)
                .eq(FusProcess::processName, processModel.name)
                .orderByDesc(FusProcess::processVersion)
                .last("limit 1")
                .one()

        if (process != null && !repeat) {
            return process.processId
        }

        val newProcess =
            FusProcess().apply {
                this.processVersion = (process?.processVersion ?: 0) + 1
                this.processName = processModel.name
                this.processKey = processModel.key
                this.modelContent = modelContent
                this.creatorId = creator.operatorId
                this.creatorName = creator.operatorName
            }
        processMapper.insert(newProcess)
        return newProcess.processId
    }

    override fun redeploy(
        processId: String,
        modelContent: String,
    ): Boolean {
        val process = processMapper.fusSelectByIdNotNull(processId)
        val processModel = FusContext.parse(modelContent, processId, true)
        process.processName = processModel.name
        process.processKey = processModel.key
        process.modelContent = modelContent
        return processMapper.updateById(process) > 0
    }

    override fun cascadeRemove(processId: String) {
        TODO("Not yet implemented")
    }
}
