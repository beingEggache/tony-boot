@file:JvmName("MonoValues")

package tony.core.model

import java.time.temporal.Temporal

/**
 * 包装成简单值响应统一结构.
 * @return [MonoValue]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun Boolean.wrap(): MonoValue<Boolean> =
    MonoValueImpl(this)

/**
 * 包装成简单值响应统一结构.
 * @return [MonoValue]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun String.wrap(): MonoValue<String> =
    MonoValueImpl(this)

/**
 * 包装成简单值响应统一结构.
 * @return [MonoValue]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun <E : Number> E.wrap(): MonoValue<E> =
    MonoValueImpl(this)

/**
 * 包装成简单值响应统一结构.
 * @return [MonoValue]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun <E : Enum<*>> E.wrap(): MonoValue<E> =
    MonoValueImpl(this)

/**
 * 包装成简单值响应统一结构.
 * @return [Temporal]
 * @author tangli
 * @date 2025/07/17 10:21
 */
public fun <E : Temporal> E.wrap(): MonoValue<E> =
    MonoValueImpl(this)
