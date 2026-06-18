package com.example.shop.mapper;

import com.example.shop.entity.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProductMapper {

    @Insert("INSERT INTO product(name, price, stock, pic, merchant_id, status, create_time) " +
            "VALUES(#{name}, #{price}, #{stock}, #{pic}, #{merchantId}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Select("SELECT * FROM product WHERE merchant_id = #{merchantId} ORDER BY id DESC")
    List<Product> listByMerchant(Long merchantId);

    @Update("UPDATE product SET name=#{name}, price=#{price}, stock=#{stock}, pic=#{pic}, status=#{status} " +
            "WHERE id=#{id} AND merchant_id=#{merchantId}")
    int update(Product product);

    @Delete("DELETE FROM product WHERE id = #{id} AND merchant_id = #{merchantId}")
    int deleteByIdAndMerchant(@Param("id") Long id, @Param("merchantId") Long merchantId);

    @Delete("DELETE FROM product WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    // ✅ 修改这里：使用别名 merchantId 映射
    @Select("SELECT id, name, price, stock, pic, merchant_id as merchantId, status, create_time FROM product WHERE id = #{id}")
    Product getById(Long id);

    @Update("UPDATE product SET stock = #{stock} WHERE id = #{id}")
    int updateStock(@Param("id") Long id, @Param("stock") Integer stock);

    @Update("UPDATE product SET status = 1 WHERE id = #{id} AND merchant_id = #{merchantId}")
    int putOnSale(@Param("id") Long id, @Param("merchantId") Long merchantId);

    @Update("UPDATE product SET status = 0 WHERE id = #{id} AND merchant_id = #{merchantId}")
    int putOffSale(@Param("id") Long id, @Param("merchantId") Long merchantId);

    @Select("SELECT * FROM product ORDER BY id DESC")
    List<Product> listAll();
}