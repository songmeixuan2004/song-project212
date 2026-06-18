package com.example.shop.mapper;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO order_info(order_no, user_id, total_price, status, create_time, pay_time) " +
            "VALUES(#{orderNo}, #{userId}, #{totalPrice}, #{status}, #{createTime}, #{payTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Select("SELECT o.*, u.username as user_name FROM order_info o " +
            "LEFT JOIN sys_user u ON o.user_id = u.id " +
            "WHERE o.user_id = #{userId} ORDER BY o.create_time DESC")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "total_price", property = "totalPrice"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "pay_time", property = "payTime"),
            @Result(column = "user_name", property = "userName")
    })
    List<Order> listByUserId(Long userId);

    @Select("SELECT o.*, u.username as user_name FROM order_info o " +
            "LEFT JOIN sys_user u ON o.user_id = u.id " +
            "WHERE o.id = #{id}")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "total_price", property = "totalPrice"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "pay_time", property = "payTime"),
            @Result(column = "user_name", property = "userName")
    })
    Order selectById(Long id);

    @Update("UPDATE order_info SET status = 1, pay_time = NOW() WHERE id = #{orderId}")
    int pay(Long orderId);

    @Update("UPDATE order_info SET status = #{status} WHERE id = #{id}")
    int updateById(Order order);

    @Update("UPDATE order_info SET status = 2 WHERE id = #{orderId}")
    int ship(Long orderId);

    @Select("SELECT IFNULL(SUM(total_price), 0) FROM order_info WHERE status >= 1")
    BigDecimal sumSales();

    @Select("SELECT COUNT(*) FROM order_info")
    int countAll();

    @Select("SELECT o.*, u.username as user_name FROM order_info o " +
            "LEFT JOIN sys_user u ON o.user_id = u.id " +
            "ORDER BY o.create_time DESC")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "total_price", property = "totalPrice"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "pay_time", property = "payTime"),
            @Result(column = "user_name", property = "userName")
    })
    List<Order> listAllOrders();

    // ✅ 修改：根据商家ID查询订单 - 写在一行避免换行符问题
    @Select("SELECT DISTINCT o.*, u.username as user_name FROM order_info o LEFT JOIN order_item oi ON o.id = oi.order_id LEFT JOIN sys_user u ON o.user_id = u.id WHERE oi.merchant_id = #{merchantId} ORDER BY o.create_time DESC")
    @Results({
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "total_price", property = "totalPrice"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "pay_time", property = "payTime"),
            @Result(column = "user_name", property = "userName")
    })
    List<Order> listOrdersByMerchantId(@Param("merchantId") Long merchantId);

    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> getOrderItemsByOrderId(Long orderId);

    @Delete("DELETE FROM order_info WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
    // 查询发货超过指定天数的订单
    @Select("SELECT * FROM order_info WHERE status = 2 AND ship_time < #{dateTime}")
    List<Order> findShippedOrdersOlderThan(@Param("dateTime") LocalDateTime dateTime);

}