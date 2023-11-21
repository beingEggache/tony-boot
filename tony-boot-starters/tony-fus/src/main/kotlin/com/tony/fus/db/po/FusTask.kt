package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
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
    public var taskId: String = ""

    /**
     * 租户ID
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var tenantId: String = ""

    /**
     * 创建人ID
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var creatorId: String = ""

    /**
     * 创建人
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var creatorName: String = ""

    /**
     * 创建时间
     */
    @OrderBy
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    public val createTime: LocalDateTime = LocalDateTime.now()

    /**
     * 流程实例ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var instanceId: String = ""

    /**
     * 父任务ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var parentTaskId: String = ""

    /**
     * 任务名称
     */
    public var taskName: String = ""

    /**
     * 任务类型: 1.主办, 2.转办, 3.委派, 4.会签
     */
    public var taskType: TaskType = TaskType.MAJOR

    /**
     * 参与类型: 1.发起、其它, 2.按顺序依次审批, 3.会签, 4.或签, 5.票签, 10.抄送
     */
    public var performType: PerformType = PerformType.UNKNOWN

    /**
     * 变量json
     */
    public var variable: String = "{}"

    /**
     * 委托人ID
     */
    public var assignorId: String = ""

    /**
     * 委托人
     */
    public var assignorName: String = ""

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
     * 已阅: 0.否 1.是
     */
    public var viewed: Boolean = false

    /**
     * 完成时间
     */
    public var finishTime: LocalDateTime? = null
}
