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

package com.tony.fus.model

import com.tony.fus.FusContext
import com.tony.fus.db.enums.TaskType
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.db.po.FusTask
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.extension.fusThrowIf

/**
 * FusExecution is
 * @author tangli
 * @date 2023/10/19 19:16
 * @since 1.0.0
 */
public class FusExecution(
    public val process: FusProcess,
    public val instance: FusInstance,
    public val userId: String,
    args: Map<String, Any?>,
) {
    public var nextTaskActor: FusTaskActor? = null

    public var task: FusTask? = null

    public val taskList: MutableList<FusTask> = mutableListOf()

    public val variable: MutableMap<String, Any?> =
        HashMap<String, Any?>().apply {
            putAll(args)
        }

    public fun endInstance() {
        val instanceId = instance.instanceId
        FusContext
            .queryService
            .listTaskByInstanceId(instanceId)
            .forEach { task ->
                fusThrowIf(task.taskType == TaskType.MAJOR, "存在未完成的主办任务")
                FusContext.taskService.complete(task.taskId, "ADMIN")
            }
        FusContext.runtimeService.complete(instanceId)
    }

    public fun addTasks(taskList: Collection<FusTask>) {
        this
            .taskList
            .addAll(taskList)
    }
}
