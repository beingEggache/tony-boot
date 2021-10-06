package com.tony.core.test

import com.tony.core.ApiProperty
import com.tony.core.ApiResult
import com.tony.core.utils.formatToPercent
import com.tony.core.utils.println
import org.junit.jupiter.api.Test
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

/**
 *
 * @author tangli
 * @since 2021-05-21 12:48
 */
class Test {

    @ExperimentalStdlibApi
    @Test
    fun testIfNullOrEmpty() {
        val kType = typeOf<ApiResult<Map<String, List<String>>>>()

        kType.arguments.forEach {
            it.type?.javaType.println()
        }
    }
}
