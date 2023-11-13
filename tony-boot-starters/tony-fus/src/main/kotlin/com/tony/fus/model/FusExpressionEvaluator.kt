package com.tony.fus.model

import com.tony.fus.extension.fusThrowIfNull
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

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

internal class SpelExpressionEvaluator(
    private val expressionParser: ExpressionParser = SpelExpressionParser(),
) : FusExpressionEvaluator {
    override fun eval(
        conditionList: List<List<FusNodeExpression>>,
        args: Map<String, Any?>,
    ): Boolean =
        eval(conditionList) { expr ->
            StandardEvaluationContext().run {
                setVariables(args)
                expressionParser
                    .parseExpression(expr)
                    .getValue(this, Boolean::class.java)
                    .fusThrowIfNull()
            }
        }
}
