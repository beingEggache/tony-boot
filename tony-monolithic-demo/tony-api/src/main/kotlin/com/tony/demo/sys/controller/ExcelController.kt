package com.tony.demo.sys.controller

import cn.idev.excel.FastExcel
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.demo.permission.NoPermissionCheck
import com.tony.demo.sys.dto.resp.ExcelResp
import com.tony.web.utils.responseEntity
import io.swagger.v3.oas.annotations.Operation
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

/**
 * ExcelController is
 * @author tangli
 * @date 2023/08/01 19:09
 */
@RestController
@Validated
class ExcelController {
    @NoPermissionCheck
    @NoLoginCheck
    @Operation(summary = "excel")
    @PostMapping("/excel/export")
    fun export(): ResponseEntity<ByteArray> =
        ByteArrayOutputStream()
            .use {
                FastExcel
                    .write(it, ExcelResp::class.java)
                    .sheet(0)
                    .doWrite {
                        (1..10).map {
                            ExcelResp().apply {
                                name = "张$it"
                                age = it
                                sex = if (it % 2 == 1) "男" else "女"
                                birthDate = LocalDate.now()
                            }
                        }
                    }
                it.toByteArray()
            }.responseEntity("test.xlsx")
}
