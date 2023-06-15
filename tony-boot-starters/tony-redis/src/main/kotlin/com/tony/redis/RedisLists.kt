package com.tony.redis

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.SpringContexts
import com.tony.redis.service.RedisService

/**
 * redis 列表操作单例.
 *
 * @author tangli
 * @since 2021-03-24 10:12
 */
public object RedisLists {

    private val redisService: RedisService by SpringContexts.getBeanByLazy()

    /**
     * @see [RedisService.leftPop]
     */
    @JvmStatic
    public fun <T : Any> leftPop(key: String, type: Class<T>): T? =
        redisService.leftPop(key, type)

    /**
     * @see [leftPop]
     */
    @JvmStatic
    public fun <T : Any> leftPop(key: String, type: JavaType): T? =
        redisService.leftPop(key, type)

    /**
     * @see [leftPop]
     */
    public fun <T : Any> leftPop(key: String, type: TypeReference<T>): T? =
        redisService.leftPop(key, type)

    /**
     * @see [leftPop]
     */
    public fun <T : Any> leftPop(key: String, count: Long): List<Any>? =
        redisService.leftPop<T>(key, count)

    /**
     * @see [RedisService.rightPop]
     */
    public fun <T : Any> rightPop(key: String, type: Class<T>): T? =
        redisService.rightPop(key, type)

    /**
     * @see [rightPop]
     */
    public fun <T : Any> rightPop(key: String, type: JavaType): T? =
        redisService.rightPop(key, type)

    /**
     * @see [rightPop]
     */
    public fun <T : Any> rightPop(key: String, type: TypeReference<T>): T? =
        redisService.rightPop(key, type)

    /**
     * @see [rightPop]
     */
    public fun <T : Any> rightPop(key: String, count: Long): List<Any>? =
        redisService.rightPop<T>(key, count)

    /**
     * @see [RedisService.index]
     */
    public fun <T : Any> index(key: String, index: Long, type: Class<T>): T? =
        redisService.index(key, index, type)

    /**
     * @see [index]
     */
    public fun <T : Any> index(key: String, index: Long, type: JavaType): T? =
        redisService.index(key, index, type)

    /**
     * @see [index]
     */
    public fun <T : Any> index(key: String, index: Long, type: TypeReference<T>): T? =
        redisService.index(key, index, type)

    /**
     * @see [RedisService.range]
     */
    public fun <T : Any> range(key: String, start: Long, end: Long): List<T>? =
        redisService.range(key, start, end)

    /**
     * @see [RedisService.size]
     */
    public fun size(key: String): Long? =
        redisService.size(key)
}
