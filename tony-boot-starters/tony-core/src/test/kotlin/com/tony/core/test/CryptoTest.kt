package com.tony.core.test

import com.tony.crypto.symmetric.Des
import com.tony.crypto.symmetric.decryptToByte
import com.tony.crypto.symmetric.enums.CryptoDigestMode
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.crypto.symmetric.decryptToString
import com.tony.crypto.symmetric.encryptToString
import com.tony.utils.decodeBase64


fun main() {
//    testAes(CryptoDigestMode.BASE64)
//    testAes(CryptoDigestMode.HEX)
//    testDes(CryptoDigestMode.BASE64)
//    testDes(CryptoDigestMode.HEX)

    val decodeBase64 =
        "C+thLOj3BU6siFnThLpRfpErVslSBt0AMKrXpeP76uOvgNFY/9V8kKyIWdOEulF+72TExsRH4b35rH+iz21FyKyIWdOEulF+G5OQKLgk9EI="
            .toByteArray()
            .decodeBase64()

    val toString = Des.decrypt(decodeBase64, "xvwe23dvxs".toByteArray()).toString(Charsets.UTF_8)
    println(toString)

}

private fun testAes(mode: CryptoDigestMode) {
    val aesKey = "1234567890abcdefFEDCBA0987654321"
    val oriStr = """{
                  "name": "123",
                  "age": 0
                }""".trimIndent()
    println("aes $mode oriStr: $oriStr")
    val encryptedStr = oriStr.encryptToString(SymmetricCryptoAlgorithm.AES, aesKey, mode)
    println("aes $mode encryptedStr: $encryptedStr")
    val decryptedStr = encryptedStr.decryptToString(SymmetricCryptoAlgorithm.AES, aesKey, mode)
    println("aes $mode decryptedStr: $decryptedStr")
}

private fun testDes(mode: CryptoDigestMode) {
    val desKey = "xvwe23dvxs"
    val oriStr = """{
                  "name": "123",
                  "age": 0
                }""".trimIndent()
    println("des $mode oriStr: $oriStr")
    val encryptedStr = oriStr.encryptToString(SymmetricCryptoAlgorithm.DES, desKey, mode)
    println("des $mode encryptedStr: $encryptedStr")
    val decryptedStr = encryptedStr.decryptToString(SymmetricCryptoAlgorithm.DES, desKey, mode)
    println("des $mode decryptedStr: $decryptedStr")
}

