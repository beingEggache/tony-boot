/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.assist.DateUtils.calculateDateDifference
import com.aizuda.bpm.engine.assist.DateUtils.currentDate
import com.aizuda.bpm.engine.core.enums.InstanceState
import java.util.Date

/**
 * 历史流程实例实体类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlwHisInstance : FlwInstance() {
    /**
     * 状态 0，活动 1，结束
     */
    public var instanceState: Int? = null
        protected set

    /**
     * 结束时间
     */
    public var endTime: Date? = null

    /**
     * 处理耗时
     */
    public var duration: Long? = null

    public fun setInstanceState(instanceState: InstanceState) {
        this.instanceState = instanceState.value
    }

    public fun setInstanceState(instanceState: Int) {
        isNull(InstanceState.get(instanceState), "插入的实例状态异常 [instanceState=$instanceState]")
        this.instanceState = instanceState
    }

    /**
     * 计算流程实例处理耗时
     */
    public fun calculateDuration() {
        this.endTime = currentDate
        this.duration = calculateDateDifference(this.createTime, this.endTime)
    }

    public override fun toString(): String =
        "FlwHisInstance(instanceState=" + this.instanceState + ", endTime=" + this.endTime + ", duration=" +
            this.duration +
            ")"

    public companion object {
        public fun of(
            flwInstance: FlwInstance,
            instanceState: InstanceState,
        ): FlwHisInstance {
            val his = FlwHisInstance()
            his.id = flwInstance.id
            his.tenantId = flwInstance.tenantId
            his.createId = flwInstance.createId
            his.createBy = flwInstance.createBy
            his.createTime = flwInstance.createTime
            his.processId = flwInstance.processId
            his.parentInstanceId = flwInstance.parentInstanceId
            his.priority = flwInstance.priority
            his.instanceNo = flwInstance.instanceNo
            his.businessKey = flwInstance.businessKey
            his.variable = flwInstance.variable
            his.currentNodeName = flwInstance.currentNodeName
            his.currentNodeKey = flwInstance.currentNodeKey
            his.expireTime = flwInstance.expireTime
            his.lastUpdateBy = flwInstance.lastUpdateBy
            his.lastUpdateTime = flwInstance.lastUpdateTime
            his.instanceState = instanceState.value
            if (InstanceState.ACTIVE != instanceState) {
                his.calculateDuration()
            }
            return his
        }
    }
}
