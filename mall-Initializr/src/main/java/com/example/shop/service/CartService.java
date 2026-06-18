package com.example.shop.service;

import com.example.shop.entity.Cart;
import java.util.List;

public interface CartService {
    List<Cart> list(Long userId);        // 购物车列表
    void add(Cart cart);                 // 添加/加购
    void update(Cart cart);              // 修改数量
    void delete(Long id);                // 删除
    void clear(Long userId);             // 清空
    Cart getById(Long id);               // 根据ID查询
    int getCount(Long userId);           // 获取购物车商品数量
}