package com.tony.db.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.tony.cache.RedisKeys
import com.tony.cache.RedisManager
import com.tony.db.CacheKeys
import com.tony.db.po.Module
import com.tony.dto.enums.ModuleType
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

@Repository
interface ModuleDao : BaseMapper<Module> {

    fun selectModulesByUserIdAndAppId(
        @Param("userId") userId: String,
        @Param("appId") appId: String,
        @Param("types") types: List<ModuleType>
    ): List<Module>

    fun selectByModuleGroups(moduleGroups: List<String>): List<Module>

    fun selectModuleByRoleId(roleId: String): List<Module>

    companion object {
        fun clearModuleCache(userId: String = "*") {

            RedisManager.delete(RedisKeys.genKey(CacheKeys.USER_FRONTEND_MODULES_CACHE_KEY, userId))
            RedisManager.delete(RedisKeys.genKey(CacheKeys.USER_API_MODULES_CACHE_KEY, userId))
        }
    }
}
