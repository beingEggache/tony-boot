package com.tony.mono.dto.req;

import com.tony.Pageable;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
public class UserListQueryReq implements Pageable {

    private Long page;

    private Long size;

    private Collection<String> ascs;

    private Collection<String> descs;

    private String query;
}
