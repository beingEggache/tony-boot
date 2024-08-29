/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.exception

/**
 * FlowLong流程引擎异常类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlowLongException : RuntimeException {
    public constructor() : super()

    public constructor(msg: String?, cause: Throwable?) : super(msg) {
        super.initCause(cause)
    }

    public constructor(msg: String?) : super(msg)

    public constructor(cause: Throwable?) : super() {
        super.initCause(cause)
    }
}
