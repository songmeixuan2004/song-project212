package com.example.shop.config;

import com.example.shop.entity.SysUser;
import com.example.shop.mapper.SysUserMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleInterceptor implements HandlerInterceptor {

    private final SysUserMapper sysUserMapper;

    public RoleInterceptor(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取用户ID（开发阶段简单实现）
        String userIdStr = request.getHeader("userId");
        if (userIdStr == null) {
            response.setStatus(401);
            response.getWriter().write("未登录");
            return false;
        }

        Long userId = Long.parseLong(userIdStr);
        SysUser user = sysUserMapper.selectById(userId);

        if (user == null) {
            response.setStatus(401);
            response.getWriter().write("用户不存在");
            return false;
        }

        String uri = request.getRequestURI();

        // 商家接口权限校验
        if (uri.startsWith("/merchant/") && !"MERCHANT".equals(user.getRole())) {
            response.setStatus(403);
            response.getWriter().write("无商家权限");
            return false;
        }

        // 管理员接口权限校验
        if (uri.startsWith("/admin/") && !"ADMIN".equals(user.getRole())) {
            response.setStatus(403);
            response.getWriter().write("无管理员权限");
            return false;
        }

        return true;
    }
}