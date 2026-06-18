package com.example.shop.service.impl;

import com.example.shop.entity.AfterSale;
import com.example.shop.entity.Product;
import com.example.shop.mapper.AfterSaleMapper;
import com.example.shop.mapper.OrderItemMapper;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.service.AfterSaleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AfterSaleServiceImpl implements AfterSaleService {

    @Resource
    private AfterSaleMapper afterSaleMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ProductMapper productMapper;

    @Override
    @Transactional
    public void apply(AfterSale afterSale) {
        System.out.println("=== AfterSaleServiceImpl.apply 开始 ===");

        if (afterSale.getUserId() == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (afterSale.getOrderId() == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        if (afterSale.getProductId() == null) {
            throw new RuntimeException("商品ID不能为空");
        }

        // 确保商品信息完整
        if (afterSale.getProductName() == null || afterSale.getProductPrice() == null) {
            Product product = productMapper.getById(afterSale.getProductId());
            if (product != null) {
                afterSale.setProductName(product.getName());
                afterSale.setProductPrice(product.getPrice());
                afterSale.setProductPic(product.getPic());
                System.out.println("从商品表获取信息: " + product.getName() + ", 价格: " + product.getPrice());
            } else {
                throw new RuntimeException("商品不存在");
            }
        }

        // 确保数量有值
        if (afterSale.getQuantity() == null || afterSale.getQuantity() <= 0) {
            afterSale.setQuantity(1);
        }

        // 确保退款金额正确
        if (afterSale.getRefundAmount() == null || afterSale.getRefundAmount().compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal refund = afterSale.getProductPrice().multiply(new BigDecimal(afterSale.getQuantity()));
            afterSale.setRefundAmount(refund);
            System.out.println("计算退款金额: " + refund);
        }

        afterSale.setApplyTime(LocalDateTime.now());
        afterSale.setStatus(0); // 待处理

        System.out.println("保存售后申请: " + afterSale);
        int result = afterSaleMapper.insert(afterSale);
        System.out.println("保存结果: " + result + ", 生成ID: " + afterSale.getId());

        if (result == 0) {
            throw new RuntimeException("保存失败");
        }

        System.out.println("=== AfterSaleServiceImpl.apply 结束 ===");
    }

    @Override
    public List<AfterSale> listByUserId(Long userId) {
        return afterSaleMapper.listByUserId(userId);
    }

    @Override
    public List<AfterSale> listAll() {
        List<AfterSale> list = afterSaleMapper.listAll();
        System.out.println("查询售后列表，数量: " + list.size());
        for (AfterSale item : list) {
            System.out.println("售后ID: " + item.getId() +
                    ", 商品: " + item.getProductName() +
                    ", 价格: " + item.getProductPrice() +
                    ", 用户ID: " + item.getUserId() +
                    ", 订单ID: " + item.getOrderId() +
                    ", 状态: " + item.getStatus());
        }
        return list;
    }

    @Override
    @Transactional
    public void handle(Long id, Integer status, String remark) {
        System.out.println("=== 处理售后 ===");
        System.out.println("售后ID: " + id + ", 状态: " + status + ", 备注: " + remark);

        AfterSale afterSale = afterSaleMapper.selectById(id);
        if (afterSale == null) {
            throw new RuntimeException("售后申请不存在");
        }

        System.out.println("原售后信息: 商品=" + afterSale.getProductName() +
                ", 价格=" + afterSale.getProductPrice() +
                ", 数量=" + afterSale.getQuantity());

        // 如果同意退款，恢复库存
        if (status == 1 && afterSale.getStatus() == 0) {
            Product product = productMapper.getById(afterSale.getProductId());
            if (product != null) {
                int newStock = product.getStock() + afterSale.getQuantity();
                productMapper.updateStock(afterSale.getProductId(), newStock);
                System.out.println("恢复库存: " + product.getName() + " 原库存:" + product.getStock() +
                        " 增加:" + afterSale.getQuantity() + " 新库存:" + newStock);
            }
        }

        int result = afterSaleMapper.updateStatus(id, status, LocalDateTime.now(), remark);
        System.out.println("更新结果: " + result);

        if (result == 0) {
            throw new RuntimeException("处理失败");
        }

        System.out.println("=== 处理完成 ===");
    }

    @Override
    public AfterSale getById(Long id) {
        return afterSaleMapper.selectById(id);
    }

    @Override
    public List<AfterSale> listByOrderId(Long orderId) {
        return afterSaleMapper.selectByOrderId(orderId);
    }
}