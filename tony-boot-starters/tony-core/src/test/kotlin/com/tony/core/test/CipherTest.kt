package com.tony.core.test

import com.tony.cipher.CipherDigestMode
import com.tony.cipher.aesDecrypt
import com.tony.cipher.aesEncrypt
import com.tony.cipher.desDecrypt
import com.tony.cipher.desEncrypt


fun main() {
    testAes(CipherDigestMode.BASE64)
    testAes(CipherDigestMode.HEX)
    testDes(CipherDigestMode.BASE64)
    testDes(CipherDigestMode.HEX)
}

private fun testAes(mode: CipherDigestMode) {
    val aesKey = "1234567890abcdefFEDCBA0987654321"
    val oriStr = "hello aloha"
    println("oriStr: $oriStr")
    val encryptedStr = oriStr.aesEncrypt(aesKey, mode)
    println("encryptedStr: $encryptedStr")
    val decryptedStr = encryptedStr.aesDecrypt(aesKey, mode)
    println("decryptedStr: $decryptedStr")
}

private fun testDes(mode: CipherDigestMode) {
    val aesKey = "xvwe23dvxs"
    val oriStr = "hello aloha"
    println("oriStr: $oriStr")
    val encryptedStr = oriStr.desEncrypt(aesKey, mode)
    println("encryptedStr: $encryptedStr")
    val decryptedStr = encryptedStr.desDecrypt(aesKey, mode)
    println("decryptedStr: $decryptedStr")
}

