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
internal class PhysicalDelete : AbstractMethod("physicalDelete") {
    override fun injectMappedStatement(
        mapperClass: Class<*>,
        modelClass: Class<*>,
        tableInfo: TableInfo,
    ): MappedStatement =
        String
            .format(
                SqlMethod.DELETE.sql,
                tableInfo.tableName,
                sqlWhereEntityWrapper(true, tableInfo),
                sqlComment()
            ).let { sql ->
                addDeleteMappedStatement(
                    mapperClass,
                    methodName,
                    createSqlSource(configuration, sql, modelClass)
                )
            }

    override fun sqlWhereEntityWrapper(
        newLine: Boolean,
        table: TableInfo,
    ): String {
        /*
         * Wrapper SQL
         */
        val sgEs = """<bind name="_sgEs_" value="ew.sqlSegment != null and ew.sqlSegment != ''"/>"""
        val andSqlSegment =
            SqlScriptUtils.convertIf(
                " AND \${$WRAPPER_SQLSEGMENT}",
                "_sgEs_ and $WRAPPER_NONEMPTYOFNORMAL",
                true
            )
        val lastSqlSegment =
            SqlScriptUtils.convertIf(
                " \${$WRAPPER_SQLSEGMENT}",
                "_sgEs_ and $WRAPPER_EMPTYOFNORMAL",
                true
            )

        /*
         * 普通 SQL 注入
         */
        var sqlScript = table.getAllSqlWhere(false, false, true, WRAPPER_ENTITY_DOT)
        sqlScript = SqlScriptUtils.convertIf(sqlScript, "$WRAPPER_ENTITY != null", true)
        sqlScript = "${SqlScriptUtils.convertWhere("$sqlScript$NEWLINE$andSqlSegment")}$NEWLINE$lastSqlSegment"
        sqlScript = SqlScriptUtils.convertIf("$sgEs$NEWLINE$sqlScript", "$WRAPPER != null", true)
        return sqlScript
    }
}
