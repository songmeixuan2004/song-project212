package com.example.shop.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productPic;  // 添加图片字段
    private Integer quantity;
    private Long merchantId;
}