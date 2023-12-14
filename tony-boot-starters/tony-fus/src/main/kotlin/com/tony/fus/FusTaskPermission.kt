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

import com.tony.fus.db.po.FusTaskActor

/**
 * 任务访问策略类.
 *
 * 用于判断给定的创建人员是否允许执行某个任务
 * @author tangli
 * @date 2023/10/19 19:18
 * @since 1.0.0
 */
public fun interface FusTaskPermission {
    /**
     * 是否拥有权限.
     *
     * 根据创建人ID、参与者集合判断是否允许访问所属任务
     * @param [userId] 操作人id
     * @param [taskActorList] 任务参与者列表
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/19 19:21
     * @since 1.0.0
     */
    public fun hasPermission(
        userId: String,
        taskActorList: List<FusTaskActor>,
    ): Boolean
}

internal class DefaultTaskPermission : FusTaskPermission {
    override fun hasPermission(
        userId: String,
        taskActorList: List<FusTaskActor>,
    ): Boolean {
        if (userId.isEmpty() || taskActorList.isEmpty()) {
            return false
        }
        return taskActorList.any { it.actorId == userId }
    }
}
