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
 * 参与类型
 * @author Tang Li
 * @date 2023/09/29 19:19
 * @since 1.0.0
 */
public enum class PerformType(
    override val value: Int,
) : IntEnumValue {
    /**
     * 发起、其它
     */
    UNKNOWN(1),

    /**
     * 按顺序依次审批
     */
    SORT(2),

    /**
     * 会签 (可同时审批，每个人必须审批通过)
     */
    COUNTERSIGN(3),

    /**
     * 或签 (有一人审批通过即可)
     */
    OR_SIGN(4),

    /**
     * 票签
     */
    VOTE_SIGN(5),

    /**
     * 抄送
     */
    COPY(10),
    ;

    internal companion object : EnumCreator<PerformType, Int>(PerformType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
