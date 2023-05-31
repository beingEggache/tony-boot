package com.tony.cache.aspect.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

@SpringBootTest(classes = [TestAspectApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AspectTest {

    @Resource
    private lateinit var testAopService: TestAopService

    @Test
    fun test(){
        testAopService.testAop(TestAnnoAopArg("aloha"))
    }
}
