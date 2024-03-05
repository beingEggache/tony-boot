package com.tony.mono.api;

import com.tony.annotation.EnableTonyBoot;
import com.tony.mono.db.config.DbConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * @author tangli
 */
@Import(DbConfig.class)
@EnableTonyBoot
@SpringBootApplication
public class MonoApiWebAppJava {

    public static void main(String[] args) {
        SpringApplication.run(MonoApiWebAppJava.class);
    }
}
