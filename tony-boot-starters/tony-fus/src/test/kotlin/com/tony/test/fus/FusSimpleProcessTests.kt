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

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 简单流程测试
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
class FusSimpleProcessTests : FusTests() {

    override val processJson = "json/simpleProcess.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = engine.processService
        processService.getById(processId)

        val args =
            mapOf(
                "day" to 8,
                "age" to 18,
                "assignee" to testOperator1Id,
            )

        engine.startInstanceById(
            processId,
            testOperator1Id,
            args,
        ).let { instance ->
            val instanceId = instance.instanceId
            // 测试会签审批人001【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1Id)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }
            Thread.sleep(2000)

            // 测试会签审批人003【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator3Id)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator3Id)
                }

            //撤回任务(条件路由子审批) 回到测试会签审批人003【审批】任务
            engine
                .queryService
                .recentHistoryTask(instanceId)
                .also { historyTask ->
                    engine
                        .taskService
                        .withdrawTask(historyTask.taskId, testOperator1Id)
                }

            // 测试会签审批人003【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator3Id)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator3Id)
                }

            // 年龄审批【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1Id)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }

            // 条件内部审核【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1Id)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }

            // 条件路由子审批【审批】 抄送 结束
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1Id)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }
        }
    }
}
