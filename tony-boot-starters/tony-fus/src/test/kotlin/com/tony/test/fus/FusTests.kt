package com.tony.test.fus

import com.tony.fus.ADMIN
import com.tony.fus.FusEngine
import com.tony.fus.model.FusOperator
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

    protected val testOperatorId = "test001"
    protected val testOperator1: FusOperator =
        FusOperator(
            testOperatorId,
            "测试1",
            "1"
        )
    protected val testOperator2: FusOperator =
        FusOperator(
            "zg0001",
            "张三",
            "1"
        )
    protected val testOperator3: FusOperator =
        FusOperator(
            "test003",
            "测试003",
            "1"
        )

    @BeforeEach
    fun before() {
        val processModelJson = getProcessModelJson()
        processId = engine.processService.deploy(processModelJson, ADMIN, false)
    }

    private fun getProcessModelJson() = PathMatchingResourcePatternResolver()
        .getResource(processJson)
        .inputStream
        .readAllBytes()
        .string()

}
