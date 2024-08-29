/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.assist

import com.aizuda.bpm.engine.exception.FlowLongException
import java.util.function.Supplier

/**
 * 断言帮助类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public object Assert {
    /**
     * 断言表达式为true
     *
     * @param expression 判断条件
     * @param message    异常打印信息
     */
    public fun isTrue(
        expression: Boolean,
        message: String?,
    ) {
        illegal(expression, message)
    }

    @JvmStatic
    public fun isFalse(
        expression: Boolean,
        supplier: Supplier<String?>,
    ) {
        illegal(!expression, supplier)
    }

    @JvmStatic
    public fun isFalse(
        expression: Boolean,
        message: String?,
    ) {
        illegal(!expression, message)
    }

    public fun isZero(
        result: Int,
        message: String?,
    ) {
        illegal(0 == result, message)
    }

    /**
     * 断言表达式为true
     *
     * @param expression 判断条件
     */
    public fun isTrue(expression: Boolean) {
        isTrue(expression, "[Assertion failed] - this expression must be false")
    }

    /**
     * 断言给定的object对象为空
     *
     * @param object  待检测对象
     * @param message 异常打印信息
     */
    public fun isNull(
        `object`: Any?,
        message: String?,
    ) {
        illegal(null == `object`, message)
    }

    /**
     * 断言给定的object对象为空
     *
     * @param object 待检测对象
     */
    public fun isNull(`object`: Any?) {
        isNull(`object`, "[Assertion failed] - the object argument must not be null")
    }

    /**
     * 断言给定的object对象为非空
     *
     * @param object  待检测对象
     * @param message 异常打印信息
     */

    /**
     * 断言给定的object对象为非空
     *
     * @param object 待检测对象
     */
    @JvmOverloads
    public fun notNull(
        `object`: Any?,
        message: String? = "[Assertion failed] - the object argument must be null",
    ) {
        illegal(null != `object`, message)
    }

    /**
     * 断言给定的object对象为空
     *
     * @param object 待检测对象
     */
    public fun isEmpty(`object`: Any?) {
        isEmpty(`object`, "[Assertion failed] - this argument must not be null or empty")
    }

    /**
     * 断言给定的object对象为空
     *
     * @param object  待检测对象
     * @param message 异常打印信息
     */
    public fun isEmpty(
        `object`: Any?,
        message: String?,
    ) {
        illegal(ObjectUtils.isEmpty(`object`), message)
    }

    /**
     * 断言给定的object对象非空
     *
     * @param object  待检测对象
     * @param message 异常打印信息
     */
    public fun isNotEmpty(
        `object`: Any?,
        message: String?,
    ) {
        illegal(ObjectUtils.isNotEmpty(`object`), message)
    }

    /**
     * 非法参数断言
     *
     * @param illegal 判断条件
     * @param message 提示内容
     */
    public fun illegal(
        illegal: Boolean,
        message: String?,
    ) {
        if (illegal) {
            throw throwable(message)
        }
    }

    /**
     * 非法参数断言
     *
     * @param illegal  判断条件
     * @param supplier 提示内容提供者
     */
    public fun illegal(
        illegal: Boolean,
        supplier: Supplier<String?>,
    ) {
        if (illegal) {
            throw throwable(supplier.get())
        }
    }

    /**
     * 非法参数断言
     *
     * @param message 提示内容
     */
    public fun illegal(message: String?): Unit =
        throw throwable(message)

    /**
     * 创建 FlowLongException 异常信息
     *
     * @param message 提示内容
     * @return [FlowLongException]
     */
    public fun throwable(message: String?): FlowLongException =
        FlowLongException(message)

    /**
     * 创建 FlowLongException 异常信息
     *
     * @param message 提示内容
     * @param cause   [Throwable]
     * @return [FlowLongException]
     */
    public fun throwable(
        message: String?,
        cause: Throwable?,
    ): FlowLongException =
        FlowLongException(message, cause)

    /**
     * 创建 FlowLongException 异常信息
     *
     * @param cause [Throwable]
     * @return [FlowLongException]
     */
    public fun throwable(cause: Throwable?): FlowLongException =
        FlowLongException(cause)
}
