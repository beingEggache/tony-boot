@file:Suppress("unused")

package com.tony.aliyun.oss

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.model.ObjectMetadata
import com.tony.Beans
import com.tony.aliyun.oss.config.AliyunOssProperties
import com.tony.utils.sanitizedPath
import java.io.InputStream

object AliYunOssManager {

    private val aliyunOssProperties: AliyunOssProperties by Beans.getBeanByLazy()

    private val ossClient: OSS by lazy {
        OSSClientBuilder().build(
            aliyunOssProperties.endpoint,
            aliyunOssProperties.accessKeyId,
            aliyunOssProperties.accessKeySecret
        )
    }

    @JvmStatic
    @JvmOverloads
    fun upload(path: String, name: String, inputStream: InputStream, metadata: ObjectMetadata? = null) =
        ossClient.run {
            val sanitizedPath = sanitizedPath(path)
            putObject(aliyunOssProperties.bucketName, "$sanitizedPath/$name", inputStream, metadata)
            "https://${aliyunOssProperties.bucketName}.${aliyunOssProperties.endpoint}/$sanitizedPath/$name"
        }
}
