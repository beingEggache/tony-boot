package com.tony.dto.resp

import com.alibaba.excel.annotation.ExcelProperty
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat

class ExcelResp {
    @field:ExcelProperty("姓名", index = 0)
    var name: String? = ""

    @field:ExcelProperty("年龄", index = 1)
    var age: Int? = null

    @field:ExcelProperty("性别", index = 2)
    var sex: String? = ""

    @field:DateTimeFormat(style = "yyyy-MM-dd")
    @field:ExcelProperty("出生日期", index = 3)
    var birthDate: LocalDate? = null
}
