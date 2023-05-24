package com.tony.enums

import com.fasterxml.jackson.annotation.JsonValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Serializable

/**
 * 全局枚举接口.
 *
 * 已和 jackson 和 mybatis-plus 结合.
 *
 * @param T 枚举值类型, 只有 [String] 和 [Int] 两种.
 */
public sealed interface EnumValue<T : Serializable> {
    @get:JsonValue
    public val value: T?
}

/**
 * 全局整形枚举接口.
 */
public interface EnumIntValue : EnumValue<Int>

/**
 * 全局字符串枚举接口.
 */
public interface EnumStringValue : EnumValue<String>

/**
 * jackson枚举构建器.
 */
public abstract class EnumCreator<out E, KEY>(
    private val clazz: Class<out E>,
) where E : EnumValue<KEY>, KEY : Serializable {
    public open fun create(value: KEY): E? = enumValues.firstOrNull { value == it.value }

    private val enumValues: Array<out E> by lazy {
        clazz.enumConstants
    }

    /**
     * jackson 解析枚举时需要一个静态方法产生对应枚举. 相关对象继承这个类并将方法标记为 [JvmStatic] 即可.
     */
    public companion object EnumCreatorFactory {

        public const val defaultIntValue: Int = 40404
        public const val defaultStringValue: String = ""
        private val creators = HashMap<Class<*>, EnumCreator<*, *>>()
        private val logger: Logger = LoggerFactory.getLogger(EnumCreatorFactory::class.java)

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <T, R> getCreator(clazz: Class<T>): EnumCreator<T, R> where T : EnumValue<R>, R : Serializable {
            return creators.getOrPut(clazz) {
                logger.debug("${clazz.name} EnumCreator initialized.")
                clazz
                    .classes
                    .firstOrNull {
                        EnumCreator::class.java.isAssignableFrom(it)
                    }
                    ?.constructors
                    ?.firstOrNull()
                    ?.newInstance(null) as EnumCreator<T, R>
            } as EnumCreator<T, R>
        }
    }
}
