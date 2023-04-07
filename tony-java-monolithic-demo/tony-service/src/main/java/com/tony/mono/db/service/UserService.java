package com.tony.mono.db.service;

import com.tony.mono.dto.req.UserLoginReq;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String login(final UserLoginReq req){
        return req.getUserName();
    }
}
