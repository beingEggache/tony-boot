@file:Suppress("unused")

package com.tony.aliyun.oss

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.model.ObjectMetadata
import java.io.InputStream

class AliYunOssManager(
    private val accessKeyId: String,
    private val accessKeySecret: String,
    private val bucketName: String,
    private val endPoint: String
) {

    private val _reg: Regex = Regex("^[/\\\\]")

    private val ossClient: OSS by lazy {
        OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret)
    }

    fun upload(path: String, name: String, inputStream: InputStream, metadata: ObjectMetadata? = null) =
        ossClient.run {
            val trimPath = _reg.replaceFirst(path, "")
            putObject(bucketName, "$trimPath/$name", inputStream, metadata)
            "https://$bucketName.$endPoint/$trimPath/$name"
        }
}
