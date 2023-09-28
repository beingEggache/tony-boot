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

package com.tony.wechat.xml

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter

@XStreamAlias("item")
public data class WechatArticleItem(
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Title")
    var title: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Description")
    var description: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("PicUrl")
    var picUrl: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Url")
    var url: String,
)

public data class ScanCodeInfo(
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ScanType")
    var scanType: String,

    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ScanResult")
    var scanResult: String,
)

public interface WechatObj {
    public var event: String?
    public var msgType: String
}

public enum class Event {

    CLICK, LOCATION, SCAN, SUBSCRIBE, UNSUBSCRIBE, VIEW;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}

public enum class MsgType {

    EVENT, IMAGE, LINK, LOCATION, MUSIC, NEWS, SHORTVIDEO, TEXT, VIDEO, VOICE;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}
