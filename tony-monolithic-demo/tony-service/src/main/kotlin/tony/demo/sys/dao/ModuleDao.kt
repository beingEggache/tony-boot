package tony.demo.sys.dao

import tony.demo.sys.dto.resp.ModuleResp
import tony.demo.sys.po.Module
import tony.mybatis.dao.BaseDao
import tony.utils.copyTo

interface ModuleDao : BaseDao<Module> {
    /**
     * 按应用程序id选择
     * @param [appId] 应用程序id
     * @return [List]<[ModuleResp]>
     * @author tangli
     * @date 2024/07/26 11:21
     */
    fun selectByAppId(appId: String): List<ModuleResp> =
        ktQuery()
            .eq(Module::appId, appId)
            .eq(Module::enabled, true)
            .list()
            .map { it.copyTo(ModuleResp::class.java) }
}
