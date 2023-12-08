package com.tony.mybatis

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.tony.ApiSession
import com.tony.utils.annotation
import com.tony.utils.hasAnnotation
import com.tony.utils.throwIfNull
import org.apache.ibatis.reflection.MetaObject

public abstract class DefaultMetaObjectHandler(
    override val apiSession: ApiSession,
) : DbMetaObjectHandler

/**
 * mybatis-plus 属性填充注解
 * @author Tang Li
 * @date 2023/12/08 14:26
 * @since 1.0.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MustBeDocumented
public annotation class MybatisPlusMetaProperty(
    val propertyType: MetaColumn,
)

public enum class MetaColumn {
    USER_ID,
    USER_NAME,
    ORG_ID,
    ORG_NAME,
    TENANT_ID,
}

internal interface DbMetaObjectHandler : MetaObjectHandler {
    val apiSession: ApiSession

    /**
     * 插入时填充数据，比如创建用户id,创建用户名字，租户id等
     *
     * @param metaObject 元对象
     */
    override fun insertFill(metaObject: MetaObject) {
        fill(metaObject, true)
    }

    /**
     * 更新时填充数据，比如更新时间、更新用户
     * 注意：必须保存完整对象才能更新对应数据
     *
     * @param metaObject 元对象
     */
    override fun updateFill(metaObject: MetaObject) {
        fill(metaObject, false)
    }

    private fun fill(
        metaObject: MetaObject,
        withInsertFill: Boolean,
    ) {
        val fieldInfoList =
            findTableInfo(metaObject)
                .fieldList
                .filter {
                    (if (withInsertFill) it.isWithInsertFill else it.isWithUpdateFill) &&
                        it.field.hasAnnotation(MybatisPlusMetaProperty::class.java)
                }

        fieldInfoList
            .forEach {
                val metaProperty = it.field.annotation(MybatisPlusMetaProperty::class.java).throwIfNull()
                val metaValue =
                    when (metaProperty.propertyType) {
                        MetaColumn.USER_ID -> apiSession.userId
                        MetaColumn.USER_NAME -> apiSession.userName
                        MetaColumn.ORG_ID -> apiSession.orgId
                        MetaColumn.ORG_NAME -> apiSession.orgName
                        MetaColumn.TENANT_ID -> apiSession.tenantId
                    }

                if (!metaValue.isNullOrBlank()) {
                    metaObject.setValue(it.field.name, metaValue)
                }
            }
    }
}
