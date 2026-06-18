package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Cart;
import com.example.shop.entity.Product;
import com.example.shop.service.CartService;
import com.example.shop.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @Resource
    private ProductService productService;

    // 查询购物车列表
    @GetMapping("/list")
    public Result<List<Cart>> list(@RequestParam Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        List<Cart> cartList = cartService.list(userId);
        return Result.success(cartList);
    }

    // 添加购物车 吗喽
    @PostMapping("/add")
    public Result<?> add(@RequestBody Cart cart) {
        try {
            // 1. 参数校验
            if (cart.getUserId() == null) {
                return Result.error("用户ID不能为空");
            }
            if (cart.getProductId() == null) {
                return Result.error("商品ID不能为空");
            }
            if (cart.getQuantity() == null || cart.getQuantity() <= 0) {
                cart.setQuantity(1); // 默认数量为1
            }

            // 2. 查询商品信息
            Product product = productService.getById(cart.getProductId());
            if (product == null) {
                return Result.error("商品不存在");
            }

            // 3. 校验商品状态（0-下架，1-上架）
            if (product.getStatus() == null || product.getStatus() == 0) {
                return Result.error("商品已下架，无法添加购物车");
            }

            // 4. 校验库存
            if (product.getStock() == null || product.getStock() < cart.getQuantity()) {
                return Result.error("库存不足，当前库存：" + (product.getStock() == null ? 0 : product.getStock()));
            }

            // 5. 添加到购物车
            cartService.add(cart);

            // 6. 返回成功信息和商品详情
            Map<String, Object> result = new HashMap<>();
            result.put("productName", product.getName());
            result.put("quantity", cart.getQuantity());
            result.put("message", "成功加入购物车");

            return Result.success(result);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 修改购物车数量
    @PostMapping("/update")
    public Result<?> update(@RequestBody Cart cart) {
        try {
            if (cart.getId() == null) {
                return Result.error("购物车ID不能为空");
            }
            if (cart.getQuantity() == null || cart.getQuantity() <= 0) {
                return Result.error("数量必须大于0");
            }

            // 可选：再次校验商品是否下架
            Cart existingCart = cartService.getById(cart.getId());
            if (existingCart != null) {
                Product product = productService.getById(existingCart.getProductId());
                if (product == null || product.getStatus() == 0) {
                    return Result.error("商品已下架，无法修改数量");
                }
                // 校验库存
                if (product.getStock() != null && product.getStock() < cart.getQuantity()) {
                    return Result.error("库存不足，当前库存：" + product.getStock());
                }
            }

            cartService.update(cart);
            return Result.success("修改成功");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 删除单条购物车记录
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        try {
            if (id == null) {
                return Result.error("购物车ID不能为空");
            }
            cartService.delete(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 清空购物车
    @DeleteMapping("/clear")
    public Result<?> clear(@RequestParam Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户ID不能为空");
            }
            cartService.clear(userId);
            return Result.success("清空成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 获取购物车商品总数（可选，用于显示角标）
    @GetMapping("/count")
    public Result<Integer> getCount(@RequestParam Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        int count = cartService.getCount(userId);
        return Result.success(count);
    }
}