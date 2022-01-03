package com.tony.web.log

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import com.tony.utils.defaultIfBlank
import java.net.InetAddress

class LogIpConversion : ClassicConverter() {

    private val ipFromNacosKey = "IP_FROM_NACOS"

    private fun getIp(propertyMap: Map<String, String>): String =
        propertyMap[ipFromNacosKey].defaultIfBlank(InetAddress.getLocalHost().hostAddress ?: "[null]")

    override fun convert(event: ILoggingEvent): String =
        getIp(event.loggerContextVO.propertyMap)
}
