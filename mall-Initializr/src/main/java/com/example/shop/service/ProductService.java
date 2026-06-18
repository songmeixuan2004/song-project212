package com.example.shop.service;

import com.example.shop.entity.Product;
import java.util.List;

public interface ProductService {
    void add(Product product);
    List<Product> listByMerchant(Long merchantId);
    void update(Product product);
    void delete(Long id, Long merchantId);
    Product getById(Long id);
    void putOnSale(Long id, Long merchantId);

    // 新增：下架
    void putOffSale(Long id, Long merchantId);
}
