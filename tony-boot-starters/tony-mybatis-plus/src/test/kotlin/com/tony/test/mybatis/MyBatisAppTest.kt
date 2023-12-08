package com.tony.test.mybatis

import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.tony.ApiSession
import com.tony.PageQuery
import com.tony.PageResult
import com.tony.annotation.EnableTonyBoot
import com.tony.utils.md5
import com.tony.mybatis.DefaultMetaObjectHandler
import com.tony.test.mybatis.db.config.DbConfig
import com.tony.test.mybatis.db.dao.UserDao
import com.tony.test.mybatis.db.po.User
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import


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
        val pageResult = userDao.ktQuery().pageResult<PageResult<User>>(PageQuery<String>())
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
                pwd = "123456$s".md5().uppercase()
            }
        }
        userDao.insertBatch(users)
    }
    @Test
    fun testDaoInsert() {
        val user =
            User().apply {
                val s = "lg1"
                userName = s
                realName = "李赣1"
                mobile = "13981842691"
                pwd = "123456$s".md5().uppercase()
            }

        userDao.insert(user)
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
    fun testDaoTransform(){
        val mapList = userDao
            .ktQuery()
            .list<Map<String, Any?>> {
                mapOf(
                    "userName" to it.userName,
                    "realName" to it.realName
                )
            }
        logger.info(mapList.toJsonString())
        val pageResult1 = userDao.selectPageResult<PageResult<User>>(PageQuery<String>(), Wrappers.emptyWrapper())
        logger.info(pageResult1.toJsonString())
    }
}


@Import(DbConfig::class)
@EnableTonyBoot
@SpringBootApplication
class TestMyBatisApp {

    @Bean
    fun defaultMetaObjectHandler() = object : DefaultMetaObjectHandler(NoopApiSession()){}
}

class NoopApiSession : ApiSession {
    override val userId: String
        get() = "1"
    override val userName: String
        get() = "aloha"
    override val tenantId: String
        get() = "1"
}
