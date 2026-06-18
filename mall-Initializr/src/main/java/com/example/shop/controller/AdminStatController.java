package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.mapper.SysUserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/stat")
public class AdminStatController {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private OrderMapper orderMapper;

    @GetMapping
    public Result<Map<String, Object>> stat() {
        Map<String, Object> map = new HashMap<>();
        map.put("userCount", sysUserMapper.countAll());
        map.put("orderCount", orderMapper.countAll());
        map.put("totalSales", orderMapper.sumSales());
        return Result.success(map);
    }
}