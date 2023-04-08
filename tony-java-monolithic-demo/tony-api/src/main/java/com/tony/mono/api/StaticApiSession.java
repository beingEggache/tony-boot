package com.tony.mono.api;

import com.tony.web.ApiSession;
import com.tony.web.WebContext;
import com.tony.web.exception.UnauthorizedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StaticApiSession implements ApiSession {
    @NotNull
    @Override
    public String getUserId() {
        return WebContext.getHeader("x-user-id");
    }

    @Nullable
    @Override
    public UnauthorizedException getUnauthorizedException() {
        if (!StringUtils.hasLength(getUserId())) {
            return new UnauthorizedException("请登录");
        }
        return null;
    }
}
