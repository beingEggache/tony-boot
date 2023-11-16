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
class FusProcessTests : FusTests() {

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

    override val processJson = "json/process.json"

    @Test
    fun testStartInstanceCondition1() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        if (process != null) {
            processService.getByVersion(process.processName, process.processVersion)
        }
        fusEngine.startInstanceById(
            processId,
            testOperator1,
            mapOf(
                "day" to 8,
                "assignee" to testOperatorId
            )
        )?.let { instance ->
            val taskList1 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            logger.info(taskList1.toJsonString())
            taskList1
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }
            val taskList2 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            logger.info(taskList2.toJsonString())
            taskList2
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }
        }
    }

    @Test
    fun testStartInstanceCondition2() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        if (process != null) {
            processService.getByVersion(process.processName, process.processVersion)
        }
        fusEngine.startInstanceById(
            processId,
            testOperator1,
            mapOf(
                "day" to 6,
                "assignee" to testOperatorId
            )
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
                    fusEngine.executeTask(task.taskId, testOperator2)
                }
        }
    }
}
