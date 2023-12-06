package com.tony.test.web.crypto.client

import com.tony.annotation.feign.FeignUnwrapResponse
import com.tony.test.web.crypto.api.FeignCryptoTestApi
import org.springframework.cloud.openfeign.FeignClient

/**
 * MethodCryptoClient is
 * @author tangli
 * @date 2023/12/06 16:23
 * @since 1.0.0
 */
@FeignUnwrapResponse
@FeignClient(name = "methodCryptoClient", url = "http://localhost:8080")
interface FeignCryptoTestClient : FeignCryptoTestApi
