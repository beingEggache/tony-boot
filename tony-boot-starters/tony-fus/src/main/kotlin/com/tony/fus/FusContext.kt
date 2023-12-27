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

package com.tony.fus

import com.tony.fus.expression.FusExpressionEvaluator
import com.tony.fus.handler.impl.DefaultCreateTaskHandler
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.FusProcessModel
import com.tony.fus.service.ProcessService
import com.tony.fus.service.QueryService
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.TaskService

/**
 * FusContext is
 * @author tangli
 * @date 2023/10/19 19:35
 * @since 1.0.0
 */
public class FusContext(
        public val processService: ProcessService,
        public val queryService: QueryService,
        public val runtimeService: RuntimeService,
        public val taskService: TaskService,
        public val expressionEvaluator: FusExpressionEvaluator,
        public val taskPermission: FusTaskPermission,
        public val interceptors: List<FusInterceptor>,
        public val taskActorProvider: FusTaskActorProvider,
) {
    /**
     * 创建任务
     * @param [execution] 执行对象
     * @param [node] 节点模型
     * @author Tang Li
     * @date 2023/12/02 19:15
     * @since 1.0.0
     */
    public fun createTask(
        execution: FusExecution,
        node: FusNode?,
    ): Unit =
        DefaultCreateTaskHandler.handle(this, execution, node)

    public companion object {
        private val processModelParser: FusProcessModelParser = DefaultFusProcessModelParser()

        public fun parse(
            content: String,
            processId: String?,
            redeploy: Boolean,
        ): FusProcessModel =
            processModelParser.parse(content, processId, redeploy)
    }
}
