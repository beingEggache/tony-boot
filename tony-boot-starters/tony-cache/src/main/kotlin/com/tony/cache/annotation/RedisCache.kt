package com.tony.cache.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
public annotation class RedisCacheable(
    /**
     * 缓存键, 支持简单字符串模板,和 expressions配合使用
     */
    val cacheKey: String,
    /**
     * 通过方法参数执行spel, 生成缓存键
     */
    val expressions: Array<String> = [],
    /**
     * 多久过期, 默认-3, 到今天结束
     */
    val expire: Long = TODAY_END,
    /**
     * 缓存空值
     */
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
    val expressions: Array<String> = [],
)
