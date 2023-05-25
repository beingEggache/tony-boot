package com.tony.web.listeners

import com.tony.utils.getLogger
import org.slf4j.Logger
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent

/**
 * 系统关闭监听器
 *
 * @author tangli
 * @since 2023/5/25 11:00
 */
public interface ContextClosedListener : ApplicationListener<ContextClosedEvent>

/**
 * 系统关闭监听器 默认实现
 *
 * @author tangli
 * @since 2023/5/25 11:00
 */
internal class DefaultContextClosedListener : ContextClosedListener {

    private val logger: Logger = getLogger("on-context-closing")
    override fun onApplicationEvent(event: ContextClosedEvent) {
        val applicationName = event.applicationContext.environment.getProperty("spring.application.name")
        logger.info("------ $applicationName close gracefully ------")
    }
}
