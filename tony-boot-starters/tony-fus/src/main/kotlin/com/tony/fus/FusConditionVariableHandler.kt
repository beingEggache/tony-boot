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

import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode

/**
 * 流程执行条件参数处理
 * @author tangli
 * @date 2024/04/03 19:16
 * @since 1.0.0
 */
public fun interface FusConditionVariableHandler {
    /**
     * 获取流程变量, 可以加一些自定义的处理.
     * @param [node] 流程节点
     * @param [execution] 执行对象
     * @author tangli
     * @date 2023/10/24 19:48
     * @since 1.0.0
     */
    public fun handle(
        node: FusNode,
        execution: FusExecution,
    ): Map<String, Any?>
}

internal class DefaultConditionVariableHandler : FusConditionVariableHandler {
    override fun handle(
        node: FusNode,
        execution: FusExecution,
    ): Map<String, Any?> =
        execution.variable
}
