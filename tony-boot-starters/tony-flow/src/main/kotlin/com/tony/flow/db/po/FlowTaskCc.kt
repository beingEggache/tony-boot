package com.tony.flow.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.flow.enums.ActorType
import java.time.LocalDateTime

/**
 * 抄送任务表
 * @author Tang Li
 * @date 2023/09/29 16:14
 * @since 1.0.0
 */
@TableName(value = "flow_task_cc")
public class FlowTaskCc {
    /**
     * 主键ID
     */
    @TableId(value = "task_cc_id")
    public var taskCcId: Long? = null

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
     * 参与者ID
     */
    @TableField(value = "actor_id")
    public var actorId: String? = null

    /**
     * 参与者名称
     */
    @TableField(value = "actor_name")
    public var actorName: String? = null

    /**
     * 参与者类型 0，用户 1，角色 2，部门
     */
    @TableField(value = "actorType")
    public var actorType: ActorType? = null

    /**
     * 任务状态 0，结束 1，活动
     */
    @TableField(value = "task_state")
    public var taskState: Int? = null

    /**
     * 完成时间
     */
    @TableField(value = "finish_time")
    public var finishTime: LocalDateTime? = null
}
