package com.example.shop.controller;
import com.example.shop.service.OrderService;
import com.example.shop.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/merchant/order")
public class MerchantOrderController {
    @Resource
    private OrderService orderService;

    // 发货：已付款 → 已发货
    @PostMapping("/ship")
    public Result<?> ship(@RequestParam Long orderId) {
        orderService.ship(orderId);
        return Result.success("发货成功");
    }
}