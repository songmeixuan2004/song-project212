package com.example.shop.config;

import com.example.shop.mapper.SysUserMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 暂时关闭拦截器
        // registry.addInterceptor(new RoleInterceptor(sysUserMapper))
        //         .addPathPatterns("/merchant/**", "/admin/**");
    }
}