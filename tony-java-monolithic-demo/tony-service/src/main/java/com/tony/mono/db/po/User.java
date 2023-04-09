package com.tony.mono.db.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("t_sys_user")
public class User {

    @TableId("user_id")
    private String userId;

    @TableField("user_name")
    private String userName;

    @TableField("real_name")
    private String realName;

    @TableField("mobile")
    private String mobile;

    @TableField("pwd")
    private String pwd;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("states")
    private Integer states;

    @TableField("remark")
    private String remark;
}
