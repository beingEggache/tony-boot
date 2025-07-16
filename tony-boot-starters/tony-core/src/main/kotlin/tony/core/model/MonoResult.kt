package tony.core.model

/**
 * 包装 [Boolean] ,[CharSequence], [Number], [Enum]
 *
 * @author tangli
 * @date 2021/12/6 10:51
 */
public data class MonoResult<T>(
    val value: T? = null,
) {
    public companion object {
        @JvmStatic
        public fun Boolean.ofMonoResult(): MonoResult<Boolean> =
            MonoResult(this)

        @JvmStatic
        public fun <E : CharSequence> E.ofMonoResult(): MonoResult<E> =
            MonoResult(this)

        @JvmStatic
        public fun <E : Number> E.ofMonoResult(): MonoResult<E> =
            MonoResult(this)

        @JvmStatic
        public fun <E : Enum<*>> E.ofMonoResult(): MonoResult<E> =
            MonoResult(this)
    }
}
