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

package tony.test.knife4j.resp

import cn.idev.excel.annotation.ExcelProperty
import cn.idev.excel.annotation.format.DateTimeFormat
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(title = "测试响应标题", description = "测试响应描述")
data class TestResp(
    @param:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @param:Schema(title = "测试响应时间标题", description = "测试响应时间描述", pattern = "yyyy-MM-dd HH:mm:ss")
    val dateTime: LocalDateTime = LocalDateTime.now(),
)

class TestExcelResp {
    @ExcelProperty("姓名", index = 0)
    var name: String? = ""

    @ExcelProperty("年龄", index = 1)
    var age: Int? = null

    @ExcelProperty("性别", index = 2)
    var sex: String? = ""

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("出生日期", index = 3)
    var birthDate: LocalDate? = null
}
