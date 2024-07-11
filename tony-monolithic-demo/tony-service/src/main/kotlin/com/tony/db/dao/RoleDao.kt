package com.tony.db.dao

import com.tony.db.po.Role
import com.tony.dto.enums.ModuleType
import com.tony.dto.resp.ModuleResp
import com.tony.dto.resp.RoleResp
import com.tony.mybatis.dao.BaseDao
import org.apache.ibatis.annotations.Param

interface RoleDao : BaseDao<Role> {
    /**
     * 插入用户角色
     * @param [employeeId] 用户id
     * @param [roleIds] 角色ID
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/11 09:14
     * @since 1.0.0
     */
    fun insertEmployeeRoles(
        @Param("employeeId")
        employeeId: String,
        @Param("roleIds")
        roleIds: Collection<String>,
        @Param("tenantId")
        tenantId: String,
    ): Int

    /**
     * 删除用户角色
     * @param [employeeId] 用户id
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 11:07
     * @since 1.0.0
     */
    fun deleteEmployeeRoles(
        @Param("employeeId")
        employeeId: String,
        @Param("tenantId")
        tenantId: String,
    ): Int

    /**
     * 删除用户角色
     * @param [roleId] 角色id
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 11:07
     * @since 1.0.0
     */
    fun deleteEmployeesRole(
        @Param("roleId")
        roleId: String,
        @Param("tenantId")
        tenantId: String,
    ): Int

    /**
     * 查询用户角色
     * @param [employeeId] 员工id
     * @return [List]<[RoleResp]>
     * @author tangli
     * @date 2024/07/09 10:35
     * @since 1.0.0
     */
    fun selectEmployeeRoles(
        @Param("employeeId")
        employeeId: String,
        @Param("tenantId")
        tenantId: String,
    ): List<RoleResp>

    /**
     * 插入角色模块
     * @param [roleId] 角色id
     * @param [moduleIdList] 模块id列表
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 14:20
     * @since 1.0.0
     */
    fun insertRoleModules(
        @Param("roleId")
        roleId: String,
        @Param("moduleIdList")
        moduleIdList: Collection<String>,
        @Param("tenantId")
        tenantId: String,
    ): Int

    /**
     * 删除角色模块
     * @param [roleId] 角色id
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 14:22
     * @since 1.0.0
     */
    fun deleteRoleModules(
        @Param("roleId")
        roleId: String,
        @Param("tenantId")
        tenantId: String,
    ): Int

    /**
     * 列出角色模块
     * @param [roleId] 角色id
     * @return [List<ModuleResp>]
     * @author tangli
     * @date 2024/07/05 14:43
     * @since 1.0.0
     */
    fun selectRoleModules(
        @Param("roleId")
        roleId: String,
        @Param("moduleTypes")
        moduleTypes: Collection<ModuleType>,
        @Param("tenantId")
        tenantId: String,
    ): List<ModuleResp>
}
