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

package com.tony.fus.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue

/**
 * 任务类型: 1.主办, 2.审批, 3.抄送, 4.条件审批, 5.条件分支, 6.子流程, 7.定时器, 8.触发器, 11.转办, 12.委派, 13.委派归还
 * @author tangli
 * @date 2023/09/29 19:00
 * @since 1.0.0
 */
public enum class TaskType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 主办
     */
    MAJOR(1),

    /**
     * 审批
     *
     */
    APPROVAL(2),

    /**
     * 抄送
     *
     */
    CC(3),

    /**
     * 条件审批
     *
     */
    CONDITION_NODE(4),

    /**
     * 条件分支
     *
     */
    CONDITION_BRANCH(5),

    /**
     * 子流程
     *
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

    /**
     * 转办、代理人办理完任务直接进入下一个节点
     */
    TRANSFER(11),

    /**
     * 委派、代理人办理完任务该任务重新归还给原处理人
     */
    DELEGATE(12),

    /**
     * 委派归还任务
     */
    DELEGATE_RETURN(13),
    ;

    internal companion object : EnumCreator<TaskType, Int>(TaskType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
