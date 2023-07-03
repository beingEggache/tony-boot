package com.tony.db.dao

import com.tony.db.CacheKeys
import com.tony.db.po.Module
import com.tony.dto.enums.ModuleType
import com.tony.mybatis.dao.BaseDao
import com.tony.redis.RedisKeys
import com.tony.redis.RedisManager
import org.apache.ibatis.annotations.Param

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
            RedisManager.deleteByKeyPatterns(
                RedisKeys.genKey(CacheKeys.USER_FRONTEND_MODULES_CACHE_KEY, userId),
                RedisKeys.genKey(CacheKeys.USER_API_MODULES_CACHE_KEY, userId),
            )
        }
    }
}
