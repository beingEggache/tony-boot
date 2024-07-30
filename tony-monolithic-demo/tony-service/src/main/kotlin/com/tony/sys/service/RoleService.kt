package com.tony.sys.service

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum
import com.github.houbb.pinyin.util.PinyinHelper
import com.tony.PageQueryLike
import com.tony.PageResult
import com.tony.sys.dao.RoleDao
import com.tony.sys.dto.req.RoleAddReq
import com.tony.sys.dto.req.RoleAssignModulesReq
import com.tony.sys.dto.req.RoleDeleteReq
import com.tony.sys.dto.req.RoleModuleQuery
import com.tony.sys.dto.req.RoleQuery
import com.tony.sys.dto.req.RoleUpdateReq
import com.tony.sys.dto.resp.ModuleResp
import com.tony.sys.dto.resp.RoleResp
import com.tony.sys.po.Role
import com.tony.utils.copyTo
import com.tony.utils.genRandomInt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 角色Service
 * @author tangli
 * @date 2024/07/03 13:14
 * @since 1.0.0
 */
@Service
class RoleService(
    private val dao: RoleDao,
) {
    /**
     * 列表
     * @param [req] 请求
     * @return [PageResult]<[RoleResp]>
     * @author tangli
     * @date 2024/07/04 14:35
     * @since 1.0.0
     */
    fun list(req: PageQueryLike<RoleQuery>): PageResult<RoleResp> =
        dao
            .ktQuery()
            .like(req.query.roleName.isNotBlank(), Role::roleName, req.query.roleName)
            .eq(req.query.enabled != null, Role::enabled, req.query.enabled)
            .eq(Role::buildIn, false)
            .eq(Role::tenantId, req.query.tenantId)
            .pageResult(req)
            .map { it.copyTo() }

    /**
     * 新增
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/04 14:36
     * @since 1.0.0
     */
    fun add(req: RoleAddReq) {
        val name = req.roleName
        dao
            .ktQuery()
            .eq(Role::roleName, name)
            .eq(Role::tenantId, req.tenantId)
            .throwIfExists("已有同名数据")

        val code =
            PinyinHelper.toPinyin(name, PinyinStyleEnum.FIRST_LETTER, "").uppercase() + "${genRandomInt(4)}"
        val po =
            req
                .copyTo<Role>()
                .apply {
                    this.roleCode = code
                }
        dao.insert(po)
    }

    /**
     * 更新
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/04 14:36
     * @since 1.0.0
     */
    fun update(req: RoleUpdateReq) {
        val id = req.roleId
        val tenantId = req.tenantId

        val name = req.roleName
        dao
            .ktQuery()
            .ne(Role::roleId, id)
            .eq(Role::roleName, name)
            .eq(Role::tenantId, tenantId)
            .throwIfExists("已有同名数据")

        val updatedPo =
            req.copyTo<Role>()

        dao.updateById(updatedPo)
    }

    /**
     * 删除
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/04 14:36
     * @since 1.0.0
     */
    fun delete(req: RoleDeleteReq) {
        val id = req.roleId
        val tenantId = req.tenantId
        val po =
            dao
                .ktQuery()
                .eq(Role::roleId, id)
                .eq(Role::tenantId, tenantId)
                .oneNotNull()

        dao.deleteById(po)
        dao.deleteRoleModules(id, req.tenantId)
        dao.deleteEmployeesRole(id, tenantId)
    }

    /**
     * 分配模块
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/05 16:05
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun assignModules(req: RoleAssignModulesReq) {
        dao.deleteRoleModules(req.roleId, req.tenantId)
        req.moduleIdList
            .takeIf { it.isNotEmpty() }
            ?.let {
                dao.insertRoleModules(req.roleId, it, req.tenantId)
            }
    }

    /**
     * 列出角色模块
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/05 16:05
     * @since 1.0.0
     */
    fun listRoleModules(req: RoleModuleQuery): Collection<ModuleResp> =
        dao.selectRoleModules(req.roleId, req.moduleTypes, req.tenantId)
}
