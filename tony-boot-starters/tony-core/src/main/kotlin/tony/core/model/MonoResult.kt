package tony.core.model

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 简单值响应统一结构。
 *
 * 避免重复定义简单值响应包装类.
 * @author tangli
 * @date 2025/07/17 09:32
 */
@Schema(name = "简单值响应统一结构")
public sealed interface MonoResultLike<out T> {
    @get:Schema(description = "结果")
    public val result: T?

    public companion object {
        /**
         * 包装成简单值响应统一结构.
         * @return [BooleanMonoResult]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun Boolean.ofMonoResult(): BooleanMonoResult =
            BooleanMonoResult(this)

        /**
         * 包装成简单值响应统一结构.
         * @return [StringMonoResult]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun String.ofMonoResult(): StringMonoResult =
            StringMonoResult(this)

        /**
         * 包装成简单值响应统一结构.
         * @return [NumberMonoResult]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun <E : Number> E.ofMonoResult(): NumberMonoResult =
            NumberMonoResult(this)

        /**
         * 包装成简单值响应统一结构.
         * @return [EnumMonoResult]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun <E : Enum<*>> E.ofMonoResult(): EnumMonoResult =
            EnumMonoResult(this)
    }
}

/**
 * [Boolean]响应包装类
 * @author tangli
 * @date 2025/07/17 10:15
 */
public data class BooleanMonoResult(
    override val result: Boolean?,
) : MonoResultLike<Boolean>

/**
 * [String]响应包装类
 * @author tangli
 * @date 2025/07/17 10:15
 */
public data class StringMonoResult(
    override val result: String?,
) : MonoResultLike<String>

/**
 * [Number]响应包装类
 * @author tangli
 * @date 2025/07/17 10:15
 */
public data class NumberMonoResult(
    override val result: Number?,
) : MonoResultLike<Number>

/**
 * [Enum]响应包装类
 * @author tangli
 * @date 2025/07/17 10:15
 */
public data class EnumMonoResult(
    override val result: Enum<*>?,
) : MonoResultLike<Enum<*>>
