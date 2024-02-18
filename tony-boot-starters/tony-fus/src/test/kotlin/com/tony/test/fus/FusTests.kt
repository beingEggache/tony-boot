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

package com.tony.test.fus

import com.tony.fus.Fus
import com.tony.utils.string
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
@SpringBootTest(classes = [TestFusApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
abstract class FusTests {

    lateinit var processId: String

    abstract val processJson: String

    protected val testOperator1Id = "test001"
    protected val testOperator1Name = "测试001"
    protected val testOperator2Id = "test002"
    protected val testOperator2Name = "测试002"
    protected val testOperator3Id = "test003"
    protected val testOperator3Name = "测试003"

    @BeforeEach
    fun before() {
        val processModelJson = getProcessModelJson()
        processId = Fus.processService.deploy(processModelJson, false)
    }

    protected fun getProcessModelJson(processJson: String = this.processJson) = PathMatchingResourcePatternResolver()
        .getResource(processJson)
        .inputStream
        .readAllBytes()
        .string()

}
