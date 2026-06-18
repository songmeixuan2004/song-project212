package com.example.shop.mapper;

import com.example.shop.entity.AfterSale;
import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AfterSaleMapper {

    // 申请售后
    @Insert("INSERT INTO after_sale(order_id, order_item_id, user_id, product_id, product_name, " +
            "product_price, product_pic, quantity, reason, description, images, type, refund_amount, apply_time, status) " +
            "VALUES(#{orderId}, #{orderItemId}, #{userId}, #{productId}, #{productName}, " +
            "#{productPrice}, #{productPic}, #{quantity}, #{reason}, #{description}, #{images}, " +
            "#{type}, #{refundAmount}, #{applyTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AfterSale afterSale);

    // 查询用户售后记录
    @Select("SELECT * FROM after_sale WHERE user_id = #{userId} ORDER BY apply_time DESC")
    @Results({
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "order_item_id", property = "orderItemId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "product_price", property = "productPrice"),
            @Result(column = "product_pic", property = "productPic"),
            @Result(column = "refund_amount", property = "refundAmount"),
            @Result(column = "apply_time", property = "applyTime"),
            @Result(column = "handle_time", property = "handleTime"),
            @Result(column = "handle_remark", property = "handleRemark"),
            @Result(column = "user_name", property = "userName")
    })
    List<AfterSale> listByUserId(Long userId);

    // 查询所有售后（商家用）
    @Select("SELECT * FROM after_sale ORDER BY apply_time DESC")
    @Results({
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "order_item_id", property = "orderItemId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "product_price", property = "productPrice"),
            @Result(column = "product_pic", property = "productPic"),
            @Result(column = "refund_amount", property = "refundAmount"),
            @Result(column = "apply_time", property = "applyTime"),
            @Result(column = "handle_time", property = "handleTime"),
            @Result(column = "handle_remark", property = "handleRemark")
    })
    List<AfterSale> listAll();

    // 根据ID查询
    @Select("SELECT * FROM after_sale WHERE id = #{id}")
    @Results({
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "order_item_id", property = "orderItemId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "product_price", property = "productPrice"),
            @Result(column = "product_pic", property = "productPic"),
            @Result(column = "refund_amount", property = "refundAmount"),
            @Result(column = "apply_time", property = "applyTime"),
            @Result(column = "handle_time", property = "handleTime"),
            @Result(column = "handle_remark", property = "handleRemark")
    })
    AfterSale selectById(Long id);

    // 根据订单ID查询
    @Select("SELECT * FROM after_sale WHERE order_id = #{orderId}")
    List<AfterSale> selectByOrderId(Long orderId);

    // 处理售后
    @Update("UPDATE after_sale SET status = #{status}, handle_time = #{handleTime}, handle_remark = #{handleRemark} " +
            "WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status,
                     @Param("handleTime") LocalDateTime handleTime, @Param("handleRemark") String handleRemark);
}