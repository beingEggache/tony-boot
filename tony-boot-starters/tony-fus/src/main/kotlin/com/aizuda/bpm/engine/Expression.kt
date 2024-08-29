/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.assist.ObjectUtils
import com.aizuda.bpm.engine.model.NodeExpression
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * 条件表达式
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface Expression {
    /**
     * 根据表达式串、参数解析表达式并返回指定类型
     *
     * @param conditionList 条件组列表
     * @param args          参数列表
     * @return T 返回对象
     */
    public fun eval(
        conditionList: List<List<NodeExpression>>,
        args: Map<String?, Any?>,
    ): Boolean

    /**
     * 根据表达式串、参数解析表达式并返回指定类型
     *
     * @param conditionList 条件组列表
     * @param argsSupplier  参数列表提供者
     * @param evalFunc      执行表单式函数
     * @return true 成功 false 失败
     */
    public fun eval(
        conditionList: List<List<NodeExpression>>,
        argsSupplier: Supplier<Map<String?, Any?>>,
        evalFunc: Function<String?, Boolean>,
    ): Boolean {
        if (ObjectUtils.isEmpty(conditionList)) {
            return false
        }
        val args = argsSupplier.get()
        val expr =
            conditionList
                .stream()
                .map { cl: List<NodeExpression> ->
                    cl
                        .stream()
                        .map { t: NodeExpression -> exprOfArgs(t, args) }
                        .collect(Collectors.joining(" && "))
                }.collect(Collectors.joining(" || "))
        return evalFunc.apply(expr)
    }

    public fun exprOfArgs(
        nodeExpression: NodeExpression,
        args: Map<String?, Any?>,
    ): String {
        var value = nodeExpression.value
        val fieldValue = args[nodeExpression.field]
        if (fieldValue is String) {
            value = "'" + nodeExpression.value + "'"
        }
        return "#" + nodeExpression.field + nodeExpression.operator + value
    }
}
