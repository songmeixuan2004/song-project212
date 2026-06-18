package com.example.shop.mapper;

import com.example.shop.entity.Favorite;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface FavoriteMapper {

    // 添加收藏
    @Insert("INSERT INTO favorite(user_id, product_id, product_name, product_price, product_pic, create_time) " +
            "VALUES(#{userId}, #{productId}, #{productName}, #{productPrice}, #{productPic}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Favorite favorite);

    // 取消收藏
    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND product_id = #{productId}")
    int delete(@Param("userId") Long userId, @Param("productId") Long productId);

    // 查询用户收藏列表
    @Select("SELECT * FROM favorite WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Favorite> listByUserId(Long userId);

    // 检查是否已收藏
    @Select("SELECT COUNT(*) FROM favorite WHERE user_id = #{userId} AND product_id = #{productId}")
    int checkFavorite(@Param("userId") Long userId, @Param("productId") Long productId);
}