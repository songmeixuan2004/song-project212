package com.example.shop.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;          // 商品ID
    private String name;      // 商品名称
    private BigDecimal price; // 价格
    private Integer stock;    // 库存
    private String pic;       // 图片
    private Long merchantId;  // 商家ID（谁发布的）
    private Integer status=1;   // 1上架 0下架
    private LocalDateTime createTime;
}