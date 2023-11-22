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
        val processService = fusEngine.processService
        processService.getById(processId)

        val args = mapOf(
            "day" to 8,
            "assignee" to testOperatorId
        )

        fusEngine.startInstanceById(
            processId,
            testOperator1,
            args
        ).let { instance ->
            // 发起
            val taskList1 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)

            taskList1
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

            //会签1
            fusEngine
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator1.operatorId)
                .also { task ->
                    fusEngine.executeTask(
                        task.taskId,
                        testOperator1
                    )
                }

            // 会签2
            fusEngine
                .queryService
                .taskByInstanceIdAndActorId(instance.instanceId, testOperator3.operatorId)
                .also { task ->
                    fusEngine.executeTask(
                        task.taskId,
                        testOperator3
                    )
                }
        }
    }
}
