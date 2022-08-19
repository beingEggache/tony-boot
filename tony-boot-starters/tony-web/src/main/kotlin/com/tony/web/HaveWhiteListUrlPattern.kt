/**
 * HaveWhiteListUrlPattern
 *
 * @author tangli
 * @since 2022/1/17 14:02
 */
package com.tony.web

import com.tony.Env
import com.tony.utils.sanitizedPath
import org.springframework.core.env.Environment

interface HaveWhiteListUrlPattern {

    fun whiteUrlPatterns(prefix: String): Set<String> {
        val actuatorBasePath = environment.getProperty("management.endpoints.web.base-path", "/actuator")
        val actuatorPrefix = sanitizedPath("$prefix/$actuatorBasePath")
        val errorPath = environment.getProperty("server.error.path", environment.getProperty("error.path", "/error"))
        return setOf(
            sanitizedPath("$actuatorPrefix/**"),
            sanitizedPath("$prefix/$errorPath"),
            sanitizedPath("$prefix/swagger-resources/**"),
            sanitizedPath("$prefix/v2/api-docs/**"),
            sanitizedPath("$prefix/v3/api-docs/**"),
            sanitizedPath("$prefix/webjars/**"),
            sanitizedPath("$prefix/**/*.html"),
            sanitizedPath("$prefix/**/*.ico"),
            sanitizedPath("$prefix/**/*.png"),
            sanitizedPath("$prefix/**/*.js"),
            sanitizedPath("$prefix/**/*.js.map"),
            sanitizedPath("$prefix/**/*.css"),
            sanitizedPath("$prefix/**/*.css.map"),
            sanitizedPath(Env.getProperty("springdoc.swagger-ui.path", "/swagger-ui.html")),
            sanitizedPath(Env.getProperty("springdoc.swagger-ui.config-url", "/v3/api-docs/swagger-config")),
            sanitizedPath(
                Env.getProperty(
                    "springdoc.swagger-ui.oauth2-redirect-url",
                    "/swagger-ui/oauth2-redirect.html"
                )
            ),
            sanitizedPath(Env.getProperty("springdoc.api-docs.path", "/v3/api-docs"))
        )
    }

    val environment: Environment
        get() = Env
}
