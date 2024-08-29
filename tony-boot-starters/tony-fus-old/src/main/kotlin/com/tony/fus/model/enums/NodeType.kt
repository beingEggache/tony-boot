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

package com.tony.fus.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 节点类型
 * @author tangli
 * @date 2023/10/24 19:54
 * @since 1.0.0
 */
public enum class NodeType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 结束
     */
    END(-1),

    /**
     * 发起人
     */
    INITIATOR(1),

    /**
     * 审批人
     */
    APPROVER(2),

    /**
     * 抄送人
     */
    CC(3),

    /**
     * 条件审批
     */
    CONDITIONAL_APPROVE(4),

    /**
     * 条件分支
     */
    CONDITIONAL_BRANCH(5),

    /**
     * 子流程
     */
    SUB_PROCESS(6),

    /**
     * 定时器任务
     *
     */
    TIMER(7),

    /**
     * 触发器任务
     *
     */
    TRIGGER(8),
    ;

    internal companion object : EnumCreator<NodeType, Int>(NodeType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
