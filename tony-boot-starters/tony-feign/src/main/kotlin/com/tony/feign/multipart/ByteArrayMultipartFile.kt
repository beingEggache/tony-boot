/**
 * ByteArrayMultipartFile
 *
 * @author Tang Li
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
 * @author Tang Li
 * @date 2023/09/13 10:36
 * @since 1.0.0
 */
public class ByteArrayMultipartFile(
    private val originalFilename: String,
    private val bytes: ByteArray,
    private val name: String = "",
    private val contentType: String? = null,
) : MultipartFile {

    override fun isEmpty(): Boolean = bytes.isEmpty()

    override fun getSize(): Long = bytes.size.toLong()

    override fun getBytes(): ByteArray = bytes

    override fun getInputStream(): InputStream = ByteArrayInputStream(bytes)

    override fun getName(): String = name

    override fun getOriginalFilename(): String = originalFilename

    override fun getContentType(): String? = contentType

    override fun transferTo(destination: File) {
        FileOutputStream(destination).use {
            it.write(bytes)
        }
    }
}
