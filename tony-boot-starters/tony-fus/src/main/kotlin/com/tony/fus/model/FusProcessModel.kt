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

package com.tony.fus.model

/**
 * FusProcessModel is
 * @author tangli
 * @date 2023/10/25 19:30
 * @since 1.0.0
 */
public data class FusProcessModel(
    public val name: String,
    public val key: String,
    public val node: FusNode?,
) {
    public fun getNode(nodeName: String?): FusNode? =
        node?.getNode(nodeName)

    public fun buildParentNode(rootNode: FusNode? = node): FusProcessModel {
        rootNode?.conditionNodes?.forEach { conditionNode ->
            conditionNode.childNode?.also { conditionChildNode ->
                conditionChildNode.parentNode = rootNode
                buildParentNode(conditionChildNode)
            }
        }
        rootNode?.childNode?.also { childNode ->
            childNode.parentNode = rootNode
            buildParentNode(childNode)
        }
        return this
    }

    /**
     * 清除父节点
     * @param [rootNode] 根节点
     * @author tangli
     * @date 2024/02/05 10:09
     * @since 1.0.0
     */
    public fun cleanParentNode(rootNode: FusNode? = node) {
        rootNode?.parentNode = null
        rootNode
            ?.conditionNodes
            ?.forEach { conditionNode ->
                conditionNode
                    .childNode
                    .also { conditionChildNode ->
                        cleanParentNode(conditionChildNode)
                    }
            }

        rootNode
            ?.childNode
            ?.also { childNode ->
                childNode.parentNode = null
                cleanParentNode(childNode)
            }
    }
}
