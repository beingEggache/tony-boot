package com.tony.flow.model

/**
 * EL 表达式
 * @author tangli
 * @date 2023/10/19 10:12
 * @since 1.0.0
 */
public interface FlowExpression {

    /**
     * 根据表达式串、参数解析表达式并返回指定类型
     * @param [conditionList] 条件组列表
     * @param [args] 参数列表
     * @author Tang Li
     * @date 2023/11/09 11:39
     * @since 1.0.0
     */
    public fun eval(
        conditionList: List<List<FlowNodeExpression>>,
        args: Map<String, Any?>,
    ): Boolean

    public fun eval(
        conditionList: List<List<FlowNodeExpression>>,
        func: java.util.function.Function<String, Boolean>,
    ): Boolean {
        if (conditionList.isEmpty()) {
            return false
        }

        val expr =
            conditionList
                .joinToString(" || ") { clist ->
                    clist
                        .joinToString(" && ") {
                            "#${it.field}${it.operator}${it.value}"
                        }
                }

        return func.apply(expr)
    }
}
