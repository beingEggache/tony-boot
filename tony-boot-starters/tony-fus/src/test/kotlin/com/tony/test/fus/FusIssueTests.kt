package com.tony.test.fus

import com.tony.fus.FusContext
import com.tony.utils.genRandomInt
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * FusIssueTests is
 * @author tangli
 * @date 2024/01/16 10:32
 * @since 1.0.0
 */
class FusIssueTests : FusTests() {

    override val processJson: String = "json/conditionEnd.json"

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testConditionEnd() {
        FusContext
            .startProcess(
                processId,
                testOperator3Id,
                "FusIssueTests.testConditionEnd${genRandomInt(6)}",
            )
            .let { instance ->

                val instanceId = instance.instanceId
                val taskList2 =
                    FusContext
                        .queryService
                        .listTaskByInstanceId(instanceId)

                taskList2
                    .forEach { task ->
                        FusContext.executeTask(
                            task.taskId,
                            testOperator2Id,
                            mutableMapOf(
                                "day" to 1
                            )
                        )
                    }
            }
    }

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testTerminate() {
        FusContext
            .startProcess(
                processId,
                testOperator3Id,
                "FusIssueTests.testTerminate${genRandomInt(6)}",
            )
            .let { instance ->
                FusContext.runtimeService.terminate(instance.instanceId, testOperator2Id)
            }

    }
}
