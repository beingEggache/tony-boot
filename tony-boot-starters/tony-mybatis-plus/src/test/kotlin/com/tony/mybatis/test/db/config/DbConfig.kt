package com.tony.mybatis.test.db.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import com.tony.id.IdGenerator
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan(
    basePackages = ["com.tony.mybatis.test.db"]
)
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.mybatis.test.dao")
class DbConfig {
    @Bean
    internal fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
        return interceptor
    }

    @Bean
    fun identifierGenerator(): IdentifierGenerator =
        IdentifierGenerator { IdGenerator.nextId() }
}
