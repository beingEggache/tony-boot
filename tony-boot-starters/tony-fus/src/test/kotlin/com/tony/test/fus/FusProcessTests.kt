package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusProcessTests : FusTests() {

    override val processJson = "json/process.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testStartInstanceCondition1() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        processService.getByNameAndVersion(process.processName, process.processVersion)
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
                    fusEngine.executeTask(task.taskId, testOperator1)
                }
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testStartInstanceCondition2() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        processService.getByNameAndVersion(process.processName, process.processVersion)
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
