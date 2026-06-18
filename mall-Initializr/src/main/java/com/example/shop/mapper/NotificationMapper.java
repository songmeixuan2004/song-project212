package com.example.shop.mapper;

import com.example.shop.entity.Notification;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("INSERT INTO notification(user_id, user_role, type, title, content, is_read, create_time) " +
            "VALUES(#{userId}, #{userRole}, #{type}, #{title}, #{content}, 0, #{createTime})")
    int insert(Notification notification);

    @Select("SELECT * FROM notification WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Notification> listByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM notification WHERE user_role = #{userRole} ORDER BY create_time DESC")
    List<Notification> listByUserRole(@Param("userRole") String userRole);

    @Update("UPDATE notification SET is_read = 1 WHERE id = #{id}")
    int markAsRead(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM notification WHERE user_id = #{userId} AND is_read = 0")
    int countUnread(@Param("userId") Long userId);
    @Update("UPDATE notification SET is_read = 1 WHERE user_id = #{userId}")
    int markAllByUserId(@Param("userId") Long userId);
}