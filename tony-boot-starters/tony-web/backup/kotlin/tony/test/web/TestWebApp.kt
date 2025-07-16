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

package tony.test.web

import com.fasterxml.jackson.databind.BeanProperty
import tony.core.annotation.EnableTonyBoot
import tony.core.jackson.InjectableValueSupplier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableTonyBoot
@SpringBootApplication
class TestWebApp {

    @Bean
    fun stringInject(): InjectableValueSupplier =
        object : InjectableValueSupplier {
            var count: Int = 0
            override val name: String
                get() = "string0"

            override fun value(property: BeanProperty?, instance: Any?): String {
                println(property)
                println(instance)
                println(count++)
                return "string0"
            }
        }

    @Bean
    fun stringInject1(): InjectableValueSupplier =
        object : InjectableValueSupplier {
            var count: Int = 0
            override val name: String
                get() = "string1"

            override fun value(property: BeanProperty?, instance: Any?): String {
                println(property)
                println(instance)
                println(count++)
                return "string1"
            }
        }

    @Bean
    fun stringInject2(): InjectableValueSupplier =
        object : InjectableValueSupplier {
            var count: Int = 0
            override val name: String
                get() = "string2"

            override fun value(property: BeanProperty?, instance: Any?): String {
                println(property)
                println(instance)
                println(count++)
                return "string2"
            }
        }

    @Bean
    fun stringInject3(): InjectableValueSupplier =
        object : InjectableValueSupplier {
            var count: Int = 0
            override val name: String
                get() = "string3"

            override fun value(property: BeanProperty?, instance: Any?): String {
                println(property)
                println(instance)
                println(count++)
                return "string3"
            }
        }
}

fun main() {
    runApplication<TestWebApp>()
}
