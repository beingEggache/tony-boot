package com.tony.core.test

import com.tony.crypto.symmetric.decryptToBytes
import com.tony.crypto.symmetric.decryptToString
import com.tony.crypto.symmetric.encryptToBytes
import com.tony.crypto.symmetric.encryptToString
import com.tony.crypto.symmetric.enums.CryptoEncoding
import com.tony.crypto.symmetric.enums.SymmetricCryptoAlgorithm
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
        encoding: CryptoEncoding,
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
            Arguments.of(SymmetricCryptoAlgorithm.AES, CryptoEncoding.BASE64, originString, aesSecret),
            Arguments.of(SymmetricCryptoAlgorithm.AES, CryptoEncoding.HEX, originString, aesSecret),
            Arguments.of(SymmetricCryptoAlgorithm.DES, CryptoEncoding.BASE64, originString, desSecret),
            Arguments.of(SymmetricCryptoAlgorithm.DES, CryptoEncoding.HEX, originString, desSecret),
        )
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource(value = ["encryptBytesTestArgumentSource"])
    fun testBytesSymmetricCryptoEncrypt(
        algorithm: SymmetricCryptoAlgorithm,
        encoding: CryptoEncoding,
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
            Arguments.of(SymmetricCryptoAlgorithm.AES, CryptoEncoding.BASE64, originBytes, aesSecret),
            Arguments.of(SymmetricCryptoAlgorithm.AES, CryptoEncoding.HEX, originBytes, aesSecret),
            Arguments.of(SymmetricCryptoAlgorithm.DES, CryptoEncoding.BASE64, originBytes, desSecret),
            Arguments.of(SymmetricCryptoAlgorithm.DES, CryptoEncoding.HEX, originBytes, desSecret),
        )
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource(value = ["decryptStringTestArgumentSource"])
    fun testStringSymmetricCryptoDecrypt(
        algorithm: SymmetricCryptoAlgorithm,
        encoding: CryptoEncoding,
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
            encoding: CryptoEncoding,
        ): Arguments {
            return Arguments.of(
                algorithm,
                encoding,
                originString.encryptToString(algorithm, secret, encoding),
                secret,
            )
        }

        return Stream.of(
            argument(SymmetricCryptoAlgorithm.AES, aesSecret, CryptoEncoding.BASE64),
            argument(SymmetricCryptoAlgorithm.AES, aesSecret, CryptoEncoding.HEX),
            argument(SymmetricCryptoAlgorithm.DES, desSecret, CryptoEncoding.BASE64),
            argument(SymmetricCryptoAlgorithm.DES, desSecret, CryptoEncoding.HEX),
        )
    }

    @Order(4)
    @ParameterizedTest
    @MethodSource(value = ["decryptBytesTestArgumentSource"])
    fun testBytesSymmetricCryptoDecrypt(
        algorithm: SymmetricCryptoAlgorithm,
        encoding: CryptoEncoding,
        encryptedBytes: ByteArray,
        secret: String,
    ) {
        val originBytes = encryptedBytes.decryptToBytes(algorithm, secret, encoding)
        logger.info(
            "origin string: ${originBytes.toString(Charsets.UTF_8)}, " +
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
            encoding: CryptoEncoding,
        ): Arguments {
            return Arguments.of(
                algorithm,
                encoding,
                originBytes.encryptToBytes(algorithm, secret, encoding),
                secret
            )
        }

        return Stream.of(
            argument(SymmetricCryptoAlgorithm.AES, aesSecret, CryptoEncoding.BASE64),
            argument(SymmetricCryptoAlgorithm.AES, aesSecret, CryptoEncoding.HEX),
            argument(SymmetricCryptoAlgorithm.DES, desSecret, CryptoEncoding.BASE64),
            argument(SymmetricCryptoAlgorithm.DES, desSecret, CryptoEncoding.HEX),
        )
    }
}
