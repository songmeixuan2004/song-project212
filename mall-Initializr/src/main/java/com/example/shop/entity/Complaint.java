package com.example.shop.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Complaint {
    private Long id;
    private Long userId;
    private String userName;
    private String targetType;  // product/order/merchant
    private Long targetId;
    private String targetName;
    private String title;
    private String content;
    private String images;
    private Integer status;  // 0待处理,1已处理,2已驳回
    private String reply;
    private LocalDateTime replyTime;
    private LocalDateTime createTime;
}