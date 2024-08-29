/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.fus

import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.model.FusNodeExpression
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * 表达式计算器
 * @author tangli
 * @date 2023/11/09 19:39
 * @since 1.0.0
 */
internal data object FusExpressionEvaluator {
    @JvmStatic
    private val expressionParser: ExpressionParser = SpelExpressionParser()

    /**
     * 根据表达式串、参数解析表达式并返回指定类型
     * @param [conditionList] 条件组列表
     * @param [args] 参数列表
     * @author tangli
     * @date 2023/11/09 19:39
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmStatic
    internal fun eval(
        conditionList: List<List<FusNodeExpression>>,
        args: Map<String, Any?>,
    ): Boolean =
        eval(conditionList, args) { expr ->
            StandardEvaluationContext().run {
                setVariables(args)
                expressionParser
                    .parseExpression(expr)
                    .getValue(this, Boolean::class.java)
                    .fusThrowIfNull()
            }
        }

    @JvmStatic
    private fun eval(
        conditionList: List<List<FusNodeExpression>>,
        args: Map<String, Any?>,
        func: java.util.function.Function<String, Boolean>,
    ): Boolean {
        if (conditionList.isEmpty()) {
            return false
        }

        val expr =
            conditionList
                .joinToString(" || ") { expressionList ->
                    expressionList.joinToString(" && ") { expression ->
                        exprOfArgs(expression, args)
                    }
                }

        return func.apply(expr)
    }

    private fun exprOfArgs(
        expression: FusNodeExpression,
        args: Map<String, Any?>,
    ): String {
        val expressionField = expression.field
        val value =
            if (args[expressionField] is String) {
                """'${expression.value}'"""
            } else {
                expression.value
            }
        return kotlin.run {
            """#$expressionField${expression.operator}$value"""
        }
    }
}
