package tony.mono.api;

import tony.web.WebContext;
import tony.web.WebSession;
import tony.web.exception.UnauthorizedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StaticApiSession implements WebSession {
    @NotNull
    @Override
    public String getUserId() {
        return WebContext.request().getHeader("X-User-Id");
    }

    @Nullable
    @Override
    public UnauthorizedException getUnauthorizedException() {
        if (!StringUtils.hasLength(getUserId())) {
            return new UnauthorizedException("请登录");
        }
        return null;
    }

    @Nullable
    @Override
    public String getTenantId() {
        return null;
    }
}
