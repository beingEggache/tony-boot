package com.tony.db.service

import com.tony.cache.annotation.RedisCacheable
import com.tony.core.exception.BizException
import com.tony.core.utils.defaultIfBlank
import com.tony.db.CacheKeys
import com.tony.db.dao.ModuleDao
import com.tony.db.po.Module
import com.tony.pojo.enums.ModuleType
import com.tony.pojo.resp.ModuleResp
import com.tony.pojo.resp.RouteAndComponentModuleResp
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

    val rootRouteModuleIdRegex = Regex("^[a-zA-Z0-9]+$")

    @RedisCacheable(
        cacheKey = CacheKeys.USER_FRONTEND_MODULES_CACHE_KEY,
        paramsNames = ["userId"]
    )
    fun listRouteAndComponentModules(userId: String, appId: String): RouteAndComponentModuleResp {
        val modules = moduleDao.selectModulesByUserIdAndAppId(userId, appId, listOf(
            ModuleType.ROUTE, ModuleType.COMPONENT
        )).map { it.toDto() }

        val routeModules = modules.filter { it.moduleType == ModuleType.ROUTE }

        val rootRouteModules = routeModules.filter { it.isRootRouteModule }

        val componentModule = modules.filter { it.moduleType == ModuleType.COMPONENT }

        return RouteAndComponentModuleResp(rootRouteModules.onEach { module ->
            module.findAndSetChildren(routeModules)
        }, componentModule)
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
        moduleDao.deleteByModuleType(moduleType)
        modules.forEach {
            it.appId = appId
            moduleDao.insertDynamic(it)
        }
    }

    private fun ModuleResp.findAndSetChildren(routeModules: List<ModuleResp>): ModuleResp =
        routeModules.filter {
            isMyChild(it.moduleId)
        }.onEach {
            it.findAndSetChildren(routeModules)
        }.let { children ->
            this.children = children
            this
        }

    private fun ModuleResp.isMyChild(otherModuleId: String) =
        Regex("^${moduleId}-[a-zA-Z0-9]+$").matches(otherModuleId)

    val ModuleResp.isRootRouteModule: Boolean
        get() = moduleType == ModuleType.ROUTE &&
            rootRouteModuleIdRegex.matches(moduleId)

    private fun Module.toDto() =
        ModuleResp(
            moduleId.defaultIfBlank(),
            moduleName.defaultIfBlank(),
            moduleValue.defaultIfBlank(),
            moduleType)


}
