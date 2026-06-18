package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.SysUser;
import com.example.shop.mapper.SysUserMapper;
import com.example.shop.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserMapper sysUserMapper;

    @PostMapping("/register")
    public Result<?> register(@RequestBody SysUser user) {
        try {
            sysUserService.register(user);
            Map<String, Object> result = new HashMap<>();
            result.put("username", user.getUsername());
            result.put("role", user.getRole());
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/registerMerchant")
    public Result<?> registerMerchant(@RequestBody SysUser user, @RequestParam String shopName) {
        try {
            System.out.println("=== 商家注册 ===");
            System.out.println("用户名: " + user.getUsername());
            System.out.println("店铺名: " + shopName);

            user.setRole("merchant");
            user.setStatus(0); // 待审核状态
            sysUserService.register(user);

            Map<String, Object> result = new HashMap<>();
            result.put("username", user.getUsername());
            result.put("role", "merchant");
            result.put("shopName", shopName);
            result.put("status", "pending");
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody SysUser user, HttpSession session) {
        try {
            SysUser loginUser = sysUserService.login(user.getUsername(), user.getPassword());

            // 商家需要审核通过才能登录
            if ("merchant".equals(loginUser.getRole()) && loginUser.getStatus() == 0) {
                return Result.error("账号正在审核中，请等待审核通过");
            }
            if ("merchant".equals(loginUser.getRole()) && loginUser.getStatus() == 2) {
                return Result.error("账号审核被拒绝");
            }

            session.setAttribute("userId", loginUser.getId());
            session.setAttribute("username", loginUser.getUsername());
            session.setAttribute("role", loginUser.getRole());
            loginUser.setPassword(null);
            return Result.success(loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/current")
    public Result<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return Result.error("未登录");
        }
        SysUser user = sysUserService.getUserById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出成功");
    }

    @GetMapping("/debug")
    public Result<?> debug() {
        List<SysUser> users = sysUserMapper.selectAll();
        Map<String, Object> result = new HashMap<>();
        result.put("userCount", users.size());
        result.put("users", users);
        return Result.success(result);
    }

    @GetMapping("/listAll")
    public Result<List<SysUser>> listAll() {
        List<SysUser> users = sysUserMapper.selectAll();
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    @GetMapping("/info/{id}")
    public Result<SysUser> getUserInfo(@PathVariable Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody SysUser user) {
        try {
            sysUserMapper.updateUser(user);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/toggleStatus")
    public Result<?> toggleStatus(@RequestParam Long id, @RequestParam Integer status) {
        try {
            sysUserMapper.updateStatus(id, status);
            return Result.success("操作成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/setRole")
    public Result<?> setRole(@RequestParam Long id, @RequestParam String role) {
        try {
            sysUserMapper.updateRole(id, role);
            return Result.success("设置成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 获取待审核商家列表
    @GetMapping("/pendingMerchants")
    public Result<List<SysUser>> getPendingMerchants() {
        List<SysUser> merchants = sysUserMapper.selectPendingMerchants();
        merchants.forEach(m -> m.setPassword(null));
        return Result.success(merchants);
    }

    // 获取已通过商家列表
    @GetMapping("/approvedMerchants")
    public Result<List<SysUser>> getApprovedMerchants() {
        List<SysUser> merchants = sysUserMapper.selectApprovedMerchants();
        merchants.forEach(m -> m.setPassword(null));
        return Result.success(merchants);
    }

    // 审核商家（通过/拒绝）
    @PostMapping("/auditMerchant")
    public Result<?> auditMerchant(@RequestParam Long id, @RequestParam Integer status) {
        try {
            // status: 1-通过, 2-拒绝
            sysUserMapper.updateStatus(id, status);
            String msg = status == 1 ? "审核通过" : "已拒绝";
            return Result.success(msg);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/changePwd")
    public Result<?> changePassword(@RequestParam Long id, @RequestParam String oldPwd, @RequestParam String newPwd) {
        try {
            SysUser user = sysUserMapper.selectById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            if (!user.getPassword().equals(oldPwd)) {
                return Result.error("原密码错误");
            }
            sysUserMapper.updatePassword(id, newPwd);
            return Result.success("密码修改成功，请重新登录");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    // 冻结用户
    @PostMapping("/freeze")
    public Result<?> freezeUser(@RequestParam Long id) {
        try {
            sysUserMapper.updateStatus(id, 2);
            return Result.success("冻结成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 解冻用户
    @PostMapping("/unfreeze")
    public Result<?> unfreezeUser(@RequestParam Long id) {
        try {
            sysUserMapper.updateStatus(id, 1);
            return Result.success("解冻成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}