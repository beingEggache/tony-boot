package com.tony.test

import com.tony.db.service.ModuleService
import com.tony.jwt.config.JwtToken
import com.tony.utils.println
import com.tony.utils.toJsonString
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

/**
 *
 * @author tangli
 * @since 2020-11-04 16:03
 */
@SpringBootTest(classes = [MonoApiWebTestApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ModuleTest {

    @Resource
    lateinit var moduleService: ModuleService

    @Test
    fun test() {
        val modules = moduleService.listRouteAndComponentModules("b066a8a6dc2a4bdbb6013043df8400b2", "Lx-Api")
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


