package com.example.shop.service;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;

import java.util.List;

public interface OrderService {
    Order create(Order order);
    List<Order> list(Long userId);
    void pay(Long orderId);
    void ship(Long orderId);
    void finish(Long orderId);
    List<OrderItem> getOrderItems(Long orderId);
}