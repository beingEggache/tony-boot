package com.tony.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.dto.enums.ModuleType
import java.time.LocalDateTime

/**
 * <p>
 * 模块/权限表
 * </p>
 *
 * @author Tang Li
 * @date 2020-11-15
 */
@TableName("sys_module")
class Module {
    /**
     * 模块/权限ID
     */
    @TableId
    var moduleId: String? = null

    /**
     * 应用ID
     */
    var appId: String? = null

    /**
     * 模块/权限名
     */
    var moduleName: String? = null

    /**
     * 模块/权限值（接口URL，前端路由，前端组件名）
     */
    var moduleValue: String? = null

    /**
     * 模块/权限类型（1：接口，2：前端路由，3：前端组件）
     */
    var moduleType: ModuleType? = null

    /**
     * 模块/权限分组
     */
    var moduleGroup: String? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 创建时间
     */
    @OrderBy
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var createTime: LocalDateTime? = null

    /**
     * 创建人
     */
    @TableField(
        fill = FieldFill.INSERT,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorId: Long? = null

    /**
     * 创建人名称
     */
    @TableField(
        fill = FieldFill.INSERT,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorName: String? = null

    /**
     * 更新时间
     */
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.ALWAYS,
        update = "CURRENT_TIMESTAMP"
    )
    var updateTime: LocalDateTime? = null

    /**
     * 更新人
     */
    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var updatorId: Long? = null

    /**
     * 更新人名称
     */
    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var updatorName: String? = null

    /**
     * 状态：1-启用，0-禁用
     */
    var enabled: Boolean? = null

    /**
     * 删除标记：1-已删除，0-未删除
     */
    @TableLogic
    var deleted: Boolean? = null

    /**
     * 租户id
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    var tenantId: Long? = null
}
