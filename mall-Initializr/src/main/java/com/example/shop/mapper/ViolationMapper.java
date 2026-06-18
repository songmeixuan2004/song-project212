package com.example.shop.mapper;

import com.example.shop.entity.Violation;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ViolationMapper {

    @Insert("INSERT INTO violation(reporter_id, reporter_name, target_id, target_name, target_type, type, content, evidence, status, create_time) " +
            "VALUES(#{reporterId}, #{reporterName}, #{targetId}, #{targetName}, #{targetType}, #{type}, #{content}, #{evidence}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Violation violation);

    // ✅ 修改：使用别名映射，确保字段正确
    @Select("SELECT " +
            "id, " +
            "reporter_id as reporterId, " +
            "reporter_name as reporterName, " +
            "target_id as targetId, " +
            "target_name as targetName, " +
            "target_type as targetType, " +
            "type, " +
            "content, " +
            "evidence, " +
            "status, " +
            "handle_result as handleResult, " +
            "handle_remark as handleRemark, " +
            "suspend_days as suspendDays, " +
            "suspend_reason as suspendReason, " +
            "create_time as createTime, " +
            "handle_time as handleTime " +
            "FROM violation ORDER BY create_time DESC")
    List<Violation> listAll();

    @Select("SELECT " +
            "id, " +
            "reporter_id as reporterId, " +
            "reporter_name as reporterName, " +
            "target_id as targetId, " +
            "target_name as targetName, " +
            "target_type as targetType, " +
            "type, " +
            "content, " +
            "evidence, " +
            "status, " +
            "handle_result as handleResult, " +
            "handle_remark as handleRemark, " +
            "suspend_days as suspendDays, " +
            "suspend_reason as suspendReason, " +
            "create_time as createTime, " +
            "handle_time as handleTime " +
            "FROM violation WHERE id = #{id}")
    Violation selectById(Long id);

    @Update("UPDATE violation SET status = #{status}, handle_result = #{handleResult}, handle_remark = #{handleRemark}, handle_time = #{handleTime} WHERE id = #{id}")
    int updateStatus(Violation violation);
}