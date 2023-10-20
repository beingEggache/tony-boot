package com.tony.flow

import com.tony.flow.cache.DefaultFlowCache
import com.tony.flow.cache.FlowCache
import com.tony.flow.model.FlowExpression
import com.tony.flow.service.ProcessService
import com.tony.flow.service.QueryService
import com.tony.flow.service.RuntimeService
import com.tony.flow.service.TaskService

/**
 * FlowContext is
 * @author tangli
 * @date 2023/10/19 10:35
 * @since 1.0.0
 */
public class FlowContext(
    public val processService: ProcessService,
    public val queryService: QueryService,
    public val runtimeService: RuntimeService,
    public val taskService: TaskService,
    public val expression: FlowExpression,
    public val taskPermission: TaskPermission,
    public val interceptors: List<FlowInterceptor>,
) {

    public companion object {
        public var flowCache: FlowCache = DefaultFlowCache()
    }
}
