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

package com.tony.wechat.xml.resp

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.tony.wechat.xml.WechatArticleItem
import com.tony.wechat.xml.WechatObj
import com.tony.wechat.xml.XStreamCDataConverter

@XStreamAlias("xml")
public data class WechatXmlResp(
    @XStreamAlias("ToUserName")
    @XStreamConverter(value = XStreamCDataConverter::class)
    var toUserName: String,
    @XStreamAlias("FromUserName")
    @XStreamConverter(value = XStreamCDataConverter::class)
    var fromUserName: String,
    @XStreamAlias("CreateTime")
    var createTime: Long,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("MsgType")
    override var msgType: String,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Event")
    override var event: String? = null,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Content")
    var content: String? = null,
    @XStreamAlias("MsgId")
    var msgId: Long? = null,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("MediaId")
    var mediaId: String? = null,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("EventKey")
    var eventKey: String? = null,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Ticket")
    var ticket: String? = null,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("Title")
    var title: String? = null,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("MusicURL")
    var musicURL: String? = null,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("HQMusicUrl")
    private var hQMusicUrl: String? = null,
    @XStreamConverter(value = XStreamCDataConverter::class)
    @XStreamAlias("ThumbMediaId")
    var thumbMediaId: String? = null,
    @XStreamAlias("ArticleCount")
    var articleCount: Int? = null,
    @XStreamAlias("Articles")
    var articles: List<WechatArticleItem>? = null,
) : WechatObj
