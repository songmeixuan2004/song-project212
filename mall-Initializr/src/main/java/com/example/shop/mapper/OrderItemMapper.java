package com.example.shop.mapper;

import com.example.shop.entity.OrderItem;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface OrderItemMapper {

    // ✅ 修改：添加 merchant_id 字段
    @Insert("INSERT INTO order_item(order_id, product_id, product_name, product_price, product_pic, quantity, merchant_id) " +
            "VALUES(#{orderId}, #{productId}, #{productName}, #{productPrice}, #{productPic}, #{quantity}, #{merchantId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItem item);

    // 根据订单ID查询商品列表
    @Select("SELECT id, order_id, product_id, product_name, product_price, product_pic, quantity, merchant_id " +
            "FROM order_item WHERE order_id = #{orderId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "product_price", property = "productPrice"),
            @Result(column = "product_pic", property = "productPic"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "merchant_id", property = "merchantId")
    })
    List<OrderItem> selectByOrderId(Long orderId);

    // 简单查询
    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> listByOrderId(Long orderId);

    // 根据订单ID删除订单商品
    @Delete("DELETE FROM order_item WHERE order_id = #{orderId}")
    int deleteByOrderId(Long orderId);
}