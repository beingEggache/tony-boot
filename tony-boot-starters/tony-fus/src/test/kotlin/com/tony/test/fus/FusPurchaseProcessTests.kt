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
        val processService = engine.processService
        processService.getById(processId)

        engine.startInstanceById(
            processId,
            testOperator1Id,
        ).let { instance ->
            // 发起
            val instanceId = instance.instanceId
            val taskList1 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }

            Thread.sleep(1000)

            //领导审批
            val taskList2 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }

            //领导撤回
            engine
                .queryService
                .recentHistoryTask(instanceId)
                .also { historyTask ->
                    engine
                        .taskService
                        .withdrawTask(historyTask.taskId, testOperator1Id)
                }

            //领导驳回
            val taskList3 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList3
                .forEach { task ->
                    engine
                        .taskService
                        .rejectTask(
                            task,
                            testOperator1Id,
                            mapOf(
                                "reason" to "不符合要求"
                            )
                        )
                }

            // 执行当前任务并跳到【经理确认】节点
            val taskList4 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList4
                .forEach { task ->
                    engine
                        .executeAndJumpTask(
                            task.taskId,
                            "经理确认",
                            testOperator1Id
                        )
                }

            // 经理确认，流程结束
            val taskList5 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList5
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }

        }
    }
}
