package com.tony.test

import com.tony.annotation.EnableTonyBoot
import com.tony.config.DbConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@EnableTonyBoot
@Import(value = [DbConfig::class])
@SpringBootApplication
class TestMonoApiWebApp

