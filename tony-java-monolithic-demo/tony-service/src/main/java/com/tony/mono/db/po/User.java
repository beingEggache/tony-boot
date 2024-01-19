package com.tony.mono.db.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author Tang Li
 * @date 2024/01/19 11:53
 */
@Getter
@Setter
@TableName("sys_user")
public class User {

    @TableId
    private String userId;

    private String userName;

    private String realName;

    private String mobile;

    private String pwd;

    private String remark;

    @OrderBy
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    private LocalDateTime createTime;

    @TableField(
        fill = FieldFill.INSERT,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    private String creatorId;

    @TableField(
        fill = FieldFill.INSERT,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    private String creatorName;

    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER,
        update = "CURRENT_TIMESTAMP"
    )
    private LocalDateTime updateTime;

    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    private String updatorId;

    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    private String updatorName;


    private Boolean enabled;

    @TableLogic
    private Boolean deleted;

    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    private String tenantId;
}
