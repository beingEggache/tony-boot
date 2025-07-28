package tony.mono.db.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tony.core.model.PageQuery;
import tony.core.model.PageResult;
import tony.mono.db.dao.UserDao;
import tony.mono.db.po.User;
import tony.mono.dto.req.UserLoginReq;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;

    public String login(final UserLoginReq req) {
        return req.getUserName();
    }

    public PageResult<User> list(final PageQuery<String> req) {
        return userDao
            .lambdaQuery()
            .like(
                StringUtils.hasLength(req.getQuery()),
                User::getUserName,
                req.getQuery()
            )
            .or(StringUtils.hasLength(req.getQuery()), it -> it.like(User::getRealName, req.getQuery()))
            .pageResult(req);
    }
}
