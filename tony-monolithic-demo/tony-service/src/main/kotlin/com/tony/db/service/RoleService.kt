package com.tony.db.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.tony.db.dao.ModuleDao
import com.tony.db.dao.RoleDao
import com.tony.db.dao.UserDao
import com.tony.db.po.Role
import com.tony.dto.req.ModuleAssignReq
import com.tony.dto.req.RoleAssignReq
import com.tony.dto.req.RoleCreateReq
import com.tony.dto.req.RoleUpdateReq
import com.tony.exception.BizException
import com.tony.utils.defaultIfBlank
import com.tony.utils.throwIf
import com.tony.utils.throwIfAndReturn
import com.tony.utils.throwIfNullAndReturn
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.Valid

/**
 *
 * @author tangli
 * @since 2020-11-03 14:35
 */
@Service
class RoleService(
    private val userDao: UserDao,
    private val moduleDao: ModuleDao
) : ServiceImpl<RoleDao, Role>() {

    @Transactional
    fun add(@Valid req: RoleCreateReq, appId: String) =
        throwIfAndReturn(baseMapper.selectById(req.roleId) != null, "角色ID已重复") {
            baseMapper.insert(
                Role().apply {
                    this.roleId = req.roleId
                    this.roleName = req.roleName
                    this.remark = req.remark
                    this.appId = appId
                }
            )
        }

    @Transactional
    fun update(@Valid req: RoleUpdateReq) = baseMapper.selectById(req.roleId).throwIfNullAndReturn("不存在此角色") {
        baseMapper.updateById(
            Role().apply {
                this.roleId = req.roleId
                this.roleName = req.roleName
                this.remark = req.remark
            }
        )
    }

    fun page(query: String?, page: Long = 1, size: Long = 10) =
        baseMapper.selectPage(
            Page(page, size),
            KtQueryWrapper(Role::class.java).like(!query.isNullOrBlank(), Role::roleName, query)
        ).toPageResult()

    fun selectByUserId(userId: String?, appId: String): List<Role> = baseMapper.selectByUserId(userId, appId)

    @Transactional
    fun assignRole(req: RoleAssignReq) {
        req.userIdList.forEach { userId ->
            baseMapper.deleteUserRoleByUserId(userId)
            userDao.selectById(userId) ?: throw BizException("不存在的用户:$userId")
            req.roleIdList.forEach { roleId ->
                baseMapper.selectById(roleId) ?: throw BizException("不存在的角色:$roleId")
                baseMapper.insertUserRole(userId, roleId)
                ModuleDao.clearModuleCache(userId)
            }
        }
    }

    @Transactional
    fun assignModule(req: ModuleAssignReq) {
        val moduleIdList = moduleDao.selectByModuleGroups(req.moduleGroupList).map {
            it.moduleId.defaultIfBlank()
        }
        throwIf(!moduleIdList.any(), "没找到对应模块:${req.moduleGroupList.joinToString()}")

        req.roleIdList.forEach { roleId ->
            baseMapper.deleteRoleModuleByRoleId(roleId)
            baseMapper.selectById(roleId) ?: throw BizException("不存在的角色:$roleId")
            moduleIdList.forEach { moduleId ->
                baseMapper.selectById(roleId) ?: throw BizException("不存在的模块:$moduleId")
                baseMapper.insertRoleModule(roleId, moduleId)
                ModuleDao.clearModuleCache()
            }
        }
    }
}
