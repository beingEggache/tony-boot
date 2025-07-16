package tony.core.model

import com.fasterxml.jackson.annotation.JsonIgnore
import tony.core.utils.ifNullOrBlank

/**
 * 可组装树形结构
 *
 * @author tangli
 * @date 2025/07/16 11:36
 */
public interface ForestLike<T : ForestLike<T>> : TreeLike<T> {
    /**
     * 节点编码, 用于区分节点位置
     *
     * @return [String]
     * @author tangli
     * @date 2025/07/16 11:37
     */
    public val code: String?

    /**
     * 获取节点分隔符.
     *
     * @return [String]
     * @author tangli
     * @date 2025/07/16 11:39
     */
    @get:JsonIgnore
    public val splitter: String
        get() = "-"

    /**
     * 是否根节点.
     *
     * @return boolean
     * @author tangli
     * @date 2025/07/16 11:40
     */
    @get:JsonIgnore
    public val isRoot: Boolean
        get() = !code.ifNullOrBlank().contains(splitter)

    /**
     * 获取排序.
     *
     * @return int
     * @author tangli
     * @date 2025/07/16 11:42
     */
    @get:JsonIgnore
    public val sort: Int?

    override val children: MutableCollection<T>

    /**
     * 判断 otherCode 是否为当前节点的直接子节点。
     * 规则：
     * 1. 必须以 "$code$splitter" 开头。
     * 2. 剩余部分（suffix）不能为空，且不能再包含 splitter。
     * @param otherCode 其他代码
     * @return boolean
     * @author tangli
     * @date 2025/07/16 11:51
     */
    public fun isChildNode(otherCode: String?): Boolean {
        val prefix = code + splitter
        if (otherCode?.startsWith(prefix) == false) return false
        val suffix = otherCode.ifNullOrBlank().removePrefix(prefix)
        return suffix.isNotEmpty() && !suffix.contains(splitter)
    }

    /**
     * 将列表组装成树形结构.
     * @param [nodes]
     * @author tangli
     * @date 2025/07/16 14:44
     */
    public fun <C : Collection<T>> findAndSetChildren(nodes: C) {
        nodes
            .filter { node ->
                isChildNode(node.code)
            }.sortedBy { node ->
                node.sort ?: 0
            }.onEach { node ->
                node.findAndSetChildren(nodes)
            }.let { children ->
                this.children.addAll(children)
            }
    }
}
