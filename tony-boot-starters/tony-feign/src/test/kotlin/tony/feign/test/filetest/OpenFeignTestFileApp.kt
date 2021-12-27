/**
 * tony-dependencies
 * OpenFeignTestFileApp
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/27 11:39
 */
package tony.feign.test.filetest

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients("tony.feign.test.client")
@SpringBootApplication
@EnableTonyBoot
class OpenFeignTestFileApp
