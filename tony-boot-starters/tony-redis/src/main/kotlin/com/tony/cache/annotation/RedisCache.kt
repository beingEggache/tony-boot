package com.tony.cache.annotation

/**
 * redis缓存注解.
 *
 * 给常规的 @Cacheable 加了过期时间
 *
 * @author tangli
 * @since 2023/5/24 18:10
 */
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

/**
 * redis 删除缓存注解.
 *
 * 可重复注解.
 *
 * @author tangli
 * @since 2023/5/24 18:11
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
public annotation class RedisCacheEvict(
    val cacheKey: String,
    val expressions: Array<String> = [],
)
