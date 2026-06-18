package com.example.shop.service.impl;

import com.example.shop.entity.Favorite;
import com.example.shop.entity.Product;
import com.example.shop.mapper.FavoriteMapper;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    @Resource
    private ProductMapper productMapper;

    @Override
    @Transactional
    public void add(Favorite favorite) {
        if (favorite.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (favorite.getProductId() == null) {
            throw new RuntimeException("商品ID不能为空");
        }

        // 检查是否已收藏
        if (isFavorited(favorite.getUserId(), favorite.getProductId())) {
            throw new RuntimeException("已经收藏过了");
        }

        // 获取商品信息
        Product product = productMapper.getById(favorite.getProductId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        favorite.setProductName(product.getName());
        favorite.setProductPrice(product.getPrice());
        favorite.setProductPic(product.getPic());
        favorite.setCreateTime(LocalDateTime.now());

        favoriteMapper.insert(favorite);
    }

    @Override
    @Transactional
    public void cancel(Long userId, Long productId) {
        favoriteMapper.delete(userId, productId);
    }

    @Override
    public List<Favorite> listByUserId(Long userId) {
        return favoriteMapper.listByUserId(userId);
    }

    @Override
    public boolean isFavorited(Long userId, Long productId) {
        return favoriteMapper.checkFavorite(userId, productId) > 0;
    }
}