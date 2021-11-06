package com.tony.db.typehandler

import com.tony.enums.EnumValue
import com.tony.utils.asTo
import java.io.Serializable
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType

open class EnumValueTypeHandler<E, KEY>(private val enumClass: Class<E>) :
    BaseTypeHandler<Enum<E>>()
    where E : Enum<E>,
          KEY : Serializable,
          E : EnumValue<KEY> {

    private fun getValue(obj: Enum<E>) =
        obj.asTo<EnumValue<KEY>>()?.value

    private fun valueOf(value: Any) =
        enumClass.enumConstants.firstOrNull { value == getValue(it) }

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: Enum<E>, jdbcType: JdbcType?) {
        if (jdbcType == null) {
            ps.setObject(i, getValue(parameter))
        } else { // see r3589
            ps.setObject(i, getValue(parameter), jdbcType.TYPE_CODE)
        }
    }

    override fun getNullableResult(rs: ResultSet, columnName: String?) =
        if (null == rs.getObject(columnName) && rs.wasNull()) {
            null
        } else valueOf(rs.getObject(columnName))

    override fun getNullableResult(rs: ResultSet, columnIndex: Int) =
        if (null == rs.getObject(columnIndex) && rs.wasNull()) {
            null
        } else valueOf(rs.getObject(columnIndex))

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int) =
        if (null == cs.getObject(columnIndex) && cs.wasNull()) {
            null
        } else valueOf(cs.getObject(columnIndex))
}
