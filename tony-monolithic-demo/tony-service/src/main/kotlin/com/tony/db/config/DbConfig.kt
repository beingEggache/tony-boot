package com.tony.db.config

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan(
    basePackages = ["com.tony.db"],
)
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.db.dao")
class DbConfig
