package tony.core.model

import java.time.temporal.Temporal

/**
 * [T]响应包装类
 * @author tangli
 * @date 2025/07/17 10:15
 */
public data class MonoResult<T> private constructor(
    private val result: T?,
) : MonoResultLike<T> {
    override fun getResult(): T? =
        result

    public companion object {
        /**
         * 包装成简单值响应统一结构.
         * @return [MonoResult]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun Boolean.ofMonoResult(): MonoResultLike<Boolean> =
            MonoResult(this)

        /**
         * 包装成简单值响应统一结构.
         * @return [MonoResult]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun String.ofMonoResult(): MonoResultLike<String> =
            MonoResult(this)

        /**
         * 包装成简单值响应统一结构.
         * @return [MonoResult]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun <E : Number> E.ofMonoResult(): MonoResultLike<E> =
            MonoResult(this)

        /**
         * 包装成简单值响应统一结构.
         * @return [MonoResult]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun <E : Enum<*>> E.ofMonoResult(): MonoResultLike<E> =
            MonoResult(this)

        /**
         * 包装成简单值响应统一结构.
         * @return [Temporal]
         * @author tangli
         * @date 2025/07/17 10:21
         */
        @JvmStatic
        public fun <E : Temporal> E.ofMonoResult(): MonoResultLike<E> =
            MonoResult(this)
    }
}
