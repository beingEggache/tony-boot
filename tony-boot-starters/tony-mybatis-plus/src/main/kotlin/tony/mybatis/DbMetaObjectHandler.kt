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

package tony.mybatis

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfo
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.function.Function
import org.apache.ibatis.reflection.MetaObject
import org.slf4j.LoggerFactory
import tony.ApiSession
import tony.core.utils.annotation
import tony.core.utils.asTo
import tony.core.utils.hasAnnotation
import tony.core.utils.notBlank
import tony.core.utils.throwIfNull

/**
 * mybatis-plus 属性填充注解
 * @author tangli
 * @date 2023/12/08 19:26
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
 * @author tangli
 * @date 2023/12/14 19:44
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
     * @author tangli
     * @date 2023/12/14 19:43
     */
    public fun fill(
        metaObject: MetaObject,
        withInsertFill: Boolean,
    )
}

/**
 * 默认元对象处理程序
 * @author tangli
 * @date 2023/12/14 19:51
 */
public open class DefaultMetaObjectHandler(
    private val apiSession: ApiSession,
    private val metaPropProviders: Map<MetaColumn, Function<in Any?, out Any?>> = mapOf(),
) : DbMetaObjectHandler {
    private val logger = LoggerFactory.getLogger(DbMetaObjectHandler::class.java)
    private val metaPropertyCache: ConcurrentMap<Field, MybatisPlusMetaProperty> = ConcurrentHashMap()
    private val tableFieldInfoCache: ConcurrentMap<TableInfo, List<TableFieldInfo>> = ConcurrentHashMap()

    override fun fill(
        metaObject: MetaObject,
        withInsertFill: Boolean,
    ) {
        val tableInfo = findTableInfo(metaObject)
        tableFieldInfoCache
            .getOrPut(tableInfo) {
                tableInfo
                    .fieldList
                    .filter { tableFieldInfo ->
                        (if (withInsertFill) tableFieldInfo.isWithInsertFill else tableFieldInfo.isWithUpdateFill) &&
                            tableFieldInfo.field.hasAnnotation(MybatisPlusMetaProperty::class.java)
                    }
            }.forEach { tableFieldInfo ->
                val field = tableFieldInfo.field
                val metaProperty =
                    metaPropertyCache.getOrPut(field) {
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
