package tony.demo.sys.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tony.PageQueryLike
import tony.PageResult
import tony.demo.sys.dao.DictDao
import tony.demo.sys.dao.DictTypeDao
import tony.demo.sys.dto.req.DictAddReq
import tony.demo.sys.dto.req.DictDeleteReq
import tony.demo.sys.dto.req.DictQuery
import tony.demo.sys.dto.req.DictTypeAddReq
import tony.demo.sys.dto.req.DictTypeDeleteReq
import tony.demo.sys.dto.req.DictTypeQuery
import tony.demo.sys.dto.req.DictTypeUpdateReq
import tony.demo.sys.dto.req.DictUpdateReq
import tony.demo.sys.dto.resp.DictResp
import tony.demo.sys.dto.resp.DictTypeResp
import tony.demo.sys.dto.resp.DictValuesMapResp
import tony.demo.sys.po.Dict
import tony.demo.sys.po.DictType
import tony.demo.trait.listAndSetChildren
import tony.utils.copyTo
import tony.utils.throwIfTrue

/**
 * 字典Service
 * @author tangli
 * @date 2024/07/26 11:36
 */
@Service
class DictService(
    private val dictDao: DictDao,
    private val dictTypeDao: DictTypeDao,
) {
    /**
     * 新增字典
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/29 15:38
     * @since 1.0.0
     */
    fun addDict(req: DictAddReq) {
        val po = req.copyTo<Dict>()
        dictDao
            .ktQuery()
            .eq(Dict::dictTypeId, req.dictTypeId)
            .eq(Dict::dictName, req.dictName)
            .throwIfExists("已有同名数据")
        dictDao
            .ktQuery()
            .eq(Dict::dictTypeId, req.dictTypeId)
            .eq(Dict::dictCode, req.dictCode)
            .throwIfExists("已有同编码数据")
        dictDao
            .ktQuery()
            .eq(Dict::dictTypeId, req.dictTypeId)
            .eq(Dict::dictValue, req.dictValue)
            .throwIfExists("已有同值数据")
        dictDao.insert(po)
    }

    /**
     * 更新字典
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/29 15:38
     * @since 1.0.0
     */
    fun updateDict(req: DictUpdateReq) {
        val po = req.copyTo<Dict>()
        val id = req.dictId
        val typeId = req.dictTypeId
        dictDao
            .ktQuery()
            .ne(Dict::dictId, id)
            .eq(Dict::dictTypeId, typeId)
            .eq(Dict::dictName, req.dictName)
            .throwIfExists("已有同名数据")
        dictDao
            .ktQuery()
            .ne(Dict::dictId, id)
            .eq(Dict::dictTypeId, typeId)
            .eq(Dict::dictCode, req.dictCode)
            .throwIfExists("已有同编码数据")
        dictDao.updateById(po)
    }

    /**
     * 删除字典
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/29 15:38
     * @since 1.0.0
     */
    fun deleteDict(req: DictDeleteReq) {
        val id = req.dictId
        val po = dictDao.selectByIdNotNull(id)
        po.buildIn.throwIfTrue("内建字典不可删除")
        dictDao.deleteById(po)
    }

    /**
     * 字典列表
     * @param [req] 请求
     * @return [PageResult]<[DictResp]>
     * @author tangli
     * @date 2024/07/29 15:38
     * @since 1.0.0
     */
    fun dictList(req: PageQueryLike<DictQuery>): PageResult<DictResp> =
        dictDao
            .ktQuery()
            .eq(req.query.dictTypeId.isNotBlank(), Dict::dictTypeId, req.query.dictTypeId)
            .like(req.query.dictName.isNotBlank(), Dict::dictName, req.query.dictName)
            .pageResult(req)
            .map { it.copyTo<DictResp>() }

    /**
     * 字典映射.
     * @param [appId] 应用程序id
     * @return [Map]<[String], [DictResp]>
     * @author tangli
     * @date 2024/07/29 15:39
     * @since 1.0.0
     */
    fun dictValuesMap(appId: String): DictValuesMapResp {
        val dictTypes =
            dictTypeDao
                .ktQuery()
                .select(DictType::dictTypeCode, DictType::dictTypeId)
                .and {
                    it.eq(DictType::buildIn, true).or().eq(DictType::appId, appId)
                }.list()

        if (dictTypes.isEmpty()) {
            return DictValuesMapResp(
                mapOf()
            )
        }

        val dicts =
            dictDao
                .ktQuery()
                .`in`(Dict::dictTypeId, dictTypes.map { it.dictTypeId })
                .list()
                .map { it.copyTo<DictResp>() }

        val dictValues =
            dicts
                .fold(mutableMapOf<String, MutableMap<String, DictResp>>()) { acc, dict ->
                    dictTypes
                        .firstOrNull { dictType -> dict.dictTypeId == dictType.dictTypeId }
                        ?.dictTypeCode
                        .takeIf {
                            it?.isNotEmpty() == true
                        }?.let { dictTypeCode ->
                            acc.getOrPut(dictTypeCode) { mutableMapOf() }.apply { put(dict.dictValue, dict) }
                        }
                    acc
                }

        return DictValuesMapResp(
            dictValues
        )
    }

    /**
     * 树
     * @param [req] 请求
     * @return [List]<[DictTypeResp]>
     * @author tangli
     * @date 2024/07/26 13:07
     * @since 1.0.0
     */
    fun dictTypeTree(req: DictTypeQuery): List<DictTypeResp> =
        dictTypeDao
            .ktQuery()
            .like(req.dictTypeName.isNotBlank(), DictType::dictTypeName, req.dictTypeName)
            .notIn(req.excludeDictTypeIds.isNotEmpty(), DictType::dictTypeId, req.excludeDictTypeIds)
            .orderByDesc(DictType::sort)
            .list()
            .map {
                it.copyTo<DictTypeResp>()
            }.listAndSetChildren()

    /**
     * 新增字典类型
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/26 13:08
     * @since 1.0.0
     */
    fun addDictType(req: DictTypeAddReq) {
        val name = req.dictTypeName
        val code = req.dictTypeCode
        val parentId = req.parentDictTypeId
        dictTypeDao
            .ktQuery()
            .eq(DictType::parentDictTypeId, parentId)
            .eq(DictType::dictTypeName, name)
            .and {
                it.eq(DictType::buildIn, true).or().eq(DictType::appId, req.appId)
            }.throwIfExists("已有同名数据")

        dictTypeDao
            .ktQuery()
            .eq(DictType::dictTypeCode, code)
            .and {
                it.eq(DictType::buildIn, true).or().eq(DictType::appId, req.appId)
            }.throwIfExists("已有同编码数据")

        val parentCodeSeq =
            if (parentId.isNotBlank()) {
                dictTypeDao
                    .ktQuery()
                    .eq(DictType::dictTypeId, parentId)
                    .select(DictType::dictTypeCodeSeq)
                    .oneObjNotNull<String>("上级不存在") + "-"
            } else {
                ""
            }
        val po =
            req
                .copyTo<DictType>()
                .apply {
                    this.dictTypeCodeSeq = parentCodeSeq + code
                }
        dictTypeDao.insert(po)
    }

    /**
     * 更新字典类型
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/26 13:08
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun updateDictType(req: DictTypeUpdateReq) {
        val id = req.dictTypeId
        val po =
            dictTypeDao
                .ktQuery()
                .eq(DictType::dictTypeId, id)
                .eq(DictType::buildIn, false)
                .oneNotNull()

        val name = req.dictTypeName
        val parentId = req.parentDictTypeId
        dictTypeDao
            .ktQuery()
            .eq(DictType::parentDictTypeId, parentId)
            .ne(DictType::dictTypeId, id)
            .eq(DictType::dictTypeName, name)
            .and {
                it
                    .eq(DictType::buildIn, true)
                    .or()
                    .eq(DictType::appId, req.appId)
            }.throwIfExists("已有同名数据")

        val newCodeSeq =
            if (parentId.isNotBlank()) {
                dictTypeDao
                    .ktQuery()
                    .eq(DictType::dictTypeId, parentId)
                    .select(DictType::dictTypeCodeSeq)
                    .oneObjNotNull<String>("上级不存在") + "-"
            } else {
                ""
            } + po.dictTypeCode

        val children =
            dictTypeDao
                .ktQuery()
                .likeRight(DictType::dictTypeCodeSeq, po.dictTypeCodeSeq)
                .list()
                .onEach {
                    it.dictTypeCodeSeq = newCodeSeq + "-" + it.dictTypeCodeSeq
                }
        dictTypeDao.updateById(children)
        val updatedPo =
            req
                .copyTo<DictType>()
                .apply {
                    this.dictTypeCodeSeq = newCodeSeq
                }
        dictTypeDao.updateById(updatedPo)
    }

    /**
     * 删除
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/04 14:39
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun delete(req: DictTypeDeleteReq) {
        val id = req.dictTypeId
        val po =
            dictTypeDao
                .ktQuery()
                .eq(DictType::dictTypeId, id)
                .oneNotNull()
        po.buildIn.throwIfTrue("内建不可删除")

        dictTypeDao
            .ktQuery()
            .ne(DictType::dictTypeId, id)
            .likeRight(DictType::dictTypeCodeSeq, po.dictTypeCodeSeq)
            .throwIfExists("存在子节点, 不可删除")

        dictDao
            .ktQuery()
            .eq(Dict::dictTypeId, id)
            .throwIfExists("存在字典数据, 不可删除")

        dictTypeDao.deleteById(po)
    }
}
