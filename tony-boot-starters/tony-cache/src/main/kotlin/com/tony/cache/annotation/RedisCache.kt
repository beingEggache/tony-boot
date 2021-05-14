package com.tony.cache.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisCacheable(
    val cacheKey: String,
    val paramsNames: Array<String> = [],
    val expire: Long = TODAY_END,
    val cacheEmpty: Boolean = false
) {
    companion object {
        const val TODAY_END = -3L
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisCacheEvict(
    val cacheKeys: Array<String>,
    val paramsNames: Array<String> = []
)
