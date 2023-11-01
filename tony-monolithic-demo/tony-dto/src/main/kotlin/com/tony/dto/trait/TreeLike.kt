package com.tony.dto.trait

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.utils.defaultIfBlank

/**
 *
 * @author Tang Li
 * @date 2020-11-23 11:10
 */

private const val CODE_PATTERN_STR = "[a-zA-Z0-9一二三四五六七八九零ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩ]+"
private val ancestorPattern: Regex = Regex(CODE_PATTERN_STR)

fun <T : TreeLike<T>> List<T>.listAndSetChildren(): List<T> =
    filter {
        it.isAncestor()
    }.onEach {
        it.findAndSetChildren(this)
    }

interface TreeLike<T : TreeLike<T>> {
    fun isMyChild(otherCode: String?) =
        Regex("^$code-$CODE_PATTERN_STR$").matches(otherCode.defaultIfBlank())

    @JsonIgnore
    fun isAncestor() =
        ancestorPattern.matches(code.defaultIfBlank())

    @get:JsonIgnore
    val code: String?

    @get:JsonIgnore
    val order: Int?

    var children: List<T>?

    fun findAndSetChildren(nodes: List<T>) {
        nodes
            .filter {
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
