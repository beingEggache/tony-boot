@file:JvmName("HttpUtils")

package com.tony.http

import com.tony.core.exception.ApiException
import com.tony.core.utils.defaultIfBlank
import com.tony.core.utils.jsonToObj
import com.tony.core.utils.toDeepLink
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.conn.socket.PlainConnectionSocketFactory
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicHeader
import java.io.InputStream
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private val client = HttpClients.custom().apply {

    val sslContext = SSLContext.getInstance("TLS").apply {
        val tm = object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) = Unit
            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) = Unit
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
        }
        init(null, arrayOf<TrustManager>(tm), null)
    }
    val socketFactory = SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier())
    setSSLSocketFactory(socketFactory)
    val socketFactoryRegistry = RegistryBuilder.create<ConnectionSocketFactory>()
        .register("http", PlainConnectionSocketFactory.INSTANCE)
        .register("https", socketFactory).build()
    val connManager = PoolingHttpClientConnectionManager(socketFactoryRegistry)
    setConnectionManager(connManager)
}.build()

private fun httpExecute(request: HttpUriRequest, headers: List<Pair<String, String>>? = null) =
    try {
        headers?.apply {
            request.setHeaders(headers.map { BasicHeader(it.first, it.second) }.toTypedArray())
        }
        client.execute(request)
    } catch (e: Exception) {
        throw ApiException(e.message.defaultIfBlank(), e)
    }

@JvmOverloads
fun String.httpGet(
    params: Map<String, Any?>? = null,
    headers: List<Pair<String, String>>? = null
): HttpResponseWrapper {
    val query = params.let {
        "?".takeUnless { contains("?") }.orEmpty() + it.toDeepLink()
    }
    return HttpResponseWrapper(
        httpExecute(HttpGet("$this$query"), headers)
    )
}

@JvmOverloads
fun String.httpPost(body: String? = null, headers: List<Pair<String, String>>? = null) =
    HttpResponseWrapper(
        httpExecute(
            HttpPost(this).apply {
                if (!body.isNullOrBlank()) {
                    entity = StringEntity(body, Charsets.UTF_8)
                }
            },
            headers
        )
    )

inline fun <R> CloseableHttpResponse.doWithContent(crossinline action: (InputStream) -> R): R =
    use { r -> r.entity.content.use(action) }

class HttpResponseWrapper(val response: CloseableHttpResponse) {

    inline fun <reified T> json() =
        response.doWithContent { it.jsonToObj<T>() }

    fun string() = response.doWithContent { String(it.readBytes()) }
}
