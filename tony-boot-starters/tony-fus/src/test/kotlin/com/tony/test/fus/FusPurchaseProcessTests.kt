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
 * 采购审批测试
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
class FusPurchaseProcessTests : FusTests() {

    override val processJson = "json/purchase.json"

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        FusContext.startProcessById(
            processId,
            testOperator1Id,
            businessKey = "FusPurchaseProcessTests.test${genRandomInt(6)}",
        ).let { instance ->
            val instanceId = instance.instanceId

            //领导审批
            val taskList2 =
                FusContext
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    FusContext.executeTask(task.taskId, testOperator1Id)
                }

            //领导撤回
            FusContext
                .queryService
                .recentHistoryTask(instanceId, "领导审批")
                .also { historyTask ->
                    FusContext
                        .taskService
                        .withdrawTask(historyTask.taskId, testOperator1Id)
                }

            //领导驳回
            val taskList3 =
                FusContext
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList3
                .forEach { task ->
                    FusContext
                        .taskService
                        .rejectTask(
                            task.taskId,
                            testOperator1Id,
                            mapOf(
                                "reason" to "不符合要求"
                            )
                        )
                }

            // 执行当前任务并跳到【经理确认】节点
            val taskList4 =
                FusContext
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList4
                .forEach { task ->
                    FusContext
                        .executeJumpTask(
                            task.taskId,
                            "经理确认",
                            testOperator1Id
                        )
                }

            // 经理确认，流程结束
            val taskList5 =
                FusContext
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList5
                .forEach { task ->
                    FusContext.executeTask(task.taskId, testOperator1Id)
                }
        }
    }
}
