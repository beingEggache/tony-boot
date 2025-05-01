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

package tony.test.feign.module.unwrap.api

import tony.test.feign.dto.Person
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface FeignUnwrapTestApi {

    @PostMapping("/test/boolean")
    fun boolean(): Boolean

    @PostMapping("/test/booleanArray")
    fun booleanArray(): BooleanArray

    @PostMapping("/test/byte")
    fun byte(): Byte

    @PostMapping("/test/short")
    fun short(): Short

    @PostMapping("/test/shortArray")
    fun shortArray(): ShortArray

    @PostMapping("/test/int")
    fun int(): Int

    @PostMapping("/test/intArray")
    fun intArray(): IntArray

    @PostMapping("/test/long")
    fun long(): Long

    @PostMapping("/test/longArray")
    fun longArray(): LongArray

    @PostMapping("/test/float")
    fun float(): Float

    @PostMapping("/test/floatArray")
    fun floatArray(): FloatArray

    @PostMapping("/test/double")
    fun double(): Double

    @PostMapping("/test/doubleArray")
    fun doubleArray(): DoubleArray

    @PostMapping("/test/char")
    fun char(): Char

    @PostMapping("/test/charArray")
    fun charArray(): CharArray

    @PostMapping("/test/string")
    fun string(): String

    @PostMapping("/test/array")
    fun array(): Array<*>

    @PostMapping("/test/list")
    fun list(): List<*>

    @PostMapping("/test/test-json-unwrap1")
    fun map(@RequestBody person: Person): Map<String, *>

    @PostMapping("/test/test-json-unwrap2")
    fun person(@RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-validate")
    fun validate(@Validated @RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-exception")
    fun exception(@RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-biz-exception")
    fun bizException(@RequestBody person: Person): Person

    @PostMapping("/test/test-json-unwrap-api-exception")
    fun apiException(@RequestBody person: Person): Person
}
