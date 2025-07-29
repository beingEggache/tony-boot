package tony.web.crypto

import tony.core.SpringContexts
import tony.core.crypto.CryptoProvider

/**
 * DefaultCryptoProvider is
 * @author tangli
 * @date 2025/07/29 11:10
 */
internal class DefaultCryptoProvider : CryptoProvider {
    override val secret = SpringContexts.Env.getRequiredProperty("web.crypto.secret")
}
