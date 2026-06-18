package com.example.shop.mapper;

import com.example.shop.entity.Logistics;
import com.example.shop.entity.LogisticsTrack;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface LogisticsMapper {

    // 创建物流
    @Insert("INSERT INTO logistics(order_id, logistics_no, logistics_company, status, " +
            "sender_name, sender_phone, receiver_name, receiver_phone, receiver_address, create_time, update_time) " +
            "VALUES(#{orderId}, #{logisticsNo}, #{logisticsCompany}, #{status}, " +
            "#{senderName}, #{senderPhone}, #{receiverName}, #{receiverPhone}, #{receiverAddress}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Logistics logistics);

    // 更新物流
    @Update("UPDATE logistics SET logistics_no=#{logisticsNo}, logistics_company=#{logisticsCompany}, " +
            "status=#{status}, update_time=#{updateTime} WHERE id=#{id}")
    int update(Logistics logistics);

    // 根据订单ID查询物流
    @Select("SELECT * FROM logistics WHERE order_id = #{orderId}")
    @Results({
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "logistics_no", property = "logisticsNo"),
            @Result(column = "logistics_company", property = "logisticsCompany"),
            @Result(column = "sender_name", property = "senderName"),
            @Result(column = "sender_phone", property = "senderPhone"),
            @Result(column = "receiver_name", property = "receiverName"),
            @Result(column = "receiver_phone", property = "receiverPhone"),
            @Result(column = "receiver_address", property = "receiverAddress"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    Logistics findByOrderId(Long orderId);

    // 添加物流轨迹
    @Insert("INSERT INTO logistics_track(logistics_id, status, content, time) VALUES(#{logisticsId}, #{status}, #{content}, #{time})")
    int insertTrack(LogisticsTrack track);

    // 查询物流轨迹
    @Select("SELECT * FROM logistics_track WHERE logistics_id = #{logisticsId} ORDER BY time DESC")
    List<LogisticsTrack> findTracksByLogisticsId(Long logisticsId);
}