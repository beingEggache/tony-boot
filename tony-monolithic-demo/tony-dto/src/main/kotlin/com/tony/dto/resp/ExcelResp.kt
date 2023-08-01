package com.tony.dto.resp

import com.alibaba.excel.annotation.ExcelProperty
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat

class ExcelResp {
    @ExcelProperty("姓名", index = 0)
    var name: String? = ""

    @ExcelProperty("年龄", index = 1)
    var age: Int? = null

    @ExcelProperty("性别", index = 2)
    var sex: String? = ""

    @field:DateTimeFormat(style = "yyyy-MM-dd")
    @ExcelProperty("出生日期", index = 3)
    var birthDate: LocalDate? = null
}
