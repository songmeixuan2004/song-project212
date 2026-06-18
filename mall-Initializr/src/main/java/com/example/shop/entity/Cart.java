package com.example.shop.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Cart {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime createTime;

    // 前端展示用
    private String productName;
    private String productPic;
    private BigDecimal productPrice;
}