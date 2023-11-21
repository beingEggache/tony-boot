package com.tony.test.fus

import org.junit.jupiter.api.Test

/**
 * 驳回测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusRejectTests : FusTests() {

    override val processJson = "json/orSign.json"

    @Test
    fun testReject() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        processService.getByNameAndVersion(process.processName, process.processVersion)
        fusEngine.startInstanceById(
            processId,
            testOperator1,
        )?.let { instance ->
            val taskList1 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)

            taskList1
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

            val taskList2 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)

            taskList2
                .forEach { task ->
                    fusEngine.taskService.rejectTask(
                        task,
                        testOperator1,
                        mapOf("reason" to "不符合要求")
                    )
                }
        }
    }
}
