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
 * 任务状态: 1.活动, 2.跳转, 3.完成, 4.拒绝, 5.超时, 6.终止
 * @author tangli
 * @date 2023/09/29 19:00
 * @since 1.0.0
 */
public enum class TaskState(
    override val value: Int,
) : IntEnumValue {
    /**
     * 活动
     */
    ACTIVE(1),

    /**
     * 跳转
     */
    JUMP(2),

    /**
     * 完成
     */
    COMPLETED(3),

    /**
     * 拒绝
     */
    REJECTED(4),

    /**
     * 撤销
     */
    REVOKE(5),

    /**
     * 超时
     */
    EXPIRED(6),

    /**
     * 终止
     */
    TERMINATED(7),
    ;

    internal companion object : EnumCreator<TaskState, Int>(TaskState::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)

        fun of(instanceState: InstanceState): TaskState {
            if (instanceState == InstanceState.REJECTED) {
                return REJECTED
            }
            if (instanceState == InstanceState.EXPIRED) {
                return EXPIRED
            }
            return COMPLETED
        }
    }
}
