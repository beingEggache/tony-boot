package com.tony.mybatis.test

import com.tony.SpringContexts
import com.tony.annotation.EnableTonyBoot
import com.tony.mybatis.db.dao.UserDao
import com.tony.mybatis.db.po.User
import org.junit.jupiter.api.Test
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.AdviceMode
import org.springframework.context.annotation.ComponentScan
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.annotation.Resource


@SpringBootTest(
    classes = [TestMyBatisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class MyBatisAppTest {

    @Resource
    lateinit var userDao: UserDao

    @Test
    fun testDaoQuery() {
        val list = userDao.ktQuery().list()
        println(userDao::class.java)
        println(list)
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
    basePackages = ["com.tony.mybatis.db"],
)
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.ASPECTJ)
@MapperScan("com.tony.mybatis.db.dao")
@EnableTonyBoot
@SpringBootApplication
class TestMyBatisApp
