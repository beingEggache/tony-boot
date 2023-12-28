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
import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 会签测试
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
class FusCounterSignTests : FusTests() {

    override val processJson = "json/counterSign.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = FusContext.processService
        processService.getById(processId)

        val args = mapOf(
            "day" to 8,
            "assignee" to testOperator1Id
        )

        FusContext.startInstanceById(
            processId,
            testOperator1Id,
            args
        ).let { instance ->
            // 测试会签审批人001【审批】
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator1Id)
                .also { task ->
                    FusContext.executeTask(
                        task.taskId,
                        testOperator1Id
                    )
                }

            // 执行任务跳转任意节点
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator3Id)
                .also { task ->
                    FusContext.executeJumpTask(task.taskId, "发起人", testOperator3Id)
                }

            // 执行发起
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator1Id)
                .also { task ->
                    FusContext.executeTask(
                        task.taskId,
                        testOperator1Id
                    )
                }

            // 测试会签审批人001【审批】
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator1Id)
                .also { task ->
                    FusContext.executeTask(
                        task.taskId,
                        testOperator1Id
                    )
                }

            // 测试会签审批人003【审批】
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator3Id)
                .also { task ->
                    FusContext.executeTask(
                        task.taskId,
                        testOperator3Id
                    )
                }
        }
    }
}
