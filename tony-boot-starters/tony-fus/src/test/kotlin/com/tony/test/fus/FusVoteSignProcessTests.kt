package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 票签流程测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusVoteSignProcessTests : FusTests() {

    override val processJson = "json/voteSign.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = engine.processService
        processService.getById(processId)

        engine.startInstanceById(
            processId,
            testOperator1,
        ).let { instance ->
            // 发起
            val instanceId = instance.instanceId
            val taskList1 =
                engine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    engine.executeTask(task.taskId, testOperator1)
                }

            //test1 领导审批同意
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1.operatorId)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1)
                }

            //test3 领导审批同意
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator3.operatorId)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator3)
                }
        }
    }
}
