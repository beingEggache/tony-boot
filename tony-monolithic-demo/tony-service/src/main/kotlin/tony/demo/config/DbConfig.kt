package tony.demo.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import java.util.function.Function
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.PropertySource
import org.springframework.transaction.annotation.EnableTransactionManagement
import tony.ApiSession
import tony.demo.sys.dao.EmployeeDao
import tony.demo.sys.po.Employee
import tony.id.IdGenerator
import tony.misc.YamlPropertySourceFactory
import tony.mybatis.DefaultMetaObjectHandler
import tony.mybatis.MetaColumn

@MapperScan("tony.demo.*.dao")
@ComponentScan(basePackages = ["tony.demo"])
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:db.config.yml", factory = YamlPropertySourceFactory::class)
@Configuration(proxyBeanMethods = false)
class DbConfig {
    @Bean
    private fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.MYSQL))
        return interceptor
    }

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    private fun metaObjectHandler(
        apiSession: ApiSession,
        userNameProvider: Function<in Any?, out Any?>,
    ): MetaObjectHandler =
        DefaultMetaObjectHandler(
            apiSession,
            mapOf(MetaColumn.USER_NAME to userNameProvider)
        )

    @Bean
    private fun userNameProvider(
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
    private fun identifierGenerator(): IdentifierGenerator =
        IdentifierGenerator { IdGenerator.nextId() }
}
