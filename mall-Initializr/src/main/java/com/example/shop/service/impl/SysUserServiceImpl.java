package com.example.shop.service.impl;

import com.example.shop.entity.SysUser;
import com.example.shop.mapper.SysUserMapper;
import com.example.shop.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser login(String username, String password) {
        System.out.println("=== 登录验证 ===");
        System.out.println("用户名: " + username);

        SysUser user = sysUserMapper.selectByUsername(username);
        System.out.println("查询结果: " + (user == null ? "null" : user.getUsername()));

        if (user == null) {
            List<SysUser> all = sysUserMapper.selectAll();
            System.out.println("数据库中的用户名:");
            for (SysUser u : all) {
                System.out.println("  - '" + u.getUsername() + "'");
            }
            throw new RuntimeException("用户不存在: " + username);
        }

        // 商家需要审核通过才能登录
        if ("merchant".equals(user.getRole())) {
            if (user.getStatus() == 0) {
                throw new RuntimeException("账号正在审核中，请等待审核通过");
            }
            if (user.getStatus() == 2) {
                throw new RuntimeException("账号审核被拒绝");
            }
        }

        if (user.getStatus() == 0 && !"merchant".equals(user.getRole())) {
            throw new RuntimeException("账号已被禁用");
        }

        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        sysUserMapper.updateLastLoginTime(user.getId(), LocalDateTime.now());
        return user;
    }

    @Override
    public void register(SysUser user) {
        System.out.println("=== 用户注册 ===");
        System.out.println("用户名: " + user.getUsername());
        System.out.println("角色: " + user.getRole());

        // 检查用户名是否已存在
        SysUser exist = sysUserMapper.selectByUsername(user.getUsername());
        if (exist != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 确保创建时间有值
        user.setCreateTime(LocalDateTime.now());
        System.out.println("注册时间: " + user.getCreateTime());

        // 设置角色
        if (user.getRole() == null) {
            user.setRole("user");
        }

        // 根据角色设置状态
        if ("merchant".equals(user.getRole())) {
            user.setStatus(0);  // 商家：待审核
            System.out.println("商家注册，状态设为待审核(0)");
        } else {
            user.setStatus(1);  // 普通用户：直接正常
            System.out.println("普通用户注册，状态设为正常(1)");
        }

        if (user.getAvatar() == null) {
            user.setAvatar("");
        }
        if (user.getPhone() == null) {
            user.setPhone("");
        }

        int result = sysUserMapper.insert(user);
        System.out.println("插入结果: " + result);

        if (result == 0) {
            throw new RuntimeException("注册失败");
        }

        System.out.println("注册成功，用户状态: " + (user.getStatus() == 0 ? "待审核" : "正常"));
    }

    @Override
    public SysUser getUserById(Long id) {
        return sysUserMapper.selectById(id);
    }
}