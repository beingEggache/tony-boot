/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.test.mybatis

import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.tony.ApiSession
import com.tony.PageQuery
import com.tony.annotation.EnableTonyBoot
import com.tony.mybatis.DbMetaObjectHandler
import com.tony.mybatis.DefaultMetaObjectHandler
import com.tony.test.mybatis.db.config.DbConfig
import com.tony.test.mybatis.db.dao.EmployeeDao
import com.tony.test.mybatis.db.po.Employee
import com.tony.utils.genRandomInt
import com.tony.utils.getLogger
import com.tony.utils.md5
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import


@SpringBootTest(
    classes = [TestMyBatisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class MyBatisAppTest {

    @Resource
    lateinit var employeeDao: EmployeeDao

    private val logger = getLogger()

    @BeforeAll
    fun beforeAll() {
        //namedParameterJdbcTemplate.execute("delete from sys_user") { it.execute() }
    }

    @Order(1)
    @Test
    fun testDaoInsert() {
        val user =
            Employee().apply {
                val s = "lg3"
                account = s
                realName = "李赣3"
                employeeMobile = "13981842693"
                pwd = "123456$s".md5().uppercase()
            }

        employeeDao.insert(user)
    }

    @Order(2)
    @Test
    fun testDaoInsertBatch() {
        val users = (1..999).map { index ->
            Employee().apply {
                val indexStr = index.toString().padStart(3, '0')
                val s = "sxc$indexStr"
                account = s
                realName = "孙笑川$indexStr"
                employeeMobile = "13984842$indexStr"
                pwd = "${"123456".md5().uppercase()}$s".md5().uppercase()
            }
        }
        employeeDao.insert(users)
    }

    @Order(3)
    @Test
    fun testDaoUpdate() {
        val one = employeeDao
            .ktQuery()
            .eq(Employee::employeeMobile, "13981842693")
            .one()
        one.account = "lg1"

        employeeDao
            .updateById(one)

    }

    @Order(4)
    @Test
    fun testDaoOneMap() {
        val list = employeeDao
            .ktQuery()
            .select("sum(enabled)")
            .oneMap()
        println(list)
    }

    @Order(5)
    @Test
    fun testDaoTransform() {
        val mapList = employeeDao
            .ktQuery()
            .list<Map<String, Any?>> {
                mapOf(
                    "account" to it.account,
                    "realName" to it.realName
                )
            }
        logger.info(mapList.toJsonString())
        val pageResult1 = employeeDao.selectPageResult(PageQuery<String>(), Wrappers.emptyWrapper())
        logger.info(pageResult1.toJsonString())
    }

    @Order(6)
    @Test
    fun testDaoQuery() {
        val employeeId = employeeDao.ktQuery().list().first().employeeId
        val oneMap = employeeDao.ktQuery().eq(Employee::employeeId, employeeId).oneMap()
        logger.info(oneMap.toJsonString())
        val oneObj = employeeDao.ktQuery().eq(Employee::employeeId, employeeId).oneObj<String>()
        logger.info(oneObj!!::class.java.name)
        logger.info(oneObj.toJsonString())
        val pageResult = employeeDao.ktQuery().pageResult(PageQuery<String>())
        logger.info(pageResult.toJsonString())
    }

    @Order(7)
    @Test
    fun testDaoChainUpdateAndPhysicalRemove() {

        val userNameStr = "lg${genRandomInt(6)}"
        val user =
            Employee().apply {
                account = userNameStr
                realName = "李赣$userNameStr"
                employeeMobile = "13981$userNameStr"
                pwd = "${"123456".md5().uppercase()}$userNameStr".md5().uppercase()
            }

        employeeDao.insert(user)

        employeeDao
            .ktUpdate()
            .eq(Employee::account, userNameStr)
            .set(Employee::realName, "测试测试")
            .update(user)

        employeeDao
            .ktUpdate()
            .eq(Employee::account, userNameStr)
            .remove()

        employeeDao
            .ktUpdate()
            .eq(Employee::account, userNameStr)
            .physicalRemove()
    }

    @Order(8)
    @Test
    fun testPhysicalRemove() {
        val userIdList = (1..50).map {
            val userNameStr1 = "lg${genRandomInt(6)}"
            val user1 =
                Employee().apply {
                        account = userNameStr1
                        realName = "李赣$userNameStr1"
                        employeeMobile = "13981$userNameStr1"
                        pwd = "123456$userNameStr1".md5().uppercase()
                    }

            employeeDao.insert(user1)
            user1.employeeId
        }

        employeeDao.physicalDeleteByIds(userIdList)

        val userNameStr1 = "lg${genRandomInt(6)}"
        val user1 =
            Employee().apply {
                    account = userNameStr1
                    realName = "李赣$userNameStr1"
                    employeeMobile = "13981$userNameStr1"
                    pwd = "123456$userNameStr1".md5().uppercase()
                }

        employeeDao.insert(user1)
        employeeDao.physicalDeleteById(user1.employeeId)
    }
}

@Import(DbConfig::class)
@EnableTonyBoot
@SpringBootApplication
class TestMyBatisApp {

    @Bean
    fun defaultMetaObjectHandler(): DbMetaObjectHandler = DefaultMetaObjectHandler(NoopApiSession())
}

class NoopApiSession : ApiSession {
    override val appId: String
        get() = "appId"
    override val userId: String
        get() = "1"
    override val userName: String
        get() = "aloha"
    override val tenantId: String
        get() = "1"
}
