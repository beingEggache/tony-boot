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

@file:JvmName("WechatXmls")

package tony.wechat.xml

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.converters.basic.StringConverter
import com.thoughtworks.xstream.core.util.QuickWriter
import com.thoughtworks.xstream.io.naming.NameCoder
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter
import com.thoughtworks.xstream.io.xml.XppDriver
import java.io.Writer
import java.util.Collections
import java.util.WeakHashMap
import tony.utils.asToNotNull

private val xStreamMap = WeakHashMap<Class<*>, XStream>()
private val processedClz: MutableSet<Class<*>> = Collections.newSetFromMap(WeakHashMap())

internal class XStreamCDataConverter : StringConverter() {
    override fun toString(obj: Any?) =
        "<![CDATA[" + super.toString(obj) + "]]>"
}

internal class WechatDriver : XppDriver() {
    override fun createWriter(out: Writer) =
        WechatPrintWriter(out, nameCoder)
}

internal class WechatPrintWriter(
    writer: Writer,
    nameCoder: NameCoder,
) : PrettyPrintWriter(writer, nameCoder) {
    override fun encodeNode(name: String) =
        name

    override fun writeText(
        writer: QuickWriter,
        text: String,
    ) =
        if (
            (text.startsWith(PREFIX_CDATA) && text.endsWith(SUFFIX_CDATA)) ||
            text.startsWith(PREFIX_MEDIA_ID) &&
            text.endsWith(SUFFIX_MEDIA_ID)
        ) {
            writer.write(text)
        } else {
            super.writeText(writer, text)
        }

    companion object {
        private const val PREFIX_CDATA = "<![CDATA["
        private const val PREFIX_MEDIA_ID = "<MediaId>"
        private const val SUFFIX_CDATA = "]]>"
        private const val SUFFIX_MEDIA_ID = "</MediaId>"
    }
}

/**
 * 一个类对应一个XStream,就没有 Class Alias 冲突问题了
 */
public fun <T> xStream(clz: Class<T>): XStream =
    xStreamMap.getOrPut(clz) {
        XStream(WechatDriver()).apply {
            ignoreUnknownElements()
            if (clz !in processedClz) {
                processAnnotations(clz)
            }
            processedClz.add(clz)
        }
    }

/**
 * 针对微信的xml转换
 */
public inline fun <reified T> T?.toXmlString(): String =
    if (this == null) {
        ""
    } else {
        xStream(T::class.java).toXML(this)
    }

/**.
 * 针对微信的xml转换
 */
public inline fun <reified T> CharSequence.xmlToObj(): T =
    xStream(T::class.java).fromXML(this.toString()).asToNotNull()
