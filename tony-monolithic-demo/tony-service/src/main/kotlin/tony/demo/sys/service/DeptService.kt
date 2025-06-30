package tony.demo.sys.service

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum
import com.github.houbb.pinyin.util.PinyinHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tony.demo.sys.dao.DeptDao
import tony.demo.sys.dto.req.DeptAddReq
import tony.demo.sys.dto.req.DeptDeleteReq
import tony.demo.sys.dto.req.DeptQuery
import tony.demo.sys.dto.req.DeptUpdateReq
import tony.demo.sys.dto.resp.DeptResp
import tony.demo.sys.po.Dept
import tony.demo.trait.listAndSetChildren
import tony.utils.copyTo
import tony.utils.genRandomInt

/**
 * 角色Service
 * @author tangli
 * @date 2024/07/03 13:14
 */
@Service
class DeptService(
    private val dao: DeptDao,
) {
    /**
     * 部门树
     * @param [req] 请求
     * @return [List]<[DeptResp]>
     * @author tangli
     * @date 2024/07/04 14:38
     */
    fun tree(req: DeptQuery): List<DeptResp> =
        dao
            .ktQuery()
            .like(req.deptName.isNotBlank(), Dept::deptName, req.deptName)
            .eq(req.enabled != null, Dept::enabled, req.enabled)
            .notIn(req.excludeDeptIds.isNotEmpty(), Dept::deptId, req.excludeDeptIds)
            .eq(Dept::tenantId, req.tenantId)
            .orderByDesc(Dept::sort)
            .list()
            .map {
                it.copyTo<DeptResp>()
            }.listAndSetChildren()

    /**
     * 新增
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/04 14:39
     */
    fun add(req: DeptAddReq) {
        val name = req.deptName
        val parentId = req.parentDeptId
        dao
            .ktQuery()
            .eq(Dept::parentDeptId, parentId)
            .eq(Dept::deptName, name)
            .eq(Dept::tenantId, req.tenantId)
            .throwIfExists("已有同名数据")

        val parentCodeSeq =
            if (parentId.isNotBlank()) {
                dao
                    .ktQuery()
                    .eq(Dept::deptId, parentId)
                    .select(Dept::deptCodeSeq)
                    .oneObjNotNull<String>("上级不存在") + "-"
            } else {
                ""
            }

        val code =
            PinyinHelper.toPinyin(name, PinyinStyleEnum.FIRST_LETTER, "").uppercase() + "${genRandomInt(4)}"
        val po =
            req
                .copyTo<Dept>()
                .apply {
                    this.deptCode = code
                    this.deptCodeSeq = parentCodeSeq + code
                }
        dao.insert(po)
    }

    /**
     * 更新
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/04 14:39
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun update(req: DeptUpdateReq) {
        val id = req.deptId
        val tenantId = req.tenantId
        val po =
            dao
                .ktQuery()
                .eq(Dept::deptId, id)
                .eq(Dept::tenantId, tenantId)
                .oneNotNull()

        val name = req.deptName
        val parentId = req.parentDeptId
        dao
            .ktQuery()
            .eq(Dept::parentDeptId, parentId)
            .ne(Dept::deptId, id)
            .eq(Dept::deptName, name)
            .eq(Dept::tenantId, tenantId)
            .throwIfExists("已有同名数据")

        val newCodeSeq =
            if (parentId.isNotBlank()) {
                dao
                    .ktQuery()
                    .eq(Dept::deptId, parentId)
                    .select(Dept::deptCodeSeq)
                    .oneObjNotNull<String>("上级不存在") + "-"
            } else {
                ""
            } + po.deptCode

        val children =
            dao
                .ktQuery()
                .likeRight(Dept::deptCodeSeq, po.deptCodeSeq)
                .eq(Dept::tenantId, tenantId)
                .list()
                .onEach {
                    it.deptCodeSeq = newCodeSeq + "-" + it.deptCode
                }
        dao.updateById(children)

        val updatedPo =
            req
                .copyTo<Dept>()
                .apply {
                    this.deptCodeSeq = newCodeSeq
                }
        dao.updateById(updatedPo)
    }

    /**
     * 删除
     * @param [req] 绿色
     * @author tangli
     * @date 2024/07/04 14:39
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun delete(req: DeptDeleteReq) {
        val id = req.deptId
        val tenantId = req.tenantId
        val po =
            dao
                .ktQuery()
                .eq(Dept::deptId, id)
                .eq(Dept::tenantId, tenantId)
                .oneNotNull()

        dao
            .ktQuery()
            .ne(Dept::deptId, id)
            .likeRight(Dept::deptCodeSeq, po.deptCodeSeq)
            .eq(Dept::tenantId, tenantId)
            .throwIfExists("存在子节点, 不能删除")

        dao.deleteById(po)
        dao.deleteEmployeeDepts(id, tenantId)
    }
}
