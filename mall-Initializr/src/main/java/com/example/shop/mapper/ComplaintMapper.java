package com.example.shop.mapper;

import com.example.shop.entity.Complaint;
import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ComplaintMapper {

    @Insert("INSERT INTO complaint(user_id, user_name, target_type, target_id, target_name, title, content, images, status, create_time) " +
            "VALUES(#{userId}, #{userName}, #{targetType}, #{targetId}, #{targetName}, #{title}, #{content}, #{images}, 0, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Complaint complaint);

    @Select("SELECT * FROM complaint ORDER BY create_time DESC")
    List<Complaint> selectAll();

    @Select("SELECT * FROM complaint WHERE status = 0 ORDER BY create_time DESC")
    List<Complaint> selectPending();

    @Select("SELECT * FROM complaint WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Complaint> selectByUserId(Long userId);

    @Select("SELECT * FROM complaint WHERE id = #{id}")
    Complaint selectById(Long id);

    // 修复：添加 @Param 注解
    @Update("UPDATE complaint SET status = #{status}, reply = #{reply}, reply_time = #{replyTime} WHERE id = #{id}")
    int updateReply(@Param("id") Long id, @Param("status") Integer status,
                    @Param("reply") String reply, @Param("replyTime") LocalDateTime replyTime);

    @Delete("DELETE FROM complaint WHERE id = #{id}")
    int deleteById(Long id);
}