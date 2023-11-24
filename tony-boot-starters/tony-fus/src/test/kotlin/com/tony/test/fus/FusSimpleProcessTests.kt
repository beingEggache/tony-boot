package com.tony.test.fus

import org.junit.jupiter.api.Test
import org.springframework.transaction.annotation.Transactional

/**
 * 简单流程测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
class FusSimpleProcessTests : FusTests() {

    override val processJson = "json/simpleProcess.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = engine.processService
        processService.getById(processId)

        val args =
            mapOf(
                "day" to 8,
                "age" to 18,
                "assignee" to testOperatorId,
            )

        engine.startInstanceById(
            processId,
            testOperator1,
            args,
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

            // 测试会签审批人001【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1.operatorId)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1)
                }
            Thread.sleep(2000)

            // 测试会签审批人003【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator3.operatorId)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator3)
                }

            //撤回任务(条件路由子审批) 回到测试会签审批人003【审批】任务
            engine
                .queryService
                .recentHistoryTask(instanceId)
                .also { historyTask ->
                    engine
                        .taskService
                        .withdrawTask(historyTask.taskId, testOperator1)
                }

            // 测试会签审批人003【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator3.operatorId)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator3)
                }

            // 年龄审批【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1.operatorId)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1)
                }

            // 条件内部审核【审批】
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1.operatorId)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1)
                }

            // 条件路由子审批【审批】 抄送 结束
            engine
                .queryService
                .taskByInstanceIdAndActorId(instanceId, testOperator1.operatorId)
                .also { task ->
                    engine.executeTask(task.taskId, testOperator1)
                }
        }
    }
}
