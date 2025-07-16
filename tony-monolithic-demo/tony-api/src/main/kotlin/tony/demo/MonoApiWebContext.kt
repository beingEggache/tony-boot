package tony.demo

import tony.core.utils.ifNull
import tony.web.WebContext

/**
 * MonoApiWebContext is
 * @author tangli
 * @date 2024/07/11 09:06
 */
data object MonoApiWebContext {
    const val TENANT_ID_HEADER_NAME: String = "X-Tenant-ID"
    const val APP_ID_HEADER_NAME: String = "X-App-ID"
    val WebContext.tenantId: String
        get() {
            return request.getHeader(TENANT_ID_HEADER_NAME).ifNull("")
        }

    val WebContext.appId: String
        get() {
            return request.getHeader(APP_ID_HEADER_NAME).ifNull("")
        }
}
