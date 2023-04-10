package com.tony.mono.test.db;

import com.tony.annotation.EnableTonyBoot;
import com.tony.mono.db.config.DbConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(DbConfig.class)
@EnableTonyBoot
@SpringBootApplication
public class TestMonoServiceAppJava {}
