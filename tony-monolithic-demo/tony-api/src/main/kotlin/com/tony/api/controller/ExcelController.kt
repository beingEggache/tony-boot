package com.tony.api.controller

import com.alibaba.excel.EasyExcel
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.dto.resp.ExcelResp
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
 * @since 2023/08/01 10:09
 */
@RestController
@Validated
class ExcelController {

    @NoLoginCheck
    @Operation(summary = "excel")
    @PostMapping("/excel/export")
    fun export(): ResponseEntity<ByteArray> =
        ByteArrayOutputStream().use {
            EasyExcel
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
