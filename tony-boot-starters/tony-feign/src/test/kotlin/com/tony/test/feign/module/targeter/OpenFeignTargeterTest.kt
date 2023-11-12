package com.tony.test.feign.module.targeter

import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.targeter.client.OpenFeignTestWithTargeterClient
import com.tony.test.feign.module.targeter.client.OpenFeignTestWithoutTargeterClient
import com.tony.utils.getLogger
import com.tony.utils.println
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [OpenFeignTestTargeterApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class OpenFeignTargeterTest {

    @Resource
    lateinit var openFeignTestWithTargeterClient: OpenFeignTestWithTargeterClient

    @Resource
    lateinit var openFeignTestWithoutTargeterClient: OpenFeignTestWithoutTargeterClient

    private val logger = getLogger()

    @Test
    fun testWithGlobalInterceptor() {
        val personReq = Person(listOf(1, 2, 3).toIntArray(), 123, "http:", mapOf("qwe" to 123))
        val client = openFeignTestWithTargeterClient

        client.boolean().toJsonString().println()
        client.booleanArray().toJsonString().println()
        client.byte().toJsonString().println()
        client.short().toJsonString().println()
        client.shortArray().toJsonString().println()
        client.int().toJsonString().println()
        client.intArray().toJsonString().println()
        client.long().toJsonString().println()
        client.longArray().toJsonString().println()
        client.float().toJsonString().println()
        client.floatArray().toJsonString().println()
        client.double().toJsonString().println()
        client.doubleArray().toJsonString().println()
        client.char().toJsonString().println()
        client.charArray().toJsonString().println()
        client.string().toJsonString().println()
        client.array().toJsonString().println()
        client.list().toJsonString().println()

        val map = client.map(personReq)
        logger.info("map ------- ${map.toJsonString()}")

        val personResp = client.person(personReq)
        logger.info("personResp ------- ${personResp.toJsonString()}")

        try {
            val validate = client.validate(Person(listOf(1, 2, 3).toIntArray(), null, "http:", mapOf("qwe" to 123)))
            logger.info("validate ------- ${validate.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("validate exception ------- ${e.message}")
        }

        try {
            val exception = client.exception(personReq)
            logger.info("exception -------${exception.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("exception exception ------- ${e.message}")
        }

        try {
            val bizException = client.bizException(personReq)
            logger.info("bizException -------${bizException.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("bizException exception ------- ${e.message}")
        }

        try {
            val apiException = client.apiException(personReq)
            logger.info("apiException -------${apiException.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("apiException exception ------- ${e.message}")
        }
    }

    @Test
    fun testWithoutGlobalInterceptor() {
        val personReq = Person(listOf(1, 2, 3).toIntArray(), 123, "http:", mapOf("qwe" to 123))
        val client = openFeignTestWithoutTargeterClient

        client.boolean()
        client.booleanArray()
        client.byte()
        client.short()
        client.shortArray()
        client.int()
        client.intArray()
        client.long()
        client.longArray()
        client.float()
        client.floatArray()
        client.double()
        client.doubleArray()
        client.char()
        client.charArray()
        client.string()
        client.array()
        client.list()

        val map = client.map(personReq)
        logger.info("map ------- ${map.toJsonString()}")

        val personResp = client.person(personReq)
        logger.info("personResp ------- ${personResp.toJsonString()}")

        try {
            val validate = client.validate(Person(listOf(1, 2, 3).toIntArray(), null, "http:", mapOf("qwe" to 123)))
            logger.info("validate ------- ${validate.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("validate exception ------- ${e.message}")
        }

        try {
            val exception = client.exception(personReq)
            logger.info("exception -------${exception.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("exception exception ------- ${e.message}")
        }

        try {
            val bizException = client.bizException(personReq)
            logger.info("bizException -------${bizException.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("bizException exception ------- ${e.message}")
        }

        try {
            val apiException = client.apiException(personReq)
            logger.info("apiException -------${apiException.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("apiException exception ------- ${e.message}")
        }
    }
}
