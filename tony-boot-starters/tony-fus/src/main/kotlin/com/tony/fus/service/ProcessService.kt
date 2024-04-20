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

import com.tony.fus.Fus
import com.tony.fus.FusProcessModelParser
import com.tony.fus.db.mapper.FusProcessMapper
import com.tony.fus.db.po.FusProcess
import com.tony.fus.extension.fusListThrowIfEmpty
import com.tony.fus.extension.fusSelectByIdNotNull
import com.tony.fus.extension.fusThrowIf
import com.tony.utils.jsonNode

/**
 * 流程定义 Service
 * @author tangli
 * @date 2023/10/09 19:20
 * @since 1.0.0
 */
public sealed interface ProcessService {
    /**
     * 根据主键ID获取流程定义对象
     * @param [processId] 流程id
     * @return [FusProcess] 流程定义对象
     * @author tangli
     * @date 2023/10/09 19:23
     * @since 1.0.0
     */
    public fun getById(processId: String): FusProcess

    /**
     * 根据[processKey]获取流程定义对象
     * @param [processKey] 唯一标识
     * @param [version] 流程版本
     * @return [FusProcess]
     * @author tangli
     * @date 2024/01/17 10:17
     * @since 1.0.0
     */
    public fun getByKey(
        processKey: String,
        version: Int?,
    ): FusProcess

    /**
     * 根据[processKey]获取流程定义对象
     * @param [processKey] 唯一标识
     * @return [FusProcess]
     * @author tangli
     * @date 2024/01/17 10:17
     * @since 1.0.0
     */
    public fun getByKey(processKey: String): FusProcess =
        getByKey(processKey, null)

    /**
     * 部署流程
     * @param [modelContent] 流程定义json字符串
     * @param [repeat] 是否重复部署 true 存在版本+1新增一条记录 false 存在流程直接返回
     * @return [String] 流程id
     * @author tangli
     * @date 2023/10/09 19:52
     * @since 1.0.0
     */
    public fun deploy(
        modelContent: String,
        repeat: Boolean,
    ): String

    /**
     * 重新部署流程
     * @param [processId] 流程id
     * @param [modelContent] 流程定义json字符串
     * @param [processVersion] 流程版本
     * @return [Boolean]
     * @author tangli
     * @date 2023/10/09 19:53
     * @since 1.0.0
     */
    public fun redeploy(
        processId: String,
        modelContent: String,
        processVersion: Int,
    ): Boolean

    /**
     * 级联移除
     *
     * 谨慎使用！！！不可恢复，
     * 级联删除指定流程定义的所有数据
     * @param [processId] 流程id
     * @author tangli
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

    override fun getByKey(
        processKey: String,
        version: Int?,
    ): FusProcess =
        processMapper
            .ktQuery()
            .eq(FusProcess::processKey, processKey)
            .eq(version != null, FusProcess::processVersion, version)
            .last("limit 1")
            .orderByDesc(FusProcess::processVersion)
            .fusListThrowIfEmpty()
            .first()

    override fun deploy(
        modelContent: String,
        repeat: Boolean,
    ): String {
        fusThrowIf(modelContent.isEmpty(), "modelContent can not be empty")

        val compressedModelContent = modelContent.jsonNode().toString()
        val processModel = FusProcessModelParser.parse(compressedModelContent)

        val process =
            processMapper
                .ktQuery()
                .select(FusProcess::processId, FusProcess::processVersion)
                .eq(FusProcess::processKey, processModel.key)
                .orderByDesc(FusProcess::processVersion)
                .last("limit 1")
                .one()

        if (process != null) {
            return if (!repeat) {
                process.processId
            } else {
                fusThrowIf(
                    !redeploy(process.processId, compressedModelContent, process.processVersion + 1),
                    "Redeploy failed"
                )
                process.processId
            }
        }

        val newProcess =
            FusProcess().apply {
                this.processVersion = 1
                this.enabled = true
                this.processName = processModel.name
                this.processKey = processModel.key
                this.modelContent = compressedModelContent
            }
        fusThrowIf(
            processMapper.insert(newProcess) < 1,
            "Deploy failed"
        )
        return newProcess.processId
    }

    override fun redeploy(
        processId: String,
        modelContent: String,
        processVersion: Int,
    ): Boolean {
        val process = processMapper.fusSelectByIdNotNull(processId)
        val processModel = FusProcessModelParser.parse(modelContent, process.modelKey, true)
        process.processVersion = processVersion
        process.processName = processModel.name
        process.processKey = processModel.key
        process.modelContent = modelContent
        return processMapper.updateById(process) > 0
    }

    override fun cascadeRemove(processId: String) {
        Fus.runtimeService.cascadeRemoveByProcessId(processId)
        processMapper.deleteById(processId)
    }
}
