package com.tony.flow.model

import com.tony.flow.FlowContext

/**
 * 模型实例接口.
 * @author tangli
 * @date 2023/10/19 10:11
 * @since 1.0.0
 */
public fun interface FlowModel {
    /**
     * 执行流程元素.
     * @param [flowContext] 流上下文
     * @param [flowExecution] 流程执行
     * @author Tang Li
     * @date 2023/10/24 16:48
     * @since 1.0.0
     */
    public fun execute(
        flowContext: FlowContext,
        flowExecution: FlowExecution,
    )
}
