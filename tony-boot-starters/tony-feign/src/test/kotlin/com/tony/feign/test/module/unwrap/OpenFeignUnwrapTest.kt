package com.tony.feign.test.module.unwrap

import com.tony.feign.test.unwrap.client.OpenFeignTestUnwrapClient
import com.tony.feign.test.dto.Person
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [OpenFeignTestUnwrapApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class OpenFeignUnwrapTest {

    @Resource
    lateinit var openFeignTestUnwrapClient: OpenFeignTestUnwrapClient

    private val logger = getLogger()

    @Test
    fun testUnwrap() {
        val personReq = Person(listOf(1, 2, 3).toIntArray(), 123, "http:", mapOf("qwe" to 123))

        val map = openFeignTestUnwrapClient.map(personReq)
        logger.info("map ------- ${map.toJsonString()}")

        val personResp = openFeignTestUnwrapClient.person(personReq)
        logger.info("personResp ------- ${personResp.toJsonString()}")

        try {
            val validate = openFeignTestUnwrapClient.validate(Person(listOf(1, 2, 3).toIntArray(), null, "http:", mapOf("qwe" to 123)))
            logger.info("validate ------- ${validate.toJsonString()}")
        }catch (e: Exception){
            logger.warn("validate exception ------- ${ e.message }")
        }

        try {
            val exception = openFeignTestUnwrapClient.exception(personReq)
            logger.info("exception -------${exception.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("exception exception ------- ${ e.message }")
        }

        try {
            val unauthorizedException = openFeignTestUnwrapClient.unauthorizedException(personReq)
            logger.info("unauthorizedException -------${unauthorizedException.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("unauthorizedException exception ------- ${ e.message }")
        }

        try {
            val bizException = openFeignTestUnwrapClient.bizException(personReq)
            logger.info("bizException -------${bizException.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("bizException exception ------- ${ e.message }")
        }

        try {
            val apiException = openFeignTestUnwrapClient.apiException(personReq)
            logger.info("apiException -------${apiException.toJsonString()}")
        } catch (e: Exception) {
            logger.warn("apiException exception ------- ${ e.message }")
        }
    }
}
