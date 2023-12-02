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
        test(false, 8)
    }

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testReject() {
        test(true, 8)
    }

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testDay() {
        test(false, 3)
    }

    fun test(reject: Boolean, day: Int) {
        val processService = engine.processService
        processService.getById(processId)

        val args = mutableMapOf<String, Any?>(
            "day" to day,
            "assignee" to testOperator1Id
        )
        engine.startInstanceById(
            processId,
            testOperator1Id
        ).let { instance ->
            val instanceId = instance.instanceId

            // 发起, 执行条件路由
            val taskList1 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }


            val taskList2 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id, args)
                }

            if (reject) {
                engine.runtimeService.reject(instanceId, testOperator1Id)
                return
            }

        }
    }
}
