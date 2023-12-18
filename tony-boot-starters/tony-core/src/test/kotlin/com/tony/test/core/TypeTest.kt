package com.tony.test.core

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.ApiResultLike
import com.tony.MonoResult
import com.tony.utils.toJavaType
import org.junit.jupiter.api.Test

/**
 * TypeTest is
 * @author tangli
 * @date 2023/12/18 16:47
 * @since 1.0.0
 */
object TypeTest {

    @Test
    fun test(){
        val type = object : TypeReference<Map<String, List<Map<String, Set<List<ApiResultLike<MonoResult<String>>>>>>>>(){}
        val javaType = type.type.toJavaType()
        println(javaType)
    }
}
