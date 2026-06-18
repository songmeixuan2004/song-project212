package com.example.shop.service.impl;

import com.example.shop.entity.Review;
import com.example.shop.mapper.ReviewMapper;
import com.example.shop.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private ReviewMapper reviewMapper;

    @Override
    @Transactional
    public void add(Review review) {
        if (review.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (review.getProductId() == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            review.setRating(5);
        }

        review.setStatus(1); // 直接发布
        review.setLikeCount(0);
        review.setCreateTime(LocalDateTime.now());

        reviewMapper.insert(review);
    }

    @Override
    public List<Review> listByProductId(Long productId) {
        return reviewMapper.listByProductId(productId);
    }

    @Override
    public List<Review> listByUserId(Long userId) {
        return reviewMapper.listByUserId(userId);
    }

    @Override
    @Transactional
    public void reply(Long id, String reply) {
        reviewMapper.reply(id, reply, LocalDateTime.now());
    }

    @Override
    public List<Review> listAll() {
        return reviewMapper.listAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        reviewMapper.deleteById(id);
    }

    // ✅ 新增：根据商家ID查询评论
    @Override
    public List<Review> listByMerchantId(Long merchantId) {
        return reviewMapper.listByMerchantId(merchantId);
    }
}