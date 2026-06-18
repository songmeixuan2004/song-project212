package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Review;
import com.example.shop.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    // 添加评论
    @PostMapping("/add")
    public Result<?> add(@RequestBody Review review) {
        try {
            reviewService.add(review);
            return Result.success("评论成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 查询商品评论（用户端）
    @GetMapping("/listByProduct")
    public Result<List<Review>> listByProduct(@RequestParam Long productId) {
        return Result.success(reviewService.listByProductId(productId));
    }

    // 查询用户评论
    @GetMapping("/listByUser")
    public Result<List<Review>> listByUser(@RequestParam Long userId) {
        return Result.success(reviewService.listByUserId(userId));
    }

    // 商家回复评论
    @PostMapping("/reply")
    public Result<?> reply(@RequestParam Long id, @RequestParam String reply) {
        try {
            reviewService.reply(id, reply);
            return Result.success("回复成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ✅ 新增：商家端查询自己商品的评论
    @GetMapping("/listByMerchant")
    public Result<List<Map<String, Object>>> listByMerchant(@RequestParam Long merchantId) {
        try {
            List<Review> list = reviewService.listByMerchantId(merchantId);

            // 转换为前端需要的格式（驼峰命名）
            List<Map<String, Object>> result = list.stream().map(review -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", review.getId());
                map.put("orderId", review.getOrderId());
                map.put("productId", review.getProductId());
                map.put("userId", review.getUserId());
                map.put("userName", review.getUserName());
                map.put("rating", review.getRating());
                map.put("content", review.getContent());
                map.put("images", review.getImages());
                map.put("reply", review.getReply());
                map.put("status", review.getStatus());
                map.put("likeCount", review.getLikeCount());
                map.put("createTime", review.getCreateTime());
                map.put("replyTime", review.getReplyTime());
                return map;
            }).collect(Collectors.toList());

            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 查询所有评论（管理员用）
    @GetMapping("/listAll")
    public Result<List<Map<String, Object>>> listAll() {
        try {
            List<Review> list = reviewService.listAll();
            List<Map<String, Object>> result = list.stream().map(review -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", review.getId());
                map.put("orderId", review.getOrderId());
                map.put("productId", review.getProductId());
                map.put("userId", review.getUserId());
                map.put("userName", review.getUserName());
                map.put("rating", review.getRating());
                map.put("content", review.getContent());
                map.put("images", review.getImages());
                map.put("reply", review.getReply());
                map.put("status", review.getStatus());
                map.put("likeCount", review.getLikeCount());
                map.put("createTime", review.getCreateTime());
                map.put("replyTime", review.getReplyTime());
                return map;
            }).collect(Collectors.toList());
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    // 删除评论
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteById(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}