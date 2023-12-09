package com.tony.test.knife4j.resp

import com.alibaba.excel.annotation.ExcelProperty
import com.alibaba.excel.annotation.format.DateTimeFormat
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(title = "测试响应标题", description = "测试响应描述")
data class TestResp(
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "测试响应时间标题", description = "测试响应时间描述", pattern = "yyyy-MM-dd HH:mm:ss")
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
