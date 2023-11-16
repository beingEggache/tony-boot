package com.tony.test.fus

import com.tony.fus.model.FusOperator
import com.tony.utils.getLogger
import org.junit.jupiter.api.Test

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusRejectTests : FusTests() {

    private val logger = getLogger()

    private val testOperatorId = "test001"
    private val testOperator1: FusOperator =
        FusOperator(
            testOperatorId,
            "测试1",
            "1"
        )
    private val testOperator2: FusOperator =
        FusOperator(
            "zg0001",
            "张三",
            "1"
        )

    override val processJson = "json/orSign.json"

    @Test
    fun testReject() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        if (process != null) {
            processService.getByVersion(process.processName, process.processVersion)
        }
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
