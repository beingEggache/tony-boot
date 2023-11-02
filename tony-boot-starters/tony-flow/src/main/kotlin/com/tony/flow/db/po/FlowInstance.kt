package com.tony.flow.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 * 流程实例
 * @author Tang Li
 * @date 2023/09/29 16:13
 * @since 1.0.0
 */
@TableName(value = "flow_instance")
public open class FlowInstance {
    /**
     * 主键ID
     */
    @TableId(value = "instance_id")
    public var instanceId: Long? = null

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
     * 流程定义ID
     */
    @TableField(value = "process_id")
    public var processId: Long? = null

    /**
     * 优先级
     */
    @TableField(value = "priority")
    public var priority: Int? = null

    /**
     * 流程实例编号
     */
    @TableField(value = "instance_no")
    public var instanceNo: String? = null

    /**
     * 业务KEY
     */
    @TableField(value = "business_key")
    public var businessKey: String? = null

    /**
     * 变量json
     */
    @TableField(value = "variable")
    public var variable: String? = null

    /**
     * 流程实例版本
     */
    @TableField(value = "instance_version")
    public var instanceVersion: Int? = null

    /**
     * 期望完成时间
     */
    @TableField(value = "expire_time")
    public var expireTime: LocalDateTime? = null

    /**
     * 更新人id
     */
    @TableField(value = "updator_id")
    public var updatorId: Long? = null

    /**
     * 上次更新人
     */
    @TableField(value = "updator_name")
    public var updatorName: String? = null

    /**
     * 上次更新时间
     */
    @TableField(value = "update_time")
    public var updateTime: LocalDateTime? = null
}
