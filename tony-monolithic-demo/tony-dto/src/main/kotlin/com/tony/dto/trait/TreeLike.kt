package com.tony.dto.trait

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.utils.defaultIfBlank

/**
 *
 * @author Tang Li
 * @date 2020-11-23 11:10
 */

private const val codePatternStr = "[a-zA-Z0-9一二三四五六七八九零ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩ]+"
private val ancestorPattern: Regex = Regex(codePatternStr)

fun <T : TreeLike<T>> List<T>.listAndSetChildren(): List<T> {
    val rootFundsItem = this
        .filter { it.isAncestor() }
    return rootFundsItem.onEach {
        it.findAndSetChildren(this)
    }
}

interface TreeLike<T : TreeLike<T>> {

    fun isMyChild(otherCode: String?) =
        Regex("^$code-$codePatternStr$").matches(otherCode.defaultIfBlank())

    @JsonIgnore
    fun isAncestor() = ancestorPattern.matches(code.defaultIfBlank())

    @get:JsonIgnore
    val code: String?

    @get:JsonIgnore
    val order: Int?

    var children: List<T>?

    fun findAndSetChildren(nodes: List<T>) {
        nodes.filter {
            isMyChild(it.code)
        }.sortedBy {
            it.order
        }.onEach {
            it.findAndSetChildren(nodes)
        }.let { children ->
            this.children = children
            this
        }
    }
}
