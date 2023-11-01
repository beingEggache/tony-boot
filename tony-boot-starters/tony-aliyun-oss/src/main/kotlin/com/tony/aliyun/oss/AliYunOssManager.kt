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

package com.tony.aliyun.oss

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.model.ObjectMetadata
import com.tony.SpringContexts
import com.tony.aliyun.oss.config.AliyunOssProperties
import com.tony.utils.sanitizedPath
import java.io.InputStream

/**
 * 阿里云oss Manager
 * @author Tang Li
 * @date 2023/09/28 09:53
 * @since 1.0.0
 */
public object AliYunOssManager {
    private val aliyunOssProperties: AliyunOssProperties by SpringContexts.getBeanByLazy()

    private val ossClient: OSS by lazy {
        OSSClientBuilder().build(
            aliyunOssProperties.endpoint,
            aliyunOssProperties.accessKeyId,
            aliyunOssProperties.accessKeySecret
        )
    }

    /**
     * Uploads the file to the Bucket from the @{link InputStream} with the ObjectMetadata information
     * @param [path] 路径
     * @param [name] 名称
     * @param [inputStream] 输入流
     * @param [metadata] 元数据
     * @return [String]
     * @author Tang Li
     * @date 2023/09/28 09:53
     * @since 1.0.0
     */
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
