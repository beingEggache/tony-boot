package com.tony.web.crypto.test.req

import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * TestReqs is
 * @author tangli
 * @since 2023/05/26 17:14
 */
data class TestReq(
    @get:NotBlank(message = "请输入姓名")
    val name: String? = null,
    @get:NotNull(message = "请输入年龄")
    val age: Int? = null,
    val mode: SymmetricCryptoAlgorithm? = null,
)
