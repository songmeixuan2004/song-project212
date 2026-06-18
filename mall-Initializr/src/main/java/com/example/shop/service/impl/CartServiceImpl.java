package com.example.shop.service.impl;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Product;
import com.example.shop.mapper.CartMapper;
import com.example.shop.service.CartService;
import com.example.shop.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductService productService;

    @Override
    public List<Cart> list(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        return cartMapper.listByUserId(userId);
    }

    @Override
    public void add(Cart cart) {
        if (cart.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (cart.getProductId() == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        if (cart.getQuantity() == null || cart.getQuantity() <= 0) {
            cart.setQuantity(1);
        }

        // 商品下架校验
        Product product = productService.getById(cart.getProductId());
        if (product == null || product.getStatus() == 0) {
            throw new RuntimeException("商品已下架，无法加入购物车");
        }

        // 库存校验
        if (product.getStock() != null && product.getStock() < cart.getQuantity()) {
            throw new RuntimeException("库存不足，当前库存：" + product.getStock());
        }

        cart.setCreateTime(LocalDateTime.now());

        // 执行插入或更新
        int result = cartMapper.insertOrUpdate(cart);
        if (result == 0) {
            throw new RuntimeException("添加购物车失败");
        }
    }

    @Override
    public void update(Cart cart) {
        if (cart.getId() == null) {
            throw new RuntimeException("购物车ID不能为空");
        }
        if (cart.getQuantity() == null || cart.getQuantity() <= 0) {
            throw new RuntimeException("商品数量必须大于0");
        }

        // 检查购物车记录是否存在
        Cart existingCart = cartMapper.selectById(cart.getId());
        if (existingCart == null) {
            throw new RuntimeException("购物车记录不存在");
        }

        int result = cartMapper.updateQuantity(cart);
        if (result == 0) {
            throw new RuntimeException("修改数量失败");
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new RuntimeException("购物车ID不能为空");
        }
        int result = cartMapper.deleteById(id);
        if (result == 0) {
            throw new RuntimeException("删除失败，记录不存在");
        }
    }

    @Override
    public void clear(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        cartMapper.clearByUserId(userId);
    }

    @Override
    public Cart getById(Long id) {
        if (id == null) {
            return null;
        }
        return cartMapper.selectById(id);
    }

    @Override
    public int getCount(Long userId) {
        if (userId == null) {
            return 0;
        }
        return cartMapper.countByUserId(userId);
    }
}