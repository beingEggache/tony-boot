package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.PerformType
import com.tony.fus.db.enums.TaskType
import java.time.LocalDateTime

/**
 * 任务表
 * @TableName fus_task
 */
@TableName
public open class FusTask {
    /**
     * 主键ID
     */
    @TableId
    public var taskId: String? = null

    /**
     * 租户ID
     */
    public var tenantId: String? = null

    /**
     * 创建人ID
     */
    public var creatorId: String? = null

    /**
     * 创建人
     */
    public var creatorName: String? = null

    /**
     * 创建时间
     */
    @OrderBy
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    public var createTime: LocalDateTime? = null

    /**
     * 流程实例ID
     */
    public var instanceId: String? = null

    /**
     * 父任务ID
     */
    public var parentTaskId: String? = null

    /**
     * 任务名称
     */
    public var taskName: String? = null

    /**
     * 任务显示名称
     */
    public var displayName: String? = null

    /**
     * 任务类型: 1-主办, 2-转办, 3-委派, 4-会签
     */
    public var taskType: TaskType? = null

    /**
     * 参与类型: 1-发起、其它, 2-按顺序依次审批, 3-会签, 4-或签, 5-票签, 10-抄送
     */
    public var performType: PerformType? = null

    /**
     * 任务处理的url
     */
    public var actionUrl: String? = null

    /**
     * 变量json
     */
    public var variable: String? = null

    /**
     * 委托人ID
     */
    public var assignorId: String? = null

    /**
     * 委托人
     */
    public var assignorName: String? = null

    /**
     * 任务期望完成时间
     */
    public var expireTime: LocalDateTime? = null

    /**
     * 提醒时间
     */
    public var remindTime: LocalDateTime? = null

    /**
     * 提醒次数
     */
    public var remindRepeat: Int? = null

    /**
     * 已阅 0，否 1，是
     */
    public var viewed: Boolean? = null

    /**
     * 完成时间
     */
    public var finishTime: LocalDateTime? = null
}
