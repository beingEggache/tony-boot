package com.tony.aliyun.oss

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.model.ObjectMetadata
import com.tony.SpringContexts
import com.tony.aliyun.oss.config.AliyunOssProperties
import com.tony.utils.sanitizedPath
import java.io.InputStream

public object AliYunOssManager {

    private val aliyunOssProperties: AliyunOssProperties by SpringContexts.getBeanByLazy()

    private val ossClient: OSS by lazy {
        OSSClientBuilder().build(
            aliyunOssProperties.endpoint,
            aliyunOssProperties.accessKeyId,
            aliyunOssProperties.accessKeySecret
        )
    }

    @JvmStatic
    @JvmOverloads
    public fun putObject(
        path: String,
        name: String,
        inputStream: InputStream,
        metadata: ObjectMetadata? = null,
    ): String =
        ossClient.run {
            val sanitizedPath = sanitizedPath("$path/$name").removePrefix("/")
            putObject(aliyunOssProperties.bucketName, sanitizedPath, inputStream, metadata)
            "https://${aliyunOssProperties.bucketName}.${aliyunOssProperties.endpoint}/$sanitizedPath"
        }
}
