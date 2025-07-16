package tony.core.model

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
    public val children: Collection<T>
}
