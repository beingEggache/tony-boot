package com.tony.test

import com.tony.PageQuery
import com.tony.db.service.RoleService
import com.tony.dto.req.RoleCreateReq
import com.tony.dto.req.RoleUpdateReq
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback

/**
 *
 * @author tangli
 * @date 2020-11-04 16:03
 */
@SpringBootTest(classes = [TestMonoApiWebApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MonoApiWebTest {

    @Resource
    private lateinit var roleService: RoleService

    @Rollback(false)
    @Test
    fun testAddRole() {
        roleService.add(
            RoleCreateReq(
                roleName = "test",
            ),
            "test"
        )
    }

    @Rollback(false)
    @Test
    fun testUpdateRole() {
        roleService.update(
            RoleUpdateReq(
                roleId = "7522585322053",
                roleName = "testUpdate",
            )
        )
    }

    @Test
    fun testPageRole() {
        roleService.page(PageQuery())
    }
    @Test
    fun testListRole() {
        roleService.list()
    }
}


