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

package com.tony.fus.model

import com.tony.fus.extension.fusThrowIfNull
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * 表达式计算器
 * @author Tang Li
 * @date 2023/11/09 19:39
 * @since 1.0.0
 */
public interface FusExpressionEvaluator {
    /**
     * 根据表达式串、参数解析表达式并返回指定类型
     * @param [conditionList] 条件组列表
     * @param [args] 参数列表
     * @author Tang Li
     * @date 2023/11/09 19:39
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
