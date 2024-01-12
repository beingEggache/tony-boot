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

package com.tony.fus.service

import com.tony.fus.FusContext
import com.tony.fus.db.mapper.FusProcessMapper
import com.tony.fus.db.po.FusProcess
import com.tony.fus.extension.fusSelectByIdNotNull
import com.tony.fus.extension.fusThrowIf
import com.tony.utils.jsonNode

/**
 * 流程定义 Service
 * @author Tang Li
 * @date 2023/10/09 19:20
 * @since 1.0.0
 */
public sealed interface ProcessService {
    /**
     * 根据主键ID获取流程定义对象
     * @param [processId] 流程id
     * @return [FusProcess] 流程定义对象
     * @author Tang Li
     * @date 2023/10/09 19:23
     * @since 1.0.0
     */
    public fun getById(processId: String): FusProcess

    /**
     * 部署流程
     * @param [modelContent] 流程定义json字符串
     * @param [userId] 操作人id
     * @param [repeat] 是否重复部署 true 存在版本+1新增一条记录 false 存在流程直接返回
     * @return [String] 流程id
     * @author Tang Li
     * @date 2023/10/09 19:52
     * @since 1.0.0
     */
    public fun deploy(
        modelContent: String,
        userId: String,
        repeat: Boolean,
    ): String

    /**
     * 重新部署流程
     * @param [processId] 流程id
     * @param [modelContent] 流程定义json字符串
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/09 19:53
     * @since 1.0.0
     */
    public fun redeploy(
        processId: String,
        modelContent: String,
    ): Boolean

    /**
     * 级联移除
     *
     * 谨慎使用！！！不可恢复，
     * 级联删除指定流程定义的所有数据
     * @param [processId] 流程id
     * @author Tang Li
     * @date 2023/10/10 19:19
     * @since 1.0.0
     */
    public fun cascadeRemove(processId: String)
}

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
        FusContext.runtimeService.cascadeRemoveByProcessId(processId)
        processMapper.deleteById(processId)
    }
}
