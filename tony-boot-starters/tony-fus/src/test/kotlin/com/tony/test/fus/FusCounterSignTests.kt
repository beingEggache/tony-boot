package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 会签测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusCounterSignTests : FusTests() {

    override val processJson = "json/counterSign.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
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
            // 发起
            val taskList1 =
                engine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)

            taskList1
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }

            //会签1
            engine
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator1Id)
                .also { task ->
                    engine.executeTask(
                        task.taskId,
                        testOperator1Id
                    )
                }

            // 会签2
            engine
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator3Id)
                .also { task ->
                    engine.executeTask(
                        task.taskId,
                        testOperator3Id
                    )
                }
        }
    }
}
