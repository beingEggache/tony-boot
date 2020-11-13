package com.tony.db.dao

import com.tony.db.po.Module
import com.tony.pojo.enums.ModuleType
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

@Repository
interface ModuleDao {

    fun deleteByPk(moduleId: String?): Int

    fun deleteByModuleType(moduleTypes: List<ModuleType>): Int

    fun insert(record: Module?): Int

    fun insertDynamic(record: Module?): Int

    fun selectByPk(moduleId: String?): Module?

    fun updateByPkDynamic(record: Module?): Int

    fun updateByPk(record: Module?): Int

    fun selectAll(): List<Module>

    fun selectModulesByUserIdAndAppId(
        @Param("userId") userId: String,
        @Param("appId") appId: String,
        @Param("types") types: List<ModuleType>): List<Module>
}
