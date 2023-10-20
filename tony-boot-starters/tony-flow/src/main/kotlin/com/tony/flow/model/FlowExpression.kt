package com.tony.flow.model

/**
 * EL 表达式
 * @author tangli
 * @date 2023/10/19 10:12
 * @since 1.0.0
 */
public interface FlowExpression {

    /**
     * eval
     * @param [type] 类型
     * @param [expr] expr 表达式
     * @param [args] args 参数
     * @return [T]?
     * @author Tang Li
     * @date 2023/10/19 10:13
     * @since 1.0.0
     */
    public fun <T : Any> eval(type: Class<T>, expr: String, args: Any): T?
}
