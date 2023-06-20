@file:JvmName("Redis")

package com.tony.redis

import com.tony.SpringContexts
import com.tony.redis.service.RedisService
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.redis.core.script.RedisScript

internal val redisTemplate: RedisTemplate<String, Any> by SpringContexts.getBeanByLazy("redisTemplate")

public val redisService: RedisService by SpringContexts.getBeanByLazy()

public val valueOp: ValueOperations<String, Any> by lazy {
    redisTemplate.opsForValue()
}

public val listOp: ListOperations<String, Any> by lazy {
    redisTemplate.opsForList()
}

public val hashOp: HashOperations<String, String, Any> by lazy {
    redisTemplate.opsForHash()
}

/**
 * 简单锁脚本引用
 */
internal val lockScript: RedisScript<Long> =
    RedisScript.of(ClassPathResource("META-INF/scripts/lockKey.lua"), Long::class.java)

/**
 * 批量删除脚本
 * @since redis 3.2+
 */
internal val deleteKeyByPatternScript: RedisScript<Long?> =
    RedisScript.of(ClassPathResource("META-INF/scripts/deleteByKeyPatterns.lua"), Long::class.java)

private val QUOTES_CHARS = arrayOf('\'', '\"')

internal fun String.trimQuotes(): String = when {
    this.length < 2 -> this
    first() in QUOTES_CHARS && last() in QUOTES_CHARS -> substring(1, this.length - 1)
    else -> this
}
