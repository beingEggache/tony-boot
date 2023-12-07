package com.tony.web.crpto

import com.tony.codec.enums.Encoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm

/**
 * 加/解密相关 provider
 * @author tangli
 * @date 2023/12/07 11:33
 * @since 1.0.0
 */
public interface CryptoProvider {
    /**
     * 加解密算法, 目前只支持 aes/des, 默认des
     */
    public val algorithm: SymmetricCryptoAlgorithm

    /**
     * 秘钥
     */
    public val secret: String

    /**
     * 二进制编码
     */
    public val encoding: Encoding
}
