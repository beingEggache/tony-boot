package tony.core.log

import dev.blaauwendraad.masker.json.JsonMasker
import dev.blaauwendraad.masker.json.config.JsonMaskingConfig
import org.springframework.util.unit.DataSize
import tony.core.utils.removeLineBreak

/**
 * 脱敏日志接口.
 *
 * 抽象一些共有方法.
 * @author tangli
 * @date 2025/08/05 14:22
 */
public interface DesensitizedLogger {
    /**
     * json 掩码器
     */
    public val jsonMasker: JsonMasker

    /**
     * 是否启用脱敏
     */
    public val enableDesensitized: Boolean

    /**
     * 获取脱敏字段
     * @return [Set]<[String]>
     * @author tangli
     * @date 2025/08/05 10:42
     */
    public val desensitizedFields: Set<String>

    /**
     * 获取脱敏请求头
     * @return [Set]<[String]>
     * @author tangli
     * @date 2025/08/05 10:42
     */
    public val desensitizedRequestHeaders: Set<String>

    /**
     * 获取脱敏响应标头
     * @return [Set]<[String]>
     * @author tangli
     * @date 2025/08/05 10:42
     */
    public val desensitizedResponseHeaders: Set<String>

    /**
     * 脱敏 标头
     * @param [headers] 头
     * @author tangli
     * @date 2025/08/05 14:28
     */
    public fun desensitizedHeaders(headers: Set<Map.Entry<String, String?>>): String =
        headers.joinToString(";;") {
            val headerName = it.key
            val headerValue = it.value
            val value =
                if (enableDesensitized && desensitizedRequestHeaders.contains(headerName)) {
                    headerValue?.replaceRange(0 until headerValue.length, "******")
                } else {
                    headerValue
                }
            "$headerName:$value"
        }

    /**
     * 请求/响应体
     * @param [contentByteArray] 内容字节数组
     * @param [contentType] 内容类型
     * @param [bodyMaxSize] 体最大尺寸
     * @param [isTextMediaTypes] 是否文本媒体类型
     * @param [isJsonMediaType] 是 JSON 媒体类型
     * @return [String]
     * @author tangli
     * @date 2025/08/05 14:35
     */
    public fun desensitizeBody(
        contentByteArray: ByteArray?,
        contentType: String?,
        bodyMaxSize: Long,
        isTextMediaTypes: Boolean,
        isJsonMediaType: Boolean,
    ): String =
        if (!isTextMediaTypes) {
            "[$contentType]"
        } else {
            contentByteArray?.let { bytes ->
                val size = bytes.size.toLong()
                when {
                    size in 1..bodyMaxSize &&
                        enableDesensitized &&
                        desensitizedFields.isNotEmpty() &&
                        isJsonMediaType -> String(jsonMasker.mask(bytes)).removeLineBreak()

                    size in 1..bodyMaxSize -> String(bytes).removeLineBreak()

                    size >= bodyMaxSize -> "[too long content, length = ${DataSize.ofBytes(size)}]"

                    else -> "[null]"
                }
            }
        } ?: "[null]"

    /**
     * json 掩码器
     * @return [JsonMasker]
     * @author tangli
     * @date 2025/08/05 14:34
     */
    public fun jsonMasker(): JsonMasker =
        if (enableDesensitized && desensitizedFields.isNotEmpty()) {
            JsonMasker.getMasker(
                JsonMaskingConfig
                    .builder()
                    .maskKeys(desensitizedFields)
                    .build()
            )
        } else {
            NoOpJsonMasker()
        }
}
