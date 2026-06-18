package com.example.shop.mapper;

import com.example.shop.entity.SysUser;
import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SysUserMapper {

    @Select("SELECT * FROM sys_user WHERE username = '${username}'")
    SysUser selectByUsername(String username);

    @Insert("INSERT INTO sys_user(username, password, phone, role, avatar, status, create_time) " +
            "VALUES('${username}', '${password}', '${phone}', '${role}', '${avatar}', ${status}, '${createTime}')")
    int insert(SysUser user);

    @Select("SELECT * FROM sys_user WHERE id = ${id}")
    SysUser selectById(Long id);

    @Update("UPDATE sys_user SET last_login_time = '${lastLoginTime}' WHERE id = ${id}")
    int updateLastLoginTime(@Param("id") Long id, @Param("lastLoginTime") LocalDateTime lastLoginTime);

    @Select("SELECT COUNT(*) FROM sys_user")
    int countAll();

    @Select("SELECT COUNT(*) FROM sys_user WHERE role = 'merchant' AND status = 1")
    int countMerchants();

    @Select("SELECT * FROM sys_user")
    List<SysUser> selectAll();

    @Select("SELECT * FROM sys_user WHERE role = 'merchant' AND status = 0")
    List<SysUser> selectPendingMerchants();

    @Select("SELECT * FROM sys_user WHERE role = 'merchant' AND status = 1")
    List<SysUser> selectApprovedMerchants();

    @Update("UPDATE sys_user SET phone = '${phone}' WHERE id = ${id}")
    int updateUser(SysUser user);

    @Update("UPDATE sys_user SET status = ${status} WHERE id = ${id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE sys_user SET role = '${role}' WHERE id = ${id}")
    int updateRole(@Param("id") Long id, @Param("role") String role);

    @Update("UPDATE sys_user SET password = '${password}' WHERE id = ${id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
    // 封禁用户
    @Update("UPDATE sys_user SET status = #{status}, suspend_end_time = #{suspendEndTime}, suspend_reason = #{suspendReason} WHERE id = #{id}")
    int updateSuspendStatus(@Param("id") Long id,
                            @Param("status") Integer status,
                            @Param("suspendEndTime") LocalDateTime suspendEndTime,
                            @Param("suspendReason") String suspendReason);

    // 查询商家信息（如果有商家表）
    @Select("SELECT id, username, status FROM sys_user WHERE role = 'merchant' AND id = #{id}")
    SysUser selectMerchantById(Long id);
    

}