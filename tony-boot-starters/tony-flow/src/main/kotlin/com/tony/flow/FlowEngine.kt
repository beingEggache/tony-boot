package com.tony.flow

import com.tony.flow.service.ProcessService
import com.tony.flow.service.QueryService
import com.tony.flow.service.RuntimeService

/**
 * FlowEngine is
 * @author tangli
 * @date 2023/10/19 14:33
 * @since 1.0.0
 */
public interface FlowEngine {

    public fun configure(config: FlowContext)

    public val context: FlowContext

    public val processService: ProcessService
        get() = context.processService
    public val queryService: QueryService
        get() = context.queryService
    public val runtimeService: RuntimeService
        get() = context.runtimeService
}
