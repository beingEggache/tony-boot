package com.tony.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import com.tony.ApiSession
import com.tony.id.IdGenerator
import com.tony.mybatis.DefaultMetaObjectHandler
import com.tony.mybatis.MetaColumn
import com.tony.sys.dao.EmployeeDao
import com.tony.sys.po.Employee
import java.util.function.Function
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan(
    basePackages = ["com.tony"]
)
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.*.dao")
class DbConfig {
    @Bean
    internal fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
        return interceptor
    }

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    internal fun metaObjectHandler(
        apiSession: ApiSession,
        userNameProvider: Function<in Any?, out Any?>,
    ): MetaObjectHandler =
        DefaultMetaObjectHandler(
            apiSession,
            mapOf(MetaColumn.USER_NAME to userNameProvider)
        )

    @Bean
    internal fun userNameProvider(
        @Lazy employeeDao: EmployeeDao,
    ): Function<in Any?, out Any?> =
        Function<Any?, Any?> {
            employeeDao
                .ktQuery()
                .select(Employee::account)
                .eq(Employee::employeeId, it)
                .oneObj()
        }

    @Bean
    fun identifierGenerator(): IdentifierGenerator =
        IdentifierGenerator { IdGenerator.nextId() }
}
