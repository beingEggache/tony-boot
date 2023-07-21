@file:Suppress("unused")

package com.tony.backup.web.support

import com.tony.backup.annotation.web.support.InjectEmptyIfNull.Companion.DEFAULT_EMPTY
import com.tony.utils.isCollectionLike
import com.tony.utils.isNumberTypes
import com.tony.utils.isStringLikeType
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.jsonToObj
import org.slf4j.LoggerFactory
import java.time.temporal.Temporal
import java.util.Date

/**
 * RequestBodyFieldInjectors is
 * @author tangli
 * @since 2023/07/06 14:59
 */
internal class IfNullRequestBodyFieldInjector : RequestBodyFieldInjector(DEFAULT_EMPTY) {

    private val logger = LoggerFactory.getLogger(IfNullRequestBodyFieldInjector::class.java)
    override fun value(fieldType: Class<*>): Any? = when {
        fieldType.isStringLikeType() -> ""
        fieldType.isCollectionLike() -> "[]".jsonToObj(fieldType)
        !fieldType.isTypesOrSubTypesOf(
            Enum::class.java,
            Date::class.java,
            Temporal::class.java,
            Boolean::class.java,
            Boolean::class.javaPrimitiveType
        ) && !fieldType.isNumberTypes() &&
            fieldType != Any::class.java -> "{}".jsonToObj(fieldType)

        else -> {
            logger.warn("Do not support ${fieldType.typeName}")
            null
        }
    }
}
