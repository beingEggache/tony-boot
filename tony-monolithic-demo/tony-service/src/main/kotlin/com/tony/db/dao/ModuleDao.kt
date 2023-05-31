package com.tony.db.dao

import com.tony.cache.RedisKeys
import com.tony.cache.RedisManager
import com.tony.db.CacheKeys
import com.tony.db.po.Module
import com.tony.dto.enums.ModuleType
import com.tony.mybatis.dao.BaseDao
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

@Repository
interface ModuleDao : BaseDao<Module> {

    fun selectModulesByUserIdAndAppId(
        @Param("userId") userId: String,
        @Param("appId") appId: String,
        @Param("types") types: List<ModuleType>,
    ): List<Module>

    fun selectByModuleGroups(moduleGroups: List<String>): List<Module>

    fun selectModuleByRoleId(roleId: String): List<Module>

    companion object {
        fun clearModuleCache(userId: String = "*") {
            RedisManager.deleteWithKeyPatterns(
                RedisKeys.genKey(CacheKeys.USER_FRONTEND_MODULES_CACHE_KEY, userId),
                RedisKeys.genKey(CacheKeys.USER_API_MODULES_CACHE_KEY, userId),
            )
        }
    }
}
