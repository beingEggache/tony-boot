package com.tony.test.feign.module.unwrap

import com.tony.test.feign.dto.Person
import com.tony.test.feign.module.unwrap.client.FeignWithUnwrapTestClient
import com.tony.test.feign.module.unwrap.client.FeignWithoutUnwrapTestClient
import com.tony.utils.getLogger
import com.tony.utils.println
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.boot.test.context.SpringBootTest

@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(
    properties = [
        "server.port=10005"
    ],
    classes = [FeignUnwrapTestApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class FeignUnwrapTest {

    @Resource
    lateinit var feignWithUnwrapTestClient: FeignWithUnwrapTestClient

    @Resource
    lateinit var feignWithoutUnwrapTestClient: FeignWithoutUnwrapTestClient

    private val logger = getLogger()

    @Test
    fun testWithUnwrap() {
        val personReq = Person(listOf(1, 2, 3).toIntArray(), 123, "http:", mapOf("qwe" to 123))
        val client = feignWithUnwrapTestClient

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
    fun testWithoutUnwrap() {
        val personReq = Person(listOf(1, 2, 3).toIntArray(), 123, "http:", mapOf("qwe" to 123))
        val client = feignWithoutUnwrapTestClient

        client.boolean().println()
        try {
            client.booleanArray().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }
        client.byte().println()
        client.short().println()
        try {
            client.shortArray().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }
        client.int().println()
        try {
            client.intArray().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }
        client.long().println()
        try {
            client.longArray().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }
        client.float().println()
        try {
            client.floatArray().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }
        client.double().println()
        try {
            client.doubleArray().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }
        client.char().println()
        try {
            client.charArray().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }
        client.string().println()
        try {
            client.array().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }
        try {
            client.list().println()
        } catch (e: Exception) {
            logger.error(e.message)
        }

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
