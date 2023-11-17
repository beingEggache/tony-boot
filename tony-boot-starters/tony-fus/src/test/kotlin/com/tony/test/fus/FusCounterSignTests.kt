package com.tony.test.fus

import com.tony.fus.model.FusOperator
import com.tony.utils.alsoIf
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusCounterSignTests : FusTests() {

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

    override val processJson = "json/counterSign.json"

    @Rollback(false)
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testOrSign() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        if (process != null) {
            processService.getByVersion(process.processName, process.processVersion)
        }

        val args = mapOf(
            "day" to 8,
            "assignee" to testOperatorId
        )

        fusEngine.startInstanceById(
            processId,
            testOperator1,
            args
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


            //会签1
            val taskList2 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList2
                .forEach { task ->
                    fusEngine
                        .queryService
                        .listTaskActorByTaskId(task.taskId)
                        .any {
                            it.actorId == testOperator1.operatorId
                        }.alsoIf {
                            fusEngine.executeTask(
                                task.taskId,
                                testOperator1
                            )
                        }

                }

            // 会签2
            val taskList3 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList3
                .forEach { task ->
                    fusEngine
                        .queryService
                        .listTaskActorByTaskId(task.taskId)
                        .any {
                            it.actorId == testOperator3.operatorId
                        }.alsoIf {
                            fusEngine.executeTask(
                                task.taskId,
                                testOperator3
                            )
                        }
                }

        }
    }
}
