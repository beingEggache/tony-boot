/**
 * EnumTypeHandler
 *
 * @author Tang Li
 * @date 2022/7/12 15:47
 */
package com.tony.mybatis.typehandler

import com.tony.enums.EnumValue
import java.io.Serializable
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType

/**
 * EnumTypeHandler
 * @author Tang Li
 * @date 2023/09/13 10:40
 * @since 1.0.0
 */
public open class EnumTypeHandler<E, KEY>(enumClass: Class<E>) :
    BaseTypeHandler<E>()
    where E : Enum<E>,
          KEY : Serializable,
          E : EnumValue<KEY> {

    private val enumValueMap = enumClass.enumConstants.associateBy { it.value }

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: E, jdbcType: JdbcType?) {
        if (jdbcType == null) {
            ps.setObject(i, parameter.value)
        } else { // see r3589
            ps.setObject(i, parameter.value, jdbcType.TYPE_CODE)
        }
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): E? =
        if (null == rs.getObject(columnName) && rs.wasNull()) {
            null
        } else {
            enumValueMap[rs.getObject(columnName)]
        }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): E? =
        if (null == rs.getObject(columnIndex) && rs.wasNull()) {
            null
        } else {
            enumValueMap[rs.getObject(columnIndex)]
        }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): E? =
        if (null == cs.getObject(columnIndex) && cs.wasNull()) {
            null
        } else {
            enumValueMap[cs.getObject(columnIndex)]
        }
}
