package com.tony.mybatis.sqlinjector.method

import com.baomidou.mybatisplus.core.enums.SqlMethod
import com.baomidou.mybatisplus.core.injector.AbstractMethod
import com.baomidou.mybatisplus.core.metadata.TableInfo
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils
import org.apache.ibatis.mapping.MappedStatement

/**
 * PhysicalDeleteById is
 * @author tangli
 * @date 2024/06/26 13:00
 * @since 1.0.0
 */
internal class PhysicalDeleteByIds : AbstractMethod("physicalDeleteByIds") {
    override fun injectMappedStatement(
        mapperClass: Class<*>,
        modelClass: Class<*>,
        tableInfo: TableInfo,
    ): MappedStatement =
        String
            .format(
                SqlMethod.DELETE_BY_IDS.sql,
                tableInfo.tableName,
                tableInfo.keyColumn,
                SqlScriptUtils.convertForeach(
                    SqlScriptUtils.convertChoose(
                        "@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(item.getClass())",
                        "#{item}",
                        "#{item." + tableInfo.keyProperty + "}"
                    ),
                    COLL,
                    null,
                    "item",
                    COMMA
                )
            ).let { sql ->
                addDeleteMappedStatement(
                    mapperClass,
                    methodName,
                    createSqlSource(configuration, sql, Any::class.java)
                )
            }
}
