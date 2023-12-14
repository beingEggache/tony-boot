/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.test.core

import com.tony.codec.enums.Encoding
import com.tony.crypto.symmetric.decryptToBytes
import com.tony.crypto.symmetric.decryptToString
import com.tony.crypto.symmetric.encryptToBytes
import com.tony.crypto.symmetric.encryptToString
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
import com.tony.utils.string
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.slf4j.LoggerFactory
import java.util.stream.Stream

@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
object CryptoTest {

    private val logger = LoggerFactory.getLogger(CryptoTest::class.java)

    @Order(1)
    @ParameterizedTest
    @MethodSource(value = ["encryptStringTestArgumentSource"])
    fun testStringSymmetricCryptoEncrypt(
        algorithm: SymmetricCryptoAlgorithm,
        encoding: Encoding,
        originString: String,
        secret: String,
    ) {
        logger.info("encrypted: ${originString.encryptToString(algorithm, secret, encoding)}")
    }

    @JvmStatic
    fun encryptStringTestArgumentSource(): Stream<Arguments> {
        val originString = """{"name": "Tom","age": 18}"""
        val aesSecret = "1234567890abcdefFEDCBA0987654321"
        val desSecret = "xvwe23dvxs"
        return Stream.of(
            Arguments.of(SymmetricCryptoAlgorithm.AES, Encoding.BASE64, originString, aesSecret),
            Arguments.of(SymmetricCryptoAlgorithm.AES, Encoding.HEX, originString, aesSecret),
            Arguments.of(SymmetricCryptoAlgorithm.DES, Encoding.BASE64, originString, desSecret),
            Arguments.of(SymmetricCryptoAlgorithm.DES, Encoding.HEX, originString, desSecret),
        )
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource(value = ["encryptBytesTestArgumentSource"])
    fun testBytesSymmetricCryptoEncrypt(
        algorithm: SymmetricCryptoAlgorithm,
        encoding: Encoding,
        originBytes: ByteArray,
        secret: String,
    ) {
        logger.info("encrypted: ${originBytes.encryptToBytes(algorithm, secret, encoding).toList()}")
    }

    @JvmStatic
    fun encryptBytesTestArgumentSource(): Stream<Arguments> {
        val originBytes = """{"name": "Tom","age": 18}""".toByteArray()
        val aesSecret = "1234567890abcdefFEDCBA0987654321"
        val desSecret = "xvwe23dvxs"
        return Stream.of(
            Arguments.of(SymmetricCryptoAlgorithm.AES, Encoding.BASE64, originBytes, aesSecret),
            Arguments.of(SymmetricCryptoAlgorithm.AES, Encoding.HEX, originBytes, aesSecret),
            Arguments.of(SymmetricCryptoAlgorithm.DES, Encoding.BASE64, originBytes, desSecret),
            Arguments.of(SymmetricCryptoAlgorithm.DES, Encoding.HEX, originBytes, desSecret),
        )
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource(value = ["decryptStringTestArgumentSource"])
    fun testStringSymmetricCryptoDecrypt(
        algorithm: SymmetricCryptoAlgorithm,
        encoding: Encoding,
        encryptedString: String,
        secret: String,
    ) {
        logger.info("origin string: ${encryptedString.decryptToString(algorithm, secret, encoding)}")
    }

    @JvmStatic
    fun decryptStringTestArgumentSource(): Stream<Arguments> {
        val originString = """{"name": "Tom","age": 18}"""
        val aesSecret = "1234567890abcdefFEDCBA0987654321"
        val desSecret = "xvwe23dvxs"

        fun argument(
            algorithm: SymmetricCryptoAlgorithm,
            secret: String,
            encoding: Encoding,
        ): Arguments {
            return Arguments.of(
                algorithm,
                encoding,
                originString.encryptToString(algorithm, secret, encoding),
                secret,
            )
        }

        return Stream.of(
            argument(SymmetricCryptoAlgorithm.AES, aesSecret, Encoding.BASE64),
            argument(SymmetricCryptoAlgorithm.AES, aesSecret, Encoding.HEX),
            argument(SymmetricCryptoAlgorithm.DES, desSecret, Encoding.BASE64),
            argument(SymmetricCryptoAlgorithm.DES, desSecret, Encoding.HEX),
        )
    }

    @Order(4)
    @ParameterizedTest
    @MethodSource(value = ["decryptBytesTestArgumentSource"])
    fun testBytesSymmetricCryptoDecrypt(
        algorithm: SymmetricCryptoAlgorithm,
        encoding: Encoding,
        encryptedBytes: ByteArray,
        secret: String,
    ) {
        val originBytes = encryptedBytes.decryptToBytes(algorithm, secret, encoding)
        logger.info(
            "origin string: ${originBytes.string()}, " +
                "origin bytes: ${originBytes.toList()}"
        )
    }

    @JvmStatic
    fun decryptBytesTestArgumentSource(): Stream<Arguments> {
        val originBytes = """{"name": "Tom","age": 18}""".toByteArray()
        val aesSecret = "1234567890abcdefFEDCBA0987654321"
        val desSecret = "xvwe23dvxs"

        fun argument(
            algorithm: SymmetricCryptoAlgorithm,
            secret: String,
            encoding: Encoding,
        ): Arguments {
            return Arguments.of(
                algorithm,
                encoding,
                originBytes.encryptToBytes(algorithm, secret, encoding),
                secret
            )
        }

        return Stream.of(
            argument(SymmetricCryptoAlgorithm.AES, aesSecret, Encoding.BASE64),
            argument(SymmetricCryptoAlgorithm.AES, aesSecret, Encoding.HEX),
            argument(SymmetricCryptoAlgorithm.DES, desSecret, Encoding.BASE64),
            argument(SymmetricCryptoAlgorithm.DES, desSecret, Encoding.HEX),
        )
    }
}
