/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.spring.event

import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.enums.EventType
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.listener.TaskListener
import com.aizuda.bpm.engine.model.NodeModel
import java.util.function.Supplier
import org.springframework.context.ApplicationEventPublisher

/**
 * Spring boot Event 异步任务监听处理器
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class EventTaskListener public constructor(
    private val eventPublisher: ApplicationEventPublisher,
) : TaskListener {
    override fun notify(
        eventType: EventType?,
        supplier: Supplier<FlwTask?>,
        nodeModel: NodeModel?,
        flowCreator: FlowCreator?,
    ): Boolean {
        val taskEvent = TaskEvent()
        taskEvent.eventType = eventType
        taskEvent.flwTask = supplier.get()
        taskEvent.nodeModel = nodeModel
        taskEvent.flowCreator = flowCreator
        eventPublisher.publishEvent(taskEvent)
        return true
    }
}
