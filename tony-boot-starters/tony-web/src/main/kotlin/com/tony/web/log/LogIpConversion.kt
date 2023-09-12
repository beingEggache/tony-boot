package com.tony.web.log

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import com.tony.utils.defaultIfBlank
import com.tony.utils.localIp

/**
 * logback 获取本地ip
 *
 * @author Tang Li
 * @date 2023/5/25 11:01
 */
public class LogIpConversion : ClassicConverter() {

    private val ipFromNacosKey = "IP_FROM_NACOS"

    private fun getIp(propertyMap: Map<String, String>): String =
        propertyMap[ipFromNacosKey].defaultIfBlank(localIp)

    override fun convert(event: ILoggingEvent): String =
        getIp(event.loggerContextVO.propertyMap)
}
