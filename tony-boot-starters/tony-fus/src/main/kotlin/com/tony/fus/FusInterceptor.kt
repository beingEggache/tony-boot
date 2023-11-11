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
     * @param [fusContext] 上下文
     * @param [fusExecution] 执行对象
     * @author Tang Li
     * @date 2023/10/19 13:58
     * @since 1.0.0
     */
    public fun handle(
        fusContext: FusContext,
        fusExecution: FusExecution?,
    )
}
