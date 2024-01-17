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
import com.tony.fus.db.enums.TaskState
import com.tony.utils.toDate
import java.time.LocalDateTime

/**
 * 历史任务
 * @author Tang Li
 * @date 2023/09/29 19:03
 * @since 1.0.0
 */

@TableName
public class FusHistoryTask : FusTask() {
    /**
     * 外部流程定义ID
     */
    public var outProcessId: String = ""

    /**
     * 外部流程实例ID
     */
    public var outInstanceId: String = ""

    /**
     * 结束时间
     */
    public var endTime: LocalDateTime? = null

    /**
     * 任务状态: 1.活动, 2.跳转, 3.完成, 4.拒绝, 5.超时, 6.终止
     */
    public var taskState: TaskState = TaskState.ACTIVE

    /**
     * 处理耗时
     */
    public var duration: Long? = null

    /**
     * 设置结束时间及处理耗时
     */
    public fun setEndTimeAndDuration() {
        val now = LocalDateTime.now()
        this.endTime = now
        this.duration = now.toDate().time - createTime.toDate().time
    }
}
