package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 拒绝测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusRejectTests : FusTests() {

    override val processJson = "json/process.json"

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
            val taskList1 =
                engine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)

            taskList1
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator3Id)
                }

            // 测试拒绝任务至 test3
            val taskList2 =
                engine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList2
                .forEach { task ->
                    engine.taskService.rejectTask(
                        task,
                        testOperator1Id
                    )
                }

            // 当前审批驳回到发起人，发起人审批
            val taskList3 =
                engine
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList3
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator3Id)
                }
        }
    }
}
