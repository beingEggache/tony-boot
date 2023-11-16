package com.tony.test.fus

import com.tony.fus.model.FusOperator
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import org.junit.jupiter.api.Test

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusOrSignTests : FusTests() {

    private val logger = getLogger()

    private val testOperatorId = "test001"
    private val testOperator1: FusOperator =
        FusOperator(
            testOperatorId,
            "测试1",
            "1"
        )
    private val testOperator3: FusOperator =
        FusOperator(
            "test003",
            "测试003",
            "1"
        )

    override val processJson = "json/orSign.json"

    @Test
    fun testOrSign() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        if (process != null) {
            processService.getByVersion(process.processName, process.processVersion)
        }
        fusEngine.startInstanceById(
            processId,
            testOperator1,
        )?.let { instance ->
            // 发起
            val taskList1 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)

            logger.info(taskList1.toJsonString())
            taskList1
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

            //驳回
            val taskList2 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList2
                .forEach { task ->
                    fusEngine.taskService.rejectTask(
                        task,
                        testOperator3,
                        mapOf("reason" to "不符合要求")
                    )
                }

            // 调整, 再发起
            val taskList3 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList3
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

            val taskList4 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList4
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator3)
                }
        }
    }
}
