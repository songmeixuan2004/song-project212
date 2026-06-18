package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Product;
import com.example.shop.service.ProductService;
import com.example.shop.mapper.ProductMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductMapper productMapper;

    @PostMapping
    public Result<?> add(@RequestBody Product product) {
        if (!StringUtils.hasText(product.getName())) {
            return Result.error("商品名称不能为空");
        }
        if (product.getPrice() == null) {
            return Result.error("价格不能为空");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            return Result.error("库存不能为负数");
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("价格必须大于0");
        }

        // ✅ 删除这段代码，不要覆盖前端传来的值
        // if (product.getMerchantId() == null) {
        //     product.setMerchantId(1L);
        // }

        // ✅ 改为校验
        if (product.getMerchantId() == null) {
            return Result.error("商家ID不能为空");
        }

        product.setCreateTime(LocalDateTime.now());
        product.setStatus(1);
        productService.add(product);
        return Result.success("商品新增成功");
    }
    @GetMapping("/list")
    public Result<List<Product>> list(
            @RequestParam(value = "merchantId", required = false) Long merchantId) {
        if (merchantId == null) {
            merchantId = 1L;
        }
        return Result.success(productService.listByMerchant(merchantId));
    }

    @PutMapping
    public Result<?> update(@RequestBody Product product) {
        if (product.getId() == null) {
            return Result.error("商品ID不能为空");
        }
        productService.update(product);
        return Result.success("修改成功");
    }

    @DeleteMapping
    public Result<?> delete(
            @RequestParam Long id,
            @RequestParam Long merchantId) {
        System.out.println("=== Controller收到删除请求 ===");
        System.out.println("id: " + id);
        System.out.println("merchantId: " + merchantId);

        if (id == null || merchantId == null) {
            return Result.error("id和merchantId不能为空");
        }

        try {
            productService.delete(id, merchantId);
            System.out.println("删除成功");
            return Result.success("删除成功");
        } catch (Exception e) {
            System.out.println("删除失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 上架商品
    @PostMapping("/onSale")
    public Result<?> putOnSale(@RequestParam Long id, @RequestParam Long merchantId) {
        System.out.println("=== Controller收到上架请求 ===");
        System.out.println("id: " + id);
        System.out.println("merchantId: " + merchantId);

        try {
            productService.putOnSale(id, merchantId);
            return Result.success("上架成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 下架商品
    @PostMapping("/offSale")
    public Result<?> putOffSale(@RequestParam Long id, @RequestParam Long merchantId) {
        System.out.println("=== Controller收到下架请求 ===");
        System.out.println("id: " + id);
        System.out.println("merchantId: " + merchantId);

        try {
            productService.putOffSale(id, merchantId);
            return Result.success("下架成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/test")
    public Result<?> testDelete(@RequestParam Long id) {
        System.out.println("测试删除，id: " + id);
        try {
            int rows = productMapper.deleteById(id);
            System.out.println("影响行数: " + rows);
            if (rows > 0) {
                return Result.success("删除成功，影响行数: " + rows);
            } else {
                return Result.error("删除失败，商品不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("异常: " + e.getMessage());
        }
    }
    // 获取所有商品（管理员用）
    @GetMapping("/listAll")
    public Result<List<Product>> listAll() {
        return Result.success(productMapper.listAll());
    }
}