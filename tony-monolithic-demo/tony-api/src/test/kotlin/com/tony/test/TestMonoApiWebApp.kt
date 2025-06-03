package tony.test

import tony.annotation.EnableTonyBoot
import tony.demo.config.DbConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@EnableTonyBoot
@Import(value = [DbConfig::class])
@SpringBootApplication
class TestMonoApiWebApp

