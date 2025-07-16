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

package tony.core.jackson

/**
 * Jackson 注入
 * @author tangli
 * @date 2023/09/28 19:16
 */
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.InjectableValues
import tony.core.utils.asToNotNull
import tony.core.utils.getLogger

/**
 * Jackson 注入.
 *
 * 没用.
 *
 * 对于 Kotlin 非空字段, 接受 absent, 不接受 null 值.
 *
 * {} 是可接受的, { "name": null } 是不可接受的.
 *
 * 对 Kotlin 不 友好, 建议只在 Java  使用, Kotlin 环境下, 建议只在 可变修饰的可空类型的 setter上使用.
 *
 * @author tangli
 * @date 2023/09/28 19:16
 */
public class InjectableValuesBySupplier(
    private val values: Map<String, InjectableValueSupplier>,
) : InjectableValues() {
    private val logger = getLogger()

    override fun findInjectableValue(
        valueId: Any,
        ctxt: DeserializationContext,
        property: BeanProperty,
        beanInstance: Any?,
    ): Any? {
        val key = valueId.asToNotNull<String>()
        val ob = values[key]?.value(property, beanInstance)
        if (!values.containsKey(key)) {
            logger.warn("""No injectable id with value '$key' found (for property '${property.name}')""")
        }
        return ob
    }
}

/**
 * Jackson 注入 Supplier.
 * @author tangli
 * @date 2023/09/13 19:17
 */
public interface InjectableValueSupplier {
    public val name: String

    public fun value(
        property: BeanProperty?,
        instance: Any?,
    ): Any
}

/**
 * 抽象Jackson 注入 Supplier.
 * @author tangli
 * @date 2023/09/13 19:18
 */
public abstract class AbstractInjectableValueSupplier(
    override val name: String,
) : InjectableValueSupplier {
    public constructor(type: Class<*>) : this(type.name)
}
