package com.tony.dto.req

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.constraints.Positive

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
