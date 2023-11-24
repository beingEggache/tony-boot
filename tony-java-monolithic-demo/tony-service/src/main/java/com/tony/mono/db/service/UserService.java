package com.tony.mono.db.service;

import com.tony.PageQueryLike;
import com.tony.PageResult;
import com.tony.mono.db.dao.UserDao;
import com.tony.mono.db.po.User;
import com.tony.mono.dto.req.UserLoginReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;

    public String login(final UserLoginReq req) {
        return req.getUserName();
    }

    public PageResult<User> list(PageQueryLike<String> req) {
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
