package com.tony.api

import com.tony.core.PageResult
import com.tony.core.utils.uuid
import com.tony.webcore.WebContext
import com.tony.webcore.auth.ApiSession
import com.tony.webcore.auth.NoLoginCheck
import com.tony.webcore.jackson.MaskConverter
import com.tony.webcore.jackson.MobileMaskFun
import com.tony.webcore.jackson.NameMaskFun
import net.sourceforge.tess4j.Tesseract
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 *
 * @author tangli
 * @since 2020-11-05 9:08
 */
@RestController
class IndexController(
    private val apiSession: ApiSession
) {

    @NoLoginCheck
    @GetMapping("/string")
    fun string() = ""

    @NoLoginCheck
    @GetMapping("/int")
    fun int() = 1

    @NoLoginCheck
    @GetMapping("/boolean")
    fun boolean() = false

    @NoLoginCheck
    @GetMapping("/char")
    fun char() = 'a'

    @NoLoginCheck
    @GetMapping("/enum")
    fun enum() = Gender.MALE

    @NoLoginCheck
    @GetMapping("/array")
    fun array() = arrayOf(1)

    @NoLoginCheck
    @GetMapping("/intArray")
    fun intArray() = IntArray(1) { 1 }

    @NoLoginCheck
    @GetMapping("/list")
    fun list() = listOf(1)

    @NoLoginCheck
    @GetMapping("/empty-list")
    fun emptyList() = listOf<Any>()

    @NoLoginCheck
    @GetMapping("/empty-array")
    fun emptyArray() = arrayOf<Any>()

    @NoLoginCheck
    @GetMapping("/obj")
    fun obj(): Person? = null

    @NoLoginCheck
    @GetMapping("/date")
    fun date() = Date()

    @NoLoginCheck
    @GetMapping("/local-date")
    fun localDate() = LocalDate.now()

    @NoLoginCheck
    @GetMapping("/local-date-time")
    fun localDateTime() = LocalDateTime.now()

    @NoLoginCheck
    @GetMapping("/page-result")
    fun pageResult() = PageResult<Int>(IntArray(1) { 1 }, 1, 1, 1, 1, false)

    @NoLoginCheck
    @PostMapping("/test-post-param")
    fun testPostParam(@RequestBody person: Person) = 0

    @NoLoginCheck
    @GetMapping("/test-param")
    fun testGetParam(@RequestParam int: Int) = 0

    @NoLoginCheck
    @GetMapping("/login")
    fun login(userId: String?) = apiSession.genTokenString("userId" to "tony")

    @GetMapping("/user-id")
    fun userId() = WebContext.userId

    @GetMapping("/person")
    fun person() = Person(Gender.MALE, "aa", "123")

    @PostMapping("/file")
    fun file(multipartFile: MultipartFile): String? {
        val tesseract = Tesseract()
        val tempFile = File.createTempFile(
            "temp${uuid()}",
            ".${getSuffix(multipartFile.originalFilename)}"
        )
        multipartFile.transferTo(tempFile)
        val ocr = tesseract.doOCR(
            tempFile
        )
        return ocr
    }
}

fun getSuffix(originalFilename: String?) = originalFilename?.split('.')?.last()

enum class Gender {
    MALE
}

data class Person(
    val gender: Gender,
    @MaskConverter(NameMaskFun::class)
    val name: String,
    @MaskConverter(MobileMaskFun::class)
    val mobile: String
)
