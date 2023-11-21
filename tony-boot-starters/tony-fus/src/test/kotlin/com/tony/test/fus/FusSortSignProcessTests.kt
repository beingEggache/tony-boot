package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 依次审批测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusSortSignProcessTests : FusTests() {

    override val processJson = "json/sortSign.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        processService.getByNameAndVersion(process.processName, process.processVersion)
        fusEngine.startInstanceById(
            processId,
            testOperator1,
        )?.let { instance ->
            // 发起
            val instanceId = instance.instanceId
            val taskList1 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

            //领导审批
            val taskList2 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator1)
                }

            //领导审批
            val taskList3 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList3
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator3)
                }
        }
    }
}
