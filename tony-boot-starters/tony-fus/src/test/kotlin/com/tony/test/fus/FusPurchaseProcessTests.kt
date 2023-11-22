package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 采购审批测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusPurchaseProcessTests : FusTests() {

    override val processJson = "json/purchase.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = fusEngine.processService
        processService.getById(processId)

        fusEngine.startInstanceById(
            processId,
            testOperator1,
        ).let { instance ->
            // 发起
            val instanceId = instance.instanceId
            val taskList1 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

            Thread.sleep(1000)

            //领导审批
            val taskList2 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

            //领导撤回
            fusEngine
                .queryService
                .recentHistoryTask(instanceId)
                .also { historyTask ->
                    fusEngine
                        .taskService
                        .withdrawTask(historyTask.taskId, testOperator1)
                }

            //领导驳回
            val taskList3 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList3
                .forEach { task ->
                    fusEngine
                        .taskService
                        .rejectTask(
                            task,
                            testOperator1,
                            mapOf(
                                "reason" to "不符合要求"
                            )
                        )
                }

            // 执行当前任务并跳到【经理确认】节点
            val taskList4 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList4
                .forEach { task ->
                    fusEngine
                        .executeAndJumpTask(
                            task.taskId,
                            "经理确认",
                            testOperator1
                        )
                }

            // 经理确认，流程结束
            val taskList5 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList5
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

        }
    }
}
