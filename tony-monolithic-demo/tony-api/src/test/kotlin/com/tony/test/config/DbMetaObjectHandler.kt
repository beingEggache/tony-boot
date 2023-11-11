package com.tony.test.config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.tony.exception.BizException
import org.apache.ibatis.reflection.MetaObject
import java.util.Objects

class DbMetaObjectHandler : MetaObjectHandler {
    /**
     * 插入时填充数据，比如创建用户id,创建用户名字，租户id等
     *
     * @param metaObject 元对象
     */
    override fun insertFill(metaObject: MetaObject) {
        if (!isWeb) {
            return
        }
        if (!metaObject.hasGetter(CREATOR_ID) ||
            Objects.nonNull(metaObject.getValue(CREATOR_ID)) ||
            !metaObject.hasGetter(TENANT_ID) ||
            Objects.nonNull(metaObject.getValue(TENANT_ID))
        ) {
            return
        }
        if (userId == null || tenantId == null) {
            throw BizException("userId or tenantId blank.")
        }
        strictInsertFill(
            metaObject,
            CREATOR_ID,
            Long::class.javaObjectType,
            userId
        )
        strictInsertFill(
            metaObject,
            TENANT_ID,
            Long::class.javaObjectType,
            tenantId
        )
        if (metaObject.hasGetter(CREATOR_NAME)) {
            strictInsertFill(
                metaObject,
                CREATOR_NAME,
                String::class.java,
                userName
            )
        }
    }

    /**
     * 更新时填充数据，比如更新时间、更新用户
     * 注意：必须保存完整对象才能更新对应数据
     *
     * @param metaObject 元对象
     */
    override fun updateFill(metaObject: MetaObject) {
        if (!isWeb) {
            strictUpdateFill(metaObject, UPDATOR_ID, Long::class.javaObjectType, 1L)
            strictUpdateFill(metaObject, UPDATOR_NAME, String::class.java, "SYSTEM")
            return
        }
        val userId = userId
        userId?.let {
            strictUpdateFill(metaObject, UPDATOR_ID, Long::class.javaObjectType, it)
            strictUpdateFill(metaObject, UPDATOR_NAME, String::class.java, userName)
        }
    }

    /**
     * 账户id
     *
     * @return 账户id
     */
    val userId: Long? = 1

    /**
     * 用户信息
     *
     * @return 用户信息
     */
    val userName: String = "aloha"

    /**
     * 租户id
     *
     * @return 租户id
     */
    val tenantId: Long? = 1

    /**
     * 是否web环境
     *
     * @return 是否web环境
     */
    val isWeb: Boolean = true

    companion object {
        const val CREATOR_ID = "creatorId"
        const val UPDATOR_ID = "updatorId"
        const val CREATOR_NAME = "creatorName"
        const val UPDATOR_NAME = "updatorName"
        const val TENANT_ID = "tenantId"
    }
}
