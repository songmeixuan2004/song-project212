package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Order;
import com.example.shop.mapper.OrderMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Resource
    private OrderMapper orderMapper;

    // 查询所有订单（管理员用）
    @GetMapping("/list")
    public Result<List<Order>> listAll() {
        List<Order> orders = orderMapper.listAllOrders();
        return Result.success(orders);
    }
}