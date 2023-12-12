package com.tony

/**
 * 自定义实现 session.
 *
 * @author Tang Li
 * @date 2023/11/12 10:52
 */
public interface ApiSession {
    /**
     * 用户id
     */
    public val userId: String?

    /**
     * 用户名称
     */
    public val userName: String?
        get() = ""

    /**
     * 部门/组织 id
     */
    public val orgId: String?
        get() = ""

    /**
     * 部门/组织 名称
     */
    public val orgName: String?
        get() = ""

    /**
     * 租户 id
     */
    public val tenantId: String?
}
