package tony.web.utils

import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ServletsTest {
    @Nested
    @DisplayName("remoteIp 扩展属性测试")
    inner class RemoteIpTest {
        @Test
        fun `X-Real-IP 优先返回`() {
            val req = mockRequest(
                xRealIp = "1.1.1.1",
                xForwardedFor = "2.2.2.2",
                ip = "3.3.3.3",
                remoteAddr = "4.4.4.4"
            )
            assertEquals("1.1.1.1", req.remoteIp)
        }

        @Test
        fun `X-Forwarded-For 返回第一个有效IP`() {
            val req = mockRequest(
                xRealIp = "unknown",
                xForwardedFor = "2.2.2.2, 5.5.5.5",
                ip = "3.3.3.3",
                remoteAddr = "4.4.4.4"
            )
            assertEquals("2.2.2.2", req.remoteIp)
        }

        @Test
        fun `ip header 返回`() {
            val req = mockRequest(
                xRealIp = "",
                xForwardedFor = "",
                ip = "3.3.3.3",
                remoteAddr = "4.4.4.4"
            )
            assertEquals("3.3.3.3", req.remoteIp)
        }

        @Test
        fun `remoteAddr 多IP取第一个`() {
            val req = mockRequest(
                xRealIp = null,
                xForwardedFor = null,
                ip = null,
                remoteAddr = "6.6.6.6, 7.7.7.7"
            )
            assertEquals("6.6.6.6", req.remoteIp)
        }

        @Test
        fun `全部无效时返回 remoteAddr`() {
            val req = mockRequest(
                xRealIp = "",
                xForwardedFor = "",
                ip = "",
                remoteAddr = "8.8.8.8"
            )
            assertEquals("8.8.8.8", req.remoteIp)
        }
    }
}

private fun mockRequest(
    xRealIp: String? = null,
    xForwardedFor: String? = null,
    ip: String? = null,
    remoteAddr: String? = null
): HttpServletRequest {
    val req = Mockito.mock(HttpServletRequest::class.java)
    Mockito.`when`(req.getHeader("X-Real-IP")).thenReturn(xRealIp)
    Mockito.`when`(req.getHeader("X-Forwarded-For")).thenReturn(xForwardedFor)
    Mockito.`when`(req.getHeader("ip")).thenReturn(ip)
    Mockito.`when`(req.remoteAddr).thenReturn(remoteAddr)
    return req
}
