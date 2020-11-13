package com.tony.db.po

import com.tony.pojo.enums.ModuleType

/**
 * 模块/权限表
 *
 */
class Module {
    /**
     * 模块/权限ID
     *
     */
    var moduleId: String? = null

    /**
     * 应用ID
     *
     */
    var appId: String? = null

    /**
     * 模块/权限名
     *
     */
    var moduleName: String? = null

    /**
     * 模块/权限值（接口URL，前端路由，前端组件名）
     *
     */
    var moduleValue: String? = null

    /**
     * 模块/权限类型（1：接口，2：前端路由，3：前端组件）
     *
     */
    var moduleType: ModuleType? = null

    /**
     * 模块/权限分组
     *
     */
    var moduleGroup: String? = null

    /**
     * 模块/权限说明
     *
     */
    var moduleDescription: String? = null

}
