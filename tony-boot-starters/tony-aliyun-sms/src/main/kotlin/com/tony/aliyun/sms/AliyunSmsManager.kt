package com.tony.aliyun.sms

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.http.MethodType
import com.aliyuncs.profile.DefaultProfile
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct

public class AliyunSmsManager(
    private val accessKeyId: String,
    private val accessKeySecret: String,
    private val smsSignName: String,
    private val timeout: String,
) {

    private val acsClient: DefaultAcsClient by lazy {
        val profile = DefaultProfile.getProfile(
            regionId,
            accessKeyId,
            accessKeySecret,
        )

        DefaultProfile.addEndpoint(regionId, product, endpoint)
        DefaultAcsClient(profile)
    }

    private val logger = LoggerFactory.getLogger(AliyunSmsManager::class.java)

    @PostConstruct
    private fun initSmsClient() {
        System.setProperty("sun.net.client.defaultConnectTimeout", timeout)
        System.setProperty("sun.net.client.defaultReadTimeout", timeout)
    }

    /**
     * 发送短信
     * @param mobile 手机号
     * @param templateCode 阿里云短信模板code
     * @param templateParam 模板参数
     */
    public fun send(mobile: String, templateCode: String, templateParam: String = ""): SendSmsResponse? {
        return try {
            acsClient.getAcsResponse(
                SendSmsRequest().apply {
                    sysMethod = MethodType.POST
                    phoneNumbers = mobile
                    signName = smsSignName
                    this.templateCode = templateCode
                    this.templateParam = templateParam
                },
            )
        } catch (e: ClientException) {
            logger.error("${e.errCode}:${e.errMsg}")
            null
        }
    }

    internal companion object {
        private const val regionId: String = "cn-hangzhou"
        private const val product: String = "Dysmsapi"
        private const val endpoint = "dysmsapi.aliyuncs.com"
    }
}
