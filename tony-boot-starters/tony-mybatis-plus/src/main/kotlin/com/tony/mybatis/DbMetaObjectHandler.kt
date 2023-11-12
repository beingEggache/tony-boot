package com.tony.mybatis

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.tony.ApiSession
import com.tony.SpringContexts
import com.tony.mybatis.config.MyBatisPlusProperties
import com.tony.utils.applyIf
import org.apache.ibatis.reflection.MetaObject

public abstract class DefaultMetaObjectHandler(
    private val apiSession: ApiSession,
) : DbMetaObjectHandler {
    /**
     * 账户id
     *
     * @return 账户id
     */
    override val userId: Long?
        get() = apiSession.userId?.toLongOrNull()

    /**
     * 租户id
     *
     * @return 租户id
     */
    override val tenantId: Long?
        get() = apiSession.tenantId?.toLongOrNull()
}

internal interface DbMetaObjectHandler : MetaObjectHandler {
    /**
     * 插入时填充数据，比如创建用户id,创建用户名字，租户id等
     *
     * @param metaObject 元对象
     */
    override fun insertFill(metaObject: MetaObject) {
        applyIf(
            userId != null &&
                metaObject.hasSetter(fieldCreatorIdName)
        ) {
            strictInsertFill(
                metaObject,
                fieldCreatorIdName,
                Long::class.javaObjectType,
                userId
            )
        }
        applyIf(
            userName != null &&
                metaObject.hasSetter(fieldCreatorNameName)
        ) {
            strictInsertFill(
                metaObject,
                fieldCreatorNameName,
                String::class.java,
                userName
            )
        }
        applyIf(
            tenantId != null &&
                metaObject.hasSetter(fieldTenantIdName)
        ) {
            strictInsertFill(
                metaObject,
                fieldTenantIdName,
                Long::class.javaObjectType,
                tenantId
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
        applyIf(
            userId != null &&
                metaObject.hasSetter(fieldUpdatorIdName)
        ) {
            strictUpdateFill(metaObject, fieldUpdatorIdName, Long::class.javaObjectType, userId)
        }
        applyIf(
            userName != null &&
                metaObject.hasSetter(fieldUpdatorNameName)
        ) {
            strictInsertFill(
                metaObject,
                fieldUpdatorNameName,
                String::class.java,
                userName
            )
        }
    }

    /**
     * 账户id
     *
     * @return 账户id
     */
    val userId: Long?

    /**
     * 用户信息
     *
     * @return 用户信息
     */
    val userName: String?

    /**
     * 租户id
     *
     * @return 租户id
     */
    val tenantId: Long?

    private val fieldCreatorIdName
        get() = myBatisPlusProperties.fieldCreatorIdName
    private val fieldUpdatorIdName
        get() = myBatisPlusProperties.fieldUpdatorIdName
    private val fieldCreatorNameName
        get() = myBatisPlusProperties.fieldCreatorNameName
    private val fieldUpdatorNameName
        get() = myBatisPlusProperties.fieldUpdatorNameName
    private val fieldTenantIdName
        get() = myBatisPlusProperties.fieldTenantIdName
}

private val myBatisPlusProperties: MyBatisPlusProperties by SpringContexts.getBeanByLazy<MyBatisPlusProperties>()
