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
        originString: String,
        algorithm: SymmetricCryptoAlgorithm,
        secret: String,
        encoding: CryptoEncoding,
    ) {
        val encryptedString = originString.encryptToString(algorithm, secret, encoding)
        logger.info(
            "origin:$originString, " +
                "algorithm: $algorithm, " +
                "encoding: ${encoding.toString().padEnd(10)}, " +
                "encrypted: $encryptedString"
        )
    }

    @JvmStatic
    fun encryptStringTestArgumentSource(): Stream<Arguments> {
        val originString = """{"name": "Tom","age": 18}"""
        val aesSecret = "1234567890abcdefFEDCBA0987654321"
        val desSecret = "xvwe23dvxs"
        return Stream.of(
            Arguments.of(originString, SymmetricCryptoAlgorithm.AES, aesSecret, CryptoEncoding.BASE64),
            Arguments.of(originString, SymmetricCryptoAlgorithm.AES, aesSecret, CryptoEncoding.HEX),
            Arguments.of(originString, SymmetricCryptoAlgorithm.DES, desSecret, CryptoEncoding.BASE64),
            Arguments.of(originString, SymmetricCryptoAlgorithm.DES, desSecret, CryptoEncoding.HEX),
        )
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource(value = ["encryptBytesTestArgumentSource"])
    fun testBytesSymmetricCryptoEncrypt(
        originBytes: ByteArray,
        algorithm: SymmetricCryptoAlgorithm,
        secret: String,
        encoding: CryptoEncoding,
    ) {
        val encryptedBytes = originBytes.encryptToBytes(algorithm, secret, encoding)
        logger.info(
            "origin:$originBytes, " +
                "algorithm: $algorithm, " +
                "encoding: ${encoding.toString().padEnd(10)}, " +
                "encrypted: $encryptedBytes"
        )
    }

    @JvmStatic
    fun encryptBytesTestArgumentSource(): Stream<Arguments> {
        val originBytes = """{"name": "Tom","age": 18}""".toByteArray()
        val aesSecret = "1234567890abcdefFEDCBA0987654321"
        val desSecret = "xvwe23dvxs"
        return Stream.of(
            Arguments.of(originBytes, SymmetricCryptoAlgorithm.AES, aesSecret, CryptoEncoding.BASE64),
            Arguments.of(originBytes, SymmetricCryptoAlgorithm.AES, aesSecret, CryptoEncoding.HEX),
            Arguments.of(originBytes, SymmetricCryptoAlgorithm.DES, desSecret, CryptoEncoding.BASE64),
            Arguments.of(originBytes, SymmetricCryptoAlgorithm.DES, desSecret, CryptoEncoding.HEX),
        )
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource(value = ["decryptStringTestArgumentSource"])
    fun testStringSymmetricCryptoDecrypt(
        encryptedString: String,
        algorithm: SymmetricCryptoAlgorithm,
        secret: String,
        encoding: CryptoEncoding,
    ) {
        val originString = encryptedString.decryptToString(algorithm, secret, encoding)
        logger.info(
            "algorithm: $algorithm, " +
                "encoding: ${encoding.toString().padEnd(10)}, " +
                "origin: $originString, " +
                "encrypted: $encryptedString"
        )
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
                originString.encryptToString(algorithm, secret, encoding),
                algorithm,
                secret,
                encoding
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
        encryptedBytes: ByteArray,
        algorithm: SymmetricCryptoAlgorithm,
        secret: String,
        encoding: CryptoEncoding,
    ) {
        val originBytes = encryptedBytes.decryptToBytes(algorithm, secret, encoding)
        logger.info(
            "algorithm: $algorithm, " +
                "encoding: ${encoding.toString().padEnd(10)}, " +
                "origin bytes: $originBytes, " +
                "origin string: ${originBytes.toString(Charsets.UTF_8)}, " +
                "encrypted: $encryptedBytes"
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
                originBytes.encryptToBytes(algorithm, secret, encoding),
                algorithm,
                secret,
                encoding
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
