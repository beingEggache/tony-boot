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

package tony.mybatis.sqlinjector

import com.baomidou.mybatisplus.core.injector.AbstractMethod
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector
import com.baomidou.mybatisplus.core.metadata.TableInfo
import org.apache.ibatis.session.Configuration
import tony.mybatis.sqlinjector.method.PhysicalDelete
import tony.mybatis.sqlinjector.method.PhysicalDeleteById
import tony.mybatis.sqlinjector.method.PhysicalDeleteByIds

/**
 * 自定义方法注入
 * @author tangli
 * @date 2024/06/26 12:57
 * @since 1.0.0
 */
internal class TonySqlInjector : DefaultSqlInjector() {
    override fun getMethodList(
        configuration: Configuration,
        mapperClass: Class<*>,
        tableInfo: TableInfo,
    ): MutableList<AbstractMethod> =
        super
            .getMethodList(configuration, mapperClass, tableInfo)
            .apply {
                addAll(
                    mutableListOf(
                        PhysicalDelete(),
                        PhysicalDeleteById(),
                        PhysicalDeleteByIds()
                    )
                )
            }
}
