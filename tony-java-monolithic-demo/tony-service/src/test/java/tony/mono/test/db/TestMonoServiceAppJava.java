package tony.mono.test.db;

import tony.core.annotation.EnableTonyBoot;
import tony.mono.db.config.DbConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(DbConfig.class)
@EnableTonyBoot
@SpringBootApplication
public class TestMonoServiceAppJava {}
