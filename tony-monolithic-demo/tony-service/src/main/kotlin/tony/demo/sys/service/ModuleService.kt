package tony.demo.sys.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tony.core.model.RowsWrapper
import tony.core.utils.copyTo
import tony.core.utils.listToTree
import tony.core.utils.throwIfEmpty
import tony.core.utils.treeToList
import tony.demo.sys.dao.ModuleDao
import tony.demo.sys.dto.enums.ModuleType
import tony.demo.sys.dto.query.ModuleQuery
import tony.demo.sys.dto.req.ModuleSubmitReq
import tony.demo.sys.dto.resp.ModuleResp
import tony.demo.sys.po.Module

/**
 * 模块Service
 * @author tangli
 * @date 2024/07/03 13:14
 */
@Service
class ModuleService(
    private val moduleDao: ModuleDao,
) {
    /**
     * 提交全部
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/05 16:33
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun submitAll(req: RowsWrapper<ModuleSubmitReq>) {
        val rows = req.rows.throwIfEmpty()
        moduleDao
            .ktUpdate()
            .and { param ->
                rows.forEach { module -> param.or().likeRight(Module::moduleCodeSeq, module.moduleCodeSeq) }
            }.physicalRemove()
        val moduleList = rows.treeToList().map<ModuleSubmitReq, Module> { it.copyTo() }
        moduleDao.insert(moduleList)
    }

    /**
     * 树
     * @param [appId] 应用程序id
     * @return [List]<[ModuleResp]>
     * @author tangli
     * @date 2024/07/05 16:33
     */
    fun tree(appId: String): List<ModuleResp> =
        moduleDao
            .ktQuery()
            .eq(Module::appId, appId)
            .orderByDesc(Module::moduleCode)
            .list()
            .map { it.copyTo<ModuleResp>() }
            .listToTree()

    /**
     * 列表
     * @param [query] 应用程序id
     * @return [List]<[ModuleResp]>
     * @author tangli
     * @date 2024/07/08 10:40
     */
    fun list(query: ModuleQuery): List<ModuleResp> =
        moduleDao
            .ktQuery()
            .eq(Module::appId, query.appId)
            .`in`(Module::moduleType, mutableListOf(ModuleType.NODE).plus(query.moduleTypes))
            .orderByDesc(Module::moduleCode)
            .list()
            .map { it.copyTo<ModuleResp>() }
}
