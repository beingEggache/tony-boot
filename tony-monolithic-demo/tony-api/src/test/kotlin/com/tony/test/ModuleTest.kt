package com.tony.test

import com.tony.api.ApiWebApp
import com.tony.core.utils.toJsonString
import com.tony.db.service.ModuleService
import javax.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 *
 * @author tangli
 * @since 2020-11-04 16:03
 */
@SpringBootTest(classes = [ApiWebApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ModuleTest {

    @Resource
    lateinit var moduleService: ModuleService

    @Test
    fun test() {
        val modules = moduleService.listRouteAndComponentModules("b066a8a6dc2a4bdbb6013043df8400b2", "Lx-Api")
        println(modules.toJsonString())

    }

}


