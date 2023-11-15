package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.enums.TaskState
import java.time.LocalDateTime

/**
 * 抄送任务表
 * @author Tang Li
 * @date 2023/09/29 16:14
 * @since 1.0.0
 */
@TableName
public class FusTaskCc {
    /**
     * 主键ID
     */
    @TableId
    public var taskCcId: String? = null

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
     * 参与者ID
     */
    public var actorId: String? = null

    /**
     * 参与者名称
     */
    public var actorName: String? = null

    /**
     * 参与者类型 0，用户 1，角色 2，部门
     */
    public var actorType: ActorType? = null

    /**
     * 任务状态 0，结束 1，活动
     */
    public var taskState: TaskState? = null

    /**
     * 完成时间
     */
    public var finishTime: LocalDateTime? = null
}
