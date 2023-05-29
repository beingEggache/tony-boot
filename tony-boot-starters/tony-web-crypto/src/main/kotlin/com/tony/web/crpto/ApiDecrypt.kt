package com.tony.web.crpto

/**
 * 配合 [DecryptRequestAdvice] 将 RequestBody 解密
 * @author tangli
 * @since 2023/05/26 16:55
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class ApiDecrypt
