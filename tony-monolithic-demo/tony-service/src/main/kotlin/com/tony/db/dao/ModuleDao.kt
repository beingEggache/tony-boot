package com.tony.db.dao

import com.tony.db.po.Module
import com.tony.dto.resp.ModuleResp
import com.tony.mybatis.dao.BaseDao
import com.tony.utils.copyTo

interface ModuleDao : BaseDao<Module> {
    /**
     * 按应用程序id选择
     * @param [appId] 应用程序id
     * @return [List]<[ModuleResp]>
     * @author tangli
     * @date 2024/07/26 11:21
     * @since 1.0.0
     */
    fun selectByAppId(appId: String): List<ModuleResp> =
        ktQuery()
            .eq(Module::appId, appId)
            .eq(Module::enabled, true)
            .list()
            .map { it.copyTo(ModuleResp::class.java) }
}
