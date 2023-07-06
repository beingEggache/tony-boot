package com.tony.web.support.annotation

import com.tony.web.support.DEFAULT_EMPTY

/**
 * RequestBody 字段值注入.
 * @author tangli
 * @since 2023/06/08 09:26
 */
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS,
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class InjectRequestBodyField(
    /**
     * 获取与[value]相等的[com.tony.web.support.RequestBodyFieldInjector]注入值.
     */
    val value: String = "",

    /**
     * 当为 null 时才设值
     */
    val defaultIfNull: Boolean = false,
)

/**
 * 当字段为 null 时,如果是字符串, 注入空字符串, 如果是空列表数组集合,注入 '[]',如果是空对象,空map,注入 '{}'.
 *
 * ### 请自行控制反射安全, 做好空构造函数. ###
 *
 * @author tangli
 * @since 2023/7/6 15:07
 */
@Target(
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.RUNTIME)
@InjectRequestBodyField(value = DEFAULT_EMPTY, defaultIfNull = true)
public annotation class InjectEmptyIfNull
