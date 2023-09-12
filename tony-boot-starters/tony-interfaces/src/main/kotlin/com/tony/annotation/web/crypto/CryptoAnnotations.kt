package com.tony.annotation.web.crypto

/**
 * 配合 DecryptRequestBodyAdvice 将 RequestBody 解密
 * @author Tang Li
 * @date 2023/05/26 16:55
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class DecryptRequestBody

/**
 * 配合 EncryptResponseBodyAdvice 将 ResponseBody 加密
 * @author Tang Li
 * @date 2023/05/26 16:55
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
public annotation class EncryptResponseBody
