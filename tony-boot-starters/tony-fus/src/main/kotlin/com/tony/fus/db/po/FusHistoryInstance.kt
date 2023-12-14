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

package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.InstanceState
import java.time.LocalDateTime

/**
 * 历史流程实例
 * @author Tang Li
 * @date 2023/09/29 19:13
 * @since 1.0.0
 */
@TableName
public class FusHistoryInstance : FusInstance() {
    /**
     * 流程实例状态: 1.审批中, 2.审批通过, 3.审批拒绝, 4.超时结束, 5.强制终止, 6.撤销审批
     */
    public var instanceState: InstanceState = InstanceState.ACTIVE

    /**
     * 结束时间
     */
    public var endTime: LocalDateTime? = null
}
