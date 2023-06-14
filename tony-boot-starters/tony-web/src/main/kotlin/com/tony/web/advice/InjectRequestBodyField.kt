package com.tony.web.advice

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * RequestBody 字段值注入.
 * ## 当对应字段使用此注解时会标上 [JsonIgnore] ##
 * @author tangli
 * @since 2023/06/08 09:26
 */
@JacksonAnnotationsInside
@JsonIgnore
@Target(
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class InjectRequestBodyField(
    /**
     * 获取与[value]相等的[InjectRequestBodyAdvice]注入值.
     */
    val value: String = "",
)
