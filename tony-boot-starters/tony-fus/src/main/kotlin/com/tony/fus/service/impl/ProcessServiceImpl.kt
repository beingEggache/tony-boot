/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.fus.service.impl

import com.tony.fus.FusContext
import com.tony.fus.db.mapper.FusProcessMapper
import com.tony.fus.db.po.FusProcess
import com.tony.fus.extension.fusSelectByIdNotNull
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.service.ProcessService
import com.tony.utils.jsonNode

/**
 * ProcessServiceImpl is
 * @author tangli
 * @date 2023/10/26 19:43
 * @since 1.0.0
 */
internal class ProcessServiceImpl(
    private val processMapper: FusProcessMapper,
) : ProcessService {
    override fun getById(processId: String): FusProcess =
        processMapper.fusSelectByIdNotNull(processId, "流程[id=$processId]不存在")

    override fun deploy(
        modelContent: String,
        userId: String,
        repeat: Boolean,
    ): String {
        fusThrowIf(modelContent.isEmpty(), "modelContent can not be empty")

        val compressedModelContent = modelContent.jsonNode().toString()
        val processModel =
            FusContext
                .processModelParser
                .parse(compressedModelContent, null, false)

        val process =
            processMapper
                .ktQuery()
                .select(FusProcess::processId, FusProcess::processVersion)
                .eq(FusProcess::processKey, processModel.key)
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
                this.modelContent = compressedModelContent
                this.creatorId = userId
            }
        processMapper.insert(newProcess)
        return newProcess.processId
    }

    override fun redeploy(
        processId: String,
        modelContent: String,
    ): Boolean {
        val process = processMapper.fusSelectByIdNotNull(processId)
        val processModel = FusContext.processModelParser.parse(modelContent, processId, true)
        process.processName = processModel.name
        process.processKey = processModel.key
        process.modelContent = modelContent
        return processMapper.updateById(process) > 0
    }

    override fun cascadeRemove(processId: String) {
        TODO("Not yet implemented")
    }
}
