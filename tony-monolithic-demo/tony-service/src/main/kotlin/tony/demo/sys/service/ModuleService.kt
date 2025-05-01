package tony.demo.sys.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tony.RowsWrapper
import tony.demo.sys.dao.ModuleDao
import tony.demo.sys.dto.enums.ModuleType
import tony.demo.sys.dto.req.ModuleQuery
import tony.demo.sys.dto.req.ModuleSubmitReq
import tony.demo.sys.dto.resp.ModuleResp
import tony.demo.sys.po.Module
import tony.demo.trait.listAndSetChildren
import tony.demo.trait.treeToList
import tony.utils.copyTo
import tony.utils.throwIfEmpty

/**
 * 模块Service
 * @author tangli
 * @date 2024/07/03 13:14
 * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
     */
    fun tree(appId: String): List<ModuleResp> =
        moduleDao
            .ktQuery()
            .eq(Module::appId, appId)
            .orderByDesc(Module::moduleCode)
            .list()
            .map { it.copyTo<ModuleResp>() }
            .listAndSetChildren()

    /**
     * 列表
     * @param [query] 应用程序id
     * @return [List]<[ModuleResp]>
     * @author tangli
     * @date 2024/07/08 10:40
     * @since 1.0.0
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
