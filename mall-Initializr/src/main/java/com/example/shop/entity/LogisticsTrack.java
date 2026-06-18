package com.example.shop.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogisticsTrack {
    private Long id;
    private Long logisticsId;
    private Integer status;
    private String content;
    private LocalDateTime time;
}