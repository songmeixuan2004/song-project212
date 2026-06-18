package com.example.shop.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Favorite {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productPic;
    private LocalDateTime createTime;
}