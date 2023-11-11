package com.tony.fus.handler

import com.tony.fus.FusContext
import com.tony.fus.model.FusExecution

/**
 * 流程各模型操作处理
 * @author tangli
 * @date 2023/10/25 09:23
 * @since 1.0.0
 */
public fun interface FusHandler {
    public fun handle(
        context: FusContext,
        execution: FusExecution?,
    )
}
