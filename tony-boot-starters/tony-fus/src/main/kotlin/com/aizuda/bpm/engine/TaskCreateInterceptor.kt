/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowLongContext

/**
 * 流程任务创建拦截器
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface TaskCreateInterceptor {
    /**
     * 拦截前置处理方法
     *
     * @param flowLongContext 流程引擎上下文
     * @param execution       执行对象
     */
    public fun before(
        flowLongContext: FlowLongContext?,
        execution: Execution?,
    ) {
        // 默认不处理
    }

    /**
     * 拦截后置处理方法
     *
     * @param flowLongContext 流程引擎上下文
     * @param execution       执行对象
     */
    public fun after(
        flowLongContext: FlowLongContext?,
        execution: Execution?,
    )
}
