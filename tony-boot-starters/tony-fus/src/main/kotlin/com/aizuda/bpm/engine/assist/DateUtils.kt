/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.assist

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

/**
 * 日期帮助类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public object DateUtils {
    @JvmStatic
    public val currentDate: Date
        /**
         * 当前时间 Date 类型
         *
         * @return [Date]
         */
        get() = Date()

    /**
     * 当前时间 LocalDateTime 类型
     *
     * @return [LocalDateTime]
     */
    @JvmStatic
    public fun now(): LocalDateTime =
        LocalDateTime.now()

    /**
     * 日期判断
     *
     * @param arg0 开始时间
     * @param arg1 结束时间
     * @return true 开始时间大于结束时间 false 开始时间小于结束时间
     */
    @JvmStatic
    public fun after(
        arg0: Date?,
        arg1: Date?,
    ): Boolean =
        null != arg0 && null != arg1 && arg0.after(arg1)

    /**
     * 日期 LocalDateTime 转为 Date
     *
     * @param localDateTime [LocalDateTime]
     * @return [Date]
     */
    @JvmStatic
    public fun toDate(localDateTime: LocalDateTime?): Date? {
        if (null == localDateTime) {
            return null
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    /**
     * 计算时间差
     *
     * @param startDate 开始时间
     * @param endDate   接受时间
     * @return 时间差
     */
    @JvmStatic
    public fun calculateDateDifference(
        startDate: Date?,
        endDate: Date?,
    ): Long? {
        if (null == startDate || null == endDate) {
            return null
        }
        return endDate.time - startDate.time
    }

    /**
     * 解析定时器任务时间
     *
     * @param time 自定义触发时间
     * @return [Date]
     */
    @JvmStatic
    public fun parseTimerTaskTime(time: String): Date? {
        var expireTime: LocalDateTime? = null
        val timeArr = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val l = timeArr.size
        if (l == 2) {
            val vary = timeArr[0].toLong()
            val unit = timeArr[1]
            if ("d" == unit) {
                expireTime = now().plusDays(vary)
            } else if ("h" == unit) {
                expireTime = now().plusHours(vary)
            } else if ("m" == unit) {
                expireTime = now().plusMinutes(vary)
            }
        } else if (l == 3) {
            val hours = timeArr[0].toLong()
            val minutes = timeArr[1].toLong()
            val seconds = timeArr[2].toLong()
            expireTime = now().plusHours(hours).plusMinutes(minutes).plusSeconds(seconds)
        }
        return toDate(expireTime)
    }
}
