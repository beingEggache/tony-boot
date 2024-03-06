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

/**
 * ByteArrayMultipartFile
 *
 * @author tangli
 * @date 2021/12/8 9:06
 */
package com.tony.feign.multipart

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import org.springframework.web.multipart.MultipartFile

/**
 * 二进制 MultipartFile.
 * 可用来 feign 上传二进制.
 * @author tangli
 * @date 2023/09/13 19:36
 * @since 1.0.0
 */
public class ByteArrayMultipartFile(
    private val originalFilename: String,
    private val bytes: ByteArray,
    private val name: String = "",
    private val contentType: String? = null,
) : MultipartFile {
    override fun isEmpty(): Boolean =
        bytes.isEmpty()

    override fun getSize(): Long =
        bytes
            .size
            .toLong()

    override fun getBytes(): ByteArray =
        bytes

    override fun getInputStream(): InputStream =
        ByteArrayInputStream(bytes)

    override fun getName(): String =
        name

    override fun getOriginalFilename(): String =
        originalFilename

    override fun getContentType(): String? =
        contentType

    override fun transferTo(destination: File) {
        FileOutputStream(destination).use {
            it.write(bytes)
        }
    }
}
