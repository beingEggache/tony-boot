/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.spring.event

import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.enums.EventType
import com.aizuda.bpm.engine.entity.FlwHisInstance
import com.aizuda.bpm.engine.listener.InstanceListener
import com.aizuda.bpm.engine.model.NodeModel
import java.util.function.Supplier
import org.springframework.context.ApplicationEventPublisher

/**
 * Spring boot Event 异步实例监听处理器
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class EventInstanceListener public constructor(
    private val eventPublisher: ApplicationEventPublisher,
) : InstanceListener {
    override fun notify(
        eventType: EventType?,
        supplier: Supplier<FlwHisInstance?>,
        nodeModel: NodeModel?,
        flowCreator: FlowCreator?,
    ): Boolean {
        val instanceEvent = InstanceEvent()
        instanceEvent.eventType = eventType
        instanceEvent.flwInstance = supplier.get()
        instanceEvent.nodeModel = nodeModel
        instanceEvent.flowCreator = flowCreator
        eventPublisher.publishEvent(instanceEvent)
        return true
    }
}
