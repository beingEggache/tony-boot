package com.tony.web.listeners

import com.tony.utils.getLogger
import org.slf4j.Logger
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent

public interface ContextClosedListener : ApplicationListener<ContextClosedEvent>

internal class DefaultContextClosedListener : ContextClosedListener {

    private val logger: Logger = getLogger("on-context-closing")
    override fun onApplicationEvent(event: ContextClosedEvent) {
        val applicationName = event.applicationContext.environment.getProperty("spring.application.name")
        logger.info("------ $applicationName close gracefully ------")
    }
}
