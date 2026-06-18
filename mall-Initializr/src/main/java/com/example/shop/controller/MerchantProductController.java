package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Product;
import com.example.shop.service.ProductService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/merchant/product")
public class MerchantProductController {

    @Resource
    private ProductService productService;

    // 新增商品
    @PostMapping("/add")
    public Result<?> add(@RequestBody Product product) {
        productService.add(product);
        return Result.success("新增成功");
    }

    // 商家商品列表
    @GetMapping("/list")
    public Result<List<Product>> list(@RequestParam Long merchantId) {
        return Result.success(productService.listByMerchant(merchantId));
    }

    // 修改商品
    @PostMapping("/update")
    public Result<?> update(@RequestBody Product product) {
        productService.update(product);
        return Result.success("修改成功");
    }

    // 删除商品
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam Long id,
                            @RequestParam Long merchantId) {
        productService.delete(id, merchantId);
        return Result.success("删除成功");
    }
}