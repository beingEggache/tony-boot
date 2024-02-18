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

package com.tony.test.fus

import com.tony.fus.FusContext
import com.tony.utils.genRandomInt
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * 依次审批测试
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
class FusSortSignProcessTests : FusTests() {

    override val processJson = "json/sortSign.json"

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = FusContext.processService
        processService.getById(processId)

        FusContext.startProcessById(
            processId,
            testOperator1Id,
            businessKey = "FusSortSignProcessTests.test${genRandomInt(6)}",
        ).let { instance ->
            val instanceId = instance.instanceId

            // 会签审批人001【审批】，执行转办、任务交给 test2 处理
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(
                    instanceId,
                    testOperator1Id
                ).also { task ->
                    FusContext
                        .taskService
                        .transferTask(
                            task.taskId,
                            testOperator1Id,
                            testOperator2Id
                        )
                }

            // 被转办人 test2 审批
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(
                    instanceId,
                    testOperator2Id
                ).also { task ->
                    FusContext.executeTask(
                        task.taskId,
                        testOperator2Id
                    )
                }

            // test3 领导审批同意
            val taskList3 =
                FusContext
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList3
                .forEach { task ->
                    FusContext.executeTask(task.taskId, testOperator3Id)
                }
        }
    }
}
