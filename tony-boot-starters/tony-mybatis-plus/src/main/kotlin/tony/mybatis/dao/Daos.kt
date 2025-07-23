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

@file:JvmName("Daos")

/**
 * daoExtension
 *
 * @author tangli
 * @date 2022/07/13 19:10
 */

package tony.mybatis.dao

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap
import tony.core.utils.asToNotNull
import tony.core.utils.isTypesOrSubTypesOf
import tony.core.utils.rawClass

@get:JvmSynthetic
internal val ENTITY_CLASS_MAP = ConcurrentHashMap<Class<*>, Class<*>>()

/**
 * 实际类别
 * @author tangli
 * @date 2023/09/28 19:54
 */
@JvmSynthetic
internal fun <T : Any> BaseDao<T>.actualClass() =
    if (!Proxy.isProxyClass(this::class.java)) {
        this::class.java
    } else {
        this::class.java
            .genericInterfaces
            .first {
                it
                    .rawClass()
                    .isTypesOrSubTypesOf(BaseDao::class.java)
            }.rawClass()
    }

/**
 * 获取entityClass
 * @return [Class]<[T]>
 * @author tangli
 * @date 2023/09/28 19:53
 */
@JvmSynthetic
internal fun <T : Any> BaseDao<T>.getEntityClass(): Class<T> =
    ENTITY_CLASS_MAP
        .getOrPut(this::class.java) {
            actualClass().typeParamOfSuperInterface(BaseDao::class.java).rawClass()
        }.asToNotNull()

/**
 * 获取接口的泛型参数
 * @param [type] 类型
 * @param [index] 类型位置, 默认第一个
 * @return [Type]
 * @author tangli
 * @date 2023/09/13 19:28
 */
internal fun Class<*>.typeParamOfSuperInterface(
    type: Class<*>,
    index: Int = 0,
): Type {
    val genericInterfaces = this.genericInterfaces
    val matchedInterface =
        genericInterfaces.firstOrNull {
            it.rawClass().name == type.typeName
        } ?: throw IllegalStateException("$this does not implement the $type")
    check(matchedInterface !is Class<*>) { "${matchedInterface.typeName} constructed without actual type information" }
    return matchedInterface.asToNotNull<ParameterizedType>().actualTypeArguments[index]
}
