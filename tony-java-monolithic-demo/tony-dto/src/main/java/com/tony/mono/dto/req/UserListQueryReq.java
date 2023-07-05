package com.tony.mono.dto.req;

import com.tony.PageQueryLike;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class UserListQueryReq implements PageQueryLike {

    private Long page;

    private Long size;

    private Set<String> ascs;

    private Set<String> descs;

    private String query;
}
