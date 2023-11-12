package com.tony.mybatis.test.db.dao

import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.test.db.po.Role
import org.apache.ibatis.annotations.Param

interface RoleDao : BaseDao<Role> {
    fun selectByUserId(
        @Param("userId")
        userId: String?,
        @Param("appId")
        appId: String,
    ): List<Role>

    fun insertUserRole(
        @Param("userId")
        userId: String,
        @Param("roleId")
        roleId: String,
    )

    fun insertRoleModule(
        @Param("roleId")
        roleId: String,
        @Param("moduleId")
        moduleId: String,
    )

    fun deleteRoleModuleByRoleId(roleId: String)

    fun deleteUserRoleByRoleId(roleId: String)

    fun deleteUserRoleByUserId(userId: String)
}
