package com.lx.water.platform.pojo.req

import com.lx.water.platform.pojo.DatePattern
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime
import java.util.Date
import javax.validation.constraints.Positive
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.RequestParam

/**
 *
 * @author tangli
 * @since 2020-11-14 13:24
 */
@ApiModel("分页对象")
data class PageReq(
    @ApiModelProperty("页码")
    @get:Positive(message = "页码请输入正整数")
    @RequestParam(required = false, defaultValue = "1")
    val page: Long = 1,
    @ApiModelProperty("每页数据量")
    @get:Positive(message = "每页数据量请输入正整数")
    @RequestParam(required = false, defaultValue = "10")
    val size: Long = 10
)

@ApiModel("时间段和关键字模糊查询")
data class TimePeriodAndKeywordQuery(

    @ApiModelProperty("开始时间", notes = "2020-11-01")
    @get:DateTimeFormat(pattern = DatePattern.yyyyMMdd)
    @RequestParam(required = false)
    val startTime: Date?,

    @ApiModelProperty("结束时间", notes = "2020-12-01")
    @DateTimeFormat(pattern = DatePattern.yyyyMMdd)
    @RequestParam(required = false)
    val endTime: Date?,

    @ApiModelProperty(name = "query")
    @RequestParam(required = false)
    val query: String?
)
