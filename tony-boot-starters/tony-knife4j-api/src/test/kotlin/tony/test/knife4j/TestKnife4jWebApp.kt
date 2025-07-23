/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.test.knife4j

import jakarta.annotation.Resource
import org.springdoc.api.AbstractOpenApiResource
import org.springdoc.webmvc.api.OpenApiWebMvcResource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import tony.core.annotation.EnableTonyBoot
import tony.core.utils.toJsonString
import java.util.Locale

@EnableTonyBoot
@SpringBootApplication
class TestKnife4jWebApp {

    @Resource
    lateinit var openApiWebMvcResource: OpenApiWebMvcResource

    @EventListener(ApplicationReadyEvent::class)
    fun syncApiJson() {
        val method = AbstractOpenApiResource::class
            .java
            .getDeclaredMethod(
                "getOpenApi",
                String::class.java,
                Locale::class.java
            )
        method.setAccessible(true)
        val openApi = method.invoke(
            openApiWebMvcResource,
            "http://localhost:10000",
            Locale.CHINA
        )
        openApi.toJsonString()
    }
}
