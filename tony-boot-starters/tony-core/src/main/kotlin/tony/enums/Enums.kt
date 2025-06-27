/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:JvmName("Enums")

package tony.enums

/**
 * 全局枚举
 * @author tangli
 * @date 2023/09/13 19:16
 */
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tony.utils.asToNotNull
import tony.utils.isTypesOrSubTypesOf
import tony.utils.throwIfNull

/**
 * 全局枚举接口.
 * 已和 jackson 和 mybatis-plus 结合.
 * @author tangli
 * @date 2023/09/13 19:16
 */
public sealed interface EnumValue<T : Serializable> {
    @get:JsonValue
    public val value: T?
}

/**
 * 全局整形枚举接口.
 * @author tangli
 * @date 2023/09/13 19:16
 */
public interface IntEnumValue : EnumValue<Int>

/**
 * 全局字符串枚举接口.
 * @author tangli
 * @date 2023/09/13 19:16
 */
public interface StringEnumValue : EnumValue<String>

public const val DEFAULT_INT_VALUE: Int = -1

public const val DEFAULT_STRING_VALUE: String = ""

@get:JvmSynthetic
internal val creators = ConcurrentHashMap<Class<*>, EnumCreator<*, *>>()

private val logger: Logger = LoggerFactory.getLogger(EnumCreator::class.java)

internal sealed interface EnumCreatorFactory {
    @JvmSynthetic
    fun getCreator(clazz: Class<*>): EnumCreator<*, *> =
        creators
            .getOrPut(clazz) {
                logger.debug("${clazz.name} EnumCreator initialized.")
                clazz
                    .classes
                    .firstOrNull { it.isTypesOrSubTypesOf(EnumCreator::class.java) }
                    .throwIfNull("${clazz.name} must have an EnumCreator.")
                    .constructors
                    .first()
                    .newInstance(null)
                    .asToNotNull()
            }

    fun <T, R> creatorOf(
        clazz: Class<T>,
    ): EnumCreator<T, R>
        where T : EnumValue<R>,
              R : Serializable =
        creators
            .getOrPut(clazz) {
                logger.debug("${clazz.name} EnumCreator initialized.")
                clazz
                    .classes
                    .firstOrNull { it.isTypesOrSubTypesOf(EnumCreator::class.java) }
                    .throwIfNull("${clazz.name} must have an EnumCreator.")
                    .constructors
                    .first()
                    .newInstance(null)
                    .asToNotNull()
            }.asToNotNull()
}

/**
 * 枚举 构建器.
 * @author tangli
 * @date 2023/09/13 19:16
 */
public abstract class EnumCreator<out E, KEY>(
    private val clazz: Class<out E>,
) where E : EnumValue<KEY>, KEY : Serializable {
    public open fun create(value: KEY): E? =
        enumValues.firstOrNull { value == it.value }

    private val enumValues: Array<out E> by lazy(LazyThreadSafetyMode.NONE) {
        clazz.enumConstants
    }

    public companion object : EnumCreatorFactory {
        @JvmStatic
        override fun <T, R> creatorOf(clazz: Class<T>): EnumCreator<T, R> where T : EnumValue<R>, R : Serializable =
            super.creatorOf(clazz)
    }
}

/**
 * 字符串枚举 构建器.
 * @author tangli
 * @date 2023/09/13 19:15
 */
public abstract class StringEnumCreator(
    clazz: Class<out StringEnumValue>,
) : EnumCreator<StringEnumValue, String>(clazz) {
    /**
     * jackson 解析枚举时需要一个静态方法产生对应枚举. 相关对象继承这个类并将方法标记为 [JvmStatic] 即可.
     */
    public companion object : EnumCreatorFactory {
        @JvmStatic
        override fun getCreator(clazz: Class<*>): StringEnumCreator =
            super.getCreator(clazz).asToNotNull()
    }
}

/**
 * int枚举 构建器.
 * @author tangli
 * @date 2023/09/13 19:16
 */
public abstract class IntEnumCreator(
    clazz: Class<out IntEnumValue>,
) : EnumCreator<IntEnumValue, Int>(clazz) {
    /**
     * jackson 解析枚举时需要一个静态方法产生对应枚举. 相关对象继承这个类并将方法标记为 [JvmStatic] 即可.
     */
    public companion object : EnumCreatorFactory {
        @JvmStatic
        override fun getCreator(clazz: Class<*>): IntEnumCreator =
            super.getCreator(clazz).asToNotNull()
    }
}
