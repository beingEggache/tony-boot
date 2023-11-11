package com.tony.fus.model

/**
 * 表达式计算器
 * @author Tang Li
 * @date 2023/11/09 11:39
 * @since 1.0.0
 */
public interface FusExpressionEvaluator {
    /**
     * 根据表达式串、参数解析表达式并返回指定类型
     * @param [conditionList] 条件组列表
     * @param [args] 参数列表
     * @author Tang Li
     * @date 2023/11/09 11:39
     * @since 1.0.0
     */
    public fun eval(
        conditionList: List<List<FusNodeExpression>>,
        args: Map<String, Any?>,
    ): Boolean

    public fun eval(
        conditionList: List<List<FusNodeExpression>>,
        func: java.util.function.Function<String, Boolean>,
    ): Boolean {
        if (conditionList.isEmpty()) {
            return false
        }

        val expr =
            conditionList
                .joinToString(" || ") { clist ->
                    clist.joinToString(" && ") { "#${it.field}${it.operator}${it.value}" }
                }

        return func.apply(expr)
    }
}
