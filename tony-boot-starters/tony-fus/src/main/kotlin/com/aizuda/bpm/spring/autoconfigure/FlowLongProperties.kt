/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.spring.autoconfigure

import com.aizuda.bpm.engine.scheduling.RemindParam
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * 配置属性
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
@ConfigurationProperties(prefix = "flowlong")
public class FlowLongProperties public constructor() {
    /**
     * 提醒时间
     */
    @NestedConfigurationProperty
    public var remind: RemindParam? = null

    /**
     * 事件监听配置
     */
    @NestedConfigurationProperty
    public var eventing: EventingParam? = null
}
