/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.mybatis

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.tony.ApiSession
import com.tony.utils.annotation
import com.tony.utils.asTo
import com.tony.utils.hasAnnotation
import com.tony.utils.notBlank
import com.tony.utils.throwIfNull
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.function.Function
import org.apache.ibatis.reflection.MetaObject
import org.slf4j.LoggerFactory

/**
 * mybatis-plus 属性填充注解
 * @author Tang Li
 * @date 2023/12/08 19:26
 * @since 1.0.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MustBeDocumented
public annotation class MybatisPlusMetaProperty(
    val propertyType: MetaColumn,
    val override: Boolean = false,
    val relativeProp: String = "",
)

public enum class MetaColumn {
    USER_ID,
    USER_NAME,
    ORG_ID,
    ORG_NAME,
    TENANT_ID,
}

/**
 * 元对象字段填充控制器抽接口，实现公共字段自动写入
 * @author Tang Li
 * @date 2023/12/14 19:44
 * @since 1.0.0
 */
public interface DbMetaObjectHandler : MetaObjectHandler {
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

    /**
     * 填充数据
     * @param [metaObject] 元对象
     * @param [withInsertFill] true 插入填充, false 更新填充,
     * @author Tang Li
     * @date 2023/12/14 19:43
     * @since 1.0.0
     */
    public fun fill(
        metaObject: MetaObject,
        withInsertFill: Boolean,
    )
}

/**
 * 默认元对象处理程序
 * @author Tang Li
 * @date 2023/12/14 19:51
 * @since 1.0.0
 */
public open class DefaultMetaObjectHandler(
    private val apiSession: ApiSession,
    private val metaPropProviders: Map<MetaColumn, Function<Any?, Any?>> = mapOf(),
) : DbMetaObjectHandler {
    private val logger = LoggerFactory.getLogger(DbMetaObjectHandler::class.java)
    private val metaPropertyMap: ConcurrentMap<Field, MybatisPlusMetaProperty> = ConcurrentHashMap()

    override fun fill(
        metaObject: MetaObject,
        withInsertFill: Boolean,
    ) {
        findTableInfo(metaObject)
            .fieldList
            .filter {
                (if (withInsertFill) it.isWithInsertFill else it.isWithUpdateFill) &&
                    it.field.hasAnnotation(MybatisPlusMetaProperty::class.java)
            }.forEach {
                val field = it.field
                val metaProperty =
                    metaPropertyMap.getOrPut(field) {
                        field.annotation(MybatisPlusMetaProperty::class.java).throwIfNull()
                    }
                val metaValue =
                    when (metaProperty.propertyType) {
                        MetaColumn.USER_ID -> apiSession.userId
                        MetaColumn.USER_NAME -> fromRelativePropOrDefault(metaProperty, metaObject, apiSession.userName)
                        MetaColumn.ORG_ID -> apiSession.orgId
                        MetaColumn.ORG_NAME -> fromRelativePropOrDefault(metaProperty, metaObject, apiSession.orgName)
                        MetaColumn.TENANT_ID -> apiSession.tenantId
                    }

                val fieldName = field.name
                val fieldValue = metaObject.getValue(fieldName).asTo<String>()
                if (
                    metaValue.notBlank() &&
                    (metaProperty.override || fieldValue.isNullOrBlank())
                ) {
                    metaObject.setValue(fieldName, metaValue)
                }
            }
    }

    private fun fromRelativePropOrDefault(
        metaProperty: MybatisPlusMetaProperty,
        metaObject: MetaObject,
        default: Any?,
    ): Any? =
        if (metaProperty.relativeProp.isBlank()) {
            default
        } else {
            val metaPropProvider = metaPropProviders[metaProperty.propertyType]
            if (metaPropProvider == null) {
                logger.warn("metaPropProvider with ${metaProperty.propertyType} is null.")
            }
            metaPropProvider?.apply(metaObject.getValue(metaProperty.relativeProp))
        }
}
