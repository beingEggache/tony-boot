package tony.demo.sys.dao

import org.apache.ibatis.annotations.Param
import tony.demo.sys.dto.enums.ModuleType
import tony.demo.sys.dto.resp.ModuleResp
import tony.demo.sys.dto.resp.RoleResp
import tony.demo.sys.po.Role
import tony.mybatis.dao.BaseDao

interface RoleDao : BaseDao<Role> {
    /**
     * 插入员工角色
     * @param [employeeId] 员工id
     * @param [roleIds] 角色id
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 11:02
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
     * @param [employeeId] 员工id
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 11:07
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
     * @param [tenantId] 租户id
     * @param [includeBuildIn] 包括内置
     * @return [List]<[RoleResp]>
     * @author tangli
     * @date 2024/07/26 11:07
     */
    fun selectEmployeeRoles(
        @Param("employeeId")
        employeeId: String,
        @Param("tenantId")
        tenantId: String,
        @Param("includeBuildIn")
        includeBuildIn: Boolean,
    ): List<RoleResp>

    /**
     * 查询用户角色
     * @param [employeeId] 员工id
     * @param [tenantId] 租户id
     * @return [List]<[RoleResp]>
     * @author tangli
     * @date 2024/07/26 11:07
     */
    fun selectEmployeeRoles(
        employeeId: String,
        tenantId: String,
    ): List<RoleResp> =
        selectEmployeeRoles(
            employeeId,
            tenantId,
            false
        )

    /**
     * 查询员工是否具有内置角色
     * @param [employeeId] 员工id
     * @param [roleId] 角色id
     * @return [Boolean]
     * @author tangli
     * @date 2024/07/26 11:15
     */
    fun selectEmployeeHasBuildInRole(
        employeeId: String,
        roleId: String,
    ): Boolean =
        selectEmployeeRoles(
            employeeId,
            "",
            true
        ).any { it.roleId == roleId }

    /**
     * 插入角色模块
     * @param [roleId] 角色id
     * @param [moduleIdList] 模块id列表
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 14:20
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
     * @return [List]<[ModuleResp]>
     * @author tangli
     * @date 2024/07/05 14:43
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
