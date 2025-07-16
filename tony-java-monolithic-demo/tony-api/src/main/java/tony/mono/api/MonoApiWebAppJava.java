package tony.mono.api;

import tony.core.annotation.EnableTonyBoot;
import tony.mono.db.config.DbConfig;
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
