package com.tony.test.fus

import com.tony.fus.db.mapper.FusHistoryInstanceMapper
import com.tony.fus.db.po.FusHistoryInstance
import com.tony.utils.getLogger
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 15:17
 * @since 1.0.0
 */
@SpringBootTest(classes = [TestFusApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FusAnyTests {

    private val logger = getLogger()

    @Resource
    private lateinit var historyInstanceMapper: FusHistoryInstanceMapper

    @Test
    fun test() {
        historyInstanceMapper
            .ktQuery()
            .select(FusHistoryInstance::createTime)
            .listObj<Any>()
            .forEach {
                logger.info(it.toString())
            }
    }
}
