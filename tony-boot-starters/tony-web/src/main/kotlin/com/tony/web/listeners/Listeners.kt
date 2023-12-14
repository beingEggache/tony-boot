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

package com.tony.web.listeners

import com.tony.utils.getLogger
import org.slf4j.Logger
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent

/**
 * 系统关闭监听器
 *
 * @author Tang Li
 * @date 2023/05/25 19:00
 */
public interface ContextClosedListener : ApplicationListener<ContextClosedEvent>

/**
 * 系统关闭监听器 默认实现
 *
 * @author Tang Li
 * @date 2023/05/25 19:00
 */
internal class DefaultContextClosedListener : ContextClosedListener {
    private val logger: Logger = getLogger("on-context-closing")

    override fun onApplicationEvent(event: ContextClosedEvent) {
        val applicationName =
            event
                .applicationContext
                .environment
                .getProperty("spring.application.name")
        logger.info("------ $applicationName close gracefully ------")
    }
}
