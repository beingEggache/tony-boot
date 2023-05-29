package com.tony.core.test

import com.tony.crypto.symmetric.enums.CryptoDigestMode
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.crypto.symmetric.decryptToString
import com.tony.crypto.symmetric.encryptToString


fun main() {
    testAes(CryptoDigestMode.BASE64)
    testAes(CryptoDigestMode.HEX)
    testDes(CryptoDigestMode.BASE64)
    testDes(CryptoDigestMode.HEX)
}

private fun testAes(mode: CryptoDigestMode) {
    val aesKey = "1234567890abcdefFEDCBA0987654321"
    val oriStr = "hello aloha"
    println("aes $mode oriStr: $oriStr")
    val encryptedStr = oriStr.encryptToString(SymmetricCryptoAlgorithm.AES, aesKey, mode)
    println("aes $mode encryptedStr: $encryptedStr")
    val decryptedStr = encryptedStr.decryptToString(SymmetricCryptoAlgorithm.AES, aesKey, mode)
    println("aes $mode decryptedStr: $decryptedStr")
}

private fun testDes(mode: CryptoDigestMode) {
    val aesKey = "xvwe23dvxs"
    val oriStr = "hello aloha"
    println("des $mode oriStr: $oriStr")
    val encryptedStr = oriStr.encryptToString(SymmetricCryptoAlgorithm.DES, aesKey, mode)
    println("des $mode encryptedStr: $encryptedStr")
    val decryptedStr = encryptedStr.decryptToString(SymmetricCryptoAlgorithm.DES, aesKey, mode)
    println("des $mode decryptedStr: $decryptedStr")
}

