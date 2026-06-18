package com.example.shop.service.impl;

import com.example.shop.entity.Product;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public void add(Product product) {
        productMapper.insert(product);
    }

    @Override
    public List<Product> listByMerchant(Long merchantId) {
        return productMapper.listByMerchant(merchantId);
    }

    @Override
    public void update(Product product) {
        productMapper.update(product);
    }

    @Override
    public void delete(Long id, Long merchantId) {
        System.out.println("=== Service层开始删除 ===");
        System.out.println("id: " + id);
        System.out.println("merchantId: " + merchantId);

        Product product = productMapper.getById(id);
        if (product == null) {
            System.out.println("商品不存在");
            throw new RuntimeException("商品不存在");
        }
        System.out.println("找到商品: " + product.getName());
        System.out.println("数据库merchant_id: " + product.getMerchantId());

        Long dbMerchantId = product.getMerchantId();
        if (dbMerchantId == null) {
            System.out.println("数据库中的merchant_id为NULL，自动设置为1");
            dbMerchantId = 1L;
        }

        if (!dbMerchantId.equals(merchantId)) {
            System.out.println("权限不足");
            throw new RuntimeException("无权限删除此商品");
        }

        int rows = productMapper.deleteById(id);
        System.out.println("删除影响行数: " + rows);

        if (rows == 0) {
            System.out.println("删除失败，没有删除任何数据");
            throw new RuntimeException("删除失败，没有删除任何数据");
        }

        System.out.println("删除成功");
    }

    @Override
    public Product getById(Long id) {
        return productMapper.getById(id);
    }

    @Override
    @Transactional
    public void putOnSale(Long id, Long merchantId) {
        System.out.println("=== 上架商品 ===");
        System.out.println("id: " + id);
        System.out.println("merchantId: " + merchantId);

        Product product = productMapper.getById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        Long dbMerchantId = product.getMerchantId();
        if (dbMerchantId == null) {
            dbMerchantId = 1L;
        }

        if (!dbMerchantId.equals(merchantId)) {
            throw new RuntimeException("无权限操作此商品");
        }

        if (product.getStatus() != null && product.getStatus() == 1) {
            throw new RuntimeException("商品已是上架状态");
        }

        int rows = productMapper.putOnSale(id, merchantId);
        System.out.println("上架影响行数: " + rows);

        if (rows == 0) {
            throw new RuntimeException("上架失败");
        }

        System.out.println("上架成功");
    }

    @Override
    @Transactional
    public void putOffSale(Long id, Long merchantId) {
        System.out.println("=== 下架商品 ===");
        System.out.println("id: " + id);
        System.out.println("merchantId: " + merchantId);

        Product product = productMapper.getById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        Long dbMerchantId = product.getMerchantId();
        if (dbMerchantId == null) {
            dbMerchantId = 1L;
        }

        if (!dbMerchantId.equals(merchantId)) {
            throw new RuntimeException("无权限操作此商品");
        }

        if (product.getStatus() != null && product.getStatus() == 0) {
            throw new RuntimeException("商品已是下架状态");
        }

        int rows = productMapper.putOffSale(id, merchantId);
        System.out.println("下架影响行数: " + rows);

        if (rows == 0) {
            throw new RuntimeException("下架失败");
        }

        System.out.println("下架成功");
    }
}