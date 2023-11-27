package com.tony.test.fus

import com.tony.fus.ADMIN
import com.tony.fus.FusEngine
import com.tony.test.fus.TestFusSupervisorApp.Companion.user1
import com.tony.test.fus.TestFusSupervisorApp.Companion.user2
import com.tony.test.fus.TestFusSupervisorApp.Companion.user3
import com.tony.test.fus.TestFusSupervisorApp.Companion.user4
import com.tony.utils.string
import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.transaction.annotation.Transactional

/**
 * 连续主管审批顺签测试
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
@SpringBootTest(
    properties = [
        "supervisor-task-actor-provider=true",
    ],
    classes = [TestFusSupervisorApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FusSupervisorProcessTests {
    @Resource
    lateinit var fusEngine: FusEngine

    lateinit var processId: String

    @BeforeEach
    fun before() {
        val processModelJson = getProcessModelJson()
        processId = fusEngine.processService.deploy(processModelJson, ADMIN, false)
    }

    private fun getProcessModelJson() = PathMatchingResourcePatternResolver()
        .getResource(processJson)
        .inputStream
        .readAllBytes()
        .string()

    private val processJson = "json/supervisor.json"

    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val processService = fusEngine.processService
        processService.getById(processId)

        val args =
            mapOf(
                "day" to 4
            )

        fusEngine.startInstanceById(
            processId,
            user4,
            args,
        ).let { instance ->
            // 四级部门发起审批
            val instanceId = instance.instanceId
            val taskList1 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList1
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, user4)
                }

            //四级部门审核
            val taskList2 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, user4)
                }

            //三级部门审核
            val taskList3 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList3
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, user3)
                }

            //二级部门审核
            val taskList4 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList4
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, user2)
                }

            //一级部门审核
            val taskList5 =
                fusEngine
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList5
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, user1)
                }
        }
    }
}
