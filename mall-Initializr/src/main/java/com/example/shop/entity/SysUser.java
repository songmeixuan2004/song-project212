package com.example.shop.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String role;  // user/merchant/admin
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;

    // ✅ 只保留一个 status 字段
    private Integer status;  // 1正常 0封禁
    private LocalDateTime suspendEndTime;  // 封禁结束时间
    private String suspendReason;  // 封禁原因
}