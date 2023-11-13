package com.tony.db.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import com.tony.ApiSession
import com.tony.db.dao.UserDao
import com.tony.db.po.User
import com.tony.id.IdGenerator
import com.tony.mybatis.DefaultMetaObjectHandler
import jakarta.annotation.Resource
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan(
    basePackages = ["com.tony.db"]
)
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.db.dao")
class DbConfig {
    @Bean
    internal fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
        return interceptor
    }

    @Bean
    internal fun metaObjectHandler(apiSession: ApiSession): MetaObjectHandler =
        MonoApiMetaObjectHandler(apiSession)

    @Bean
    fun identifierGenerator(): IdentifierGenerator =
        IdentifierGenerator { IdGenerator.nextId() }
}

class MonoApiMetaObjectHandler(
    apiSession: ApiSession,
) : DefaultMetaObjectHandler(apiSession) {
    @Lazy
    @Resource
    private lateinit var userDao: UserDao
    override val userName: String?
        get() =
            userDao
                .ktQuery()
                .select(User::realName)
                .eq(User::userId, userId)
                .oneObj<String>()

    override val tenantId: Long
        get() = 0L
}
