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

package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.FusContext
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.FusProcessModel
import java.time.LocalDateTime
import java.util.function.Function

/**
 * 流程定义表
 * @author Tang Li
 * @date 2023/09/29 19:13
 * @since 1.0.0
 */
@TableName
public class FusProcess {
    /**
     * 主键ID
     */
    @TableId
    public var processId: String = ""

    /**
     * 租户ID
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var tenantId: String = ""

    /**
     * 创建人ID
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var creatorId: String = ""

    /**
     * 创建人
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var creatorName: String = ""

    /**
     * 创建时间
     */
    @OrderBy
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    public val createTime: LocalDateTime = LocalDateTime.now()

    /**
     * 流程名称
     */
    public var processName: String = ""

    /**
     * 流程定义 key 唯一标识
     */
    public var processKey: String = ""

    /**
     * 流程类型
     */
    public var processType: String = ""

    /**
     * 流程版本，默认 1
     */
    public var processVersion: Int = 0

    /**
     * 流程状态: 0.不可用, 1.可用
     */
    public var enabled: Boolean = true

    /**
     * 流程模型定义JSON内容
     */
    public var modelContent: String = "{}"

    /**
     * 排序
     */
    public var sort: Int = 0

    public fun model(): FusProcessModel =
        FusContext.processModelParser.parse(modelContent, processId, false)

    public fun execute(
        execution: FusExecution,
        nodeName: String?,
    ) {
        model()
            .also { model ->
                model
                    .getNode(nodeName)
                    .fusThrowIfNull("流程模型中未发现，流程节点:$nodeName")
                    .nextNode()
                    ?.also { executeNode ->
                        executeNode.execute(execution)
                    } ?: execution.endInstance()
            }
    }

    public fun executeStart(
        userId: String,
        executionSupplier: Function<FusNode, FusExecution>,
    ): FusInstance =
        model()
            .node
            .fusThrowIfNull("流程定义[processName=$processName, processVersion=$processVersion]没有开始节点")
            .let { node ->
                fusThrowIf(
                    !FusContext.taskActorProvider.hasPermission(node, userId),
                    "No permission to execute"
                )
                val execution = executionSupplier.apply(node)
                FusContext.createTaskHandler.handle(execution, node)
                execution.instance
            }
}
