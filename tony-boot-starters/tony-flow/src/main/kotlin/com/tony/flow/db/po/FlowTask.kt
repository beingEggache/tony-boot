package com.tony.flow.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.flow.enums.PerformType
import com.tony.flow.enums.TaskType
import java.time.LocalDateTime

/**
 * 任务表
 * @TableName flow_task
 */
@TableName(value = "flow_task")
public open class FlowTask {
    /**
     * 主键ID
     */
    @TableId(value = "task_id")
    public var taskId: Long? = null

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id")
    public var tenantId: Long? = null

    /**
     * 创建人ID
     */
    @TableField(value = "creator_id")
    public var creatorId: Long? = null

    /**
     * 创建人
     */
    @TableField(value = "creator_name")
    public var creatorName: String? = null

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    public var createTime: LocalDateTime? = null

    /**
     * 流程实例ID
     */
    @TableField(value = "instance_id")
    public var instanceId: Long? = null

    /**
     * 父任务ID
     */
    @TableField(value = "parent_task_id")
    public var parentTaskId: Long? = null

    /**
     * 任务名称
     */
    @TableField(value = "task_name")
    public var taskName: String? = null

    /**
     * 任务显示名称
     */
    @TableField(value = "display_name")
    public var displayName: String? = null

    /**
     * 任务类型: 0-主办, 1-转办, 2-委派, 3-会签
     */
    @TableField(value = "task_type")
    public var taskType: TaskType? = null

    /**
     * 参与类型: 0-发起、其它, 1-按顺序依次审批, 2-会签, 3-或签, 4-抄送
     */
    @TableField(value = "perform_type")
    public var performType: PerformType? = null

    /**
     * 任务处理的url
     */
    @TableField(value = "action_url")
    public var actionUrl: String? = null

    /**
     * 变量json
     */
    @TableField(value = "variable")
    public var variable: String? = null

    /**
     * 委托人ID
     */
    @TableField(value = "assignor_id")
    public var assignorId: String? = null

    /**
     * 委托人
     */
    @TableField(value = "assignor_name")
    public var assignorName: String? = null

    /**
     * 任务期望完成时间
     */
    @TableField(value = "expire_time")
    public var expireTime: LocalDateTime? = null

    /**
     * 提醒时间
     */
    @TableField(value = "remind_time")
    public var remindTime: LocalDateTime? = null

    /**
     * 提醒次数
     */
    @TableField(value = "remind_repeat")
    public var remindRepeat: Int? = null

    /**
     * 已阅 0，否 1，是
     */
    @TableField(value = "viewed")
    public var viewed: Boolean? = null

    /**
     * 完成时间
     */
    @TableField(value = "finish_time")
    public var finishTime: LocalDateTime? = null
}
