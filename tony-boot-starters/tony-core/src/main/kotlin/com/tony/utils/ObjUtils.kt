package com.tony.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * ObjUtils is
 * @author tangli
 * @since 2023/06/13 17:10
 */
/**
 * println
 */
public fun <T> T?.println(): Unit = println(this)

/**
 * 获取 logger.
 * @param name logger 名
 */
public fun getLogger(name: String?): Logger = LoggerFactory.getLogger(name)

/**
 * 将 [this] 自身 转为 [E] 类型
 *
 * @receiver [Any]?
 * @param E 转换后的类型
 */
@Suppress("UNCHECKED_CAST")
public fun <E> Any?.asTo(): E? where E : Any = this as E?

/**
 * 将 [this] 自身 转为 [E] 类型
 *
 * @receiver [Any]
 * @param E 转换后的类型
 */
@Suppress("UNCHECKED_CAST")
public fun <E> Any.asToNotNull(): E where E : Any = this as E

/**
 * 将字符串类型或数值类型转换成数值.
 *
 * @param E 数值类型
 * @param numberType 数值类型
 */
public fun <E : Number> Any?.toNumber(numberType: Class<in E>): E {
    return when (this) {
        is Number -> this.toNumber(numberType)
        is CharSequence -> this.toNumber(numberType)
        else -> throw IllegalStateException("${this?.javaClass} can't transform to number.")
    }
}

@JvmSynthetic
public fun <T> T.getLogger(): Logger where T : Any =
    LoggerFactory.getLogger(this::class.java)
