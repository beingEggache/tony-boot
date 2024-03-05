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

package com.tony.fus.listener

import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusTask
import com.tony.fus.model.enums.EventType
import java.util.function.Supplier

/**
 * FUS Listener
 * @author tangli
 * @date 2023/11/13 19:44
 * @since 1.0.0
 */
public fun interface FusListener<T> {
    /**
     * 通知
     * @param [eventType] 事件类型
     * @param [supplier] 数据
     * @author tangli
     * @date 2023/11/13 19:44
     * @since 1.0.0
     */
    public fun notify(
        eventType: EventType,
        supplier: Supplier<T>,
    )
}

/**
 * 任务监听器
 * @author tangli
 * @date 2023/11/13 19:46
 * @since 1.0.0
 */
public interface TaskListener : FusListener<FusTask>

/**
 * 实例监听器
 * @author tangli
 * @date 2023/11/13 19:46
 * @since 1.0.0
 */
public interface InstanceListener : FusListener<FusInstance>
