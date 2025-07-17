package tony.core.model

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 树形结构接口
 *
 * @author tangli
 * @date 2025/07/16 11:33
 */
public interface TreeLike<T : TreeLike<T>> {
    /**
     * 子项
     *
     * @return [Collection]<[T]>
     * @author tangli
     * @date 2025/07/16 11:35
     */
    @get:Schema(description = "子节点")
    public val children: Collection<T>
}
