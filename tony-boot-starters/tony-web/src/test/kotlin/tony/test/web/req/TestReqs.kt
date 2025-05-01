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

package tony.test.web.req

import tony.annotation.web.support.InjectEmptyIfNull
import tony.annotation.web.support.InjectRequestBodyField
import tony.codec.enums.Encoding
import tony.enums.validate.RangedIntEnum
import tony.enums.validate.RangedStringEnum
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class TestReq(
    @get:NotBlank(message = "请输入姓名")
    val name: String? = null,
    @get:NotNull(message = "请输入年龄")
    val age: Int? = null,
    @get:RangedIntEnum(enums = [1],"不合法的testIntNum")
    val testIntEnum: TestIntEnum? = null,
    @get:RangedStringEnum(enums = ["base64"], message = "不合法的testStringEnum")
    val testStringEnum: Encoding? = null
)

@Schema(description = "测试登录请求")
data class TestLoginReq(
    @param:Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @get:NotBlank(message = "请输入用户名")
    val name: String? = null
)

@Schema(description = "测试pattern验证")
data class TestPatternReq(
    @get:Pattern(regexp = "1[0-9]{10}", message = "手机号格式不正确")
    val mobile: String?
)

@Schema(description = "测试移除请求注入支持")
data class TestRemoveInjectTestReq(
    @field:InjectRequestBodyField("int")
    val string: String? = null
)

@Schema(description = "测试请求注入")
data class TestInjectReq<T1, T2, T3, T4>(

    @get:InjectEmptyIfNull
    override val string: String? = null,

    override var string1: String? = null,

    @field:InjectRequestBodyField
    val int: Int? = null,
    @field:InjectRequestBodyField("int")
    val int2: Int? = null,
    @field:InjectRequestBodyField("go fuck Yourself")
    val map: Map<String, String>? = null,
    @field:InjectRequestBodyField("list")
    val objList: List<TestLoginReq>? = null,
    @field:InjectRequestBodyField("list")
    val objMap: Map<String, TestLoginReq>? = null,
    val list: T1? = null,
    @field:InjectRequestBodyField("go fuck Yourself")
    val testTypeParam2: T2? = null,
    @field:InjectRequestBodyField("go fuck Yourself")
    val testTypeParam3: T3? = null,
    @field:InjectRequestBodyField("go fuck Yourself")
    val testTypeParam4: T4? = null,
) : TestInjectParent(), TestInjectInterface

abstract class TestInjectParent {

    @set:InjectRequestBodyField("string2")
    abstract var string1: String?

}

interface TestInjectInterface : TestInjectInterfaceParent {
    @get:InjectRequestBodyField("string1")
    override val string1: String?
}

interface TestInjectInterfaceParent {

    @get:InjectRequestBodyField
    val string: String?

    @get:InjectRequestBodyField("string")
    val string1: String?
}
