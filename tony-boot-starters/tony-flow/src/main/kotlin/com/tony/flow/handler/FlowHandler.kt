package com.tony.flow.handler

import com.tony.flow.FlowContext
import com.tony.flow.model.FlowExecution

/**
 * 流程各模型操作处理
 * @author tangli
 * @date 2023/10/25 09:23
 * @since 1.0.0
 */
public fun interface FlowHandler {

    public fun handle(flowContext: FlowContext,flowExecution: FlowExecution)
}
