package com.tony.web.advice

/**
 * RequestBody 字段值注入.
 * @author tangli
 * @since 2023/06/08 09:26
 */
@Target(
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class InjectRequestBodyField(
    /**
     * 获取与[value]相等的[InjectRequestBodyAdvice]注入值.
     */
    val value: String = "",

    /**
     * 当为 null 时才设值
     */
    val defaultIfNull: Boolean = false,
)
