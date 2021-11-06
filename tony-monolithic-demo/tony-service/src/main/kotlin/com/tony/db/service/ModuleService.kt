package com.tony.db.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.tony.cache.RedisUtils
import com.tony.cache.annotation.RedisCacheable
import com.tony.db.CacheKeys
import com.tony.db.dao.ModuleDao
import com.tony.db.po.Module
import com.tony.db.po.Module.Companion.MODULE_TYPE
import com.tony.dto.enums.ModuleType
import com.tony.dto.resp.ModuleResp
import com.tony.dto.resp.RouteAndComponentModuleResp
import com.tony.dto.trait.listAndSetChildren
import com.tony.exception.BizException
import com.tony.utils.defaultIfBlank
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *
 * @author tangli
 * @since 2020-11-04 14:48
 */
@Service
class ModuleService(
    private val moduleDao: ModuleDao
) {

    companion object {
        val frontEndModuleTypes = listOf(ModuleType.ROUTE, ModuleType.COMPONENT)
    }

    @RedisCacheable(
        cacheKey = CacheKeys.USER_FRONTEND_MODULES_CACHE_KEY,
        paramsNames = ["userId"]
    )
    fun listRouteAndComponentModules(userId: String, appId: String): RouteAndComponentModuleResp {
        val modules = moduleDao.selectModulesByUserIdAndAppId(
            userId, appId,
            listOf(
                ModuleType.ROUTE, ModuleType.COMPONENT
            )
        ).map { it.toDto() }

        val routeModules = modules.filter { it.moduleType == ModuleType.ROUTE }

        val componentModule = modules.filter { it.moduleType == ModuleType.COMPONENT }

        return RouteAndComponentModuleResp(routeModules, componentModule)
    }

    @RedisCacheable(
        cacheKey = CacheKeys.USER_API_MODULES_CACHE_KEY,
        paramsNames = ["userId"]
    )
    fun listApiModules(userId: String, appId: String) =
        moduleDao.selectModulesByUserIdAndAppId(userId, appId, listOf(ModuleType.API)).map { it.toDto() }

    @Transactional
    fun saveModules(modules: List<Module>, moduleType: List<ModuleType>, appId: String) {
        if (modules.isEmpty()) throw BizException("模块列表为空")
        moduleDao.delete(QueryWrapper<Module>().`in`(MODULE_TYPE, moduleType))
        modules.forEach {
            it.appId = appId
            moduleDao.insert(it)
        }
        ModuleDao.clearModuleCache()
    }

    fun tree(moduleTypes: List<ModuleType>): List<ModuleResp> {
        val modules = moduleDao.selectList(QueryWrapper<Module>().`in`(MODULE_TYPE, moduleTypes)).map { it.toDto() }
        return modules.listAndSetChildren()
    }

    fun listModuleGroups(appId: String) =
        moduleDao.selectList(QueryWrapper<Module>().eq(Module.APP_ID, appId))
            .filter { !it.moduleGroup.isNullOrEmpty() }
            .flatMap { it.moduleGroup.defaultIfBlank().split(",") }
            .distinct()

    fun listByRoleId(roleId: String): List<String?> {
        if (roleId.isBlank()) throw BizException("请选择角色")
        return moduleDao.selectModuleByRoleId(roleId)
            .filter { it.moduleType in frontEndModuleTypes }
            .map { it.moduleId }
    }

    fun clearRedis() = RedisUtils.delete("*")

    private fun Module.toDto() =
        ModuleResp(
            moduleId.defaultIfBlank(),
            moduleName.defaultIfBlank(),
            moduleValue.defaultIfBlank(),
            moduleType,
            moduleGroup.defaultIfBlank()
        )
}
