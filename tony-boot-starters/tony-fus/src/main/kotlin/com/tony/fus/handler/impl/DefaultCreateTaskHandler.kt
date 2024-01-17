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

package com.tony.fus.handler.impl

import com.tony.fus.FusContext
import com.tony.fus.exception.FusException
import com.tony.fus.handler.CreateTaskHandler
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.utils.getLogger

/**
 * 任务创建操作
 * @author tangli
 * @date 2023/10/25 19:02
 * @since 1.0.0
 */
internal object DefaultCreateTaskHandler : CreateTaskHandler {
    private val logger = getLogger()

    /**
     * 根据任务模型、执行对象，创建下一个任务，并添加到execution对象的tasks集合中.
     *
     * @param [execution] 流程执行
     * @author Tang Li
     * @date 2023/10/25 19:03
     * @since 1.0.0
     */
    override fun handle(
        execution: FusExecution,
        node: FusNode?,
    ) {
        val taskList =
            FusContext
                .taskService
                .createTask(node, execution)
        execution.taskList.addAll(taskList)

        try {
            FusContext
                .interceptors
                .forEach { it.handle(execution) }
        } catch (e: Exception) {
            logger.error("interceptor error", e)
            throw FusException(e.message, cause = e)
        }
    }
}
