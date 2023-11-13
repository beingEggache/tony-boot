package com.tony.test.fus

import com.tony.fus.ADMIN
import com.tony.fus.FusEngine
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

    private val resourceResolver = PathMatchingResourcePatternResolver()

    @Test
    fun testProcess() {
        val processModelJson = resourceResolver
            .getResource("json/process.json")
            .inputStream
            .readAllBytes()
            .string()
        fusEngine.processService.deploy(processModelJson, ADMIN, false)
    }
}
