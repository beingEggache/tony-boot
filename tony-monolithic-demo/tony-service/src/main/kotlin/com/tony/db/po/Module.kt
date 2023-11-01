package com.tony.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
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
class Module : Auditable {
    /**
     * 模块/权限ID
     */
    @TableId("module_id")
    var moduleId: String? = null

    /**
     * 应用ID
     */
    @TableField("app_id")
    var appId: String? = null

    /**
     * 模块/权限名
     */
    @TableField("module_name")
    var moduleName: String? = null

    /**
     * 模块/权限值（接口URL，前端路由，前端组件名）
     */
    @TableField("module_value")
    var moduleValue: String? = null

    /**
     * 模块/权限类型（1：接口，2：前端路由，3：前端组件）
     */
    @TableField("module_type")
    var moduleType: ModuleType? = null

    /**
     * 模块/权限分组
     */
    @TableField("module_group")
    var moduleGroup: String? = null

    /**
     * 创建时间
     */
    @TableField("create_time")
    override var createTime: LocalDateTime? = null

    /**
     * 创建人
     */
    @TableField("creator_id")
    override var creatorId: Long? = null

    /**
     * 创建人名称
     */
    @TableField("creator_name")
    override var creatorName: String? = null

    /**
     * 更新时间
     */
    @TableField("update_time")
    override var updateTime: LocalDateTime? = null

    /**
     * 更新人
     */
    @TableField("updator_id")
    override var updatorId: Long? = null

    /**
     * 更新人名称
     */
    @TableField("updator_name")
    override var updatorName: String? = null

    /**
     * 状态：1-启用，0-禁用
     */
    @TableField("enabled")
    var enabled: Boolean? = null

    /**
     * 备注
     */
    @TableField("remark")
    var remark: String? = null

    /**
     * 删除标记：1-已删除，0-未删除
     */
    @TableField("deleted")
    var deleted: Boolean? = null
}
