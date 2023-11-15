package com.tony.test.fus

import com.tony.fus.ADMIN
import com.tony.fus.FusEngine
import com.tony.fus.model.FusOperator
import com.tony.utils.getLogger
import com.tony.utils.string
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
@SpringBootTest(classes = [TestFusApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FusTests {

    private val logger = getLogger()

    @Resource
    private lateinit var fusEngine: FusEngine

    private lateinit var processId: String

    private val resourceResolver = PathMatchingResourcePatternResolver()

    private val testOperatorId = "test001"
    private val testOperator: FusOperator =
        FusOperator(
            testOperatorId,
            "测试1",
            "1"
        )

    @BeforeEach
    fun before() {
        val processModelJson = resourceResolver
            .getResource("json/process.json")
            .inputStream
            .readAllBytes()
            .string()
        processId = fusEngine.processService.deploy(processModelJson, ADMIN, false)
    }

    @Test
    fun testStartInstance() {
        val processService = fusEngine.processService
        val process = processService.getById(processId)
        if (process != null) {
            processService.getByVersion(process.processName, process.processVersion)
        }
        fusEngine.startInstanceById(
            processId,
            testOperator,
            mapOf(
                "day" to 8,
                "assignee" to testOperatorId
            )
        )?.let { instance ->
            val queryService = fusEngine
                .queryService
            val taskList1 = queryService
                .listTaskByInstanceId(instance.instanceId)
            logger.info(taskList1.toJsonString())
            taskList1
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator)
                }
            val taskList2 = queryService
                .listTaskByInstanceId(instance.instanceId)
            logger.info(taskList2.toJsonString())
            taskList2
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator)
                }
        }
    }

}
