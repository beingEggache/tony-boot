package com.tony.feign.test.unwrap

import com.tony.feign.test.unwrap.client.OpenFeignTestUnwrapClient
import com.tony.feign.test.unwrap.dto.Person
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

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
        val personReq = Person(listOf(1, 2, 3).toIntArray(), 123, "432", mapOf("qwe" to 123))
        val map = openFeignTestUnwrapClient.map(personReq)
        logger.info(map.toJsonString())
        val personResp = openFeignTestUnwrapClient.person(personReq)
        logger.info(personResp.toJsonString())
        try {
            val exception = openFeignTestUnwrapClient.exception(personReq)
            logger.info(exception.toJsonString())
        }catch (e:Exception){
        }
        try {
            val exception = openFeignTestUnwrapClient.bizException(personReq)
            logger.info(exception.toJsonString())
        }catch (e:Exception){
        }
        try {
            val exception = openFeignTestUnwrapClient.apiException(personReq)
            logger.info(exception.toJsonString())
        }catch (e:Exception){
        }
    }
}
