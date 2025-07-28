package tony.knife4j.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import tony.core.utils.rawClass

/**
 * schema 参数化类型
 * @author tangli
 * @date 2025/07/23 11:35
 */
internal class SchemaParameterizedType(
    private val rawType: Type,
    private vararg val actualTypeArguments: Type,
) : ParameterizedType {
    override fun getActualTypeArguments(): Array<out Type>? =
        actualTypeArguments

    override fun getRawType(): Type =
        rawType.rawClass()

    override fun getOwnerType(): Type? =
        null

    override fun toString(): String =
        "${rawType}${actualTypeArguments.takeIf { it.isNotEmpty() }?.let { "<${it.joinToString(",")}>" }}"
}
