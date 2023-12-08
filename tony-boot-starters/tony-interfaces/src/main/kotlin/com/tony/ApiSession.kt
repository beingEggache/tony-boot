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

    public val userName: String?
        get() = ""

    public val orgId: String?
        get() = ""

    public val orgName: String?
        get() = ""

    public val tenantId: String?
}
