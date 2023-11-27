package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 驳回测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusRejectTests : FusTests() {

    override val processJson = "json/orSign.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testReject() {
        val processService = engine.processService
        processService.getById(processId)

        engine.startInstanceById(
            processId,
            testOperator1,
        ).let { instance ->
            val taskList1 =
                engine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)

            taskList1
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1)
                }

            val taskList2 =
                engine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)

            taskList2
                .forEach { task ->
                    engine.taskService.rejectTask(
                        task,
                        testOperator1,
                        mapOf("reason" to "不符合要求")
                    )
                }
        }
    }
}
