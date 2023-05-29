package com.tony.web.crpto

/**
 * 配合 [DecryptRequestBodyAdvice] 将 RequestBody 解密
 * @author tangli
 * @since 2023/05/26 16:55
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class ApiDecrypt

/**
 * 配合 [EncryptResponseBodyAdvice] 将 ResponseBody 加密
 * @author tangli
 * @since 2023/05/26 16:55
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class ApiEncrypt
