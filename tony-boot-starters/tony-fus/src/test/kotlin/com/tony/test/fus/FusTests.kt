package com.tony.test.fus

import com.tony.fus.ADMIN
import com.tony.fus.FusEngine
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.model.FusOperator
import com.tony.utils.string
import jakarta.annotation.Resource
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

    @Resource
    private lateinit var fusEngine: FusEngine

    @Resource
    private lateinit var taskMapper: FusTaskMapper

    private val resourceResolver = PathMatchingResourcePatternResolver()

    private val testOperatorId = "test001"
    private val testOperator: FusOperator =
        FusOperator(
            testOperatorId,
            "测试1",
            "1"
        )

    @Test
    fun testDeployProcess() {
        val processModelJson = resourceResolver
            .getResource("json/process.json")
            .inputStream
            .readAllBytes()
            .string()
        fusEngine.processService.deploy(processModelJson, ADMIN, false)
    }

    @Test
    fun testStartInstance() {
        val processId = "1724364540673454081"
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
            fusEngine
                .queryService
                .listTaskByInstanceId(instance.instanceId)
                .forEach { task ->
                    fusEngine.executeTask(task.taskId, testOperator)
                    fusEngine.executeTask(task.taskId, testOperator)
                }
        }
    }

    @Test
    fun getTask() {
        val task = taskMapper.selectById("1724352162695196674")
        println(task)
    }
}
