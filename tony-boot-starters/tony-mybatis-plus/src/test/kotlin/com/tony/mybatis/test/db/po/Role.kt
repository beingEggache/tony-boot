package com.tony.mybatis.test.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 * 角色
 *
 */
@TableName("t_sys_role")
class Role {
    /**
     * 角色ID
     *
     */
    @TableId("role_id")
    var roleId: String? = null

    /**
     * 应用ID
     *
     */
    @TableField("app_id")
    var appId: String? = null

    /**
     * 角色名
     *
     */
    @TableField("role_name")
    var roleName: String? = null

    /**
     * 备注
     *
     */
    @TableField("remark")
    var remark: String? = null
}
