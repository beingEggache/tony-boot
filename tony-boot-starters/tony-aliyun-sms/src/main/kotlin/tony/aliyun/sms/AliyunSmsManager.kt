/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.aliyun.sms

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.http.MethodType
import com.aliyuncs.profile.DefaultProfile
import org.slf4j.LoggerFactory

/**
 * 阿里云短信 Manager
 * @author tangli
 * @date 2023/09/28 19:55
 */
public class AliyunSmsManager(
    private val accessKeyId: String,
    private val accessKeySecret: String,
    private val smsSignName: String,
    timeout: String,
) {
    init {
        System.setProperty("sun.net.client.defaultConnectTimeout", timeout)
        System.setProperty("sun.net.client.defaultReadTimeout", timeout)
    }

    private val acsClient: DefaultAcsClient by lazy {
        val profile =
            DefaultProfile.getProfile(
                REGION_ID,
                accessKeyId,
                accessKeySecret
            )

        DefaultProfile.addEndpoint(REGION_ID, PRODUCT, ENDPOINT)
        DefaultAcsClient(profile)
    }

    private val logger = LoggerFactory.getLogger(AliyunSmsManager::class.java)

    /**
     * 发送短信
     * @param [mobile] 手机号
     * @param [templateCode] 阿里云短信模板code
     * @param [templateParam] 模板参数
     * @return [SendSmsResponse]?
     * @author tangli
     * @date 2023/09/28 19:55
     */
    public fun send(
        mobile: String,
        templateCode: String,
        templateParam: String = "",
    ): SendSmsResponse? =
        try {
            acsClient
                .getAcsResponse(
                    SendSmsRequest()
                        .apply {
                            sysMethod = MethodType.POST
                            phoneNumbers = mobile
                            signName = smsSignName
                            this.templateCode = templateCode
                            this.templateParam = templateParam
                        }
                )
        } catch (e: ClientException) {
            logger.error("${e.errCode}:${e.errMsg}")
            null
        }

    internal companion object {
        private const val REGION_ID: String = "cn-hangzhou"
        private const val PRODUCT: String = "Dysmsapi"
        private const val ENDPOINT = "dysmsapi.aliyuncs.com"
    }
}
