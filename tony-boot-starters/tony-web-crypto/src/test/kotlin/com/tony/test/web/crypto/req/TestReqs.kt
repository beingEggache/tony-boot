package com.tony.test.web.crypto.req

import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * TestReqs is
 * @author Tang Li
 * @date 2023/05/26 17:14
 */
data class TestReq(
    @get:NotBlank(message = "请输入姓名")
    val name: String? = null,
    @get:NotNull(message = "请输入年龄")
    val age: Int? = null,
    val mode: SymmetricCryptoAlgorithm? = null,
)
