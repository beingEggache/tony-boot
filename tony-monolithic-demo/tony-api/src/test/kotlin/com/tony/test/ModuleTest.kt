package com.tony.test

import com.tony.demo.sys.dto.req.ModuleQuery
import com.tony.demo.sys.service.ModuleService
import com.tony.jwt.JwtToken
import com.tony.utils.println
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 *
 * @author tangli
 * @date 2020-11-04 16:03
 */
@SpringBootTest(classes = [TestMonoApiWebApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ModuleTest {

    @Resource
    lateinit var moduleService: ModuleService

    @Test
    fun test() {
        val modules = moduleService.list(ModuleQuery())
        println(modules.toJsonString())
    }

    @Test
    fun test1() {
        JwtToken.gen("userId" to "99efd6bbc03b491191ca3206bd20046f").println()
    }

    @Test
    fun test3() {
        Thread.sleep(1000 * 20)

    }

}


