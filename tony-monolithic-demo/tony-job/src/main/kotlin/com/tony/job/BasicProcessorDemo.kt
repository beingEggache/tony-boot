package com.tony.job

import org.springframework.stereotype.Component
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.sdk.BasicProcessor

/**
 * BasicProcessorDemo is
 * @author tangli
 * @date 2024/01/23 10:37
 * @since 1.0.0
 */
@Component
class BasicProcessorDemo : BasicProcessor {
    override fun process(context: TaskContext): ProcessResult {
        context.omsLogger.info("power job start")
        context.omsLogger.info(context.jobParams)
        context.omsLogger.info(context.instanceParams)
        context.omsLogger.info("power job end")
        return ProcessResult(true, "power job result")
    }
}
