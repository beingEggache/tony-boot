@file:JvmName("-SwaggerUtils")

package tony.knife4j.utils

import java.io.InputStream
import java.lang.reflect.Type
import org.springframework.core.ResolvableType
import org.springframework.core.io.InputStreamSource
import tony.core.utils.isTypesOrSubTypesOf

/**
 * Utils is
 * @author tangli
 * @date 2025/07/24 09:33
 */
internal fun isDownloadType(type: Type): Boolean =
    type == ByteArray::class.java ||
        type.isTypesOrSubTypesOf(InputStream::class.java) ||
        type.isTypesOrSubTypesOf(InputStreamSource::class.java)

internal fun Type.typeParam(index: Int = 0): Type =
    ResolvableType.forType(this).getGeneric(index).type
