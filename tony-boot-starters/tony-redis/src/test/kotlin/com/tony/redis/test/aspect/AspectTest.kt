package com.tony.redis.test.aspect

import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestAspectApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AspectTest {

    @Resource
    private lateinit var testAopService: TestAopService

    @Test
    fun test(){
        testAopService.testAop(TestAnnoAopArg("aloha"))
    }
}
