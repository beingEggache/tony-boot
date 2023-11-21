package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.ActorType

/**
 * 任务参与者表
 * @author Tang Li
 * @date 2023/09/29 16:14
 * @since 1.0.0
 */
@TableName
public open class FusTaskActor {
    /**
     * 主键 ID
     */
    @TableId
    public var taskActorId: String = ""

    /**
     * 租户ID
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var tenantId: String = ""

    /**
     * 流程实例ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var instanceId: String = ""

    /**
     * 任务ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var taskId: String = ""

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
    public var actorType: ActorType = ActorType.USER

    /**
     * 票签权重
     */
    public var weight: Int = 0
}
