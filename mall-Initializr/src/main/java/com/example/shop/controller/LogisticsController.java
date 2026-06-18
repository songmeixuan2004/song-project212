package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Logistics;
import com.example.shop.entity.LogisticsTrack;
import com.example.shop.entity.Order;
import com.example.shop.mapper.LogisticsMapper;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.service.LogisticsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/logistics")
public class LogisticsController {

    @Resource
    private LogisticsService logisticsService;

    @Resource
    private LogisticsMapper logisticsMapper;

    @Resource
    private OrderMapper orderMapper;

    @PostMapping("/create")
    public Result<?> create(@RequestBody Logistics logistics) {
        try {
            logisticsService.create(logistics);
            return Result.success("物流信息创建成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/findByOrder")
    public Result<Logistics> findByOrder(@RequestParam Long orderId) {
        Logistics logistics = logisticsMapper.findByOrderId(orderId);
        if (logistics != null) {
            List<LogisticsTrack> tracks = logisticsMapper.findTracksByLogisticsId(logistics.getId());
            logistics.setTracks(tracks);
        }
        return Result.success(logistics);
    }

    @PostMapping("/ship")
    public Result<?> ship(@RequestParam Long orderId,
                          @RequestParam String logisticsNo,
                          @RequestParam String logisticsCompany) {
        try {
            Order order = orderMapper.selectById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            if (order.getStatus() != 1) {
                return Result.error("订单状态不是待发货");
            }

            Logistics logistics = logisticsMapper.findByOrderId(orderId);
            if (logistics == null) {
                logistics = new Logistics();
                logistics.setOrderId(orderId);
                logistics.setLogisticsNo(logisticsNo);
                logistics.setLogisticsCompany(logisticsCompany);
                logistics.setStatus(1);
                logistics.setCreateTime(LocalDateTime.now());
                logistics.setUpdateTime(LocalDateTime.now());
                logisticsMapper.insert(logistics);
            } else {
                logistics.setLogisticsNo(logisticsNo);
                logistics.setLogisticsCompany(logisticsCompany);
                logistics.setStatus(1);
                logistics.setUpdateTime(LocalDateTime.now());
                logisticsMapper.update(logistics);
            }

            LogisticsTrack track = new LogisticsTrack();
            track.setLogisticsId(logistics.getId());
            track.setStatus(1);
            track.setContent("您的订单已揽件，物流单号：" + logisticsNo);
            track.setTime(LocalDateTime.now());
            logisticsMapper.insertTrack(track);

            order.setStatus(2);
            orderMapper.updateById(order);

            return Result.success("发货成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/autoUpdate")
    public Result<?> autoUpdate(@RequestParam Long orderId) {
        try {
            Logistics logistics = logisticsMapper.findByOrderId(orderId);
            if (logistics == null) {
                return Result.error("物流信息不存在");
            }

            int currentStatus = logistics.getStatus();
            if (currentStatus >= 3) {
                return Result.error("已经是派送中状态");
            }

            int newStatus = currentStatus + 1;
            logistics.setStatus(newStatus);
            logistics.setUpdateTime(LocalDateTime.now());
            logisticsMapper.update(logistics);

            String content = "";
            if (newStatus == 2) {
                content = "您的订单已到达【XX转运中心】，正在运输中";
            } else if (newStatus == 3) {
                content = "您的订单已到达【XX网点】，快递员正在派送中";
            }

            LogisticsTrack track = new LogisticsTrack();
            track.setLogisticsId(logistics.getId());
            track.setStatus(newStatus);
            track.setContent(content);
            track.setTime(LocalDateTime.now());
            logisticsMapper.insertTrack(track);

            return Result.success("物流状态已更新至：" + (newStatus == 2 ? "运输中" : "派送中"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}