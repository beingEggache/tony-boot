@file:JvmName("Trees")

package com.tony.dto.trait

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.utils.ifNullOrBlank

/**
 *
 * @author tangli
 * @date 2020-11-23 11:10
 */

private const val CODE_PATTERN_STR = "[a-zA-Z0-9一二三四五六七八九零ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩ]+"
private val ancestorPattern: Regex = Regex(CODE_PATTERN_STR)

fun <T : ForestLike<T>> Collection<T>.listAndSetChildren(): List<T> =
    filter {
        it.isAncestor()
    }.onEach {
        it.findAndSetChildren(this)
    }

fun <T : TreeLike<T>> Collection<T>.treeToList(): List<T> {
    val list = mutableListOf<T>()
    this.fold(list) { acc, row ->
        acc.add(row)
        if (row.children.any()) {
            acc.addAll(row.children.treeToList())
        }
        acc
    }
    return list
}

interface TreeLike<T : TreeLike<T>> {
    val children: List<T>
}

interface ForestLike<T : ForestLike<T>> : TreeLike<T> {
    fun isMyChild(otherCode: String?) =
        Regex("^$code$splitter$CODE_PATTERN_STR$").matches(otherCode.ifNullOrBlank())

    @JsonIgnore
    fun isAncestor() =
        ancestorPattern.matches(code.ifNullOrBlank())

    @get:JsonIgnore
    val code: String?

    @get:JsonIgnore
    val sort: Int?

    @get:JsonIgnore
    val splitter: String
        get() = "-"

    override var children: MutableList<T>

    fun findAndSetChildren(nodes: Collection<T>) {
        nodes
            .filter {
                isMyChild(it.code)
            }.sortedBy {
                it.sort
            }.onEach {
                it.findAndSetChildren(nodes)
            }.let { children ->
                this.children.addAll(children)
                this
            }
    }
}
