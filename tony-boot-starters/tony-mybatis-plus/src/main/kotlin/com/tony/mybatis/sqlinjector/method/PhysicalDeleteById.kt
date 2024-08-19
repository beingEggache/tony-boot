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
    ): MappedStatement =
        String
            .format(
                SqlMethod.DELETE_BY_ID.sql,
                tableInfo.tableName,
                tableInfo.keyColumn,
                tableInfo.keyProperty
            ).let { sql ->
                addDeleteMappedStatement(
                    mapperClass,
                    methodName,
                    createSqlSource(configuration, sql, Any::class.java)
                )
            }
}
