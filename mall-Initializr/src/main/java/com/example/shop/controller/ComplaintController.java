package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Complaint;
import com.example.shop.mapper.ComplaintMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/complaint")
public class ComplaintController {

    @Resource
    private ComplaintMapper complaintMapper;

    @PostMapping("/submit")
    public Result<?> submit(@RequestBody Complaint complaint) {
        try {
            complaint.setCreateTime(LocalDateTime.now());
            complaint.setStatus(0);
            complaintMapper.insert(complaint);
            return Result.success("投诉已提交，我们会尽快处理");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/listAll")
    public Result<List<Complaint>> listAll() {
        return Result.success(complaintMapper.selectAll());
    }

    @GetMapping("/pending")
    public Result<List<Complaint>> pending() {
        return Result.success(complaintMapper.selectPending());
    }

    @GetMapping("/my")
    public Result<List<Complaint>> my(@RequestParam Long userId) {
        return Result.success(complaintMapper.selectByUserId(userId));
    }

    @PostMapping("/reply")
    public Result<?> reply(@RequestParam Long id, @RequestParam String reply, @RequestParam Integer status) {
        try {
            System.out.println("回复投诉: id=" + id + ", status=" + status + ", reply=" + reply);
            int result = complaintMapper.updateReply(id, status, reply, LocalDateTime.now());
            if (result > 0) {
                return Result.success("回复成功");
            } else {
                return Result.error("回复失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        complaintMapper.deleteById(id);
        return Result.success("删除成功");
    }
}