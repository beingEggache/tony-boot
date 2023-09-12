package com.tony.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 *
 * @author Tang Li
 * @date 2020-11-04 16:03
 */
@SpringBootTest(classes = [TestMonoApiWebApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MonoApiWebTest {

    @Test
    fun test() {
    }
}


