package com.tony.mybatis.db.dao

import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.db.enums.ModuleType
import com.tony.mybatis.db.po.Module
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
}
