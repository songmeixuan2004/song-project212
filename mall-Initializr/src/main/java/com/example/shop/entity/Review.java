package com.example.shop.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Review {
    private Long id;
    private Long orderId;
    private Long orderItemId;        // ✅ 新增：订单商品明细ID
    private Long productId;
    private Long userId;
    private String userName;
    private Integer rating;
    private String content;
    private String images;
    private String reply;
    private Integer status;          // 0-待审核 1-已发布 2-已驳回
    private Integer likeCount;
    private LocalDateTime createTime;
    private LocalDateTime replyTime;
    private String appendContent;    // ✅ 新增：追加评论
    private String appendImages;     // ✅ 新增：追加评论图片
    private LocalDateTime appendTime; // ✅ 新增：追加时间
    private LocalDateTime updateTime; // ✅ 新增：更新时间
}