package com.example.shop.service;

import com.example.shop.entity.Favorite;
import java.util.List;

public interface FavoriteService {
    void add(Favorite favorite);
    void cancel(Long userId, Long productId);
    List<Favorite> listByUserId(Long userId);
    boolean isFavorited(Long userId, Long productId);
}