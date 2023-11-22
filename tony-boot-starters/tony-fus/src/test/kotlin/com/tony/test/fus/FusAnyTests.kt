package com.tony.test.fus

import com.tony.PageQuery
import com.tony.PageResult
import com.tony.fus.db.mapper.FusProcessMapper
import com.tony.fus.db.po.FusProcess
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
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
    private lateinit var processMapper: FusProcessMapper

    @Test
    fun test() {
        val pageResult = processMapper
            .ktQuery()
            .orderByDesc(FusProcess::sort)
            .pageResult<PageResult<FusProcess>>(PageQuery<String>(1,3))

        logger.info(pageResult.toJsonString())
    }
}
