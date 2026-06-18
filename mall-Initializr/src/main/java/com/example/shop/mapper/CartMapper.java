package com.example.shop.mapper;

import com.example.shop.entity.Cart;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CartMapper {

    // 查询用户购物车（关联商品信息）
    @Select("SELECT " +
            "c.id, c.user_id, c.product_id, c.quantity, c.create_time, " +
            "p.name as product_name, " +
            "p.pic as product_pic, " +
            "p.price as product_price " +
            "FROM cart c " +
            "LEFT JOIN product p ON c.product_id = p.id " +
            "WHERE c.user_id = #{userId} " +
            "ORDER BY c.create_time DESC")
    @Results(id = "CartResultMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "product_name", property = "productName"),
            @Result(column = "product_pic", property = "productPic"),
            @Result(column = "product_price", property = "productPrice")
    })
    List<Cart> listByUserId(Long userId);

    // 插入或更新（需要数据库有唯一索引 uk_user_product）
    @Insert("INSERT INTO cart(user_id, product_id, quantity, create_time) " +
            "VALUES(#{userId}, #{productId}, #{quantity}, #{createTime}) " +
            "ON DUPLICATE KEY UPDATE quantity = quantity + #{quantity}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertOrUpdate(Cart cart);

    // 更新数量
    @Update("UPDATE cart SET quantity = #{quantity} WHERE id = #{id}")
    int updateQuantity(Cart cart);

    // 删除单条
    @Delete("DELETE FROM cart WHERE id = #{id}")
    int deleteById(Long id);

    // 清空用户购物车
    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    int clearByUserId(Long userId);

    // 根据ID查询
    @Select("SELECT id, user_id, product_id, quantity, create_time FROM cart WHERE id = #{id}")
    @ResultMap("CartResultMap")
    Cart selectById(Long id);

    // 统计用户购物车商品数量
    @Select("SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = #{userId}")
    int countByUserId(Long userId);
}