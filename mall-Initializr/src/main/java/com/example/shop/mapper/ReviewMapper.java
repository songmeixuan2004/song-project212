package com.example.shop.mapper;

import com.example.shop.entity.Review;
import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReviewMapper {

    // 添加评论
    @Insert("INSERT INTO review(product_id, user_id, user_name, rating, content, status, create_time) " +
            "VALUES(#{productId}, #{userId}, #{userName}, #{rating}, #{content}, 1, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Review review);

    // 查询商品评论 - 使用别名强制映射
    @Select("SELECT " +
            "id, " +
            "order_id as orderId, " +
            "product_id as productId, " +
            "user_id as userId, " +
            "user_name as userName, " +
            "rating, " +
            "content, " +
            "images, " +
            "reply, " +
            "status, " +
            "like_count as likeCount, " +
            "create_time as createTime, " +
            "reply_time as replyTime " +
            "FROM review WHERE product_id = #{productId} AND status = 1 ORDER BY create_time DESC")
    List<Review> listByProductId(Long productId);

    // 查询用户评论
    @Select("SELECT " +
            "id, " +
            "order_id as orderId, " +
            "product_id as productId, " +
            "user_id as userId, " +
            "user_name as userName, " +
            "rating, " +
            "content, " +
            "images, " +
            "reply, " +
            "status, " +
            "like_count as likeCount, " +
            "create_time as createTime, " +
            "reply_time as replyTime " +
            "FROM review WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Review> listByUserId(Long userId);

    // 商家回复评论
    @Update("UPDATE review SET reply = #{reply}, reply_time = #{replyTime} WHERE id = #{id}")
    int reply(@Param("id") Long id, @Param("reply") String reply, @Param("replyTime") LocalDateTime replyTime);

    // 查询所有评论 - 使用别名
    @Select("SELECT " +
            "id, " +
            "order_id as orderId, " +
            "product_id as productId, " +
            "user_id as userId, " +
            "user_name as userName, " +
            "rating, " +
            "content, " +
            "images, " +
            "reply, " +
            "status, " +
            "like_count as likeCount, " +
            "create_time as createTime, " +
            "reply_time as replyTime " +
            "FROM review ORDER BY create_time DESC")
    List<Review> listAll();

    // ✅ 新增：根据商家ID查询评论（通过商品关联，只查询该商家商品的评论）
    @Select("SELECT " +
            "r.id, " +
            "r.order_id as orderId, " +
            "r.product_id as productId, " +
            "r.user_id as userId, " +
            "r.user_name as userName, " +
            "r.rating, " +
            "r.content, " +
            "r.images, " +
            "r.reply, " +
            "r.status, " +
            "r.like_count as likeCount, " +
            "r.create_time as createTime, " +
            "r.reply_time as replyTime " +
            "FROM review r " +
            "LEFT JOIN product p ON r.product_id = p.id " +
            "WHERE p.merchant_id = #{merchantId} " +
            "ORDER BY r.create_time DESC")
    List<Review> listByMerchantId(@Param("merchantId") Long merchantId);

    // 删除评论
    @Delete("DELETE FROM review WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}