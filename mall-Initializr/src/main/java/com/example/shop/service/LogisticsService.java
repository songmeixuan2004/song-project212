package com.example.shop.service;

import com.example.shop.entity.Logistics;

public interface LogisticsService {
    Logistics create(Logistics logistics);
    Logistics findByOrderId(Long orderId);
    void updateStatus(Long orderId, Integer status, String trackContent);
}