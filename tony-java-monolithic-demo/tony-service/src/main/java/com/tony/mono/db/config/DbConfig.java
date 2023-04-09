package com.tony.mono.db.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = "com.tony.mono.db")
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.tony.mono.db.dao")
public class DbConfig {
}
