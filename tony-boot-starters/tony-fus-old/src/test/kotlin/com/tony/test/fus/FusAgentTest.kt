package com.tony.test.fus

import com.tony.fus.Fus
import com.tony.utils.genRandomInt
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * 代理人测试
 * @author tangli
 * @date 2024/05/02 23:37
 * @since 1.0.0
 */
class FusAgentTest : FusTests() {
    override val processJson = "json/purchase.json"

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        Fus.startProcessById(
            processId,
            testOperator1Id,
            businessKey = "FusAgentTest.test${genRandomInt(6)}",
        ).let {  instance ->
            val instanceId = instance.instanceId

            val taskList1 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    Fus
                        .taskService
                        .agentTask(
                            task.taskId,
                            testOperator1Id,
                            listOf(testOperator2Id, testOperator3Id),
                        )
                }

            Fus
                .executeTaskByInstanceId(
                    instanceId,
                    testOperator2Id
                )

            Fus
                .executeTaskByInstanceId(
                    instanceId,
                    testOperator1Id
                )

        }

    }
}
