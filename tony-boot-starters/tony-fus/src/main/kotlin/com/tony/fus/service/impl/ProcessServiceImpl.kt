package com.tony.fus.service.impl

import com.tony.fus.FusContext
import com.tony.fus.db.enums.ProcessState
import com.tony.fus.db.mapper.FusProcessMapper
import com.tony.fus.db.po.FusProcess
import com.tony.fus.extension.fusListThrowIfEmpty
import com.tony.fus.extension.fusOneNotNull
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.model.FusOperator
import com.tony.fus.service.ProcessService
import java.time.LocalDateTime

/**
 * ProcessServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:43
 * @since 1.0.0
 */
internal class ProcessServiceImpl(
    private val processMapper: FusProcessMapper,
) : ProcessService {
    override fun getById(processId: String?): FusProcess? =
        processMapper.selectById(processId)

    override fun getByVersion(
        processName: String,
        processVersion: Int?,
    ): FusProcess {
        fusThrowIf(processName.isEmpty(), "processName can not be empty")
        return processMapper
            .ktQuery()
            .eq(FusProcess::processName, processName)
            .eq(processVersion != null, FusProcess::processVersion, processVersion)
            .orderByDesc(FusProcess::processVersion)
            .last("limit 1")
            .fusOneNotNull()
    }

    override fun deploy(
        modelContent: String,
        creator: FusOperator,
        repeat: Boolean,
    ): String {
        fusThrowIf(modelContent.isEmpty(), "modelContent can not be empty")

        val processModel =
            FusContext
                .parse(modelContent, null, false)
                .fusThrowIfNull()

        val processVersion =
            processMapper
                .ktQuery()
                .select(FusProcess::processId, FusProcess::processVersion)
                .eq(FusProcess::processName, processModel.name)
                .orderByDesc(FusProcess::processVersion)
                .last("limit 1")
                .fusListThrowIfEmpty()
                .firstOrNull()
                .fusThrowIfNull()
                .let {
                    if (!repeat) {
                        return it.processId!!
                    }
                    it.processVersion!!
                }

        val process =
            FusProcess().apply {
                this.processVersion = processVersion + 1
                this.processState = ProcessState.ACTIVE
                this.processName = processModel.name
                this.displayName = processModel.name
                this.instanceUrl = processModel.instanceUrl
                this.useScope = 0
                this.modelContent = modelContent
                this.creatorId = creator.operatorId
                this.creatorName = creator.operatorName
                this.createTime = LocalDateTime.now()
            }
        processMapper.insert(process)
        return process.processId!!
    }

    override fun redeploy(
        processId: String,
        modelContent: String,
    ): Boolean {
        val process =
            processMapper
                .selectById(processId)
                .fusThrowIfNull()

        val processModel = FusContext.parse(modelContent, processId, true)
        process.processName = processModel?.name
        process.displayName = processModel?.name
        process.instanceUrl = processModel?.instanceUrl
        process.modelContent = modelContent

        return processMapper.updateById(process) > 0
    }

    override fun cascadeRemove(processId: String) {
        TODO("Not yet implemented")
    }
}
