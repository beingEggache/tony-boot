package com.tony.test.fus

import com.tony.fus.ADMIN
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
    protected lateinit var fusEngine: FusEngine

    lateinit var processId: String

    abstract val processJson: String

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

}
