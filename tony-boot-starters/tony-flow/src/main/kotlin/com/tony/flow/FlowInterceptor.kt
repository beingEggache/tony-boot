package com.tony.flow

import com.tony.flow.model.FlowExecution

/**
 * 流程引擎拦截器.
 *
 * @author tangli
 * @date 2023/10/19 10:15
 * @since 1.0.0
 */
public fun interface FlowInterceptor {

    /**
     * 拦截处理方法.
     *
     * @param [flowContext] 上下文
     * @param [flowExecution] 执行对象
     * @author Tang Li
     * @date 2023/10/19 13:58
     * @since 1.0.0
     */
    public fun handle(flowContext: FlowContext, flowExecution: FlowExecution)
}
