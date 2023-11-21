package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
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
@TableName
public open class FusInstance {
    /**
     * 主键ID
     */
    @TableId
    public var instanceId: String = ""

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
     * 流程定义ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var processId: String = ""

    /**
     * 优先级
     */
    public var priority: Int? = null

    /**
     * 业务KEY
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var businessKey: String = ""

    /**
     * 变量json
     */
    public var variable: String = "{}"

    /**
     * 期望完成时间
     */
    public var expireTime: LocalDateTime? = null

    /**
     * 更新人id
     */
    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER
    )
    public var updatorId: String = ""

    /**
     * 上次更新人
     */
    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER
    )
    public var updatorName: String = ""

    /**
     * 上次更新时间
     */
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    public val updateTime: LocalDateTime = LocalDateTime.now()
}
