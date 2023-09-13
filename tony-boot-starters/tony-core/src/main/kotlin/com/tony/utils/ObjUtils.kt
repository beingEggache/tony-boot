@file:JvmName("ObjUtils")

package com.tony.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * ObjUtils is
 * @author Tang Li
 * @date 2023/06/13 17:10
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

@JvmSynthetic
public fun <T> T.getLogger(): Logger where T : Any =
    LoggerFactory.getLogger(this::class.java)

/**
 * 是类型或子类型
 * @param [types] 类型
 * @return [Boolean]
 * @author Tang Li
 * @date 2023/09/13 10:25
 * @since 1.0.0
 */
public fun Any.isTypesOrSubTypesOf(vararg types: Class<*>?): Boolean =
    types.any { this::class.java.isTypeOrSubTypeOf(it) }
