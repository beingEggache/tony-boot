package com.tony.mybatis.test

import com.tony.JPageQuery
import com.tony.SpringContexts
import com.tony.annotation.EnableTonyBoot
import com.tony.mybatis.test.db.dao.UserDao
import com.tony.mybatis.test.db.po.User
import com.tony.utils.getLogger
import com.tony.utils.md5Uppercase
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootTest(
    classes = [TestMyBatisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class MyBatisAppTest {

    @Resource
    lateinit var userDao: UserDao

    private val logger = getLogger()

    @Test
    fun testDaoQuery() {

        val userId = userDao.ktQuery().list().first().userId
        val oneMap = userDao.ktQuery().eq(User::userId, userId).oneMap()
        logger.info(oneMap.toJsonString())
        val oneObj = userDao.ktQuery().eq(User::userId, userId).oneObj<String>()
        logger.info(oneObj!!::class.java.name)
        logger.info(oneObj.toJsonString())
        val pageResult = userDao.ktQuery().pageResult(JPageQuery<String>())
        logger.info(pageResult.toJsonString())
    }

    @Test
    fun testDaoInsertBatch() {
        val users = (1..9).map {
            User().apply {
                val s = "sxc$it"
                userName = s
                realName = "孙笑川$it"
                mobile = "1398184268$it"
                pwd = "123456$s".md5Uppercase()
            }
        }
        userDao.insertBatch(users)
    }

    @Test
    fun testDaoOneMap() {
        val list = userDao
            .ktQuery()
            .select("sum(states)")
            .oneMap()
        println(list)
    }

    @Test
    fun testQueryListMap() {
        val jdbcTemplate = SpringContexts
            .getBean<NamedParameterJdbcTemplate>()
        val list = jdbcTemplate
            .queryForList("select * from t_sys_user", mapOf<String, Any>())
        println(list)
    }

    @Test
    fun testQueryListByRowMapper() {
        val jdbcTemplate = SpringContexts
            .getBean<NamedParameterJdbcTemplate>()
        val users = jdbcTemplate.query("select * from t_sys_user", BeanPropertyRowMapper(User::class.java))
        println(users)
    }
}


@ComponentScan(
    basePackages = ["com.tony.mybatis.test.db"],
)
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.mybatis.test.db.dao")
@EnableTonyBoot
@SpringBootApplication
class TestMyBatisApp
