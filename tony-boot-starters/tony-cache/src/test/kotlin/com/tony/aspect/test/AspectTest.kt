package com.tony.aspect.test

import com.tony.utils.asTo
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

@SpringBootTest(classes = [TestAspectApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AspectTest {

    @Resource
    private lateinit var testTarget: TestTarget

    @Test
    fun test(){
        testTarget.gogo()
        testTarget.asTo<ITestMixinAspect>()?.go()
    }
}
