package com.tony.annotation.web.auth

import java.lang.annotation.Inherited

/**
 * 不需要登录检验 注解.
 *
 * @author tangli
 * @since 2023/5/25 15:14
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
public annotation class NoLoginCheck
