package com.example.shop.service.impl;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.mapper.OrderItemMapper;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;  // 注入 OrderItemMapper

    @Override
    @Transactional
    public Order create(Order order) {
        if (order.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (order.getTotalPrice() == null) {
            throw new RuntimeException("订单金额不能为空");
        }

        order.setOrderNo("ORDER" + System.currentTimeMillis());
        order.setStatus(0); // 未支付
        order.setCreateTime(LocalDateTime.now());

        orderMapper.insert(order);
        return order;
    }

    @Override
    public List<Order> list(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        return orderMapper.listByUserId(userId);
    }

    @Override
    @Transactional
    public void pay(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        orderMapper.pay(orderId);
    }

    @Override
    @Transactional
    public void ship(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new RuntimeException("只有已支付订单才能发货");
        }
        order.setStatus(2);
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void finish(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if(order == null) throw new RuntimeException("订单不存在");
        if(order.getStatus() != 2) throw new RuntimeException("当前订单不能确认收货");
        order.setStatus(3);
        orderMapper.updateById(order);
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        // 使用 selectByOrderId 或 listByOrderId 都可以
        return orderItemMapper.selectByOrderId(orderId);
    }
}