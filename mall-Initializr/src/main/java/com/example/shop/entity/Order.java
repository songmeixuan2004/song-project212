package com.example.shop.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private String userName;  // 添加用户名字段
    private BigDecimal totalPrice;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private List<OrderItem> items;
    private LocalDateTime shipTime;  // 发货时间
}