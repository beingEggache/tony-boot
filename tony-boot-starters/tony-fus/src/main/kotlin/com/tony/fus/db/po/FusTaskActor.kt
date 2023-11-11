package com.tony.fus.db.po

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
@TableName(value = "fus_task_actor")
public open class FusTaskActor {
    /**
     * 主键 ID
     */
    @TableId(value = "task_actor_id")
    public var taskActorId: String? = null

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id")
    public var tenantId: String? = null

    /**
     * 流程实例ID
     */
    @TableField(value = "instance_id")
    public var instanceId: String? = null

    /**
     * 任务ID
     */
    @TableField(value = "task_id")
    public var taskId: String? = null

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
    @TableField(value = "actor_type")
    public var actorType: ActorType? = null

    /**
     * 票签权重
     */
    @TableField(value = "weight")
    public var weight: Int? = null
}
