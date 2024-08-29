/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.spring.event

import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.enums.EventType
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.model.NodeModel
import java.io.Serializable

/**
 * 流程任务事件对象
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class TaskEvent public constructor() : Serializable {
    public var eventType: EventType? = null
    public var flwTask: FlwTask? = null
    public var nodeModel: NodeModel? = null
    public var flowCreator: FlowCreator? = null
}
