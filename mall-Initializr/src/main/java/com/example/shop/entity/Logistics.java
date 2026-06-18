package com.example.shop.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Logistics {
    private Long id;
    private Long orderId;
    private String logisticsNo;
    private String logisticsCompany;
    private Integer status;  // 0待发货,1已揽件,2运输中,3派送中,4已签收
    private String senderName;
    private String senderPhone;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<LogisticsTrack> tracks;
}