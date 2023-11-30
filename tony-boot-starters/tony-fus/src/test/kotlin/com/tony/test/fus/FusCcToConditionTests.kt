package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 测试抄送节点跟条件分支
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusCcToConditionTests : FusTests() {

    override val processJson = "json/ccToCondition.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = engine.processService
        processService.getById(processId)

        engine.startInstanceById(
            processId,
            testOperator1Id,
        ).let { instance ->
            // 发起
            val instanceId = instance.instanceId
            val taskList1 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id, mutableMapOf(
                        "day" to 8
                    ))
                }

            // 领导审批，自动抄送，流程结束
            val taskList2 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator3Id)
                }

        }
    }
}
