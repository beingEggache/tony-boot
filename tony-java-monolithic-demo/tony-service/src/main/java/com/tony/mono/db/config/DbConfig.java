package com.tony.mono.db.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.tony.id.IdGenerator;
import com.tony.misc.YamlPropertySourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = "com.tony.mono.db")
@PropertySource(value = "classpath:mybatis-plus.config.yml", factory = YamlPropertySourceFactory.class)
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.mono.db.dao")
public class DbConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        final MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        return entity -> IdGenerator.nextId();
    }
}
