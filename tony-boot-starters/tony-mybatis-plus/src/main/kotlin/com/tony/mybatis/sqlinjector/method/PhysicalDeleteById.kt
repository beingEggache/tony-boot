package com.tony.mybatis.sqlinjector.method

import com.baomidou.mybatisplus.core.enums.SqlMethod
import com.baomidou.mybatisplus.core.injector.AbstractMethod
import com.baomidou.mybatisplus.core.metadata.TableInfo
import org.apache.ibatis.mapping.MappedStatement

/**
 * PhysicalDeleteById is
 * @author tangli
 * @date 2024/06/26 13:00
 * @since 1.0.0
 */
internal class PhysicalDeleteById : AbstractMethod("physicalDeleteById") {
    override fun injectMappedStatement(
        mapperClass: Class<*>,
        modelClass: Class<*>,
        tableInfo: TableInfo,
    ): MappedStatement {
        val sqlMethod = SqlMethod.DELETE_BY_ID
        val sql =
            String.format(
                sqlMethod.sql,
                tableInfo.tableName,
                tableInfo.keyColumn,
                tableInfo.keyProperty
            )
        return addDeleteMappedStatement(
            mapperClass,
            methodName,
            super.createSqlSource(configuration, sql, Any::class.java)
        )
    }
}
