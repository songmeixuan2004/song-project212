package com.example.shop.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AfterSale {
    private Long id;
    private Long orderId;
    private Long orderItemId;
    private Long userId;
    private String userName;  // 添加用户名字段
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productPic;
    private Integer quantity;
    private String reason;
    private String description;
    private String images;
    private Integer type;
    private Integer status;
    private BigDecimal refundAmount;
    private LocalDateTime applyTime;
    private LocalDateTime handleTime;
    private String handleRemark;
}