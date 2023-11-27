package com.tony.fus.model

import com.tony.fus.FusContext

/**
 * 模型实例接口.
 * @author tangli
 * @date 2023/10/19 10:11
 * @since 1.0.0
 */
public fun interface FusModel {
    /**
     * 执行流程元素.
     * @param [context] 流上下文
     * @param [execution] 流程执行
     * @author Tang Li
     * @date 2023/10/24 16:48
     * @since 1.0.0
     */
    public fun execute(
        context: FusContext,
        execution: FusExecution,
    )
}
