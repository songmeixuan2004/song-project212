package com.example.shop.service.impl;

import com.example.shop.entity.Logistics;
import com.example.shop.entity.LogisticsTrack;
import com.example.shop.mapper.LogisticsMapper;
import com.example.shop.service.LogisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogisticsServiceImpl implements LogisticsService {

    @Resource
    private LogisticsMapper logisticsMapper;

    @Override
    @Transactional
    public Logistics create(Logistics logistics) {
        logistics.setCreateTime(LocalDateTime.now());
        logistics.setUpdateTime(LocalDateTime.now());
        logistics.setStatus(0);
        logisticsMapper.insert(logistics);

        // 添加初始轨迹
        LogisticsTrack track = new LogisticsTrack();
        track.setLogisticsId(logistics.getId());
        track.setStatus(0);
        track.setContent("订单已创建，等待发货");
        track.setTime(LocalDateTime.now());
        logisticsMapper.insertTrack(track);

        return logistics;
    }

    @Override
    public Logistics findByOrderId(Long orderId) {
        Logistics logistics = logisticsMapper.findByOrderId(orderId);
        if (logistics != null) {
            List<LogisticsTrack> tracks = logisticsMapper.findTracksByLogisticsId(logistics.getId());
            logistics.setTracks(tracks);
        }
        return logistics;
    }

    @Override
    @Transactional
    public void updateStatus(Long orderId, Integer status, String trackContent) {
        Logistics logistics = logisticsMapper.findByOrderId(orderId);
        if (logistics == null) {
            throw new RuntimeException("物流信息不存在");
        }

        logistics.setStatus(status);
        logistics.setUpdateTime(LocalDateTime.now());
        logisticsMapper.update(logistics);

        // 添加轨迹
        LogisticsTrack track = new LogisticsTrack();
        track.setLogisticsId(logistics.getId());
        track.setStatus(status);
        track.setContent(trackContent);
        track.setTime(LocalDateTime.now());
        logisticsMapper.insertTrack(track);
    }
}