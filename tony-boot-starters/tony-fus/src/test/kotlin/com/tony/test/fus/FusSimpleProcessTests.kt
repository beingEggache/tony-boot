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
import com.tony.fus.model.FusNode
import com.tony.fus.model.FusNodeAssignee
import com.tony.fus.model.enums.ApproverType
import com.tony.fus.model.enums.NodeType
import com.tony.utils.genRandomInt
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * 简单流程测试
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
class FusSimpleProcessTests : FusTests() {

    override val processJson = "json/simpleProcess.json"

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testAddNode() {
        val args = mapOf(
            "day" to 8,
            "age" to 18,
            "assignee" to testOperator1Id
        )
        FusContext
            .startProcessById(
                processId,
                testOperator1Id,
                args
            ).also { instance ->
                val instanceId = instance.instanceId

                // 测试会签审批人001【审批】，执行前置加签
                FusContext
                    .queryService
                    .taskByInstanceIdAndActorId(
                        instanceId,
                        testOperator1Id
                    ).also { task ->
                        FusContext.executeInsertNode(
                            task.taskId,
                            node("前置加签", testOperator3Id),
                            testOperator1Id,
                            true
                        )
                    }

                // 执行前加签
                FusContext
                    .executeTaskByInstanceIdAndActorId(
                        instanceId,
                        testOperator3Id
                    )

                // 测试会签审批人003【审批】，执行后置加签
                FusContext
                    .queryService
                    .taskByInstanceIdAndActorId(
                        instanceId,
                        testOperator3Id
                    ).also { task ->
                        FusContext.executeInsertNode(
                            task.taskId,
                            node("后置加签", testOperator2Id),
                            testOperator3Id,
                            false
                        )
                    }

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
                    .executeTaskByInstanceIdAndActorId(
                        instanceId,
                        testOperator2Id
                    )

                // 会签审批人003【审批】，执行委派、任务委派给 test2 处理
                FusContext
                    .queryService
                    .taskByInstanceIdAndActorId(
                        instanceId,
                        testOperator3Id
                    ).also { task ->
                        FusContext
                            .taskService
                            .delegateTask(
                                task.taskId,
                                testOperator3Id,
                                testOperator2Id
                            )
                    }

                // 被委派人 test2 解决问题，后归还任务给委派人
                FusContext
                    .queryService
                    .taskByInstanceIdAndActorId(
                        instanceId,
                        testOperator2Id
                    ).also { task ->
                        FusContext
                            .taskService
                            .resolveTask(
                                task.taskId,
                                testOperator2Id
                            )
                    }

                // 委派人 test3 执行完成任务
                FusContext
                    .executeTaskByInstanceIdAndActorId(
                        instanceId,
                        testOperator3Id
                    )

                // 执行后加签
                FusContext
                    .executeTaskByInstanceIdAndActorId(
                        instanceId,
                        testOperator2Id
                    )
            }
    }

    private fun node(nodeName: String, userId: String): FusNode {
        return FusNode().apply {
            this.nodeName = nodeName
            this.nodeType = NodeType.APPROVER
            this.approverType = ApproverType.ASSIGN
            this.nodeUserList.add(
                FusNodeAssignee(
                    userId,
                    ""
                )
            )
        }
    }

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = FusContext.processService
        processService.getById(processId)

        val args =
            mutableMapOf<String, Any?>(
                "day" to 8,
                "age" to 18,
                "assignee" to testOperator1Id,
            )

        FusContext.startProcessById(
            processId,
            testOperator1Id,
            args,
            "FusSimpleProcessTests.test${genRandomInt(6)}",
        ).also { instance ->
            val instanceId = instance.instanceId
            // 测试会签审批人001【审批】
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1Id)
                .also { task ->
                    FusContext.executeTask(task.taskId, testOperator1Id)
                }
            Thread.sleep(2000)

            // 测试会签审批人003【审批】
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator3Id)
                .also { task ->
                    FusContext.executeTask(task.taskId, testOperator3Id, args)
                }

            //撤回任务(条件路由子审批) 回到测试会签审批人003【审批】任务
            FusContext
                .queryService
                .recentHistoryTask(instanceId)
                .also { historyTask ->
                    FusContext
                        .taskService
                        .withdrawTask(historyTask.taskId, testOperator1Id)
                }

            // 测试会签审批人003【审批】
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator3Id)
                .also { task ->
                    FusContext.executeTask(task.taskId, testOperator3Id, args)
                }

            // 年龄审批【审批】
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1Id)
                .also { task ->
                    FusContext.executeTask(task.taskId, testOperator1Id)
                }

            // 条件内部审核【审批】
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1Id)
                .also { task ->
                    FusContext.executeTask(task.taskId, testOperator1Id)
                }

            // 条件路由子审批【审批】 抄送 结束
            FusContext
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1Id)
                .also { task ->
                    FusContext.executeTask(task.taskId, testOperator1Id)
                }
        }
    }
}
