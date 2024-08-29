/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.spring.adaptive

import com.aizuda.bpm.engine.Expression
import com.aizuda.bpm.engine.model.NodeExpression
import org.springframework.expression.EvaluationContext
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * Spring el表达式解析器
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author ximu
 * @since 1.0
 */
public class SpelExpression public constructor() : Expression {
    private val parser: ExpressionParser = SpelExpressionParser()

    override fun eval(
        conditionList: List<List<NodeExpression>>,
        args: Map<String?, Any?>,
    ): Boolean =
        this.eval(conditionList, { args }, { expr: String? ->
            val context: EvaluationContext = StandardEvaluationContext()
            for ((key, value) in args) {
                context.setVariable(key!!, value)
            }
            parser.parseExpression(expr!!).getValue(context, Boolean::class.java)!!
        })
}
