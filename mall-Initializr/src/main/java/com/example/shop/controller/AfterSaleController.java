package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.AfterSale;
import com.example.shop.service.AfterSaleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/afterSale")
public class AfterSaleController {

    @Resource
    private AfterSaleService afterSaleService;

    // 用户申请售后
    @PostMapping("/apply")
    public Result<?> apply(@RequestBody AfterSale afterSale) {
        try {
            System.out.println("=== 收到售后申请 ===");
            System.out.println("订单ID: " + afterSale.getOrderId());
            System.out.println("订单项ID: " + afterSale.getOrderItemId());
            System.out.println("用户ID: " + afterSale.getUserId());
            System.out.println("商品ID: " + afterSale.getProductId());
            System.out.println("商品名称: " + afterSale.getProductName());
            System.out.println("商品价格: " + afterSale.getProductPrice());
            System.out.println("商品图片: " + afterSale.getProductPic());
            System.out.println("数量: " + afterSale.getQuantity());
            System.out.println("售后类型: " + afterSale.getType());
            System.out.println("售后原因: " + afterSale.getReason());
            System.out.println("问题描述: " + afterSale.getDescription());
            System.out.println("退款金额: " + afterSale.getRefundAmount());

            afterSaleService.apply(afterSale);
            return Result.success("售后申请已提交");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 用户查询自己的售后记录
    @GetMapping("/list")
    public Result<List<AfterSale>> list(@RequestParam Long userId) {
        return Result.success(afterSaleService.listByUserId(userId));
    }

    // 商家查询所有售后
    @GetMapping("/listAll")
    public Result<List<AfterSale>> listAll() {
        return Result.success(afterSaleService.listAll());
    }

    // 商家处理售后
    @PostMapping("/handle")
    public Result<?> handle(@RequestParam Long id, @RequestParam Integer status,
                            @RequestParam(required = false) String remark) {
        try {
            afterSaleService.handle(id, status, remark);
            return Result.success("处理成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 查询售后详情
    @GetMapping("/detail")
    public Result<AfterSale> detail(@RequestParam Long id) {
        return Result.success(afterSaleService.getById(id));
    }

    // 查询订单的售后状态
    @GetMapping("/status")
    public Result<AfterSale> getStatusByOrderId(@RequestParam Long orderId) {
        List<AfterSale> list = afterSaleService.listByOrderId(orderId);
        if (list != null && !list.isEmpty()) {
            return Result.success(list.get(0));
        }
        return Result.success(null);
    }
}