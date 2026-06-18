package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Favorite;
import com.example.shop.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    // 添加收藏
    @PostMapping("/add")
    public Result<?> add(@RequestBody Favorite favorite) {
        try {
            favoriteService.add(favorite);
            return Result.success("收藏成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 取消收藏
    @DeleteMapping("/cancel")
    public Result<?> cancel(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            favoriteService.cancel(userId, productId);
            return Result.success("取消收藏成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 查询用户收藏列表
    @GetMapping("/list")
    public Result<List<Favorite>> list(@RequestParam Long userId) {
        return Result.success(favoriteService.listByUserId(userId));
    }

    // 检查是否已收藏
    @GetMapping("/check")
    public Result<Boolean> check(@RequestParam Long userId, @RequestParam Long productId) {
        return Result.success(favoriteService.isFavorited(userId, productId));
    }
}