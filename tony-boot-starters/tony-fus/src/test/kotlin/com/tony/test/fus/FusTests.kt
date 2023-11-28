package com.tony.test.fus

import com.tony.fus.FusEngine
import com.tony.utils.string
import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
@SpringBootTest(classes = [TestFusApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
abstract class FusTests {

    @Resource
    protected lateinit var engine: FusEngine

    lateinit var processId: String

    abstract val processJson: String

    protected val testOperator1Id = "test001"
    protected val testOperator1Name = "测试1"
    protected val testOperator3Id = "test003"
    protected val testOperator3Name = "测试003"

    @BeforeEach
    fun before() {
        val processModelJson = getProcessModelJson()
        processId = engine.processService.deploy(processModelJson, "ADMIN", false)
    }

    private fun getProcessModelJson() = PathMatchingResourcePatternResolver()
        .getResource(processJson)
        .inputStream
        .readAllBytes()
        .string()

}
