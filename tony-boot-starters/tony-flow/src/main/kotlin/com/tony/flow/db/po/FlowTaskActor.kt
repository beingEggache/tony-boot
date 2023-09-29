package com.tony.flow.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.flow.enums.ActorType

/**
 * 任务参与者表
 * @author Tang Li
 * @date 2023/09/29 16:14
 * @since 1.0.0
 */
@TableName(value = "flow_task_actor")
public open class FlowTaskActor {
    /**
     * 主键 ID
     */
    @TableId(value = "task_actor_id")
    public var taskActorId: Long? = null

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id")
    public var tenantId: Long? = null

    /**
     * 流程实例ID
     */
    @TableField(value = "instance_id")
    public var instanceId: Long? = null

    /**
     * 任务ID
     */
    @TableField(value = "task_id")
    public var taskId: Long? = null

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
}
