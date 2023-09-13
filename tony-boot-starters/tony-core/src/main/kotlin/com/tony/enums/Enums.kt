@file:JvmName("Enums")

package com.tony.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.tony.utils.asToNotNull
import com.tony.utils.isTypesOrSubTypesOf
import java.io.Serializable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 全局枚举接口.
 * 已和 jackson 和 mybatis-plus 结合.
 * @author Tang Li
 * @date 2023/09/13 10:16
 * @since 1.0.0
 */
public sealed interface EnumValue<T : Serializable> {
    @get:JsonValue
    public val value: T?
}

/**
 * 全局整形枚举接口.
 * @author Tang Li
 * @date 2023/09/13 10:16
 * @since 1.0.0
 */
public interface IntEnumValue : EnumValue<Int>

/**
 * 全局字符串枚举接口.
 * @author Tang Li
 * @date 2023/09/13 10:16
 * @since 1.0.0
 */
public interface StringEnumValue : EnumValue<String>

public const val DEFAULT_INT_VALUE: Int = -1

public const val DEFAULT_STRING_VALUE: String = ""

internal val creators = HashMap<Class<*>, EnumCreator<*, *>>()

private val logger: Logger = LoggerFactory.getLogger(EnumCreator::class.java)

internal sealed interface EnumCreatorFactory {

    @JvmSynthetic
    fun getCreator(clazz: Class<*>): EnumCreator<*, *> = creators.getOrPut(clazz) {
        logger.debug("${clazz.name} EnumCreator initialized.")
        clazz
            .classes
            .firstOrNull { it.isTypesOrSubTypesOf(EnumCreator::class.java) }
            ?.constructors
            ?.firstOrNull()
            ?.newInstance(null) as EnumCreator<*, *>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, R> creatorOf(clazz: Class<T>): EnumCreator<T, R>
        where T : EnumValue<R>,
              R : Serializable =
        creators.getOrPut(clazz) {
            logger.debug("${clazz.name} EnumCreator initialized.")
            clazz
                .classes
                .firstOrNull { it.isTypesOrSubTypesOf(EnumCreator::class.java) }
                ?.constructors
                ?.firstOrNull()
                ?.newInstance(null) as EnumCreator<T, R>
        }.asToNotNull()
}

/**
 * 枚举 构建器.
 * @author Tang Li
 * @date 2023/09/13 10:16
 * @since 1.0.0
 */
public abstract class EnumCreator<out E, KEY>(
    private val clazz: Class<out E>,
) where E : EnumValue<KEY>, KEY : Serializable {
    public open fun create(value: KEY): E? = enumValues.firstOrNull { value == it.value }

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
 * @author Tang Li
 * @date 2023/09/13 10:15
 * @since 1.0.0
 */
public abstract class StringEnumCreator(clazz: Class<out StringEnumValue>) :
    EnumCreator<StringEnumValue, String>(clazz) {
        /**
         * jackson 解析枚举时需要一个静态方法产生对应枚举. 相关对象继承这个类并将方法标记为 [JvmStatic] 即可.
         */
        public companion object : EnumCreatorFactory {

            @JvmStatic
            override fun getCreator(clazz: Class<*>): StringEnumCreator =
                super.getCreator(clazz) as StringEnumCreator
        }
    }

/**
 * int枚举 构建器.
 * @author Tang Li
 * @date 2023/09/13 10:16
 * @since 1.0.0
 */
public abstract class IntEnumCreator(clazz: Class<out IntEnumValue>) :
    EnumCreator<IntEnumValue, Int>(clazz) {
        /**
         * jackson 解析枚举时需要一个静态方法产生对应枚举. 相关对象继承这个类并将方法标记为 [JvmStatic] 即可.
         */
        public companion object : EnumCreatorFactory {

            @JvmStatic
            override fun getCreator(clazz: Class<*>): IntEnumCreator =
                super.getCreator(clazz) as IntEnumCreator
        }
    }
