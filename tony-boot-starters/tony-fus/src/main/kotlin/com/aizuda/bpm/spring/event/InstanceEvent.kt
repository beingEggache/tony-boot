/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.spring.event

import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.enums.EventType
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.model.NodeModel
import java.io.Serializable

/**
 * 流程实例事件对象
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class InstanceEvent public constructor() : Serializable {
    public var eventType: EventType? = null

    /**
     * EventType.complete 完成时，实例对象实际为子类 FlwHisInstance 对象
     *
     *
     * FlwHisInstance hisInstance = (FlwHisInstance) flwInstance;
     *
     */
    public var flwInstance: FlwInstance? = null
    public var nodeModel: NodeModel? = null
    public var flowCreator: FlowCreator? = null
}
