package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
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
    public var taskCcId: String = ""

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
    public var createTime: LocalDateTime? = null

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
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var taskName: String = ""

    /**
     * 任务显示名称
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var displayName: String = ""

    /**
     * 参与者ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var actorId: String = ""

    /**
     * 参与者名称
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var actorName: String = ""

    /**
     * 参与者类型: 1.用户, 2.角色, 3.部门
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var actorType: ActorType? = null

    /**
     * 任务状态: 1.结束 2.活动
     */
    public var taskState: TaskState? = null

    /**
     * 完成时间
     */
    public var finishTime: LocalDateTime? = null
}
