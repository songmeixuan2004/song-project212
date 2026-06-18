package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Notification;
import com.example.shop.mapper.NotificationMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Resource
    private NotificationMapper notificationMapper;

    // 查询用户通知
    @GetMapping("/list")
    public Result<List<Notification>> list(@RequestParam Long userId) {
        return Result.success(notificationMapper.listByUserId(userId));
    }

    // 标记单条为已读
    @PostMapping("/read")
    public Result<?> markAsRead(@RequestParam Long id) {
        notificationMapper.markAsRead(id);
        return Result.success("已读");
    }

    // 全部标记为已读
    @PostMapping("/readAll")
    public Result<?> markAllRead(@RequestParam Long userId) {
        notificationMapper.markAllByUserId(userId);
        return Result.success("全部已读");
    }
}