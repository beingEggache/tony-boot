package tony.test

import tony.PageQuery
import tony.demo.sys.dto.req.RoleAddReq
import tony.demo.sys.dto.req.RoleUpdateReq
import tony.demo.sys.service.RoleService
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
            RoleAddReq(
                roleName = "test",
                enabled = true
            ),
        )
    }

    @Rollback(false)
    @Test
    fun testUpdateRole() {
        roleService.update(
            RoleUpdateReq(
                roleId = "7522585322053",
                roleName = "testUpdate",
                enabled = true
            )
        )
    }

    @Test
    fun testPageRole() {
        roleService.list(PageQuery())
    }
}


