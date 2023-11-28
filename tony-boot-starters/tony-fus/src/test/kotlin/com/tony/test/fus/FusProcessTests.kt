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
    fun testComplete() {
        test(false)
    }
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testReject() {
        test(true)
    }

    fun test(reject: Boolean) {
        val processService = engine.processService
        processService.getById(processId)

        val args = mapOf(
            "day" to 8,
            "assignee" to testOperator1Id
        )
        engine.startInstanceById(
            processId,
            testOperator1Id,
            args
        ).let { instance ->
            val instanceId = instance.instanceId

            val taskList1 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }
            if (reject) {
                engine.runtimeService.reject(instanceId, testOperator1Id)
                return
            }

            val taskList2 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }
        }
    }
}
