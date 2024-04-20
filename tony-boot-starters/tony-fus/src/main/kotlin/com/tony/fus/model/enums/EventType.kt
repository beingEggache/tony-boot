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
 * 流程引擎监听类型.
 * @author tangli
 * @date 2023/10/25 19:59
 * @since 1.0.0
 */
public enum class EventType(
    override val value: Int,
) : IntEnumValue {

    /**
     * 发起
     */
    LAUNCH(1),
    /**
     * 创建.
     */
    CREATE(2),

    /**
     * 抄送
     */
    CC(3),

    /**
     * 分配
     */
    ASSIGNMENT(4),

    /**
     * 任务加签
     */
    ADD_TASK_ACTOR(5),

    /**
     * 任务减签
     */
    REMOVE_TASK_ACTOR(6),

    /**
     * 驳回
     */
    REJECTED(7),

    /**
     * 认领
     */
    CLAIM(8),

    /**
     * 拿回
     */
    RECLAIM(9),

    /**
     * 撤回
     */
    WITHDRAW(10),

    /**
     * 唤醒
     */
    RESUME(11),

    /**
     * 完成.
     */
    COMPLETED(12),

    /**
     * 撤销.
     */
    REVOKED(13),

    /**
     * 终止.
     */
    TERMINATED(14),

    /**
     * 更新.
     */
    UPDATE(15),

    /**
     * 删除.
     */
    DELETE(16),

    /**
     * 超时.
     */
    EXPIRED(17),

    /**
     * 跳转.
     */
    JUMP(18),
    ;

    internal companion object : EnumCreator<EventType, Int>(EventType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
