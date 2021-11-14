package com.tony.db.config

import org.mybatis.spring.annotation.MapperScan

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan(
    basePackages = ["com.tony.db"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            classes = [Service::class, Repository::class]
        )
    ]
)
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.db.dao")
class DbConfig
