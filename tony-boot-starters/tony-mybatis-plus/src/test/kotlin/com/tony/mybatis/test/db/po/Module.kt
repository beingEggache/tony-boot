package com.tony.mybatis.test.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.mybatis.test.db.enums.ModuleType

/**
 * <p>
 * 模块/权限表
 * </p>
 *
 * @author Tang Li
 * @date 2020-11-15
 */
@TableName("t_sys_module")
class Module {

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
     * 模块/权限说明
     */
    @TableField("module_description")
    var moduleDescription: String? = null
}
