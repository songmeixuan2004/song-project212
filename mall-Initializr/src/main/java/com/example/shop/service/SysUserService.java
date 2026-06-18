package com.example.shop.service;

import com.example.shop.entity.SysUser;

public interface SysUserService {
    SysUser login(String username, String password);
    void register(SysUser user);
    SysUser getUserById(Long id);
}