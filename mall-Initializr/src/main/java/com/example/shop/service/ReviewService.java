package com.example.shop.service;

import com.example.shop.entity.Review;
import java.util.List;

public interface ReviewService {
    void add(Review review);
    List<Review> listByProductId(Long productId);
    List<Review> listByUserId(Long userId);
    void reply(Long id, String reply);
    List<Review> listAll();
    void deleteById(Long id);

    // ✅ 新增：根据商家ID查询评论
    List<Review> listByMerchantId(Long merchantId);
}