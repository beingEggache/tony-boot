package com.tony.db.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.tony.db.po.Role
import org.apache.ibatis.annotations.Param

import org.springframework.stereotype.Repository

@Repository
interface RoleDao : BaseMapper<Role> {

    fun selectByUserId(
        @Param("userId")
        userId: String?,
        @Param("appId")
        appId: String
    ): List<Role>

    fun insertUserRole(
        @Param("userId")
        userId: String,
        @Param("roleId")
        roleId: String
    )

    fun insertRoleModule(
        @Param("roleId")
        roleId: String,
        @Param("moduleId")
        moduleId: String
    )

    fun deleteRoleModuleByRoleId(roleId: String)

    fun deleteUserRoleByRoleId(roleId: String)

    fun deleteUserRoleByUserId(userId: String)
}
