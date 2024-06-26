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
internal class PhysicalDelete : AbstractMethod("physicalDelete") {
    override fun injectMappedStatement(
        mapperClass: Class<*>,
        modelClass: Class<*>,
        tableInfo: TableInfo,
    ): MappedStatement {
        val sqlMethod = SqlMethod.DELETE
        val sql =
            String.format(
                sqlMethod.sql,
                tableInfo.tableName,
                sqlWhereEntityWrapper(true, tableInfo),
                sqlComment()
            )
        val sqlSource = super.createSqlSource(configuration, sql, modelClass)
        return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource)
    }
}
