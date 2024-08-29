/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.scheduling

/**
 * 提醒参数
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class RemindParam public constructor() {
    /**
     * 提醒时间 cron 表达式
     */
    public var cron: String? = null

    /**
     * 工作日设置，格式为 1,2,3...7，表示周一至周日
     */
    public var weeks: String? = null

    /**
     * 工作时间设置，格式为 8:00-18:00
     */
    public var workTime: String? = null
}
