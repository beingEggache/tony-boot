package com.tony.cache.aspect

import com.tony.PROJECT_GROUP
import com.tony.exception.ApiException
import org.slf4j.LoggerFactory
import org.springframework.expression.EvaluationException
import org.springframework.expression.common.TemplateParserContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

private val logger = LoggerFactory.getLogger("$PROJECT_GROUP.cache.aspect.ExpressionSupport")

private const val EXPRESSION_PREFIX = "#{"
private const val EXPRESSION_SUFFIX = "}"

/**
 * 表达式解析器
 */
private val expressionParser = SpelExpressionParser()

/**
 * 获取spel表达式后的结果
 *
 * @param expression  表达式
 * @param paramMap 参数map
 * @return 执行spel表达式后的结果
 */
internal fun getValueByExpressionInParamMap(expression: String, paramMap: Map<String, Any?>): Any? =
    // 获取表达式的值
    if (expression.isSpel()) {
        getExpressionValue(expression, paramMap)
    } else {
        paramMap[expression]
    }

internal fun String.isSpel() = startsWith(EXPRESSION_PREFIX) && endsWith(EXPRESSION_SUFFIX)

internal fun getExpressionValue(
    expression: String,
    paramMap: Map<String, Any?>,
): Any? {
    val deSpelExpression = expression
        .replace(EXPRESSION_PREFIX, "")
        .replace(EXPRESSION_SUFFIX, "")

    val stringList = deSpelExpression.split(".")
    if (stringList.size < 2) {
        return paramMap[deSpelExpression]
    }
    val paramName = stringList.first()
    val realExpression = "$EXPRESSION_PREFIX${stringList.drop(1).joinToString(".")}$EXPRESSION_SUFFIX"
    return try {
        expressionParser
            .parseExpression(realExpression, TemplateParserContext())
            .getValue(StandardEvaluationContext(paramMap[paramName]))
    } catch (e: EvaluationException) {
        logger.error(e.message, e)
        throw ApiException(e.message, throwable = e)
    }
}
