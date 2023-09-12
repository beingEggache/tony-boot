@file:Suppress("RedundantVisibilityModifier")

package com.tony.annotation.web.support

import com.tony.annotation.web.support.InjectEmptyIfNull.Companion.DEFAULT_EMPTY

/**
 * RequestBody 字段值注入.
 * @author Tang Li
 * @date 2023/06/08 09:26
 */
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class InjectRequestBodyField(
    /**
     * 获取与[value]相等的[com.tony.web.support.RequestBodyFieldInjector]注入值.
     */
    val value: String = "",

    /**
     * 是否覆盖原值
     */
    val override: Boolean = false,
)

/**
 * 当字段为 null 时,如果是字符串, 注入空字符串, 如果是空列表数组集合,注入 '[]',如果是空对象,空map,注入 '{}'.
 *
 * ### 请自行控制反射安全, 做好空构造函数. ###
 *
 * @author Tang Li
 * @date 2023/7/6 15:07
 */
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@InjectRequestBodyField(value = DEFAULT_EMPTY, override = true)
public annotation class InjectEmptyIfNull {
    public companion object {
        public const val DEFAULT_EMPTY: String = "DEFAULT_EMPTY"
    }
}
