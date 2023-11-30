package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 测试条件分支嵌套
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusConditionNestConditionTests : FusTests() {

    override val processJson = "json/conditionNestCondition.json"

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
                        "day" to 11
                    ))
                }

            // 人事审批
            val taskList2 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1Id)
                }

            // 领导审批，自动抄送，流程结束
            val taskList3 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList3
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator3Id)
                }

        }
    }
}
