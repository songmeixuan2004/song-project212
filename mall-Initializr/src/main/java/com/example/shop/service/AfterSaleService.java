package com.example.shop.service;

import com.example.shop.entity.AfterSale;
import java.util.List;

public interface AfterSaleService {
    void apply(AfterSale afterSale);
    List<AfterSale> listByUserId(Long userId);
    List<AfterSale> listAll();
    void handle(Long id, Integer status, String remark);
    AfterSale getById(Long id);

    List<AfterSale> listByOrderId(Long orderId);
}