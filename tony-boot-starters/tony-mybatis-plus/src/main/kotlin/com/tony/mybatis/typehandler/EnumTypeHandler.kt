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

    override fun setNonNullParameter(
        ps: PreparedStatement,
        i: Int,
        parameter: E,
        jdbcType: JdbcType?,
    ) {
        if (jdbcType == null) {
            ps.setObject(i, parameter.value)
        } else { // see r3589
            ps.setObject(i, parameter.value, jdbcType.TYPE_CODE)
        }
    }

    override fun getNullableResult(
        rs: ResultSet,
        columnName: String,
    ): E? =
        if (null == rs.getObject(columnName) && rs.wasNull()) {
            null
        } else {
            enumValueMap[rs.getObject(columnName)]
        }

    override fun getNullableResult(
        rs: ResultSet,
        columnIndex: Int,
    ): E? =
        if (null == rs.getObject(columnIndex) && rs.wasNull()) {
            null
        } else {
            enumValueMap[rs.getObject(columnIndex)]
        }

    override fun getNullableResult(
        cs: CallableStatement,
        columnIndex: Int,
    ): E? =
        if (null == cs.getObject(columnIndex) && cs.wasNull()) {
            null
        } else {
            enumValueMap[cs.getObject(columnIndex)]
        }
}
