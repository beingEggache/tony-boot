package com.tony.mybatis.test

import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.tony.SpringContexts
import com.tony.annotation.EnableTonyBoot
import com.tony.mybatis.db.dao.UserDao
import com.tony.mybatis.db.po.User
import com.tony.utils.uuid
import org.junit.jupiter.api.Test
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
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
    fun testDao() {
//        userDao.insert(
//            User().apply {
//                userId = uuid()
//                userName = "tony2"
//                realName = "李大毛2"
//                mobile = "13984842425"
//                pwd = "123456"
//                states = 666
//            },
//        )
//        userDao.insert(
//            User().apply {
//                userId = uuid()
//                userName = "tony3"
//                realName = "李大毛3"
//                mobile = "13984842426"
//                pwd = "123456"
//                states = 666
//            },
//        )
        val list = userDao
            .query()
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
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.mybatis.db.dao")
@EnableTonyBoot
@SpringBootApplication
class TestMyBatisApp
