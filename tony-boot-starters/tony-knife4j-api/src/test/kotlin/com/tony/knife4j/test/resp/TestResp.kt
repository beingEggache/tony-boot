package com.tony.knife4j.test.resp

import com.alibaba.excel.annotation.ExcelProperty
import com.alibaba.excel.annotation.format.DateTimeFormat
import com.tony.utils.defaultZoneId
import com.tony.utils.defaultZoneOffset
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Schema(title = "测试响应标题", description = "测试响应描述")
data class TestResp(
    @Schema(title = "测试响应时间标题", description = "测试响应时间描述")
    val dateTime: LocalDateTime = LocalDateTime.now(),
    @Schema(title = "测试响应zoneId标题", description = "测试响应zoneId描述")
    val zoneId: ZoneId = defaultZoneId,
    @Schema(title = "测试响应zoneOffset标题", description = "测试响应zoneOffset描述")
    val zoneOffset: ZoneOffset = defaultZoneOffset,
    @Schema(title = "测试响应env标题", description = "测试响应env描述")
    val env: List<String> = System.getProperties().entries.map { "${it.key}=${it.value}" },
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
