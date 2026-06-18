package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Notification;
import com.example.shop.entity.Violation;
import com.example.shop.mapper.NotificationMapper;
import com.example.shop.mapper.ViolationMapper;
import com.example.shop.mapper.SysUserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/violation")
public class ViolationController {

    @Resource
    private ViolationMapper violationMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private NotificationMapper notificationMapper;

    // 用户举报
    @PostMapping("/report")
    public Result<?> report(@RequestBody Violation violation) {
        try {
            System.out.println("=== 收到举报 ===");
            System.out.println("举报人ID: " + violation.getReporterId());
            System.out.println("被举报人ID: " + violation.getTargetId());
            System.out.println("违规类型: " + violation.getType());
            System.out.println("举报内容: " + violation.getContent());

            violation.setStatus(0);
            violation.setCreateTime(LocalDateTime.now());
            violationMapper.insert(violation);
            return Result.success("举报成功，平台将尽快处理");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("举报失败：" + e.getMessage());
        }
    }

    // 管理员查询所有举报
    @GetMapping("/listAll")
    public Result<List<Violation>> listAll() {
        List<Violation> list = violationMapper.listAll();
        System.out.println("查询举报列表，数量: " + (list == null ? 0 : list.size()));
        return Result.success(list);
    }

    // 管理员处理举报
    @PostMapping("/handle")
    public Result<?> handle(@RequestParam Long id,
                            @RequestParam Integer status,
                            @RequestParam(required = false) String remark,
                            @RequestParam(required = false) Integer suspendDays,
                            @RequestParam(required = false) String suspendReason) {
        try {
            Violation violation = violationMapper.selectById(id);
            if (violation == null) {
                return Result.error("举报不存在");
            }

            String handleResult = "";

            // 创建通知
            Notification notification = new Notification();
            notification.setUserId(violation.getTargetId());
            notification.setCreateTime(LocalDateTime.now());
            notification.setIsRead(0);

            if (status == 1) {
                handleResult = "已警告";
                notification.setType("warning");
                notification.setTitle("违规警告通知");
                notification.setContent("您因【" + getViolationTypeText(violation.getType()) + "】被平台警告。违规内容：" + violation.getContent());
                notification.setUserRole(violation.getTargetType() == 1 ? "user" : "merchant");
                notificationMapper.insert(notification);

            } else if (status == 2) {
                handleResult = "已封禁" + suspendDays + "天";
                notification.setType("suspend");
                notification.setTitle("账号封禁通知");
                notification.setContent("您因【" + getViolationTypeText(violation.getType()) + "】被平台封禁" + suspendDays + "天。原因：" + suspendReason);
                notification.setUserRole(violation.getTargetType() == 1 ? "user" : "merchant");
                notificationMapper.insert(notification);

                // 封禁用户/商家
                LocalDateTime suspendEndTime = LocalDateTime.now().plusDays(suspendDays);
                sysUserMapper.updateSuspendStatus(violation.getTargetId(), 0, suspendEndTime, suspendReason);

            } else if (status == 3) {
                handleResult = "已驳回";
                notification.setType("dismiss");
                notification.setTitle("举报结果通知");
                notification.setContent("您被举报的内容【" + violation.getContent() + "】经平台审核，不予处罚。");
                notification.setUserRole(violation.getTargetType() == 1 ? "user" : "merchant");
                notificationMapper.insert(notification);
            }

            violation.setStatus(status);
            violation.setHandleResult(handleResult);
            violation.setHandleRemark(remark);
            violation.setHandleTime(LocalDateTime.now());
            violationMapper.updateStatus(violation);

            return Result.success("处理成功，已发送通知");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("处理失败：" + e.getMessage());
        }
    }

    // 查询被举报人信息
    @GetMapping("/targetInfo")
    public Result<?> getTargetInfo(@RequestParam Long targetId, @RequestParam Integer targetType) {
        if (targetType == 1) {
            return Result.success(sysUserMapper.selectById(targetId));
        } else {
            return Result.success(sysUserMapper.selectMerchantById(targetId));
        }
    }

    private String getViolationTypeText(int type) {
        switch(type) {
            case 1: return "广告";
            case 2: return "色情内容";
            case 3: return "违规内容";
            case 4: return "欺诈行为";
            case 5: return "恶意评论";
            case 6: return "辱骂攻击";
            default: return "其他违规";
        }
    }
}