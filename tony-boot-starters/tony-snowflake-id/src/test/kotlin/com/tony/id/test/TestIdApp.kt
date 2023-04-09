package com.tony.id.test

import com.tony.id.IdGenerator
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootApplication
class TestIdApp

@SpringBootTest(classes = [TestIdApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class IdAppTest {

    @Test
    fun test() {
        println(IdGenerator.nextId())
    }
}
