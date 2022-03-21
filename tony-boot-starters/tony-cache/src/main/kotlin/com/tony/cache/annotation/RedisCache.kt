package com.tony.cache.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisCacheable(
    val cacheKey: String,
    val paramsNames: Array<String> = [],
    val expire: Long = TODAY_END,
    val usePrefix: Boolean = true,
    val cacheEmpty: Boolean = false
) {
    companion object {
        const val TODAY_END = -3L
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class RedisCacheEvict(
    val cacheKey: String,
    val paramsNames: Array<String> = []
)
