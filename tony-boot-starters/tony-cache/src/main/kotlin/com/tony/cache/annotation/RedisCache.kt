package com.tony.cache.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
public annotation class RedisCacheable(
    val cacheKey: String,
    val paramsNames: Array<String> = [],
    val expire: Long = TODAY_END,
    @Suppress("unused")
    val usePrefix: Boolean = true,
    val cacheEmpty: Boolean = false,
) {
    public companion object {
        public const val TODAY_END: Long = -3L
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
public annotation class RedisCacheEvict(
    val cacheKey: String,
    val paramsNames: Array<String> = [],
)
