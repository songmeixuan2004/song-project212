package com.example.shop.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private Long userId;
    private String userRole;  // user, merchant, admin
    private String type;      // warning, suspend, dismiss, order_status
    private String title;
    private String content;
    private Integer isRead;   // 0未读,1已读
    private LocalDateTime createTime;
}