package com.tony.fus

import com.tony.fus.model.FusExecution

/**
 * 流程引擎拦截器.
 *
 * @author tangli
 * @date 2023/10/19 10:15
 * @since 1.0.0
 */
public fun interface FusInterceptor {
    /**
     * 拦截处理方法.
     *
     * @param [context] 上下文
     * @param [execution] 执行对象
     * @author Tang Li
     * @date 2023/10/19 13:58
     * @since 1.0.0
     */
    public fun handle(
        context: FusContext,
        execution: FusExecution,
    )
}
